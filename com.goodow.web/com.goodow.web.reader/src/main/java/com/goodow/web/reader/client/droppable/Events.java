package com.goodow.web.reader.client.droppable;

import com.goodow.web.reader.client.editgrid.Function;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

public class Events extends SimpleQuery {

  public static final Class<Events> Events = registerPlugin(Events.class, new Plugin<Events>() {
    @Override
    public Events init(final SimpleQuery sq) {
      return new Events(sq);
    }
  });

  public static native boolean hasProperty(JavaScriptObject o, String name)/*-{
                                                                           return name in o;
                                                                           }-*/;

  private static boolean isEventCapable(final Node n) {
    return hasProperty(n, "alert") || n.getNodeType() != 3 && n.getNodeType() != 8;
  }

  private int[] eventbits;
  private int[] eventbits2;
  private int[] eventbits3;

  protected Events(final SimpleQuery sq) {
    super(sq);
  }

  public Events bind(final int eventbits, final Object data, final Function... funcs) {
    for (Element e : elements()) {
      if (isEventCapable(e)) {
        EventsListener.getInstance(e).bind(eventbits, data, funcs);
      }
    }
    return this;
  }

  public Events bind(final int eventbits, final String namespace, final Object data,
      final Function... funcs) {
    for (Element e : elements()) {
      if (isEventCapable(e)) {
        EventsListener.getInstance(e).bind(eventbits, namespace, data, funcs);
      }
    }
    return this;
  }

  public Events unbind(final int eventbits) {
    for (Element e : elements()) {
      if (isEventCapable(e)) {
        EventsListener.getInstance(e).unbind(eventbits);
      }
    }
    return this;
  }

  public Events unbind(final int eventbits, final String name) {
    for (Element e : elements()) {
      if (isEventCapable(e)) {
        EventsListener.getInstance(e).unbind(eventbits, name);
      }
    }
    return this;
  }

  public Events unbind(final String name) {
    for (Element e : elements()) {
      if (isEventCapable(e)) {
        EventsListener.getInstance(e).unbind(name);
      }
    }
    return this;
  }

}
