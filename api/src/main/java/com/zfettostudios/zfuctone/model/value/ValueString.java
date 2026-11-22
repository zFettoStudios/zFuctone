package com.zfettostudios.zfuctone.model.value;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Значение, которое может быть списком, либо строкой.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface ValueString {

    /**
     * Проверяет, представлено ли значение списком строк.
     *
     * @return {@code true}, если значение хранится как список строк
     */
    boolean isList();

    /**
     * Проверяет, представлено ли значение одиночной строкой.
     *
     * @return {@code true}, если значение хранится как одиночная строка
     */
    boolean isString();

    /**
     * Возвращает значение в виде списка строк, если оно задано как список.
     *
     * @return список строк или {@code null}, если задана одиночная строка либо значение пусто
     */
    @Nullable List<String> getList();

    /**
     * Возвращает значение в виде одиночной строки, если оно задано как строка.
     *
     * @return строка или {@code null}, если задан список строк либо значение пусто
     */
    @Nullable String getString();

    /**
     * Устанавливает значение в виде списка строк.
     *
     * @param list список строк для установки
     * @return установленный список строк
     */
    @NonNull List<String> setList(List<String> list);

    /**
     * Устанавливает значение в виде одиночной строки.
     *
     * @param string строка для установки
     * @return установленная строка
     */
    @NonNull String setString(String string);

    /**
     * Возвращает значение в виде единой строки (напрямую или склеивая элементы списка).
     *
     * @return строковое представление значения
     */
    @NonNull String asString();

    /**
     * Возвращает значение в виде списка строк (напрямую или оборачивая одиночную строку).
     *
     * @return списочное представление значения
     */
    @NonNull List<String> asList();

    /**
     * Копирует состояние из другого экземпляра {@link ValueString} в текущий объект.
     *
     * @param valueString источник значения
     * @return текущий экземпляр {@link ValueString}
     */
    @NonNull ValueString set(@NonNull ValueString valueString);
}