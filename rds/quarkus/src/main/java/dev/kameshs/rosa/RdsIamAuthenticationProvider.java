package dev.kameshs.rosa;

import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest;
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator;
import io.quarkus.arc.Unremovable;
import io.quarkus.credentials.CredentialsProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Unremovable
public class RdsIamAuthenticationProvider implements CredentialsProvider {

  private static final Logger LOGGER = Logger.getLogger(
    RdsIamAuthenticationProvider.class.getName());

  @Inject
  Config config;

  @ConfigProperty(name = "aws.region", defaultValue = "us-west-2")
  String awsRegion;

  @ConfigProperty(name = "rds.host", defaultValue = "localhost")
  String dbHost;

  @ConfigProperty(name = "rds.port", defaultValue = "5432")
  int dbPort;

  @Override
  public Map<String, String> getCredentials(String credentialsProviderName) {

    LOGGER.log(Level.INFO, "Using \"{0}\" Credentials Provider",
      credentialsProviderName);

    Map<String, String> properties = new HashMap<>();
    String dbKind = config.getValue("quarkus.datasource.db-kind", String.class);
    String jdbcUrl = config.getValue("quarkus.datasource.jdbc.url",
      String.class);

    LOGGER.log(Level.INFO, "Connecting with JDBC URL \"{0}\" ", jdbcUrl);

    if (!("postgresql".equalsIgnoreCase(dbKind) || "mysql".equalsIgnoreCase(
      dbKind))) {
      throw new IllegalStateException(
        "Database " + dbKind + " not supported for AWS RDS IAM Authentication");
    }

    if ("aws-iam".equals(credentialsProviderName)) {

      final String roleArn = System.getenv()
                                   .get("AWS_ROLE_ARN");

      LOGGER.log(Level.INFO, "Using Role {0} as JDBC user with Region {1}",
        new Object[]{roleArn, awsRegion});

      RdsIamAuthTokenGenerator generator =
        RdsIamAuthTokenGenerator.builder()
                                .credentials(
                                  new WebIdentityTokenCredentialsProvider())
                                .region(awsRegion)
                                .build();

      Optional<String> optDbUser = AWSUtils.getRoleNameFromArn(roleArn);
      String dbUser = optDbUser.orElseThrow().toLowerCase();

      LOGGER.log(Level.INFO, "Getting token for User :{0}", dbUser);

      String authToken = generator.getAuthToken(
        GetIamAuthTokenRequest.builder()
                              .port(dbPort)
                              .hostname(dbHost)
                              .userName(dbUser)
                              .build());


      LOGGER.log(Level.INFO, "Got token {0} for User ", authToken);

      properties.put(USER_PROPERTY_NAME, dbUser);
      properties.put(PASSWORD_PROPERTY_NAME, authToken);
    } else {
      properties.put(USER_PROPERTY_NAME, "demo");
      properties.put(PASSWORD_PROPERTY_NAME, "pa55Word!");
    }

    return properties;
  }

}
