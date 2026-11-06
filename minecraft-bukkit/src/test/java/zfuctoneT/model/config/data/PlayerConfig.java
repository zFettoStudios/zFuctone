package zfuctoneT.model.config.data;

import com.zfettostudios.zjtime.Time;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
@Accessors(fluent = true)
public class PlayerConfig {
    @NonFinal UUID uuid;
    @NonFinal String nickname;
    @NonFinal String displayNickname;
    @NonFinal String lastIp;
    @NonFinal String lastVersion;
    @NonFinal BigDecimal balance;
    @NonFinal List<String> historyIp = new CopyOnWriteArrayList<>();
    @NonFinal List<String> historyVersion = new CopyOnWriteArrayList<>();
    @NonFinal Map<String, Time> cooldowns = new ConcurrentHashMap<>();
}
