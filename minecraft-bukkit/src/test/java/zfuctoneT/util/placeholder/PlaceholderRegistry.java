package zfuctoneT.util.placeholder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class PlaceholderRegistry {
    Map<String, PlaceholderBinding> bindings = new ConcurrentHashMap<>();

    @FunctionalInterface
    public interface PlaceholderFunction {
        String apply(Object target, List<String> args);
    }

    private record PlaceholderBinding(
        String objectKey,
        List<Class<?>> expectedClasses,
        PlaceholderFunction function
    ) {
    }

    public PlaceholderRegistry register(
        String placeholder,
        String objectKey,
        BiFunction<Object, List<String>, String> function,
        Class<?>... classes
    ) {
        bindings.put(placeholder.toLowerCase(), new PlaceholderBinding(
            objectKey.toLowerCase(),
            Arrays.stream(classes).toList(),
            function::apply
        ));
        return this;
    }

    public String resolve(String placeholder, Map<String, Object> rawContext, List<String> args) {
        PlaceholderBinding binding = bindings.get(placeholder.toLowerCase());
        if (binding == null) return null;

        Object target = rawContext.get(binding.objectKey());
        if (target == null) return null;

        boolean isAssignable = binding.expectedClasses()
            .stream()
            .anyMatch(targetClass -> targetClass.isInstance(target));

        if (!isAssignable) return null;

        return binding.function().apply(target, args);
    }
}