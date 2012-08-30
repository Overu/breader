package com.goodow.web.reader.client.droppable;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.core.client.JsArrayString;

public class JsCache extends JavaScriptObject {
  public static JsCache create() {
    return createObject().cast();
  }

  protected JsCache() {
  }

  public final native void concat(Object ary) /*-{
                                              if (ary) this.concat(ary);
                                              }-*/;

  public final native <T> void delete(T name) /*-{
                                              delete this[name];
                                              }-*/;

  public final Object[] elements() {
    String[] keys = keys();
    Object[] ret = new Object[keys.length];
    int i = 0;
    for (String s : keys) {
      ret[i++] = get(s);
    }
    return ret;
  }

  public final native <T> boolean exists(T name) /*-{
                                                 return !!this[name];
                                                 }-*/;

  public final native <R, T> R get(T id) /*-{
                                         var r = this[id], t = typeof r;
                                         return r && t != 'number' && t != 'boolean' ? r : null;
                                         }-*/;

  public final native <T> JsArrayMixed getArray(T id) /*-{
                                                      var r = this[id];
                                                      if (r && Object.prototype.toString.call(r) == '[object Array]') {
                                                      return r;
                                                      }
                                                      return null;
                                                      }-*/;

  public final native <T> boolean getBoolean(T id) /*-{
                                                   var r = this[id], t = typeof r;
                                                   return 'boolean' == r ? r : 'true' == String(r);
                                                   }-*/;

  public final <T> JsCache getCache(final int id) {
    return (JsCache) get(id);
  }

  public final native <T> double getDouble(T id) /*-{
                                                 var r = this[id] ? Number(this[id]) : 0;
                                                 return r ? r : 0;
                                                 }-*/;

  public final <T> float getFloat(final T id) {
    return (float) getDouble(id);
  }

  public final <T> int getInt(final T id) {
    return (int) getDouble(id);
  }

  public final <T extends JavaScriptObject> T getJavaScriptObject(final Object name) {
    Object o = get(name);
    return (o instanceof JavaScriptObject) ? ((JavaScriptObject) o).<T> cast() : null;
  }

  public final native <T> String getString(T id) /*-{
                                                 return this[id] == null ? null : String(this[id]);
                                                 }-*/;

  public final int[] indexes() {
    JsArrayString a = keysImpl();
    int[] ret = new int[a.length()];
    for (int i = 0; i < a.length(); i++) {
      try {
        ret[i] = Integer.valueOf(a.get(i));
      } catch (Exception e) {
        ret[i] = i;
      }
    }
    return ret;
  }

  public final native boolean isEmpty() /*-{
                                        for (k in this) return false;
                                        return true;
                                        }-*/;

  public final String[] keys() {
    JsArrayString a = keysImpl();
    String[] ret = new String[a.length()];
    for (int i = 0; i < a.length(); i++) {
      ret[i] = a.get(i);
    }
    return ret;
  }

  public final native int length() /*-{
                                   if (typeof(this.length) == 'number') 
                                   return this.length;
                                   
                                   var key, ret = 0;
                                   // Chrome in DevMode injects a property to JS objects
                                   for (key in this) if (key != "__gwt_ObjectId") ret ++;
                                   return ret; 
                                   }-*/;

  public final void pushAll(final JavaScriptObject prevElem) {
    JsCache c = prevElem.cast();
    for (int i = 0, ilen = c.length(); i < ilen; i++) {
      put(length(), c.get(i));
    }
  }

  public final native <T, O> void put(T id, O obj) /*-{
                                                   this[id] = obj;
                                                   }-*/;

  public final String tostring() {
    String ret = getClass().getName() + "{ ";
    for (String k : keys()) {
      ret += k + "=" + get(k) + " ";
    }
    return ret + "}";
  }

  private final native JsArrayString keysImpl() /*-{
                                                var key, keys=[];
                                                // Chrome in DevMode injects a property to JS objects
                                                for(key in this) if (key != '__gwt_ObjectId') keys.push(String(key));
                                                return keys;
                                                }-*/;
}
