package com.goodow.web.core.shared;

import com.google.inject.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class ObjectType extends WebType implements Wrapper<ObjectType> {

  public static ObjectType VARIABLE = new ObjectType();

  public static final String OPERATIONS = "operations";

  public static final String PROPERTIES = "properties";

  private boolean _abstract;

  private ObjectType superType;

  private String feedPath;

  private transient Class<? extends WebService> serviceClass;

  private transient WebService service;

  private transient Map<String, Property> properties = new HashMap<String, Property>();

  private transient Map<String, Property> allProperties;

  private transient Map<String, Operation> operations = new HashMap<String, Operation>();

  private transient Provider<? extends WebObject> provider;

  private transient Accessor accessor;

  private transient Map<String, ObjectReader> readers = new HashMap<String, ObjectReader>();

  private transient Map<String, ObjectWriter> writers = new HashMap<String, ObjectWriter>();

  private transient Map<ViewType, Object> viewers = new HashMap<ViewType, Object>();

  public void addOperation(final Operation operation) {
    operations.put(operation.getName(), operation);
  }

  public void addProperty(final Property property) {
    properties.put(property.getName(), property);
  }

  public <T, W extends WebObject> void addReader(final Class<T> mediatorType,
      final ObjectReader<W, T> provider) {
    readers.put(mediatorType.getName(), provider);
  }

  public void addViewer(final ViewType type, final Object viewer) {
    viewers.put(type, viewer);
  }

  public <T, W extends WebObject> void addWriter(final Class<T> mediatorType,
      final ObjectWriter<W, T> provider) {
    writers.put(mediatorType.getName(), provider);
  }

  @Override
  public ObjectType as() {
    return this;
  }

  public WebObject create() {
    return provider.get();
  }

  @XmlTransient
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

  public String getFeedPath() {
    if (feedPath == null) {
      feedPath = getName() + "s";
    }
    return feedPath;
  }

  public Object getNativeViewer(final String name) {
    ViewType viewer = getViewer(name);
    if (viewer != null) {
      return getNativeViewer(viewer);
    }
    return null;
  }

  public Object getNativeViewer(final ViewType v) {
    Object result = viewers.get(v);
    if (result == null && superType != null) {
      result = superType.getNativeViewer(v);
    }
    return result;
  }

  public Operation getOperation(final String name) {
    Operation operation = operations.get(name);
    if (operation == null && superType != null) {
      operation = superType.getOperation(name);
    }
    return operation;
  }

  public Map<String, Property> getProperties() {
    return properties;
  }

  public Property getProperty(final String name) {
    Property result = properties.get(name);
    return result;
  }

  public <W extends WebObject, T> ObjectReader<W, T> getProvider(final Class<T> mediatorType) {
    ObjectReader<W, T> result = (ObjectReader<W, T>) readers.get(mediatorType.getName());
    if (result == null && !readers.containsKey(mediatorType.getName())) {
      result = getSuperType().getProvider(mediatorType);
      readers.put(mediatorType.getName(), result);
    }
    return result;
  }

  @Override
  public String getQualifiedName() {
    return getPackage().getName() + "." + getName();
  }

  @XmlTransient
  public WebService getService() {
    if (service == null) {
      if (superType != null) {
        service = superType.getService();
      }
    }
    return service;
  }

  @XmlTransient
  public Class<? extends WebService> getServiceClass() {
    if (serviceClass == null) {
      if (WebObject.class.equals(javaClass)) {
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

  public ViewType getViewer(final String name) {
    for (ViewType v : viewers.keySet()) {
      if (name.equals(v.getName())) {
        return v;
      }
    }
    return null;
  }

  public Set<ViewType> getViewers() {
    return viewers.keySet();
  }

  public <W extends WebObject, T> ObjectWriter<W, T> getWriter(final Class<T> mediatorType) {
    ObjectWriter<W, T> result = (ObjectWriter<W, T>) writers.get(mediatorType.getName());
    if (result == null && !writers.containsKey(mediatorType.getName())) {
      result = getSuperType().getWriter(mediatorType);
      writers.put(mediatorType.getName(), result);
    }
    return result;
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

  public void setFeedPath(final String feedName) {
    this.feedPath = feedName;
  }

  public void setProvider(final Provider<? extends WebObject> provider) {
    this.provider = provider;
  }

  public void setService(final WebService service) {
    this.service = service;
  }

  public void setServiceClass(final Class<? extends WebService> serviceClass) {
    this.serviceClass = serviceClass;
  }

  public void setSuperType(final ObjectType superType) {
    this.superType = superType;
  }

}
