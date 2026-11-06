package zfuctoneT.entity;

import java.util.UUID;

public interface ZPlayer extends ZEntity {
    boolean isOnline();

    ZPlayer online(boolean online);
    ZPlayer name(String name);
    ZPlayer uuid(UUID uuid);
    ZPlayer type(String type);
}
