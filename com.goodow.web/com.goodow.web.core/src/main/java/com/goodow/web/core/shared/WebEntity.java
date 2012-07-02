package com.goodow.web.core.shared;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@TypeDef(name = "principal", typeClass = RoleType.class)
@MappedSuperclass
@XmlType
public class WebEntity extends WebObject {

  @Id
  protected String id;

  @Version
  protected Long version;

  @ManyToOne
  protected User owner;

  @XmlTransient
  @org.hibernate.annotations.Type(type = "principal")
  @Columns(columns = {@Column(name = "tenantType"), @Column(name = "tenantName")})
  private Principal tenant;

  public String getId() {
    return id;
  }

  public User getOwner() {
    return owner;
  }

  @XmlTransient
  public Principal getTenant() {
    return tenant;
  }

  public Long getVersion() {
    return version;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public void setOwner(final User owner) {
    this.owner = owner;
  }

  public void setTenant(final Principal tenant) {
    this.tenant = tenant;
  }

  public void setVersion(final Long version) {
    this.version = version;
  }

}
