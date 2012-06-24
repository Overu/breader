package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class WebType extends NamedElement {

  protected transient Class<?> definitionClass;

  private Package _package;

  private transient TextReader<?> textReader;

  private transient TextWriter<?> textWriter;

  public Class<?> getDefinitionClass() {
    return definitionClass;
  }

  public Package getPackage() {
    return _package;
  }

  public String getQualifiedName() {
    return getPackage().getName() + "." + getName();
  }

  public <T> TextReader<T> getTextReader() {
    return (TextReader<T>) textReader;
  }

  public <T> TextWriter<T> getTextWriter() {
    return (TextWriter<T>) textWriter;
  }

  public void setDefinitionClass(final Class<?> definitionClass) {
    this.definitionClass = definitionClass;
  }

  public void setPackage(final Package _package) {
    this._package = _package;
  }

  public void setTextReader(final TextReader<?> textReader) {
    this.textReader = textReader;
  }

  public void setTextWriter(final TextWriter<?> textWriter) {
    this.textWriter = textWriter;
  }

}
