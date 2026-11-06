package zfuctoneT.entity;

import java.util.UUID;

public interface ZEntity {
    UUID uuid();
    String name();
    String type();

    ZEntity name(String name);
    ZEntity uuid(UUID uuid);
    ZEntity type(String type);
}
