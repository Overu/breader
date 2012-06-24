package com.goodow.web.core.shared;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Request<T> implements Serializable {

  @Inject
  private transient Provider<Message> message;

  private transient Operation operation;

  private Request<?> nextRequest;

  private transient Object[] args;

  private transient Receiver<T> receiver;

  private String contentType;

  private Map<EntityId, WebEntity> entities = new HashMap<EntityId, WebEntity>();

  private Map<WebEntity, EntityId> entityIds = new HashMap<WebEntity, EntityId>();

  public Response fire() {
    return message.get().send();
  }

  public Response fire(final Receiver<T> receiver) {
    return to(receiver).fire();
  }

  public Object[] getArgs() {
    return args;
  }

  public String getContentType() {
    return contentType;
  }

  public Collection<WebEntity> getEntities() {
    return entities.values();
  }

  public WebEntity getEntity(final EntityId id) {
    WebEntity entity = entities.get(id);
    if (entity == null) {
      entity = message.get().find(id);
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

  public Request<?> getNextRequest() {
    return nextRequest;
  }

  public Operation getOperation() {
    return operation;
  }

  public Receiver<T> getReceiver() {
    return receiver;
  }

  public void setArgs(final Object[] args) {
    this.args = args;
  }

  public Request<T> setContentType(final String contentType) {
    this.contentType = contentType;
    return this;
  }

  public void setNextRequest(final Request<?> nextRequest) {
    this.nextRequest = nextRequest;
  }

  public void setOperation(final Operation operation) {
    this.operation = operation;
  }

  public Request<T> to(final Receiver<T> receiver) {
    this.receiver = receiver;
    return this;
  }

}
