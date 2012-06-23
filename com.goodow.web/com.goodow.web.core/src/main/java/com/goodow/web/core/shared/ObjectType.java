package com.goodow.web.core.shared;

import com.google.inject.Provider;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ObjectType extends WebType implements Wrapper<ObjectType> {

  public static final String OPERATIONS = "operations";

  public static final String PROPERTIES = "properties";

  private boolean _abstract;

  private ObjectType superType;

  private transient Class<? extends WebService> serviceClass;

  private transient WebService service;

  private transient Map<String, Property> properties = new HashMap<String, Property>();

  private transient Map<String, Property> allProperties;

  private transient Map<String, Operation> operations = new HashMap<String, Operation>();

  private transient Provider<? extends WebObject> provider;

  private transient Accessor accessor;

  public void addOperation(final Operation operation) {
    operations.put(operation.getName(), operation);
  }

  public void addProperty(final Property property) {
    properties.put(property.getName(), property);
  }

  @Override
  public ObjectType as() {
    return this;
  }

  public WebObject create() {
    return provider.get();
  }

  public Accessor getAccessor() {
    return accessor;
  }

  public Map<String, Property> getAllProperties() {
    if (allProperties == null) {
      allProperties = new HashMap<String, Property>();
      allProperties.putAll(properties);
      if (superType != null) {
        allProperties.putAll(superType.getAllProperties());
      }
    }
    return allProperties;
  }

  public Operation getOperation(final String name) {
    Operation operation = operations.get(name);
    return operation;
  }

  public Map<String, Property> getProperties() {
    return properties;
  }

  public Property getProperty(final String name) {
    Property result = properties.get(name);
    return result;
  }

  @Override
  public String getQualifiedName() {
    return getPackage().getName() + "." + getName();
  }

  public WebService getService() {
    if (service == null) {
      if (superType != null) {
        service = superType.getService();
      }
    }
    return service;
  }

  public Class<? extends WebService> getServiceClass() {
    if (serviceClass == null) {
      if (WebObject.class.equals(definitionClass)) {
        serviceClass = (Class<? extends WebService>) WebService.class;
      } else {
        serviceClass = getSuperType().getServiceClass();
      }
    }
    return serviceClass;
  }

  public ObjectType getSuperType() {
    return superType;
  }

  public boolean isAbstract() {
    return _abstract;
  }

  public void setAbstract(final boolean value) {
    this._abstract = value;
  }

  public void setAccessor(final Accessor accessor) {
    this.accessor = accessor;
  }

  public void setProvider(final Provider<? extends WebObject> provider) {
    this.provider = provider;
  }

  public void setServiceClass(final Class<? extends WebService> serviceClass) {
    this.serviceClass = serviceClass;
  }

  public void setSuperType(final ObjectType superType) {
    this.superType = superType;
  }

}
