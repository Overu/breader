package com.goodow.web.core.shared;

import java.util.List;

public interface SectionService extends WebService<Section> {

  @Override
  public List<Section> find(WebEntity container);

  @Override
  public Section save(Section entity);

}
