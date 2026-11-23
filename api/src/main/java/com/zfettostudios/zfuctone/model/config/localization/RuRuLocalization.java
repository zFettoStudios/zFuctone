package com.zfettostudios.zfuctone.model.config.localization;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.annotation.config.LocaleName;
import com.zfettostudios.zfuctone.model.value.ValueString;

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
        message().join().format(ValueString.build("Игрок %zfuctone_player_nickname% зашел на сервер!"));
        message().quit().format(ValueString.build("Игрок %zfuctone_player_nickname% вышел с сервера!"));
    }

    private void commandCollect() {
        command().zfuctone().reload(ValueString.build("<green>Плагин успешно перезагружен!"));
        command().zfuctone().failedReload(ValueString.build("<red>Ошибка перезагрузки плагина, проверьте консоль"));
    }

    private void integrationCollect() {
        integration();
    }

    private void placeholderCollect() {
        placeholder().values().put("example", ValueString.build("пример"));
    }
}
