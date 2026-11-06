package zfuctoneT.model.config.setting;

import com.zfettostudios.zjtime.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

@Getter
@Setter
@Accessors(fluent = true)
public class TickerConfigSetting implements EnableSetting {
    @NonFinal Boolean enable;
    @NonFinal Long period;
    @NonFinal TimeUnit timeUnit;
}
