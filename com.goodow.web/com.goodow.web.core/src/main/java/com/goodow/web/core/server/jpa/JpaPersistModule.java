package com.goodow.web.core.server.jpa;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import com.google.inject.persist.finder.DynamicFinder;
import com.google.inject.persist.finder.Finder;
import com.google.inject.util.Providers;

/**
 * JPA provider for guice persist.
 * 
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public final class JpaPersistModule extends PersistModule {
  private final String jpaUnit;

  private Properties properties;

  private MethodInterceptor transactionInterceptor;
  private final List<Class<?>> dynamicFinders = new ArrayList<Class<?>>();

  public JpaPersistModule(final String jpaUnit) {
    Preconditions.checkArgument(null != jpaUnit && jpaUnit.length() > 0,
        "JPA unit name must be a non-empty string.");
    this.jpaUnit = jpaUnit;
  }

  /**
   * Adds an interface to this module to use as a dynamic finder.
   * 
   * @param iface Any interface type whose methods are all dynamic finders.
   */
  public <T> JpaPersistModule addFinder(final Class<T> iface) {
    dynamicFinders.add(iface);
    return this;
  }

  /**
   * Configures the JPA persistence provider with a set of properties.
   * 
   * @param properties A set of name value pairs that configure a JPA persistence provider as per
   *          the specification.
   */
  public JpaPersistModule properties(final Properties properties) {
    this.properties = properties;
    return this;
  }

  @Override
  protected void configurePersistence() {
    bindConstant().annotatedWith(Jpa.class).to(jpaUnit);

    if (null != properties) {
      bind(Properties.class).annotatedWith(Jpa.class).toInstance(properties);
    } else {
      bind(Properties.class).annotatedWith(Jpa.class).toProvider(Providers.<Properties> of(null));
    }

    bind(JpaPersistService.class).in(Singleton.class);

    bind(PersistService.class).to(JpaPersistService.class);
    bind(UnitOfWork.class).to(JpaPersistService.class);
    bind(EntityManager.class).toProvider(JpaPersistService.class);
    bind(EntityManagerFactory.class).toProvider(
        JpaPersistService.EntityManagerFactoryProvider.class);

    transactionInterceptor = new JpaLocalTxnInterceptor();
    requestInjection(transactionInterceptor);

    // Bind dynamic finders.
    for (Class<?> finder : dynamicFinders) {
      bindFinder(finder);
    }
  }

  @Override
  protected MethodInterceptor getTransactionInterceptor() {
    return transactionInterceptor;
  }

  private <T> void bindFinder(final Class<T> iface) {
    if (!isDynamicFinderValid(iface)) {
      return;
    }

    InvocationHandler finderInvoker = new InvocationHandler() {
      @Inject
      JpaFinderProxy finderProxy;

      @Override
      public Object invoke(final Object thisObject, final Method method, final Object[] args)
          throws Throwable {

        // Don't intercept non-finder methods like equals and hashcode.
        if (!method.isAnnotationPresent(Finder.class)) {
          // NOTE(dhanji): This is not ideal, we are using the invocation handler's equals
          // and hashcode as a proxy (!) for the proxy's equals and hashcode.
          return method.invoke(this, args);
        }

        return finderProxy.invoke(new MethodInvocation() {
          @Override
          public Object[] getArguments() {
            return null == args ? new Object[0] : args;
          }

          @Override
          public Method getMethod() {
            return method;
          }

          @Override
          public AccessibleObject getStaticPart() {
            throw new UnsupportedOperationException();
          }

          @Override
          public Object getThis() {
            throw new UnsupportedOperationException("Bottomless proxies don't expose a this.");
          }

          @Override
          public Object proceed() throws Throwable {
            return method.invoke(thisObject, args);
          }
        });
      }
    };
    requestInjection(finderInvoker);

    @SuppressWarnings("unchecked")
    // Proxy must produce instance of type given.
    T proxy =
        (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
            new Class<?>[] {iface}, finderInvoker);

    bind(iface).toInstance(proxy);
  }

  private boolean isDynamicFinderValid(final Class<?> iface) {
    boolean valid = true;
    if (!iface.isInterface()) {
      addError(iface + " is not an interface. Dynamic Finders must be interfaces.");
      valid = false;
    }

    for (Method method : iface.getMethods()) {
      DynamicFinder finder = DynamicFinder.from(method);
      if (null == finder) {
        addError("Dynamic Finder methods must be annotated with @Finder, but " + iface + "."
            + method.getName() + " was not");
        valid = false;
      }
    }
    return valid;
  }
}
