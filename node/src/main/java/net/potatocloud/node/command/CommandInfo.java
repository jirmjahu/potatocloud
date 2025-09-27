package net.potatocloud.node.command;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String name();

    String description() default "";

    String[] aliases() default {};

}
