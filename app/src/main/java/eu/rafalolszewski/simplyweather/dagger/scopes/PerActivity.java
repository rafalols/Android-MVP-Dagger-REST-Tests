package eu.rafalolszewski.simplyweather.dagger.scopes;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by rafal on 11.03.16.
 */

@Scope
@Retention(RUNTIME)
public @interface PerActivity {}

