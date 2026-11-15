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
        placeholderCollect();
    }

    private void messageCollect() {
        message().join().format("Игрок %player_name% зашел на сервер!");
        message().quit().format("Игрок %player_name% вышел с сервера!");
    }

    private void commandCollect() {
        command();
    }

    private void integrationCollect() {
        integration();
    }

    private void placeholderCollect() {
        placeholder().values().put("example", "пример");
    }
}
