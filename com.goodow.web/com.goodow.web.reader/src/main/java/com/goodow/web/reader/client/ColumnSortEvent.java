package com.goodow.web.reader.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.cellview.client.Column;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnSortEvent extends GwtEvent<ColumnSortEvent.Handler> {

  public static interface Handler extends EventHandler {

    void onColumnSort(ColumnSortEvent event);
  }

  public static class ListHandler<T> implements Handler {

    private final Map<Column<?, ?>, Comparator<T>> comparators =
        new HashMap<Column<?, ?>, Comparator<T>>();
    private List<T> list;
    private Sort curSort;

    public ListHandler(final List<T> list) {
      this.list = list;
    }

    public List<T> getList() {
      return list;
    }

    @Override
    public void onColumnSort(final ColumnSortEvent event) {
      Column<?, ?> column = event.getColumn();
      if (column == null) {
        return;
      }

      final Comparator<T> comparator = comparators.get(column);
      if (comparator == null) {
        return;
      }

      Sort sort = event.getSort();
      if (curSort != null && curSort.equals(sort)) {
        return;
      }
      switch (sort) {
        case ASC:
          asc(comparator);
          break;
        case DSC:
          dsc(comparator);
          break;

        default:
          break;
      }
      curSort = sort;
    }

    public void setComparator(final Column<T, ?> column, final Comparator<T> comparator) {
      comparators.put(column, comparator);
    }

    public void setList(final List<T> list) {
      assert list != null : "list cannot be null";
      this.list = list;
    }

    private void asc(final Comparator<T> comparator) {
      Collections.sort(list, comparator);
    }

    private void dsc(final Comparator<T> comparator) {
      Collections.sort(list, new Comparator<T>() {
        @Override
        public int compare(final T o1, final T o2) {
          return -comparator.compare(o1, o2);
        }
      });
    }
  }

  public enum Sort {
    ASC, DSC
  }

  public static Type<Handler> TYPE = new Type<Handler>();

  public static ColumnSortEvent fire(final HasHandlers source, final Column<?, ?> column,
      final Sort sort) {
    ColumnSortEvent event = new ColumnSortEvent(column, sort);
    if (TYPE != null) {
      source.fireEvent(event);
    }
    return event;
  }

  private Column<?, ?> column;
  private Sort sort;

  public ColumnSortEvent(final Column<?, ?> column, final Sort sort) {
    this.setColumn(column);
    this.setSort(sort);
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public Column<?, ?> getColumn() {
    return column;
  }

  public Sort getSort() {
    return sort;
  }

  public void setColumn(final Column<?, ?> column) {
    this.column = column;
  }

  public void setSort(final Sort sort) {
    this.sort = sort;
  }

  @Override
  protected void dispatch(final Handler handler) {
    handler.onColumnSort(this);
  }
}
