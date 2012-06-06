package com.goodow.web.core.server.jpa;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.BindingAnnotation;

/**
 * A binding annotation for internal JPA module properties.
 * 
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
@interface Jpa {
}
