package com.goodow.web.layout.server;

import com.goodow.web.core.server.ServerService;
import com.goodow.web.layout.shared.Layer;
import com.google.inject.persist.Transactional;


public class ServerLayerService extends ServerService {

  @Transactional
  public Layer save(final Layer layer) {
    em.get().persist(layer);
    return layer;
  }

}
