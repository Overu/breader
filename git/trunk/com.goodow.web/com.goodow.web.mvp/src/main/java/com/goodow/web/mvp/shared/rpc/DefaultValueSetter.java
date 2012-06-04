package com.goodow.web.mvp.shared.rpc;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.AutoBeanVisitor;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultValueSetter {

  private static final Map<Class<?>, Object> DEFAULT_VALUES;

  static {
    Map<Class<?>, Object> temp = new HashMap<Class<?>, Object>();
    temp.put(Boolean.class, Boolean.FALSE);
    temp.put(Byte.class, Byte.valueOf((byte) 0));
    temp.put(Character.class, Character.valueOf((char) 0));
    temp.put(Double.class, Double.valueOf(0));
    temp.put(Float.class, Float.valueOf(0));
    temp.put(Integer.class, Integer.valueOf(0));
    temp.put(Long.class, Long.valueOf(0));
    temp.put(Short.class, Short.valueOf((short) 0));
    temp.put(String.class, "");

    DEFAULT_VALUES = Collections.unmodifiableMap(temp);
  }

  public <T extends BaseProxy> T setDefaultValue(final T proxy) {
    AutoBean<T> bean = AutoBeanUtils.getAutoBean(proxy);
    bean.accept(new AutoBeanVisitor() {
      @Override
      public boolean visitValueProperty(final String propertyName, final Object value,
          final PropertyContext ctx) {
        Class<?> type = ctx.getType();
        if (!type.isPrimitive() && ctx.canSet() && propertyName != "id"
            && propertyName != "version") {
          Object defaultValue = DEFAULT_VALUES.get(type);
          if (defaultValue == null && Date.class == type) {
            defaultValue = new Date();
          }
          ctx.set(defaultValue);
        }
        return false;
      }
    });
    return proxy;
  }
}
