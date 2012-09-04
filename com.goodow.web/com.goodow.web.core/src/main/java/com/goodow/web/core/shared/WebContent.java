package com.goodow.web.core.shared;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@TypeDef(name = "principal", typeClass = PrincipalType.class)
@MappedSuperclass
@XmlType
public class WebContent extends WebObject {

  @Id
  protected String id;

  @Version
  protected Long version;

  @ManyToOne
  protected User owner;

  @XmlTransient
  @Type(type = "principal")
  @Columns(columns = {@Column(name = "tenantType"), @Column(name = "tenantName")})
  private WebContent tenant;

  @XmlTransient
  @Type(type = "principal")
  @Columns(columns = {@Column(name = "containerType"), @Column(name = "containerId")})
  private WebContent container;

  public WebContent getContainer() {
    return container;
  }

  public String getId() {
    return id;
  }

  public User getOwner() {
    return owner;
  }

  @XmlTransient
  public WebContent getTenant() {
    return tenant;
  }

  public Long getVersion() {
    return version;
  }

  public void setContainer(final WebContent container) {
    this.container = container;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public void setOwner(final User owner) {
    this.owner = owner;
  }

  public void setTenant(final WebContent tenant) {
    this.tenant = tenant;
  }

  public void setVersion(final Long version) {
    this.version = version;
  }

}
