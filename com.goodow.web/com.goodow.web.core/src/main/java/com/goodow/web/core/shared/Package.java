package com.goodow.web.core.shared;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Package extends NamedElement {

  private transient Map<String, Type> types = new HashMap<String, Type>();

  public void addType(final Type type) {
    types.put(type.getName(), type);
  }

  public Type getType(final String name) {
    Type type = types.get(name);
    return type;
  }

  @Override
  public EntityType type() {
    return CorePackage.Package.as();
  }

  protected void addEntityTypes(final EntityType... types) {
    for (EntityType type : types) {
      type.setPackage(this);
      addType(type);
    }
  }

  protected <T> void addOperations(final EntityType type, final OperationInfo<?>... operations) {
    for (OperationInfo<?> baseOperation : operations) {
      Operation operation = baseOperation.as();
      operation.setDeclaringType(type);
      type.addOperation(operation);
    }
  }

  protected void addParameter(final Operation operation, final String name, final Type c) {
    Parameter param = CoreFactory.Parameter.get();
    param.setName(name);
    param.setType(c);
    operation.getParameters().put(name, param);
  }

  protected <T> void addProperty(final EntityType entityType, final String name, final Type type,
      final boolean many) {
    Property property = CoreFactory.Property.get();
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

}
