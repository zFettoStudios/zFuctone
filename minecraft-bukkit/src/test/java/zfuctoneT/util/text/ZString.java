package zfuctoneT.util.text;

import java.io.Serial;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import lombok.experimental.NonFinal;
import org.jspecify.annotations.NonNull;

public class ZString implements CharSequence, Comparable<ZString>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final byte[] EMPTY_BYTES = new byte[0];

    public static final byte LATIN1 = 0;
    public static final byte UTF16 = 1;

    @NonFinal byte[] value;
    @NonFinal byte coder;
    @NonFinal int count;

    public ZString() {
        value = EMPTY_BYTES;
        coder = LATIN1;
        count = 0;
    }

    public ZString(int capacity) {
        if (capacity < 0) throw new NegativeArraySizeException("Capacity < 0: " + capacity);

        value = (capacity == 0) ? EMPTY_BYTES : new byte[capacity];
        coder = LATIN1;
        count = 0;
    }

    public ZString(CharSequence charSequence) {
        if (charSequence == null) charSequence = "null";
        count = charSequence.length();

        if (count == 0) {
            value = EMPTY_BYTES;
            coder = LATIN1;
            return;
        }

        if (charSequence instanceof ZString target) {
            coder = target.coder;
            int byteLength = target.getByteLength();
            value = new byte[byteLength];

            System.arraycopy(target.value, 0, value, 0, byteLength);
            return;
        }

        if (isLatin1(charSequence)) {
            coder = LATIN1;
            value = new byte[count];
            for (int i = 0; i < count; i++) value[i] = (byte) charSequence.charAt(i);
        }
        else {
            coder = UTF16;
            value = new byte[count << 1];
            for (int i = 0; i < count; i++) putCharUTF16(i, charSequence.charAt(i));
        }
    }

    public ZString(byte[] bytes, Charset charset) {
        if (bytes == null || bytes.length == 0) {
            value = EMPTY_BYTES;
            coder = LATIN1;
            count = 0;
            return;
        }

        if (StandardCharsets.ISO_8859_1.equals(charset) || StandardCharsets.US_ASCII.equals(charset)) {
            count = bytes.length;
            coder = LATIN1;
            value = new byte[count];

            System.arraycopy(bytes, 0, value, 0, count);
            return;
        }

        CharsetDecoder decoder = charset.newDecoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CharBuffer charBuffer = CharBuffer.allocate(bytes.length);

        decoder.decode(byteBuffer, charBuffer, true);
        decoder.flush(charBuffer);
        charBuffer.flip();

        count = charBuffer.remaining();

        if (count == 0) {
            value = EMPTY_BYTES;
            coder = LATIN1;
            return;
        }

        boolean isLatin = true;
        for (int i = 0; i < count; i++) {
            if (charBuffer.get(i) > 0xFF) {
                isLatin = false;
                break;
            }
        }

        if (isLatin) {
            coder = LATIN1;
            value = new byte[count];
            for (int i = 0; i < count; i++) value[i] = (byte) charBuffer.get(i);
        }
        else {
            coder = UTF16;
            value = new byte[count << 1];
            for (int i = 0; i < count; i++) putCharUTF16(i, charBuffer.get(i));
        }
    }

    public static ZString valueOf(Object object) {
        return switch (object) {
            case null -> new ZString("null");
            case ZString zString -> zString;
            case CharSequence charSequence -> new ZString(charSequence);
            default -> new ZString(object.toString());
        };
    }

    public ZString toLowerCase() {
        if (count == 0) return this;

        if (coder == LATIN1) {
            for (int i = 0; i < count; i++) {
                int currentByte = value[i] & 0xFF;
                if ((currentByte >= 'A' && currentByte <= 'Z') || (currentByte >= 0xC0 && currentByte <= 0xDE && currentByte!= 0xD7))
                    value[i] = (byte) (currentByte + 32);
            }
        }
        else {
            for (int i = 0; i < count; i++) {
                char currentChar = getCharUTF16(i);
                char lowerChar = Character.toLowerCase(currentChar);

                if (currentChar != lowerChar) putCharUTF16(i, lowerChar);
            }
        }

        return this;
    }

    public ZString toUpperCase() {
        if (count == 0) return this;

        if (coder == LATIN1) {
            for (int i = 0; i < count; i++) {
                int currentByte = value[i] & 0xFF;

                if (currentByte == 0xFF) {
                    inflateToUTF16();
                    break;
                }

                if (currentByte >= 'a' && currentByte <= 'z' || currentByte >= 0xE0 && currentByte!=0xF7)
                    value[i] = (byte) (currentByte - 32);
            }

            if (coder == UTF16) {
                for (int i = 0; i < count; i++) {
                    char currentChar = getCharUTF16(i);
                    char upperChar = Character.toUpperCase(currentChar);

                    if (currentChar != upperChar) putCharUTF16(i, upperChar);
                }
            }
        }
        else {
            for (int i = 0; i < count; i++) {
                char currentChar = getCharUTF16(i);
                char upperChar = Character.toUpperCase(currentChar);

                if (currentChar != upperChar) putCharUTF16(i, upperChar);
            }
        }

        return this;
    }

    public ZString join(CharSequence... elements) {
        if (elements == null || elements.length == 0) return this;

        int validElementsCount = 0;
        int elementsTotalLength = 0;

        for (CharSequence element : elements) {
            if (element != null) {
                elementsTotalLength += element.length();
                validElementsCount++;
            }
        }

        if (validElementsCount == 0) return this;

        int delimiterLength = count;
        byte delimiterCoder = coder;

        int delimitersCount = (delimiterLength > 0) ? (validElementsCount - 1) : 0;
        int totalAddedLength = elementsTotalLength + (delimitersCount * delimiterLength);

        if (delimiterLength== 0) {
            ensureCapacityInternal(totalAddedLength);
            for (CharSequence element : elements) if (element != null) append(element);

            return this;
        }

        ensureCapacityInternal(delimiterLength + totalAddedLength);

        int shift = (delimiterCoder == LATIN1) ? 0 : 1;
        int delimiterByteLen = delimiterLength << shift;
        int tempDelimiterOffset = (delimiterLength + totalAddedLength) << shift;

        System.arraycopy(value, 0, value, tempDelimiterOffset - delimiterByteLen, delimiterByteLen);

        count = 0;

        boolean first = true;
        for (CharSequence element : elements) {
            if (element == null) continue;

            if (!first) {
                if (coder == LATIN1 && delimiterCoder == UTF16) inflateToUTF16();

                int currentShift = (coder == LATIN1) ? 0 : 1;
                int srcOffset = (tempDelimiterOffset - delimiterByteLen);

                if (coder == delimiterCoder) System.arraycopy(value, srcOffset, value, count << currentShift, delimiterByteLen);
                else for (int i = 0; i < delimiterLength; i++)
                    putCharUTF16(count + i, (char) (value[(srcOffset >> 1) + i] & 0xFF));

                count += delimiterLength;
            }

            append(element);
            first = false;
        }

        return this;
    }

    public ZString join(Iterable<? extends CharSequence> elements) {
        if (elements == null) return this;

        ZString delimiter = (count > 0) ? new ZString(this) : null;
        boolean hadPreviousContent = count > 0;
        if (!hadPreviousContent) count = 0;

        boolean first = !hadPreviousContent;
        for (CharSequence element : elements) {
            if (element == null) continue;

            if (!first && delimiter != null) append(delimiter);

            append(element);
            first = false;
        }

        return this;
    }

    public static ZString join(CharSequence delimiter, CharSequence... elements) {
        if (delimiter == null) delimiter = "null";
        if (elements == null || elements.length == 0) return new ZString();

        if (delimiter instanceof ZString target) return target.join(elements);

        return new ZString(delimiter).join(elements);
    }

    public static ZString join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
        if (delimiter == null) delimiter = "null";
        if (elements == null) return new ZString();

        if (delimiter instanceof ZString target) target.join(elements);

        return new ZString(delimiter).join(elements);
    }

    public ZString append(CharSequence charSequence) {
        if (charSequence == null) charSequence = "null";
        return append(charSequence, 0, charSequence.length());
    }

    public ZString append(CharSequence charSequence, int start, int end) {
        if (charSequence == null) charSequence = "null";
        if (start < 0 || end > charSequence.length() || start > end)
            throw new IndexOutOfBoundsException("start: " + start + ", end: " + end + ", count: " + charSequence.length());

        int length = end - start;
        if (length== 0) return this;

        boolean charSequenceIsLatin1 = isLatin1(charSequence, start, end);
        if (coder == LATIN1 && !charSequenceIsLatin1) inflateToUTF16();

        ensureCapacityInternal(count + length);

        if (charSequence instanceof ZString target && target.coder == coder) {
            int shift = (coder == LATIN1) ? 0 : 1;
            System.arraycopy(target.value, start << shift, value, count << shift, length << shift);
        }
        else if (coder == LATIN1) for (int i = 0; i < length; i++) value[count + i] = (byte) charSequence.charAt(start + i);
        else for (int i = 0; i < length; i++) putCharUTF16(count + i, charSequence.charAt(start + i));

        count += length;
        return this;
    }

    public ZString insert(int index, CharSequence charSequence) {
        checkIndexForInsert(index);
        if (charSequence == null) charSequence = "null";

        int length = charSequence.length();
        if (length== 0) return this;

        boolean charSequenceIsLatin1 = isLatin1(charSequence);
        if (coder == LATIN1 && !charSequenceIsLatin1) inflateToUTF16();

        ensureCapacityInternal(count + length);

        int shift = (coder == LATIN1) ? 0 : 1;
        int srcOffset = index << shift;
        int dstOffset = (index + length) << shift;
        int bytesToMove = (count - index) << shift;

        if (bytesToMove > 0) System.arraycopy(value, srcOffset, value, dstOffset, bytesToMove);

        if (charSequence instanceof ZString target && (target.coder == coder))
            System.arraycopy(target.value, 0, value, srcOffset, length << shift);
        else if (coder == LATIN1) for (int i = 0; i < length; i++) value[index + i] = (byte) charSequence.charAt(i);
        else for (int i = 0; i < length; i++) putCharUTF16(index + i, charSequence.charAt(i));

        count += length;
        return this;
    }

    public boolean contains(CharSequence charSequence) {
        return indexOf(charSequence) >= 0;
    }

    public int indexOf(CharSequence charSequence) {
        if (charSequence == null) return -1;
        int targetLength = charSequence.length();
        if (targetLength == 0) return 0;
        if (targetLength > count) return -1;

        char firstChar = charSequence.charAt(0);
        int max = count - targetLength;

        for (int i = 0; i <= max; i++) {
            if (charAt(i) == firstChar) {
                int j = 1;
                while (j < targetLength && charAt(i + j) == charSequence.charAt(j)) j++;
                if (j ==targetLength) return i;
            }
        }

        return -1;
    }

    public ZString setByte(int index, byte inputByte) {
        checkByteIndex(index);
        value[index] = inputByte;
        return this;
    }

    public byte getByte(int index) {
        checkByteIndex(index);
        return value[index];
    }

    public ZString setCharAt(int index, char inputChar) {
        checkIndex(index);

        if (coder == LATIN1 && inputChar > 0xFF) inflateToUTF16();

        if (coder == LATIN1) value[index] = (byte) inputChar;
        else putCharUTF16(index, inputChar);

        return this;
    }

    public ZString replaceByte(byte oldByte, byte newByte) {
        for (int i = 0; i < getByteLength(); i++) if (value[i] == oldByte) value[i] = newByte;
        return this;
    }

    public ZString replace(char oldChar, char newChar) {
        if (oldChar == newChar) return this;

        if (coder == LATIN1) {
            if (oldChar <= 0xFF) {
                if (newChar > 0xFF) {
                    for (int i = 0; i < count; i++) {
                        if ((value[i] & 0xFF) == oldChar) {
                            inflateToUTF16();
                            break;
                        }
                    }
                }
                else {
                    replaceByte((byte) oldChar, (byte) newChar);
                    return this;
                }
            }
            else return this;
        }

        for (int i = 0; i < count; i++) if (getCharUTF16(i) == oldChar) putCharUTF16(i, newChar);

        return this;
    }

    public ZString replace(CharSequence target, CharSequence replacement) {
        if (target == null || replacement == null) return this;

        int targetLength = target.length();
        if (targetLength == 0) return this;

        int index = indexOf(target);
        if (index < 0) return this;

        int replacementLength = replacement.length();

        if (targetLength ==replacementLength)
            for (int i = 0; i < replacementLength; i++) setCharAt(index + i, replacement.charAt(i));
        else {
            int shift = (coder == LATIN1) ? 0 : 1;
            int diff = replacementLength - targetLength;

            if (diff > 0) ensureCapacityInternal(count + diff);

            int srcPos = (index + targetLength) << shift;
            int dstPos = (index + replacementLength) << shift;
            int bytesToMove = (count - (index + targetLength)) << shift;

            if (bytesToMove > 0) System.arraycopy(value, srcPos, value, dstPos, bytesToMove);

            count += diff;

            for (int i = 0; i < replacementLength; i++) setCharAt(index + i, replacement.charAt(i));
        }

        return this;
    }

    public ZString reverse() {
        if (count < 2) return this;

        if (coder == LATIN1) {
            for (int i = 0, j = count - 1; i < j; i++, j--) {
                byte temp = value[i];
                value[i] = value[j];
                value[j] = temp;
            }
        }
        else {
            for (int i = 0, j = count - 1; i < j; i++, j--) {
                char char1 = getCharUTF16(i);
                char char2 = getCharUTF16(j);
                putCharUTF16(i, char2);
                putCharUTF16(j, char1);
            }
        }

        return this;
    }

    @Override
    public int length() {
        return count;
    }

    @Override
    public char charAt(int index) {
        checkIndex(index);
        if (coder == LATIN1) return (char) (value[index] & 0xFF);
        return getCharUTF16(index);
    }

    @Override
    public @NonNull CharSequence subSequence(int start, int end) {
        if (start < 0 || end > count || start > end)
            throw new IndexOutOfBoundsException("start: " + start + ", end: " + end + ", count: " + count);

        int subLength = end - start;
        if (subLength == 0) return new ZString();

        ZString sub = new ZString(0);
        sub.coder = coder;
        sub.count = subLength;

        int shift = (coder == LATIN1) ? 0 : 1;
        int bytesToCopy = subLength << shift;
        sub.value = new byte[bytesToCopy];

        System.arraycopy(value, start << shift, sub.value, 0, bytesToCopy);
        return sub;
    }

    public byte[] getRawBytes() {
        return value;
    }

    public boolean contentEquals(CharSequence charSequence) {
        if (charSequence == null) return false;
        if (count != charSequence.length()) return false;

        for (int i = 0; i < count; i++) if (charAt(i) != charSequence.charAt(i)) return false;
        return true;
    }

    private int getByteLength() {
        return (coder == LATIN1) ? count : count << 1;
    }

    private static boolean isLatin1(CharSequence cs) {
        return isLatin1(cs, 0, cs.length());
    }

    private static boolean isLatin1(CharSequence charSequence, int start, int end) {
        for (int i = start; i < end; i++) if (charSequence.charAt(i) > 0xFF) return false;
        return true;
    }

    private void inflateToUTF16() {
        int newByteLen = value.length << 1;
        byte[] newBytes = new byte[newByteLen];

        for (int i = 0; i < count; i++) {
            char currentChar = (char) (value[i] & 0xFF);
            int offset = i << 1;
            newBytes[offset] = (byte) (currentChar & 0xFF);
            newBytes[offset + 1] = 0;
        }

        value = newBytes;
        coder = UTF16;
    }

    private void ensureCapacityInternal(int minimumCapacity) {
        int minByteCapacity = (coder == LATIN1) ? minimumCapacity : minimumCapacity << 1;
        if (minByteCapacity - value.length > 0) {
            int newCapacity = (value.length << 1) + 2;
            if (newCapacity - minByteCapacity < 0) newCapacity = minByteCapacity;

            value = Arrays.copyOf(value, newCapacity);
        }
    }

    private char getCharUTF16(int index) {
        int offset = index << 1;
        return (char) ((value[offset] & 0xFF) | ((value[offset + 1] & 0xFF) << 8));
    }

    private void putCharUTF16(int index, char inputChar) {
        int offset = index << 1;
        value[offset] = (byte) (inputChar & 0xFF);
        value[offset + 1] = (byte) (inputChar >> 8);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= count) throw new StringIndexOutOfBoundsException("Index: " + index + ", Size: " + count);
    }

    private void checkIndexForInsert(int index) {
        if (index < 0 || index > count) throw new StringIndexOutOfBoundsException("Index: " + index + ", Size: " + count);
    }

    private void checkByteIndex(int index) {
        if (index < 0 || index >= value.length)
            throw new ArrayIndexOutOfBoundsException("Byte Index: " + index + ", Buffer Size: " + value.length);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof CharSequence charSequence) return contentEquals(charSequence);

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < count; i++) hashCode = 31 * hashCode + charAt(i);

        return hashCode;
    }

    @Override
    public int compareTo(ZString another) {
        int length1 = count;
        int anotherLength = another.count;
        int limit = Math.min(length1, anotherLength);

        for (int i = 0; i < limit; i++) {
            char char1 = charAt(i);
            char char2 = another.charAt(i);
            if (char1 != char2) return char1 - char2;
        }

        return length1 - anotherLength;
    }

    @Override
    public @NonNull String toString() {
        if (coder == LATIN1) return new String(value, 0, count, StandardCharsets.ISO_8859_1);
        else return new String(value, 0, count << 1, StandardCharsets.UTF_16LE);
    }
}