package org.cloudlet.web.service.server.jpa;

public enum PersistenceStandard {

  JPA, JDO;

  private static final String SYS_PROP = "ol.persistence.standard";

  public static synchronized PersistenceStandard get() {
    return PersistenceStandard.valueOf(System.getProperty(SYS_PROP));
  }

  public static synchronized void set(final PersistenceStandard ps) {
    System.setProperty(SYS_PROP, ps.name());
  }

  public PersistenceStandard getAlternate() {
    return PersistenceStandard.values()[(this.ordinal() + 1) % 2];
  }
}
