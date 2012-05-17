package org.cloudlet.web.mvp;

import com.goodow.wave.test.BaseTest;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.EntityProxyChange;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;
import org.cloudlet.web.mvp.shared.tree.rpc.TreeNodeContext;
import org.cloudlet.web.mvp.shared.tree.rpc.TreeNodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class RfTest extends BaseTest {
  public static class RfTestModule extends BaseTestModule {
    @Provides
    @Singleton
    RequestTransport requestTransportProvider(final ServiceLayer serviceLayer,
        final ExceptionHandler exceptionHandler) {
      SimpleRequestProcessor processor = new SimpleRequestProcessor(serviceLayer);
      processor.setExceptionHandler(exceptionHandler);
      return new InProcessRequestTransport(processor);
    }
  }

  @Inject
  EventBus eventBus;

  @Inject
  RequestTransport requestTransport;

  TreeNodeFactory f;

  @Before
  public void setUp() {
    f = RequestFactorySource.create(TreeNodeFactory.class);
    f.initialize(eventBus, requestTransport);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testRf() {
    EntityProxyChange.registerForProxyType(eventBus, TreeNodeProxy.class,
        new EntityProxyChange.Handler<TreeNodeProxy>() {

          @Override
          public void onProxyChange(final EntityProxyChange<TreeNodeProxy> event) {
            EntityProxyId<TreeNodeProxy> proxyId = event.getProxyId();
            f.find(proxyId).fire(new Receiver<TreeNodeProxy>() {

              @Override
              public void onSuccess(final TreeNodeProxy response) {
              }
            });
          }
        });

    TreeNodeContext ctx = f.treeNodeContext();
    TreeNodeProxy root = ctx.create(TreeNodeProxy.class);
    TreeNodeProxy child = ctx.create(TreeNodeProxy.class);
    child.setPath("child path");
    root.setPath("root path");
    root.setName("root name");
    root.setChildren(Arrays.asList(child));
    ctx.put(root);
    ctx.find(0, 5, "id").with("children").to(new Receiver<List<TreeNodeProxy>>() {

      @Override
      public void onSuccess(final List<TreeNodeProxy> response) {

      }
    });
    ctx.fire(new Receiver<Void>() {
      @Override
      public void onConstraintViolation(final Set<ConstraintViolation<?>> violations) {
        super.onConstraintViolation(violations);
      }

      @Override
      public void onFailure(final ServerFailure error) {
        super.onFailure(error);
      }

      @Override
      public void onSuccess(final Void response) {
        // TODO Auto-generated method stub

      }
    });
    //
    // EntityProxyId<Proxy> proxyId = proxy.stableId();
    // f.find(proxyId).fire(new Receiver<Proxy>() {
    //
    // @Override
    // public void onSuccess(Proxy response) {
    // assertEquals("a1", response.getA());
    // }
    // });

    // ReqContext reqContext2 = f.reqContext();
    // Proxy proxy2 = reqContext.edit(proxy);
    // proxy2.setA("a2");
    // reqContext2.merge().using(proxy2);
    // reqContext2.fire();

  }

  @Override
  protected Class<? extends Module> providesModule() {
    return RfTestModule.class;
  }
}
