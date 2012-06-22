package com.goodow.web.core.shared;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrincipalType implements UserType {

  @Override
  public Object assemble(final Serializable cached, final Object arg1) throws HibernateException {
    return cached;
  }

  @Override
  public Object deepCopy(final Object value) throws HibernateException {
    return value;
  }

  @Override
  public Serializable disassemble(final Object value) throws HibernateException {
    return (Serializable) value;
  }

  @Override
  public boolean equals(final Object o1, final Object o2) throws HibernateException {
    if (o1 == o2) {
      return true;
    }
    if (o1 == null || o2 == null) {
      return false;
    }
    final Role role1 = (Role) o1;
    final Role role2 = (Role) o2;
    return role1.getClass() == role2.getClass() && role1.name().equals(role2.name());
  }

  @Override
  public int hashCode(final Object value) throws HibernateException {
    return value.hashCode();
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Object nullSafeGet(final ResultSet resultSet, final String[] names,
      final SessionImplementor session, final Object owner) throws HibernateException, SQLException {
    try {
      String type = StringType.INSTANCE.nullSafeGet(resultSet, names[0], session);
      String name = StringType.INSTANCE.nullSafeGet(resultSet, names[1], session);
      if (type != null && name != null) {
        Class<?> clazz = Class.forName(type);
        if (Enum.class.isAssignableFrom(clazz)) {
          Class<? extends Enum> enumClass = (Class<? extends Enum>) clazz;
          return Enum.valueOf(enumClass, name);
        } else {
          // TODO load entity from db
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }

  @Override
  public void nullSafeSet(final PreparedStatement st, final Object value, final int index,
      final SessionImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      StringType.INSTANCE.nullSafeSet(st, null, index, session);
      StringType.INSTANCE.nullSafeSet(st, null, index + 1, session);
    } else {
      Role role = (Role) value;
      StringType.INSTANCE.nullSafeSet(st, role.getClass().getName(), index, session);
      StringType.INSTANCE.nullSafeSet(st, role.name(), index + 1, session);
    }
  }

  @Override
  public Object replace(final Object original, final Object arg1, final Object arg2)
      throws HibernateException {
    return original;
  }

  @Override
  public Class<?> returnedClass() {
    return Role.class;
  }

  @Override
  public int[] sqlTypes() {
    return new int[] {StringType.INSTANCE.sqlType(), StringType.INSTANCE.sqlType()};
  }

}