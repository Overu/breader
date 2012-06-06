package com.goodow.web.core.shared;

public interface Reader<T, S> {

	T readFrom(S source);

}
