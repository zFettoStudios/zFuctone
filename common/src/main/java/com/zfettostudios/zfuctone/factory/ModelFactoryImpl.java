package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.model.value.PlaceholderContextImpl;
import com.zfettostudios.zfuctone.model.value.ValueString;
import com.zfettostudios.zfuctone.model.value.ValueStringImpl;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.module.integration.IntegrationHookEmpty;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ModelFactoryImpl implements ModelFactory {
    private final LazyInstance<IntegrationHook<?>> emptyIntegrationHook = ZFuctoneProvider.get().utilFactory().buildLazyInstance(IntegrationHookEmpty::new);

    @Override
    public @NonNull PlaceholderContext buildPlaceholderContext() {
        return new PlaceholderContextImpl();
    }

    @Override
    public @NonNull PlaceholderContext buildPlaceholderContext(@NonNull String rawtext) {
        return new PlaceholderContextImpl(rawtext);
    }

    @Override
    public @NonNull ValueString buildValueString() {
        return new ValueStringImpl();
    }

    @Override
    public @NonNull ValueString buildValueString(@NonNull String string) {
        return new ValueStringImpl(string);
    }

    @Override
    public @NonNull ValueString buildValueString(@NonNull List<String> list) {
        return new ValueStringImpl(list);
    }

    @Override
    public @NonNull IntegrationHook<?> emptyIntegrationHook() {
        return emptyIntegrationHook.get();
    }
}
