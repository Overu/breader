package org.cloudlet.web.service.shared;

import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.view.client.HasData;

import org.cloudlet.web.service.shared.SortPredicateProxy.SortDirection;

import java.util.logging.Logger;

public class QueryUtil {
  private static final Logger logger = Logger.getLogger(QueryUtil.class.getName());

  public static String getSort(final HasData<?> hasData) {
    if (hasData instanceof AbstractCellTable) {
      ColumnSortList columnSortList = ((AbstractCellTable<?>) hasData).getColumnSortList();
      return getSort(columnSortList);
    }
    return null;
  }

  private static String getSort(final ColumnSortList columnSortList) {
    if (columnSortList == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    for (int i = 0; i < columnSortList.size(); i++) {
      ColumnSortInfo columnSortInfo = columnSortList.get(i);
      Column<?, ?> column = columnSortInfo.getColumn();
      if (!(column instanceof HasName)) {
        logger.finest("ignore: " + Column.class.getName() + "没有实现" + HasName.class.getName()
            + ", 请提供排序的字段名");
        continue;
      }
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(", ");
      }
      String name = ((HasName) column).getName();
      sb.append(name);
      sb.append(" ");
      sb.append(columnSortInfo.isAscending() ? SortDirection.ASC : SortDirection.DESC);
    }
    return sb.length() == 0 ? null : sb.toString();
  }
}
