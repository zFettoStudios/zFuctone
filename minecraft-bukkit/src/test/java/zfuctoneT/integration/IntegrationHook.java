package zfuctoneT.integration;

public interface IntegrationHook<T> {
    T hook();
    T unhook();
}
