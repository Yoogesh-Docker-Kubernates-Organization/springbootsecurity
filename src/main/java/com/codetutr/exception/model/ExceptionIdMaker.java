package com.codetutr.exception.model;

@FunctionalInterface
public interface ExceptionIdMaker {

	String make(Throwable t);
}
