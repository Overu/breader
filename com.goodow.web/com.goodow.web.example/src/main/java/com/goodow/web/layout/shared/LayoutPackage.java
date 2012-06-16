package com.goodow.web.layout.shared;

import com.goodow.web.core.shared.*;

@com.google.inject.Singleton
public class LayoutPackage extends com.goodow.web.core.shared.Package {
  public static final EnumInfo<Direction> Direction = new EnumInfo<Direction>(Direction.class);
  public static final EnumInfo<LayoutStyle> LayoutStyle = new EnumInfo<LayoutStyle>(LayoutStyle.class);
  public LayoutPackage() {
    setName("com.goodow.web.layout.shared");
    addEntityTypes();
    addValueTypes(Direction.as(), LayoutStyle.as());
  }
}
