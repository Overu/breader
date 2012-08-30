package com.goodow.web.reader.client.droppable;

import com.goodow.web.reader.client.editgrid.Function;

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class EventsListener implements EventListener {

  private static class BindFunction {

    Object data;
    Function function;
    String nameSpace = "";
    int times = -1;
    int type;

    BindFunction(final int t, final String n, final Function f, final Object d) {
      type = t;
      function = f;
      data = d;
      if (n != null) {
        nameSpace = n;
      }
    }

    BindFunction(final int t, final String n, final Function f, final Object d, final int times) {
      this(t, n, f, d);
      this.times = times;
    }

    public boolean fire(final Event event) {
      if (times != 0) {
        times--;
        return function.f(event, data);
      }
      return true;
    }

    public boolean hasEventType(final int etype) {
      return (type & etype) == type;
    }

    @Override
    public String toString() {
      return "bind function for event type " + type;
    }
  }

  public static void clean(final Element e) {
    EventsListener ret = getSqueryEventListener(e);
    if (ret != null) {
      ret.clean();
    }
  }

  public static EventsListener getInstance(final Element e) {
    EventsListener ret = getSqueryEventListener(e);
    return ret != null ? ret : new EventsListener(e);
  }

  public static void rebind(final Element e) {
    EventsListener ret = getSqueryEventListener(e);
    if (ret != null && ret.eventBits != 0) {
      ret.sink();
    }
  }

  private static native void cleanSqListeners(Element elm) /*-{
                                                            if (elm.__gwtlistener) {
                                                            elm.__listener = elm.__gwtlistener;
                                                            }
                                                            elm.__sqevent = null;
                                                            elm.__squery = null;

                                                            }-*/;

  private static native EventListener getGwtEventListener(Element elm) /*-{
                                                                        return elm.__gwtlistener;
                                                                        }-*/;

  private static native EventsListener getSqueryEventListener(Element elm) /*-{
                                                                            return elm.__sqevent;
                                                                            }-*/;

  private static native void init(Element elm, EventsListener sqevent)/*-{
                                                                       elm.__gwtlistener = elm.__listener;
                                                                       elm.__sqevent = sqevent;
                                                                       }-*/;

  private static native void sinkEvent(Element elm, String name) /*-{
                                                                  if (!elm.__squery)
                                                                  elm.__squery = [];
                                                                  if (elm.__squery[name])
                                                                  return;
                                                                  elm.__squery[name] = true;

                                                                  var handle = function(event) {
                                                                  elm.__sqevent.@com.goodow.web.reader.client.droppable.EventsListener::dispatchEvent(Lcom/google/gwt/user/client/Event;)(event);
                                                                  };

                                                                  if (elm.addEventListener)
                                                                  elm.addEventListener(name, handle, true);
                                                                  else
                                                                  elm.attachEvent("on" + name, handle);
                                                                  }-*/;

  private Element element;

  int eventBits = 0;
  double lastEvnt = 0;
  int lastType = 0;

  public static int ONSUBMIT = 0x10000000;
  public static int ONRESIZE = 0x8000000;

  private JsObjectArray<BindFunction> elementEvents = JsObjectArray.createArray().cast();

  private EventsListener(final Element element) {
    this.element = element;
    init(element, this);
  }

  public void bind(final int eventbits, final Object data, final Function... funcs) {
    bind(eventbits, null, data, funcs);
  }

  public void bind(final int eventbits, final Object data, final Function function, final int times) {
    bind(eventbits, null, data, function, times);
  }

  public void bind(final int eventbits, final String name, final Object data,
      final Function... funcs) {
    for (Function function : funcs) {
      bind(eventbits, name, data, function, -1);
    }
  }

  public void bind(final int eventbits, final String namespace, final Object data,
      final Function function, final int times) {
    if (function == null) {
      unbind(eventbits, namespace);
      return;
    }
    eventBits |= eventbits;
    sink();
    elementEvents.add(new BindFunction(eventbits, namespace, function, data, times));
  }

  public void dispatchEvent(final Event event) {
    int etype = getTypeInt(event.getType());
    for (int i = 0; i < elementEvents.length(); i++) {
      BindFunction listener = elementEvents.get(i);
      if (listener.hasEventType(etype)) {
        if (!listener.fire(event)) {
          event.stopPropagation();
          event.preventDefault();
        }
      }
    }
  }

  public EventListener getOriginalEventListener() {
    return getGwtEventListener(element);
  }

  @Override
  public void onBrowserEvent(final Event event) {
    double now = Duration.currentTimeMillis();
    if (lastType == event.getTypeInt() && (now - lastEvnt < 10)
        && "body".equalsIgnoreCase(element.getTagName())) {
      return;
    }
    lastEvnt = now;
    lastType = event.getTypeInt();

    if (getOriginalEventListener() != null) {
      getOriginalEventListener().onBrowserEvent(event);
    }

    dispatchEvent(event);
  }

  public void unbind(final int eventbits) {
    unbind(eventbits, null);
  }

  public void unbind(final int eventbits, final String namespace) {
    JsObjectArray<BindFunction> newList = JsObjectArray.createArray().cast();
    for (int i = 0; i < elementEvents.length(); i++) {
      BindFunction listener = elementEvents.get(i);
      boolean matchNS =
          namespace == null || namespace.isEmpty() || listener.nameSpace.equals(namespace);
      boolean matchEV = eventbits <= 0 || listener.hasEventType(eventbits);
      if (matchNS && matchEV) {
        continue;
      }
      newList.add(listener);
    }
    elementEvents = newList;
  }

  public void unbind(final String event) {
    String nameSpace = event.replaceFirst("^[^\\.]+\\.*(.*)$", "$1");
    String eventName = event.replaceFirst("^([^\\.]+).*$", "$1");
    int b = getEventBits(eventName);
    unbind(b, nameSpace);
  }

  private void clean() {
    cleanSqListeners(element);
    elementEvents = JsObjectArray.createArray().cast();
    // liveBindFunctionByEventType = JsMap.create();
  }

  private int getEventBits(final String... events) {
    int ret = 0;
    for (String e : events) {
      String[] parts = e.split("[\\s,]+");
      for (String s : parts) {
        if ("submit".equals(s)) {
          ret |= ONSUBMIT;
        } else if ("resize".equals(s)) {
          ret |= ONRESIZE;
        } else {
          int event = Event.getTypeInt(s);
          if (event > 0) {
            ret |= event;
          }
        }
      }
    }
    return ret;
  }

  private int getTypeInt(final String eventName) {
    return "submit".equals(eventName) ? ONSUBMIT : "resize".equals(eventName) ? ONRESIZE : Event
        .getTypeInt(eventName);
  }

  private void sink() {
    DOM.setEventListener((com.google.gwt.user.client.Element) element, this);
    if (eventBits == ONSUBMIT) {
      sinkEvent(element, "submit");
    } else if ((eventBits | ONRESIZE) == ONRESIZE) {
      sinkEvent(element, "resize");
    } else {
      if ((eventBits | Event.FOCUSEVENTS) == Event.FOCUSEVENTS
          && element.getAttribute("tabIndex").length() == 0) {
        element.setAttribute("tabIndex", "0");
      }
      DOM.sinkEvents((com.google.gwt.user.client.Element) element, eventBits
          | DOM.getEventsSunk((com.google.gwt.user.client.Element) element));
    }
  }
}
