package com.goodow.web.core.shared;

public class BaseCorePackage extends Package {

	public static final ValueInfo<Boolean> BOOLEAN = new ValueInfo<Boolean>(
			Boolean.class) {
		@Override
		public java.lang.Boolean readFrom(final java.lang.String stringValue) {
			return java.lang.Boolean.valueOf(stringValue);
		}
	};
	public static final ValueInfo<Boolean> Boolean = new ValueInfo<Boolean>(
			Boolean.class) {
		@Override
		public java.lang.Boolean readFrom(final java.lang.String stringValue) {
			return java.lang.Boolean.valueOf(stringValue);
		}
	};
	public static final ValueInfo<Integer> INT = new ValueInfo<Integer>(
			int.class) {
		@Override
		public java.lang.Integer readFrom(final java.lang.String stringValue) {
			return java.lang.Integer.valueOf(stringValue);
		}
	};
	public static final ValueInfo<Integer> Integer = new ValueInfo<Integer>(
			Integer.class) {
		@Override
		public java.lang.Integer readFrom(final java.lang.String stringValue) {
			return java.lang.Integer.valueOf(stringValue);
		}
	};
	public static final ValueInfo<Long> LONG = new ValueInfo<Long>(
			long.class) {
		@Override
		public java.lang.Long readFrom(final java.lang.String stringValue) {
			return java.lang.Long.valueOf(stringValue);
		}
	};
	public static final ValueInfo<Long> Long = new ValueInfo<Long>(
			Long.class) {
		@Override
		public java.lang.Long readFrom(final java.lang.String stringValue) {
			return java.lang.Long.valueOf(stringValue);
		}
	};
	public static final ValueInfo<String> String = new ValueInfo<String>(
			String.class) {
		@Override
		public java.lang.String readFrom(final java.lang.String stringValue) {
			return stringValue;
		}
	};
}
