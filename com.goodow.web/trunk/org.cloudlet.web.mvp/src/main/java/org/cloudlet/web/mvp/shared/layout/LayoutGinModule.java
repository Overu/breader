package org.cloudlet.web.mvp.shared.layout;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.cloudlet.web.mvp.shared.Default;
import org.cloudlet.web.mvp.shared.BaseActivity;
import org.cloudlet.web.mvp.shared.SimpleActivityMapper;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.SimplePlaceHistoryMapper;
import org.cloudlet.web.mvp.shared.SimplePlaceTokenizer;
import org.cloudlet.web.mvp.shared.tree.TreeNodeActivity;
import org.cloudlet.web.mvp.shared.tree.TreeNodePlace;
import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;
import org.cloudlet.web.mvp.shared.tree.rpc.TreeNodeFactory;

import java.util.logging.Logger;

public final class LayoutGinModule extends AbstractGinModule {
  @Singleton
  public static class Binder {
  }

  @Singleton
  public static class BinderProvider implements Provider<Binder> {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Inject
    private SimplePlaceHistoryMapper tokenizerMapBinder;
    @Inject
    private SimplePlaceTokenizer<TreeNodePlace> treeNodeTokenizer;
    @Inject
    @Search
    private SimpleActivityMapper searchActivityMapper;
    @Inject
    @Nav
    private SimpleActivityMapper navActivityMapper;
    @Inject
    @Footer
    private SimpleActivityMapper footerActivityMapper;
    @Inject
    private Provider<BaseActivity> simpleActivityProvider;
    @Inject
    private Provider<TreeNodeActivity> treeNodeActivityProvider;

    @Override
    public Binder get() {
      logger.finest("EagerSingleton start");

      searchActivityMapper.setName(Search.KEY);
      navActivityMapper.setName(Nav.KEY);
      footerActivityMapper.setName(Footer.KEY);

      searchActivityMapper.addBinding(BasePlace.class, simpleActivityProvider);
      navActivityMapper.addBinding(BasePlace.class, simpleActivityProvider);
      footerActivityMapper.addBinding(BasePlace.class, simpleActivityProvider);

      tokenizerMapBinder.addBinding(TreeNodePlace.class, treeNodeTokenizer,
          TreeNodePlace.TOKEN_PREFIX);
      searchActivityMapper.addBinding(TreeNodePlace.class, simpleActivityProvider);
      navActivityMapper.addBinding(TreeNodePlace.class, simpleActivityProvider);
      footerActivityMapper.addBinding(TreeNodePlace.class, simpleActivityProvider);

      logger.finest("EagerSingleton end");
      return null;
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    bind(Binder.class).toProvider(BinderProvider.class).asEagerSingleton();

    bind(SimpleActivityMapper.class).annotatedWith(Search.class).to(SimpleActivityMapper.class).in(
        Singleton.class);
    bind(SimpleActivityMapper.class).annotatedWith(Nav.class).to(SimpleActivityMapper.class).in(
        Singleton.class);
    bind(SimpleActivityMapper.class).annotatedWith(Footer.class).to(SimpleActivityMapper.class).in(
        Singleton.class);
    // bind(new TypeLiteral<SimplePlaceTokenizer<TreeNodePlace>>() {
    // }).in(Singleton.class);
    // bind(new TypeLiteral<SimplePlaceTokenizer<ExclusivePlace>>() {
    // }).in(Singleton.class);

    bind(TreeNodeFactory.class).in(Singleton.class);
  }

  @Provides
  @Default
  @Singleton
  TreeNodeProxy rootTreeNodeProvider() {
    // TreeNodeProxy root = factory.treeNode().as();
    // root.setPath(SimplePlace.PATH_SEPARATOR);
    // root.setName("根");
    // // root.setChildren(new ArrayList<TreeNode>());
    // return root;
    return null;
  }

}
