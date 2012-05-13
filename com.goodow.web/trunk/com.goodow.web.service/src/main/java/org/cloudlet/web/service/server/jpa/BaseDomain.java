package org.cloudlet.web.service.server.jpa;

import org.cloudlet.web.service.server.InjectionListener;

import java.io.Serializable;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(InjectionListener.class)
@MappedSuperclass
public abstract class BaseDomain implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Version
  private Long version;

  // @Inject
  // protected transient Provider<EntityManager> em;

  public Long getId() {
    return id;
  }

  public Long getVersion() {
    return version;
  }
}
