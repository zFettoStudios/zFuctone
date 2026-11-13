package com.zfettostudios.zfuctone.model.config.localization;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.annotation.config.LocaleName;

@FilePath(path = "ru_ru.yml", localization = true)
@LocaleName("ru_ru")
public class RuRuLocalization extends Localization {
    public RuRuLocalization() {
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
