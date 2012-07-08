package com.goodow.web.core.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Serializable {

  private Map<EntityId, WebEntity> entities = new HashMap<EntityId, WebEntity>();

  private Map<WebEntity, EntityId> entityIds = new HashMap<WebEntity, EntityId>();

  public abstract WebEntity find(ObjectType objectType, String id);

  public Collection<WebEntity> getEntities() {
    return entities.values();
  }

  public WebEntity getEntity(final EntityId id) {
    WebEntity entity = entities.get(id);
    if (entity == null) {
      entity = WebPlatform.getInstance().getOrCreateEntity(id);
      entities.put(id, entity);
      entityIds.put(entity, id);
    }
    return entity;
  }

  public EntityId getEntityId(final WebEntity entity) {
    EntityId id = entityIds.get(entity);
    if (id == null) {
      id = new EntityId(entity.getObjectType().getQualifiedName());
      if (entity.getId() == null) {
        id.setClientId(Integer.toString(entities.size() + 1));
      } else {
        id.setStableId(entity.getId());
      }
      entities.put(id, entity);
      entityIds.put(entity, id);
    }
    return id;
  }

  public Collection<EntityId> getEntityIds() {
    return entities.keySet();
  }

}
