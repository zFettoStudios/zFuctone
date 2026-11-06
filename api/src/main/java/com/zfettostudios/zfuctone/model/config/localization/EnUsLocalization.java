package com.zfettostudios.zfuctone.model.config.localization;

import com.zfettostudios.zfuctone.annotation.config.FilePath;

@FilePath(path = "en_us.yml", localization = true)
public class EnUsLocalization extends Localization {
    public EnUsLocalization() {
        messageCollect();
        commandCollect();
        integrationCollect();
    }

    private void messageCollect() {
        message();
    }

    private void commandCollect() {
        command();
    }

    private void integrationCollect() {
        integration();
    }
}
