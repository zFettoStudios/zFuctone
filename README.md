# zFuctone

A powerful core for easily creating your own extensions for Minecraft servers.

## Project structure

```
zFuctone-master/
├── api/                 # Public API: interfaces, events, modules, registries
├── common/              # Shared implementations across platforms
├── minecraft-bukkit/    # Bukkit / Spigot platform implementation
├── minecraft-core/      # Core platform module (placeholder)
├── minecraft-paper/     # Paper platform implementation
├── gradle/              # Gradle wrapper
├── .github/             # CI/CD and issue templates
└── build.gradle         # Root build configuration
```

## Requirements

- Java 21+
- Gradle 8.x (wrapper included)

## Building

```bash
./gradlew build
```

The build produces shadowed JARs for each platform module in `build/compiled/`.

## Modules

Modules are registered through `ModuleController` and are enabled/disabled/reloaded
in registration order. To register a module:

```java
moduleController.register(MyModule.class, new MyModuleImpl());
```

Registered modules can be retrieved safely:

```java
MyModule module = moduleController.get(MyModule.class);
```

Attempting to register a duplicate module or to get an unregistered module
throws `IllegalStateException`.

## Platforms

- `minecraft-bukkit` — Bukkit, Spigot, and Paper (basic support).
- `minecraft-paper` — Paper-specific adapters and integrations.

## License

See `LICENSE`.
