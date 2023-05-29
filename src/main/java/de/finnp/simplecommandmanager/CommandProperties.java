package de.finnp.simplecommandmanager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandProperties {
    CommandType type() default CommandType.UNIVERSAL;
    String usage() default "/%label%";
    String permission() default "";
    String[] aliases() default "";

    enum CommandType {
        UNIVERSAL,
        PLAYER,
        CONSOLE;
    }
}
