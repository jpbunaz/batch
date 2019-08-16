package io.balteusit.framework.batch.execute.common;

import io.balteusit.framework.batch.core.BatchProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractDatabaseTest {

  protected static boolean isDatabaseInit = false;

  @BeforeAll
  protected static void init() throws Exception {
    if (!isDatabaseInit) {
      executeTestLiquibase("db-changelog.xml");
      executeTestLiquibase("test.xml");
      isDatabaseInit = true;
    }
  }

  protected static void executeTestLiquibase(String fileName) throws LiquibaseException, SQLException {
    try (Connection connection = getConnection()) {

      Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

      Liquibase liquibase = new Liquibase("liquibase/" + fileName, new ClassLoaderResourceAccessor(), database);
      liquibase.update(fileName);
      connection.commit();
    }
  }

  protected static Connection getConnection() throws SQLException {
    String url = BatchProperties.getInstance().getProperty("javax.persistence.jdbc.url");
    String user = BatchProperties.getInstance().getProperty("javax.persistence.jdbc.user");
    String password = BatchProperties.getInstance().getProperty("javax.persistence.jdbc.password");
    return DriverManager.getConnection(url, user, password);
  }


}
