package zfuctoneT.model;

public interface Module<T> {
    T enable();
    T disable();
    T reload();
}
