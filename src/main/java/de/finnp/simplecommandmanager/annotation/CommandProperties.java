package de.finnp.simplecommandmanager.annotation;

import de.finnp.simplecommandmanager.CommandType;
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
    String[] aliases() default {""};
    String description() default "";
    String invalidSender() default "This command can only be executed from the console!";
    String permissionMessage() default "You do not have permission to run this command!";

}
