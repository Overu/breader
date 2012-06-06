package com.goodow.web.core.shared;


public abstract class Serializer<T> implements TextReader<T>, TextWriter<T> {

	// @Override
	@Override
	public abstract T readFrom(String source);

	// @Override
	@Override
	public String writeTo(final T target) {
		return target == null ? null : target.toString();
	}

}
