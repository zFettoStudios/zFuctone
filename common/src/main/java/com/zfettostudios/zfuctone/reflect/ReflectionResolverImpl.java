package com.zfettostudios.zfuctone.reflect;

public class ReflectionResolverImpl implements ReflectionResolver {
    @Override
    public boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException _) {
            return false;
        }
    }
}
