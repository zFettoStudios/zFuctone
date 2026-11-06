package zfuctoneT.util.placeholder;

import androidx.collection.ArrayMap;
import zfuctoneT.model.constant.PlaceholderNames;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
public class PlaceholderContext implements TagResolver {
    @Getter
    @Accessors(fluent = true)
    private static final PlaceholderRegistry registry = new PlaceholderRegistry();

    Map<String, Object> rawContext = new ArrayMap<>();

    @Override
    public Tag resolve(@NonNull String name, ArgumentQueue arguments, @NonNull Context context) {
        List<String> args = new ArrayList<>();
        while (arguments.hasNext()) args.add(arguments.pop().value());

        Object result = registry.resolve(name, rawContext, args);
        if (result != null) return Tag.inserting(context.deserialize(result.toString()));

        return null;
    }

    @Override
    public boolean has(@NonNull String name) {
        if (rawContext.containsKey(name)) return true;
        int separatorIndex = name.indexOf('_');
        return separatorIndex != -1 && rawContext.containsKey(name.substring(0, separatorIndex));
    }

    public PlaceholderContext set(String name, Object object) {
        rawContext.put(name, object);
        return this;
    }

    public PlaceholderContext setSender(CommandSender sender) {
        rawContext.put(PlaceholderNames.SENDER.value, sender);
        return this;
    }

    public PlaceholderContext setRawText(String rawText) {
        rawContext.put(PlaceholderNames.RAWTEXT.value, rawText);
        return this;
    }

    public Optional<CommandSender> getSender() {
        return get(PlaceholderNames.SENDER.value, CommandSender.class);
    }

    public Optional<String> getRawText() {
        return get(PlaceholderNames.RAWTEXT.value, String.class);
    }

    public <T> Optional<T> get(String name, Class<T> target) {
        Object value = rawContext.get(name);
        if (target.isInstance(value)) return Optional.of(target.cast(value));

        return Optional.empty();
    }

    public <T> Optional<T> get(Class<T> target) {
        for (Object value : rawContext.values()) if (target.isInstance(value)) return Optional.of(target.cast(value));

        return Optional.empty();
    }
}