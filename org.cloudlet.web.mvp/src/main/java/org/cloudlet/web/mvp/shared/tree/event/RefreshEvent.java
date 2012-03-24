package org.cloudlet.web.mvp.shared.tree.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.web.bindery.event.shared.Event;

public final class RefreshEvent<V> extends Event<RefreshEvent.Handler<V>> implements TakesValue<V> {
  public interface Handler<V> extends EventHandler {

    void onRefresh(RefreshEvent<V> event);

  }

  public static transient final Type<Handler<?>> TYPE = new Type<Handler<?>>();

  private V value;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<Handler<V>> getAssociatedType() {
    /*
     * The instance knows its handler is of type P, but the TYPE field itself
     * does not, so we have to do an unsafe cast here.
     */
    return (Type) TYPE;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public void setValue(V value) {
    this.value = value;
  }

  @Override
  protected void dispatch(Handler<V> handler) {
    handler.onRefresh(this);
  }

}
