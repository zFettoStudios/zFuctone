package com.zfettostudios.zfuctone;

import com.velocitypowered.api.event.EventManager;
import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.listener.VelocityListener;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.MiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.VelocityMiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPiModuleEmpty;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.VelocityPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.VelocityServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.VelocityCommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.VelocityPermissionRegistry;
import com.zfettostudios.zfuctone.resolver.library.LibraryResolver;
import com.zfettostudios.zfuctone.resolver.ReflectionResolver;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.time.Duration;

public class VelocityZFuctone extends ZFuctoneImpl {
    private final VelocityLoader loader;
    private final EventManager em;

    public VelocityZFuctone(VelocityLoader loader, ReflectionResolver reflectionResolver,
                            PlatformType platformType, LibraryResolver libraryResolver) {
        this.loader = loader;
        this.em = loader.getServer().getEventManager();
        super(reflectionResolver, platformType, libraryResolver);
    }

    @Override
    public @NonNull <L> L getLoader(Class<L> loaderClass) {
        if (loaderClass.isInstance(loader)) return loaderClass.cast(loader);

        throw new InvalidTypeException(loaderClass);
    }

    @Override
    public @NonNull Object getLoader() {
        return loader;
    }

    @Override
    protected Path getDataPath() {
        return loader.getDataDirectory();
    }

    @Override
    protected void registerListeners() {
        em.register(loader, new VelocityListener());
    }

    @Override
    protected void unregisterListeners() {
        em.unregisterListeners(loader);
    }

    @Override
    protected void registerTaskScheduler() {
        loader.getServer().getScheduler()
            .buildTask(loader, taskScheduler::onTick)
            .delay(Duration.ofMillis(50))
            .repeat(Duration.ofMillis(50))
            .schedule();
    }

    @Override
    protected Builder builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends Builder {

        @Override
        protected PermissionRegistry buildPermissionRegistry() {
            return new VelocityPermissionRegistry();
        }

        @Override
        protected PlatformPlayerAdapter buildPlayerAdapter() {
            return new VelocityPlayerAdapter();
        }

        @Override
        protected PlatformServerAdapter buildServerAdapter() {
            return new VelocityServerAdapter();
        }

        @Override
        protected MiniPlaceholdersModule buildMiniPlaceholdersModule() {
            return new VelocityMiniPlaceholdersModule();
        }

        @Override
        protected PlaceholderAPIModule buildPlaceholderAPIModule() {
            return new PlaceholderAPiModuleEmpty();
        }

        @Override
        protected CommandRegistry buildCommandRegistry() {
            return new VelocityCommandRegistry();
        }
    }
}
