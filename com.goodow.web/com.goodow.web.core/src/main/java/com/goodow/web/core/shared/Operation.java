package com.goodow.web.core.shared;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Operation extends TypedElement implements Wrapper<Operation> {

  private String path;

  private HttpMethod httpMethod;

  private transient Map<String, Parameter> parameters = new LinkedHashMap<String, Parameter>();

  private ObjectType declaringType;

  private transient String qualifiedName;

  @Override
  public Operation as() {
    return this;
  }

  public ObjectType getDeclaringType() {
    return declaringType;
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public Parameter getParameter(final String name) {
    Parameter result = parameters.get(name);
    return result;
  }

  public Map<String, Parameter> getParameters() {
    return parameters;
  }

  public String getPath() {
    return path;
  }

  public String getQualifiedName() {
    if (qualifiedName == null) {
      ObjectType type = getDeclaringType();
      qualifiedName = type.getQualifiedName() + "." + getName();
    }
    return qualifiedName;
  }

  public void setDeclaringType(final ObjectType containingType) {
    this.declaringType = containingType;
  }

  public void setHttpMethod(final HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
  }

  public void setPath(final String value) {
    this.path = value;
  }

}
