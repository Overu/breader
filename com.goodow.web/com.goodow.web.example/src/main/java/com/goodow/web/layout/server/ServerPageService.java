package com.goodow.web.layout.server;

import java.util.StringTokenizer;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.goodow.web.core.server.ServerService;
import com.goodow.web.layout.shared.Direction;
import com.goodow.web.layout.shared.Layer;
import com.goodow.web.layout.shared.LayoutStyle;
import com.goodow.web.layout.shared.Page;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ServerPageService extends ServerService {

  @Inject
  ServerLayerService layerService;

  public Page findChild(final Page page, final String path) {
    Query query = em.get().createQuery("select o from Page o where o.parent=? and o.path=?");
    query.setParameter(1, page);
    query.setParameter(2, path);
    return (Page) query.getSingleResult();
  }

  public Page findPageByUri(final String uri) {
    Page result = findRootPage();
    StringTokenizer st = new StringTokenizer(uri);
    while (st.hasMoreTokens()) {
      String path = st.nextToken();
      result = findChild(result, path);
    }
    return result;
  }

  @Transactional
  public Page findRootPage() {
    // CriteriaBuilder cb = em().getCriteriaBuilder();
    // CriteriaQuery<JpaPage> c = cb.createQuery(JpaPage.class);
    // Predicate condition = cb.equal(c.from(JpaPage.class).get("path"),
    // "");
    // c.where(condition);

    // TypedQuery<JpaPage> query = em().createQuery(c);
    Query query =
        em.get().createQuery(
            "select o from " + Page.class.getName() + " o where o.parent is null and path=:path");
    query.setParameter("path", "");
    Page result = null;
    try {
      result = (Page) query.getSingleResult();
    } catch (NoResultException e) {
      result = new Page();
      result.setPath("");

      save(result);

      Layer rootLayer = new Layer();
      rootLayer.setPage(result);
      rootLayer.setTitle("顶层容器");
      rootLayer.setDirection(Direction.HORIZONTAL);
      rootLayer.setStyle(LayoutStyle.TAB);
      layerService.save(rootLayer);

      result.setLayer(rootLayer);
      save(result);

      Layer layer1 = new Layer();
      layer1.setParent(rootLayer);
      layer1.setPage(result);
      layer1.setTitle("同步加载视图>");
      layer1.setWidgetId("view1");
      layerService.save(layer1);

      Layer layer11 = new Layer();
      layer11.setParent(layer1);
      layer11.setPage(result);
      layer11.setTitle("同步加载视图1");
      layer11.setWidgetId("view11");
      layerService.save(layer11);

      Layer layer2 = new Layer();
      layer2.setParent(rootLayer);
      layer2.setPage(result);
      layer2.setTitle("异步加载视图");
      layer2.setWidgetId("view2");
      layerService.save(layer2);

      Layer layer21 = new Layer();
      layer21.setParent(layer2);
      layer21.setPage(result);
      layer21.setTitle("异步加载视图1");
      layer21.setWidgetId("view11");
      layerService.save(layer21);
    }
    return result;
  }

  @Transactional
  public Page save(final Page page) {
    em.get().persist(page);
    return page;
  }

}
