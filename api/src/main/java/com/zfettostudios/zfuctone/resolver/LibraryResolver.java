package com.zfettostudios.zfuctone.resolver;

import com.zfettostudios.zfuctone.module.Module;

import java.util.List;

public interface LibraryResolver extends Module<LibraryResolver> {
    default List<String> getAdventureArtifactIds() {
        return List.of(
            "adventure-api",
            "adventure-nbt",
            "adventure-key",
            "adventure-text-minimessage",
            "adventure-text-serializer-ansi",
            "adventure-text-serializer-plain",
            "adventure-text-serializer-legacy",
            "adventure-text-serializer-gson",
            "adventure-text-logger-slf4j"
        );
    }
    default List<String> getPacketEventsArtifactIds() {
        return List.of(
            "packeteventsmodern-api",
            "packeteventsmodern-netty-common",
            "packeteventsmodern-spigot"
        );
    }
}