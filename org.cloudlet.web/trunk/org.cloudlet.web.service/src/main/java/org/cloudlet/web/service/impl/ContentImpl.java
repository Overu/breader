package org.cloudlet.web.service.impl;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;

import org.cloudlet.web.service.Content;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContentImpl extends EObjectImpl implements Content, InvocationHandler {

  private String path;

  private EObject realObject;

  private Object proxyObject;

  public ContentImpl() {
    realObject = this;
    proxyObject = this;
  }

  @Override
  public EList<Adapter> eAdapters() {
    return realObject.eAdapters();
  }

  @Override
  public EClass eClass() {
    return realObject.eClass();
  }

  @Override
  public EObject eContainer() {
    return realObject.eContainer();
  }

  @Override
  public Object eGet(final EStructuralFeature eFeature) {
    return realObject.eGet(eFeature);
  }

  @Override
  public void eSet(final EStructuralFeature eFeature, final Object newValue) {
    realObject.eSet(eFeature, newValue);
  }

  public EObject getEObject() {
    return realObject;
  }

  @Override
  public String getId() {
    // TODO Auto-generated method stub
    return null;
  }

  // TODO
  // @Override
  // public boolean equals(Object obj) {
  // if (obj==null) return false;
  // if (obj instanceof Proxy) {
  // InvocationHandler h = Proxy.getInvocationHandler(obj);
  // return super.equals(h);
  // }
  // return super.equals(obj);
  // }

  @Override
  public String getUri() {
    Resource eResource = realObject.eResource();
    if (eResource != null) {
      String uri = eResource.getURIFragment(realObject);
      return uri;
    } else {
      return null;
    }
  }

  @Override
  public Long getVersion() {
    return null;
  }

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args)
      throws Throwable {
    this.proxyObject = proxy;
    try {
      Class<?> dc = method.getDeclaringClass();
      if (dc.equals(EObject.class) || dc.equals(InternalEObject.class)) {
        return method.invoke(realObject, args);
      } else if (dc.equals(Content.class) || dc.equals(Object.class)) {
        return method.invoke(this, args);
      } else {
        Feature f = Feature.parse(method);
        if (f != null) {
          EStructuralFeature feature = eClass().getEStructuralFeature(f.getName());
          if (f.isGetter()) {
            return eGet(feature);
          } else {
            eSet(feature, args[0]);
            return null;
          }
        }
        // TODO invoke service
        return null;
      }
    } catch (InvocationTargetException e) {
      Throwable t = e.getCause();
      throw t;
    }
  }

  public void setEObject(final EObject value) {
    this.realObject = value;
  }

  @Override
  public void setId(final String value) {

  }

  @Override
  public EntityProxyId<?> stableId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toString() {
    return realObject.toString();
  }
}
