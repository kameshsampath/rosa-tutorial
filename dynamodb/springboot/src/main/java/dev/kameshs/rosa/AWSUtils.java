package dev.kameshs.rosa;

import java.net.URI;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheSdkHttpService;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
public class AWSUtils {

  private static final Logger LOGGER = Logger.getLogger(
    AWSUtils.class.getName());

  @Value("${rosa-demos.dynamodb.endpoint-override:http://localhost:8000}")
  String localEndPointURI;

  @Profile("prod")
  @Bean
  public DynamoDbClient dynamoDbClient() {
    LOGGER.info("Building DynamoDbClient with WebIdentityProvider");
    SdkHttpClient httpClient = new ApacheSdkHttpService().createHttpClientBuilder()
                                                         .build();

    WebIdentityTokenFileCredentialsProvider credentialsProvider =
      WebIdentityTokenFileCredentialsProvider.builder()
                                             .build();
    return DynamoDbClient.builder()
                         .httpClient(httpClient)
                         .credentialsProvider(credentialsProvider)
                         .build();
  }

  @Profile("dev")
  @Bean
  public DynamoDbClient devDynamoDbClient() {
    LOGGER.info(
      "Building DynamoDbClient with Static Credentials:" + localEndPointURI);
    SdkHttpClient httpClient = new ApacheSdkHttpService().createHttpClientBuilder()
                                                         .build();

    AwsBasicCredentials credentials = AwsBasicCredentials.create("test-key",
      "test-secret");

    StaticCredentialsProvider credentialsProvider =
      StaticCredentialsProvider.create(credentials);

    return DynamoDbClient.builder()
                         .endpointOverride(URI.create(localEndPointURI))
                         .httpClient(httpClient)
                         .region(Region.AP_SOUTH_1)
                         .credentialsProvider(
                           credentialsProvider)
                         .build();
  }
}
