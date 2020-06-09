package com.codetutr.model.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PostFilter;

@Retention(RetentionPolicy.RUNTIME)
@PostFilter("principal.getUsername() == filterObject.owner or " + "principal.getUsername() == filterObject.attendee")
public @interface PostFilterEvents {

}
