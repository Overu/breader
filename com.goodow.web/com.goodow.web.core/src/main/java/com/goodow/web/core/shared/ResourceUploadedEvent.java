package com.goodow.web.core.shared;

import com.google.gwt.event.shared.GwtEvent;

public class ResourceUploadedEvent extends GwtEvent<ResourceUploadedHandler> {

  public static final Type<ResourceUploadedHandler> TYPE = new Type<ResourceUploadedHandler>();

  private final Resource newResource;

  public ResourceUploadedEvent(final Resource resource) {
    this.newResource = resource;
  }

  @Override
  public Type<ResourceUploadedHandler> getAssociatedType() {
    return TYPE;
  }

  public Resource getNewResource() {
    return newResource;
  }

  @Override
  protected void dispatch(final ResourceUploadedHandler handler) {
    handler.onResourceUpload(this);
  }
}
