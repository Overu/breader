package com.goodow.wave.test;

import com.google.inject.Inject;
import com.google.web.bindery.autobean.vm.impl.TypeUtils;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

import org.junit.Before;

public abstract class BaseRfTest<F extends RequestFactory> extends BaseTest {

  @Inject
  protected EventBus eventBus;
  protected F f;

  @Inject
  private ServiceLayer serviceLayer;
  @Inject
  private ExceptionHandler exceptionHandler;

  @Before
  public void setUp() {
    SimpleRequestProcessor processor = new SimpleRequestProcessor(serviceLayer);
    processor.setExceptionHandler(exceptionHandler);
    @SuppressWarnings("unchecked")
    Class<F> rfClass =
        (Class<F>) TypeUtils.ensureBaseType(TypeUtils.getSingleParameterization(BaseRfTest.class,
            getClass().getGenericSuperclass()));
    f = RequestFactorySource.create(rfClass);
    f.initialize(eventBus, new InProcessRequestTransport(processor));
  }

}
