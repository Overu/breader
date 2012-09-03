package com.goodow.web.reader.client.droppable;

import com.goodow.web.reader.client.editgrid.Function;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public class SimpleQuery {

  public static Class<SimpleQuery> SIMPLEQUERY = SimpleQuery.class;

  public static final BodyElement body = Document.get().getBody();

  public static final Document document = Document.get();

  protected static JsCache dataCache = null;
  private static JsMap<Class<? extends SimpleQuery>, Plugin<? extends SimpleQuery>> plugins;

  // private static Element windowData = null;

  public static SimpleQuery q(final Element element) {
    return new SimpleQuery(element);
  }

  public static SimpleQuery q(final Node n) {
    return q((Element) n);
  }

  public static <T extends SimpleQuery> T q(final T sq) {
    return sq;
  }

  public static <T extends SimpleQuery> Class<T> registerPlugin(final Class<T> plugin,
      final Plugin<T> pluginFactory) {
    if (plugins == null) {
      plugins = JsMap.createObject().cast();
    }

    plugins.put(plugin, pluginFactory);
    return plugin;
  }

  protected static <S> Object data(final Element item, final String name, final S value) {
    if (dataCache == null) {
      // windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    // item = item == window || item.getNodeName() == null ? windowData : item;
    if (item == null) {
      return value;
    }
    int id = item.hashCode();
    if (name != null && !dataCache.exists(id)) {
      dataCache.put(id, JsCache.createObject().cast());
    }

    JsCache d = dataCache.getCache(id);
    if (name != null && value != null) {
      d.put(name, value);
    }
    return name != null ? d.get(name) : id;
  }

  private NodeList<Element> nodeList = JavaScriptObject.createArray().cast();

  private Element[] elements = new Element[0];

  protected SimpleQuery(final SimpleQuery sq) {
    this(sq == null ? null : sq.get());
  }

  private SimpleQuery(final Element element) {
    this(JsNodeArray.create(element).<NodeList<Element>> cast());
  }

  private SimpleQuery(final NodeList<Element> nodes) {
    setArray(nodes);
  }

  @SuppressWarnings("unchecked")
  public <T extends SimpleQuery> T as(final Class<T> plugin) {
    if (plugin == SIMPLEQUERY) {
      return (T) q(this);
    } else if (plugins != null) {

      Plugin<?> p = plugins.get(plugin);
      if (p != null) {
        return (T) p.init(this);
      }
    }
    throw new RuntimeException("No plugin registered for class " + plugin.getName());
  }

  public SimpleQuery children(final Function f) {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      Element firstChildElement = e.getFirstChildElement();
      while (firstChildElement != null) {
        result.addNode(firstChildElement);
        if (f != null) {
          f.f(firstChildElement);
        }
        firstChildElement = firstChildElement.getNextSiblingElement();
      }
    }
    return this;
  }

  public Object data(final String name) {
    return isEmpty() ? null : data(get(0), name, null);
  }

  @SuppressWarnings("unused")
  public <T> T data(final String name, final Class<T> clz) {
    return isEmpty() ? null : (T) data(get(0), name, null);
  }

  public SimpleQuery data(final String name, final Object value) {
    for (Element e : elements) {
      data(e, name, value);
    }
    return this;
  }

  public Element[] elements() {
    return elements;
  }

  public NodeList<Element> get() {
    return nodeList;
  }

  public Element get(final int i) {
    int l = elements.length;
    if (i >= 0 && i < l) {
      return elements[i];
    }
    if (i < 0 && l + i >= 0) {
      return elements[l + i];
    }
    return null;
  }

  public boolean isEmpty() {
    return elements.length == 0;
  }

  public SimpleQuery removeData(final String name) {
    for (Element e : elements) {
      removeData(e, name);
    }
    return this;
  }

  public SimpleQuery setArray(final NodeList<Element> nodes) {
    if (nodes != null) {
      nodeList = nodes;
      int l = nodes.getLength();
      elements = new Element[l];
      for (int i = 0; i < l; i++) {
        elements[i] = nodes.getItem(i);
      }
    }
    return this;
  }

  private void removeData(final Element item, final String name) {
    if (dataCache == null) {
      // windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    // item = item == window || item.getNodeName() == null ? windowData : item;
    int id = item.hashCode();
    if (name != null) {
      if (dataCache.exists(id)) {
        dataCache.getCache(id).delete(name);
      }
      if (dataCache.getCache(id).isEmpty()) {
        removeData(item, null);
      }
    } else {
      dataCache.delete(id);
    }
  }
}
