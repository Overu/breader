package com.goodow.web.core.shared;

import com.google.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Package extends NamedElement {

  private transient Map<String, WebType> types = new HashMap<String, WebType>();

  @Inject
  private static WebPlatform platform;

  protected Package() {
  }

  public void addType(final WebType type) {
    types.put(type.getName(), type);
  }

  public WebType getType(final String name) {
    WebType type = types.get(name);
    return type;
  }

  protected void addEntityTypes(final ObjectType... types) {
    for (ObjectType type : types) {
      type.setPackage(this);
      addType(type);
    }
  }

  protected <T> void addOperations(final ObjectType type, final OperationInfo... operations) {
    for (OperationInfo operationInfo : operations) {
      Operation operation = operationInfo.as();
      operation.setDeclaringType(type);
      type.addOperation(operation);
    }
  }

  protected void addParameter(final Operation operation, final String name, final WebType type) {
    Parameter param = new Parameter();
    param.setName(name);
    param.setType(type);
    operation.getParameters().put(name, param);
  }

  protected <T> void addProperty(final ObjectType entityType, final String name,
      final WebType type, final boolean many) {
    Property property = new Property();
    property.setName(name);
    property.setType(type);
    property.setMany(many);
    entityType.as().addProperty(property);
  }

  protected void addValueTypes(final ValueType... types) {
    for (ValueType type : types) {
      type.setPackage(this);
      addType(type);
    }
  }

  protected void init() {
    platform.getPackages().put(this.getName(), this);
  }

}
