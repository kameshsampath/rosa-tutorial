package dev.kameshs.rosa.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DatasourceConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      DatasourceConfig.class);

  @Profile({"!dev"})
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource rdsDatasource() {
    LOGGER.info("Configuring RDS Datasource");
    return DataSourceBuilder.create()
                            .type(HikariDataSource.class)
                            .build();
  }

  @Profile({"dev"})
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource devDatasource() {
    LOGGER.info("Configuring Dev Datasource");
    return DataSourceBuilder.create()
                            .type(HikariDataSource.class)
                            .build();
  }
}
