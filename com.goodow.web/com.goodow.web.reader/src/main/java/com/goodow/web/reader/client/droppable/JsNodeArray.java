package com.goodow.web.reader.client.droppable;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public class JsNodeArray extends NodeList<Element> {
  public static JsNodeArray create() {
    return create((Node) null);
  }

  public static native JsNodeArray create(Node node) /*-{
                                                     return node ? [node] : [];
                                                     }-*/;

  public static JsNodeArray create(final NodeList<?> nl) {
    JsNodeArray ret = create((Node) null);
    ret.pushAll(nl);
    return ret;
  }

  protected JsNodeArray() {
  }

  public final void addNode(final Node n) {
    c().add(n);
  }

  public final void addNode(final Node n, final int i) {
    c().add(i, n);
  }

  public final void concat(final JsNodeArray ary) {
    c().concat(ary.c());
  }

  public final Element[] elements() {
    Element[] ret = new Element[size()];
    for (int i = 0; i < size(); i++) {
      ret[i] = getElement(i);
    }
    return ret;
  }

  public final Element get(final int i) {
    return getElement(i);
  }

  public final Element getElement(final int i) {
    return c().get(i).cast();
  }

  public final Node getNode(final int i) {
    return c().get(i);
  }

  public final void pushAll(final JavaScriptObject prevElem) {
    c().pushAll(prevElem);
  }

  public final int size() {
    return c().length();
  }

  private JsObjectArray<Node> c() {
    return cast();
  }
}
