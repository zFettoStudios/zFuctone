package com.zfettostudios.zfuctone.platform.adapter;

import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

public abstract class PlatformPlayerAdapterImpl implements PlatformPlayerAdapter {
    protected static final Map<Integer, List<String>> VERSIONS = Map.<Integer, List<String>>ofEntries(
        // 26.x
        Map.entry(777, List.of("26.3")),
        Map.entry(776, List.of("26.2")),
        Map.entry(775, List.of("26.1", "26.1.1", "26.1.2")),

        // 1.21.x
        Map.entry(774, List.of("1.21.11")),
        Map.entry(773, List.of("1.21.9", "1.21.10")),
        Map.entry(772, List.of("1.21.7", "1.21.8")),
        Map.entry(771, List.of("1.21.6")),
        Map.entry(770, List.of("1.21.5")),
        Map.entry(769, List.of("1.21.4")),
        Map.entry(768, List.of("1.21.2", "1.21.3")),
        Map.entry(767, List.of("1.21", "1.21.1")),

        // 1.20.x
        Map.entry(766, List.of("1.20.5", "1.20.6")),
        Map.entry(765, List.of("1.20.3", "1.20.4")),
        Map.entry(764, List.of("1.20.2")),
        Map.entry(763, List.of("1.20", "1.20.1")),

        // 1.19.x
        Map.entry(762, List.of("1.19.4")),
        Map.entry(761, List.of("1.19.3")),
        Map.entry(760, List.of("1.19.1", "1.19.2")),
        Map.entry(759, List.of("1.19")),

        // 1.18.x
        Map.entry(758, List.of("1.18.2")),
        Map.entry(757, List.of("1.18", "1.18.1")),

        // 1.17.x
        Map.entry(756, List.of("1.17.1")),
        Map.entry(755, List.of("1.17")),

        // 1.16.x
        Map.entry(754, List.of("1.16.4", "1.16.5")),
        Map.entry(753, List.of("1.16.3")),
        Map.entry(751, List.of("1.16.2")),
        Map.entry(749, List.of("1.16.1")),
        Map.entry(736, List.of("1.16")),

        // 1.15.x
        Map.entry(735, List.of("1.15.2")),
        Map.entry(715, List.of("1.15.1")),
        Map.entry(578, List.of("1.15")),

        // 1.14.x
        Map.entry(575, List.of("1.14.4")),
        Map.entry(574, List.of("1.14.3")),
        Map.entry(573, List.of("1.14.2")),
        Map.entry(498, List.of("1.14.1")),
        Map.entry(477, List.of("1.14")),

        // 1.13.x
        Map.entry(404, List.of("1.13.2")),
        Map.entry(401, List.of("1.13.1")),
        Map.entry(393, List.of("1.13")),

        // 1.12.x
        Map.entry(340, List.of("1.12.2")),
        Map.entry(338, List.of("1.12.1")),
        Map.entry(335, List.of("1.12")),

        // 1.11.x
        Map.entry(316, List.of("1.11.2")),
        Map.entry(315, List.of("1.11")),

        // 1.10.x
        Map.entry(210, List.of("1.10", "1.10.1", "1.10.2")),

        // 1.9.x
        Map.entry(110, List.of("1.9.4")),
        Map.entry(109, List.of("1.9.2")),
        Map.entry(108, List.of("1.9.1")),
        Map.entry(107, List.of("1.9")),

        // 1.8.x
        Map.entry(47, List.of("1.8", "1.8.8", "1.8.9")),

        // 1.7.x
        Map.entry(5, List.of("1.7.6", "1.7.7", "1.7.8", "1.7.9", "1.7.10")),
        Map.entry(4, List.of("1.7.2", "1.7.3", "1.7.4", "1.7.5"))
    );

    @Override
    public @NonNull String getVersionFromProtocolVersion(int protocol) {
        return getVersionFromProtocolVersion("<version>", "<version> - <version:last>", protocol);
    }

    protected String parseVersion(@NonNull String formatOne, @NonNull String format, List<String> versions) {
        String targetFormat = (versions != null && versions.size() == 1) ? formatOne : format;

        if (versions == null || versions.isEmpty()) return removeVersionTags(targetFormat);

        StringBuilder sb = new StringBuilder(targetFormat.length());
        int cursor = 0;
        int nextTag;

        while ((nextTag = targetFormat.indexOf("<version", cursor)) != -1) {
            sb.append(targetFormat, cursor, nextTag);

            int closeTag = targetFormat.indexOf(">", nextTag);
            if (closeTag == -1) break;

            String tagContent = targetFormat.substring(nextTag + 1, closeTag).trim();

            cursor = closeTag + 1;

            if (tagContent.equals("version")) sb.append(versions.getFirst());
            else if (tagContent.startsWith("version:")) {
                String arg = tagContent.substring("version:".length()).trim();

                if (arg.equalsIgnoreCase("last")) sb.append(versions.getLast());
                else {
                    try {
                        int index = Integer.parseInt(arg);
                        if (index >= 0 && index < versions.size()) sb.append(versions.get(index));

                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            else sb.append(targetFormat, nextTag, closeTag + 1);
        }

        return sb.append(targetFormat, cursor, targetFormat.length()).toString();
    }


    protected String removeVersionTags(String text) {
        return text.replaceAll("<version(?::(?:last|\\d+))?>", "");
    }

    @Override
    public @NonNull String getVersionFromProtocolVersion(@NonNull String formatOne, @NonNull String format, int protocol) {
        return parseVersion(formatOne, format, VERSIONS.get(protocol));
    }
}
