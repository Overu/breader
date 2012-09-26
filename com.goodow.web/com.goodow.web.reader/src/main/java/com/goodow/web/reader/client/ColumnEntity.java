package com.goodow.web.reader.client;

import com.google.gwt.user.cellview.client.Column;

import java.util.Comparator;

public class ColumnEntity<T> {

  Column<T, ?> column;

  int width;

  private Comparator<T> comparator;

  public ColumnEntity(final Column<T, ?> column, final int width) {
    this(column, width, null);
  }

  public ColumnEntity(final Column<T, ?> column, final int width, final Comparator<T> comparator) {
    this.column = column;
    this.width = width;
    this.comparator = comparator;
  }

  public Column<T, ?> getColumn() {
    return column;
  }

  public Comparator<T> getComparator() {
    return comparator;
  }

  public int getWidth() {
    return width;
  }

  public void setColumn(final Column<T, ?> column) {
    this.column = column;
  }

  public void setComparator(final Comparator<T> comparator) {
    this.comparator = comparator;
  }

  public void setWidth(final int width) {
    this.width = width;
  }

}
