package com.goodow.web.core.jpa;

import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebService;
import com.goodow.web.core.shared.Wrapper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.vm.impl.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

public class JpaWebService<E extends WebObject> implements WebService<E> {
  private static final Logger logger = Logger.getLogger(JpaWebService.class.getName());
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

  public Method getJavaMethod(final Operation operation) {
    Method method = operation.getJavaMethod();
    if (method == null) {
      for (Method m : operation.getDeclaringType().getServiceClass().getMethods()) {
        if (m.getName().equals(operation.getName())) {
          method = m;
          operation.setJavaMethod(method);
          break;
        }
      }
    }
    return method;
  }

  @Override
  public Class<E> getObjectType() {
    return domainClass;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T2> T2 invoke(final Wrapper<Operation> operation, final Object... args) {
    Method method = getJavaMethod(operation.as());
    try {
      return (T2) method.invoke(this, args);
    } catch (InvocationTargetException e) {
      Throwable t = e.getCause();
      t.printStackTrace();
      logger.severe(t.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      logger.severe(e.getMessage());
    }
    return null;
  }

  protected EntityManager em() {
    return em.get();
  }

}
