package com.zfettostudios.zfuctone.model.config.localization;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.annotation.config.LocaleName;
import com.zfettostudios.zfuctone.provider.FactoryProvider;

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
        message().join().format(FactoryProvider.modelFactory().buildValueString("Игрок %zfuctone_player_nickname% зашел на сервер!"));
        message().quit().format(FactoryProvider.modelFactory().buildValueString("Игрок %zfuctone_player_nickname% вышел с сервера!"));
    }

    private void commandCollect() {
        command();
    }

    private void integrationCollect() {
        integration();
    }

    private void placeholderCollect() {
        placeholder().values().put("example", FactoryProvider.modelFactory().buildValueString("пример"));
    }
}
