package com.nosbielc.crawler.central.application.aspect;

import java.lang.annotation.*;

/**
 * A markup annotation to turn off a the logging Aspect to a Method.
 *
 * @author Cleibson Gomes
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggingOff {
}
