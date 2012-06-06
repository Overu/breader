package com.goodow.web.core.shared;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@XmlType
public abstract class Entity implements Serializable {

  @Id
  protected String id;

  @Version
  protected Long version;

  public Object get(final Property property) {
    return type().getAccessor().getProperty(this, property);
  }

  public String getId() {
    return id;
  }

  public Long getVersion() {
    return version;
  }

  public void set(final Property property, final Object value) {
    type().getAccessor().setProperty(this, property, value);
  }

  public void setId(final String id) {
    this.id = id;
  }

  public void setVersion(final Long version) {
    this.version = version;
  }

  public EntityType type() {
    return CorePackage.Entity.as();
  }
}
