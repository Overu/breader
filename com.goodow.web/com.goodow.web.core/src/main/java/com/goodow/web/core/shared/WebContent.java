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

  private String path;

  private transient Property contaimentProperty;

  public Property getContaimentProperty() {
    if (contaimentProperty == null) {
      contaimentProperty = container.getObjectType().getProperty(path);
    }
    return contaimentProperty;
  }

  public WebContent getContainer() {
    return container;
  }

  public String getId() {
    return id;
  }

  public User getOwner() {
    return owner;
  }

  public String getPath() {
    return path;
  }

  @XmlTransient
  public WebContent getTenant() {
    return tenant;
  }

  public String getUri() {
    return getUriBuilder().toString();
  }

  public StringBuilder getUriBuilder() {
    if (container == null) {
      return new StringBuilder();
    }
    StringBuilder builder = container.getUriBuilder();
    builder.append("/");
    if (path == null) {
      if (getContaimentProperty().isMany()) {
        path = getObjectType().getFeedPath();
      } else {
        path = getObjectType().getName();
      }
    }
    builder.append(path);
    if (getContaimentProperty().isMany()) {
      builder.append("/").append(getId());
    }
    return builder;
  }

  public Long getVersion() {
    return version;
  }

  public void setContaimentProperty(final Property contaimentProperty) {
    this.contaimentProperty = contaimentProperty;
    this.path = contaimentProperty.getName();
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

  public void setPath(final String path) {
    this.path = path;
  }

  public void setTenant(final WebContent tenant) {
    this.tenant = tenant;
  }

  public void setVersion(final Long version) {
    this.version = version;
  }
}
