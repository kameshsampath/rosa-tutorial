package dev.kameshs.rosa.jdbc;

import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest;
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator;
import dev.kameshs.rosa.utils.AWSUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.ds.PGSimpleDataSource;

public class PostgresRdsDatasource extends PGSimpleDataSource {

  private static final Logger LOGGER = Logger.getLogger(
      PostgresRdsDatasource.class.getName());

  @Override
  public Connection getConnection() throws SQLException {
    return getConnection(null, null);
  }

  @Override
  public Connection getConnection(String user, String password)
      throws SQLException {

    String awsRegion = System.getenv()
                             .get("AWS_REGION");
    String awsRoleArn = System.getenv()
                              .get("AWS_ROLE_ARN");

    LOGGER.log(Level.FINE, "Getting RDS Connection ..");

    setProperty("sslmode", "require");

    LOGGER.log(Level.INFO, "Using Role {0} as JDBC user with Region {1}",
        new Object[]{awsRoleArn, awsRegion});

    RdsIamAuthTokenGenerator generator =
        RdsIamAuthTokenGenerator.builder()
                                .credentials(
                                    WebIdentityTokenCredentialsProvider.builder()
                                                                       .build())
                                .region(awsRegion)
                                .build();

    Optional<String> optDbUser = AWSUtils.getRoleNameFromArn(awsRoleArn);
    String dbUser = optDbUser.orElseThrow()
                             .toLowerCase();

    String authToken = generator.getAuthToken(
        GetIamAuthTokenRequest.builder()
                              .port(getPortNumbers()[0])
                              .hostname(getServerNames()[0])
                              .userName(dbUser)
                              .build());

    LOGGER.log(Level.FINEST, "Got token {0} for User ", authToken);
    LOGGER.log(Level.FINE, "JDBC URL {0} ", getURL());

    return super.getConnection(dbUser, authToken);
  }
}
