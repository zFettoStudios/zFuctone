package com.zfettostudios.zfuctone.annotation.event;

import com.zfettostudios.zfuctone.model.event.Event;
import org.jspecify.annotations.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    Event.@NonNull Priority priority() default Event.Priority.NORMAL;
    boolean ignoreCancelled() default false;
}