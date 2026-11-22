package com.zfettostudios.zfuctone.resolver;

import com.alessiodp.libby.Library;
import com.zfettostudios.zfuctone.model.lifecycle.Toggleable;

/**
 * Менеджер библиотек.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface LibraryResolver extends Toggleable<LibraryResolver> {
    LibraryResolver load(Library library);
}