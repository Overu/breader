package com.goodow.web.core.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

public abstract class Message implements Serializable {

  public static final String SERVER_ERROR = "Server Error";

  public static final String URL = "rpc";

  public static final String JSON_CONTENT_TYPE_UTF8 = "application/json; charset=utf-8";

  protected final Logger logger = Logger.getLogger(getClass().getName());

  protected Stack<Request<?>> requests = new Stack<Request<?>>();

  private Map<EntityId, WebObject> entities = new HashMap<EntityId, WebObject>();

  private Map<WebObject, EntityId> entityIds = new HashMap<WebObject, EntityId>();

  @SuppressWarnings("rawtypes")
  protected Request request;

  @SuppressWarnings("rawtypes")
  protected Response response;

  private Set<WebContent> serialized = new HashSet<WebContent>();

  @SuppressWarnings("rawtypes")
  public Message() {
    request = new Request(this);
    response = new Response(this);
  }

  public abstract WebContent find(ObjectType objectType, String id);

  public Message fire() {
    return this;
  }

  public Collection<WebObject> getEntities() {
    return entities.values();
  }

  public WebObject getEntity(final EntityId id) {
    WebObject entity = entities.get(id);
    if (entity == null) {
      if (id.getStableId() != null) {
        entity = find(id.getObjectType(), id.getStableId());
      }
      if (entity == null) {
        entity = id.getObjectType().create();
      }
      entities.put(id, entity);
      entityIds.put(entity, id);
    }
    return entity;
  }

  public EntityId getEntityId(final WebContent entity) {
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

  @SuppressWarnings("unchecked")
  public <T> Request<T> getRequest() {
    return request;
  }

  @SuppressWarnings("unchecked")
  public <T> Response<T> getResponse() {
    return response;
  }

  public boolean isSerialized(final WebContent entity) {
    return serialized.contains(entity);
  }

  public void setSerialized(final WebContent entity) {
    serialized.add(entity);
  }

}
