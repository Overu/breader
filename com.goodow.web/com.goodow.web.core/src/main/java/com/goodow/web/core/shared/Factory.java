package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Factory extends NamedElement {

  @Override
  public EntityType type() {
    return CorePackage.Factory.as();
  }

}
