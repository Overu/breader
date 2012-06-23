package com.goodow.web.core.jpa;

import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebService;
import com.goodow.web.core.shared.Wrapper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.google.web.bindery.autobean.vm.impl.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

public class JpaWebService<E extends WebEntity> implements WebService<E> {

  protected Map<String, Method> methods;

  @Inject
  protected transient Provider<EntityManager> em;

  private Class<E> domainClass;

  @SuppressWarnings("unchecked")
  protected JpaWebService() {
    Type genericSuperClass = getClass().getGenericSuperclass();
    domainClass =
        (Class<E>) TypeUtils.ensureBaseType(TypeUtils.getSingleParameterization(WebService.class,
            genericSuperClass));
  }

  @Override
  public E find(final String id) {
    return em.get().find(getEntityClass(), id);
  }

  @Override
  public Class<E> getEntityClass() {
    return domainClass;
  }

  public <M> M getJavaMethod(final String name) {
    if (methods == null) {
      methods = new HashMap<String, Method>();
      for (Class<? extends WebService> intf : getServiceInterfaces(getClass())) {
        for (Method m : intf.getDeclaredMethods()) {
          methods.put(m.getName(), m);
        }
      }
    }
    return (M) methods.get(name);
  }

  @Override
  public <T2> T2 invoke(final Wrapper<Operation> operation, final Object... args) {
    Method method = getJavaMethod(operation.as().getName());
    try {
      return (T2) method.invoke(this, args);
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  @Transactional
  public void save(final E domain) {
    if (domain.getId() == null) {
      em.get().persist(domain);
    } else {
      em.get().merge(domain);
    }
  }

  @Override
  @Transactional
  public void remove(final E domain) {
    em.get().remove(domain);
  }

  protected EntityManager em() {
    return em.get();
  }

  private Set<Class<? extends WebService>> getServiceInterfaces(final Class clazz) {
    Set<Class<? extends WebService>> result = new HashSet<Class<? extends WebService>>();
    for (Class<?> intf : clazz.getInterfaces()) {
      if (WebService.class.isAssignableFrom(intf)) {
        result.add((Class<? extends WebService>) intf);
      }
    }
    if (!Object.class.equals(clazz)) {
      result.addAll(getServiceInterfaces(clazz.getSuperclass()));
    }
    return result;
  }

}
