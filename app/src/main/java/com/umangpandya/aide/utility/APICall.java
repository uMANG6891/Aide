package com.umangpandya.aide.utility;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by umang on 22/10/16.
 */

@Retention(CLASS)
@Target({TYPE, METHOD, CONSTRUCTOR, FIELD})
public @interface APICall {
}
