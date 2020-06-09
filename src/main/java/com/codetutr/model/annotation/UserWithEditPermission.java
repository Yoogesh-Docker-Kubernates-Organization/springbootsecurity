package com.codetutr.model.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 
 * This will call the hasPermission() method of PermissionEvaluator class
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#user, 'edit')")
public @interface UserWithEditPermission {

}
