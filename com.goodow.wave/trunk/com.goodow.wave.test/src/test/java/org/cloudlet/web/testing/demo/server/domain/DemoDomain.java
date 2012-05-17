package org.cloudlet.web.testing.demo.server.domain;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
public class DemoDomain implements Serializable {
  @Inject
  private transient Provider<EntityManager> em;
  private String a;
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  private DemoDomain d;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  public String getA() {
    return a;
  }

  public DemoDomain getD() {
    return d;
  }

  public Long getId() {
    return id;
  }

  public Long getVersion() {
    return version;
  }

  public void setA(final String a) {
    this.a = a;
  }

  public void setD(final DemoDomain d) {
    this.d = d;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public void setVersion(final Long version) {
    this.version = version;
  }
}