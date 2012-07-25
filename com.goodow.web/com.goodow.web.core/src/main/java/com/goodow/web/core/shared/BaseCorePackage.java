package com.goodow.web.core.shared;

import java.util.Date;

public class BaseCorePackage extends Package {

  public static final ValueInfo<Boolean> BOOLEAN = new ValueInfo<Boolean>(Boolean.class) {
    @Override
    public java.lang.Boolean convertFrom(final java.lang.String stringValue) {
      return java.lang.Boolean.valueOf(stringValue);
    }
  };
  public static final ValueInfo<Boolean> Boolean = new ValueInfo<Boolean>(Boolean.class) {
    @Override
    public java.lang.Boolean convertFrom(final java.lang.String stringValue) {
      return java.lang.Boolean.valueOf(stringValue);
    }
  };
  public static final ValueInfo<Integer> INT = new ValueInfo<Integer>(int.class) {
    @Override
    public java.lang.Integer convertFrom(final java.lang.String stringValue) {
      return java.lang.Integer.valueOf(stringValue);
    }
  };
  public static final ValueInfo<Integer> Integer = new ValueInfo<Integer>(Integer.class) {
    @Override
    public java.lang.Integer convertFrom(final java.lang.String stringValue) {
      return java.lang.Integer.valueOf(stringValue);
    }
  };
  public static final ValueInfo<Long> LONG = new ValueInfo<Long>(long.class) {
    @Override
    public java.lang.Long convertFrom(final java.lang.String stringValue) {
      return java.lang.Long.valueOf(stringValue);
    }
  };
  public static final ValueInfo<Long> Long = new ValueInfo<Long>(Long.class) {
    @Override
    public java.lang.Long convertFrom(final java.lang.String stringValue) {
      return java.lang.Long.valueOf(stringValue);
    }
  };
  public static final ValueInfo<String> String = new ValueInfo<String>(String.class) {
    @Override
    public java.lang.String convertFrom(final java.lang.String stringValue) {
      return stringValue;
    }
  };

  public static final ValueInfo<Date> Date = new ValueInfo<Date>(Date.class) {
    @Override
    public Date convertFrom(final java.lang.String stringValue) {
      return new Date(java.lang.Long.parseLong(stringValue));
    }
  };
  public static final ValueInfo<Role> Role = new ValueInfo<Role>(Role.class) {
    @Override
    public Role convertFrom(final java.lang.String stringValue) {
      // TODO
      return null;
    }
  };
}
