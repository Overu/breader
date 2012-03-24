package org.cloudlet.web.service.server.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

@Singleton
public class DatabaseConnectionProvider extends DriverManagerConnectionProviderImpl {
  @Inject
  private static Provider<HttpServletRequest> req;
  private final Logger logger = Logger.getLogger(getClass().getName());
  public static final String CONNECTION_KEY = "connectionKey";

  @Override
  public void closeConnection(final Connection conn) throws SQLException {
    HttpServletRequest request = null;
    try {
      request = req.get();
    } catch (Exception e) {
    }
    if (request != null) {
      return;
    }
    conn.close();
    logger.log(Level.FINER, "关闭数据库连接");
  }

  @Override
  public Connection getConnection() throws SQLException {
    HttpServletRequest request = null;
    try {
      request = req.get();
    } catch (Exception e) {
    }
    if (request == null) {
      return super.getConnection();
    }
    Connection conn = (Connection) request.getAttribute(CONNECTION_KEY);
    if (conn != null) {
      return conn;
    }
    conn = super.getConnection();
    request.setAttribute(CONNECTION_KEY, conn);
    return conn;
  }
}
