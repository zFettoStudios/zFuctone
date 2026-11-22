package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.model.value.ValueString;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import org.jspecify.annotations.NonNull;

import java.util.List;

public interface ModelFactory {
    @NonNull PlaceholderContext buildPlaceholderContext();
    @NonNull PlaceholderContext buildPlaceholderContext(@NonNull String rawtext);

    @NonNull ValueString buildValueString();
    @NonNull ValueString buildValueString(@NonNull String string);
    @NonNull ValueString buildValueString(@NonNull List<String> list);

    @NonNull IntegrationHook<?> emptyIntegrationHook();
}
