package com.goodow.web.core.server;

import com.goodow.web.core.shared.Entity;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.Service;
import com.goodow.web.security.server.ServerContentService;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.vm.impl.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

public class ServerService<E extends Entity> implements Service<E> {

  protected Map<String, Method> methods;

  @Inject
  protected transient Provider<EntityManager> em;

  protected Class<E> domainClass;

  @SuppressWarnings("unchecked")
  protected ServerService() {
    domainClass =
        (Class<E>) TypeUtils.ensureBaseType(TypeUtils.getSingleParameterization(
            ServerContentService.class, getClass().getGenericSuperclass()));
  }

  @Override
  public <T> T find(final Class<T> clazz, final String id) {
    return em.get().find(clazz, id);
  }

  public <M> M getJavaMethod(final String name) {
    if (methods == null) {
      methods = new HashMap<String, Method>();
      for (Class<? extends Service> intf : getServiceInterfaces(getClass())) {
        for (Method m : intf.getDeclaredMethods()) {
          methods.put(m.getName(), m);
        }
      }
    }
    return (M) methods.get(name);
  }

  @Override
  public <T2> T2 invoke(final Operation operation, final Object... args) {
    Method method = getJavaMethod(operation.getName());
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

  protected EntityManager em() {
    return em.get();
  }

  private Set<Class<? extends Service>> getServiceInterfaces(final Class clazz) {
    Set<Class<? extends Service>> result = new HashSet<Class<? extends Service>>();
    for (Class<?> intf : clazz.getInterfaces()) {
      if (Service.class.isAssignableFrom(intf)) {
        result.add((Class<? extends Service>) intf);
      }
    }
    if (!Object.class.equals(clazz)) {
      result.addAll(getServiceInterfaces(clazz.getSuperclass()));
    }
    return result;
  }

}
