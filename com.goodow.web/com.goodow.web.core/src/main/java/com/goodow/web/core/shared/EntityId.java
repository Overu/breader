package com.goodow.web.core.shared;

public class EntityId {

  public static EntityId parseId(final String entityId) {
    int index1 = entityId.indexOf("@");
    int index2 = entityId.indexOf("#");
    String entityType = entityId.substring(0, index1);
    EntityId id = new EntityId(entityType);
    if (index2 > 0) {
      String clientId = entityId.substring(index2 + 1);
      String stableId = entityId.substring(index1 + 1, index2);
      id.setClientId(clientId);
      if (stableId.length() > 0) {
        id.setStableId(stableId);
      }
    } else {
      String stableId = entityId.substring(index1 + 1);
      id.setStableId(stableId);
    }
    return id;
  }

  private String stableId;

  private String clientId;

  private String entityType;

  public EntityId(final String entityType) {
    this.entityType = entityType;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof EntityId) {
      EntityId that = (EntityId) obj;
      if (!this.entityType.equals(that.entityType)) {
        return false;
      }
      if (this.clientId != null) {
        return this.clientId.equals(that.clientId);
      } else if (this.stableId != null) {
        return this.stableId.equals(that.stableId);
      }
    }
    return false;
  }

  public String getClientId() {
    return clientId;
  }

  public String getEntityType() {
    return entityType;
  }

  public String getStableId() {
    return stableId;
  }

  @Override
  public int hashCode() {
    if (clientId == null && stableId == null) {
      return super.hashCode();
    } else if (clientId != null) {
      return (entityType + "#" + clientId).hashCode();
    } else {
      return (entityType + "@" + stableId).hashCode();
    }
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public void setEntityType(final String entityType) {
    this.entityType = entityType;
  }

  public void setStableId(final String stableId) {
    this.stableId = stableId;
  }

  @Override
  public String toString() {
    if (clientId == null && stableId == null) {
      return super.toString();
    }
    String result = entityType + "@";
    if (stableId != null) {
      result += stableId;
    }
    if (clientId != null) {
      result += "#" + clientId;
    }
    return result;
  }

}
