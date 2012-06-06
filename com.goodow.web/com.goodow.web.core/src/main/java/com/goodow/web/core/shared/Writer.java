package com.goodow.web.core.shared;

public interface Writer<T, S> {

	S writeTo(T target);

}
