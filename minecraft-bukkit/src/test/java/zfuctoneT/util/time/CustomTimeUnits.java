package zfuctoneT.util.time;

import com.zfettostudios.zjtime.Time;
import com.zfettostudios.zjtime.TimeUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomTimeUnits {
    public static final TimeUnit TICKS = TimeUnit.create(50L, TimeUnit.MILLISECONDS, "ticks", "TICKS");

    public static TimeUnit parse(String name) {
        for (TimeUnit timeUnit : Time.TIME_UNITS) if (name.equalsIgnoreCase(timeUnit.getName().toString())) return timeUnit;
        return null;
    }
}
