package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.config.BuildConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BootstrapLoader {
    private String version = BuildConfig.PROJECT_VERSION;
    private String group = "com/github/zFettoStudios";
    private String name = BuildConfig.PROJECT_NAME;
    private String repository = BuildConfig.JITPACK_REPOSITORY;

    public void load() {
        System.out.println(buildURL());
    }

    private String buildURL() {
        return repository +
            group +
            "/" +
            name +
            "/" +
            version +
            "/" +
            name +
            "-" +
            version +
            ".jar";
    }
}
