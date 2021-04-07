package dev.kameshs.rosa;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@ApplicationScoped
public class FruitSyncService extends AbstractService {

  @Inject
  DynamoDbClient dynamoDB;

  public List<Fruit> findAll() throws Exception {
    return dynamoDB.scanPaginator(scanRequest())
                   .items()
                   .stream()
                   .map(f -> {
                     Fruit fruit = new Fruit();
                     fruit.name = f.get(FRUIT_NAME_COL)
                                   .s();
                     fruit.season = f.get(FRUIT_SEASON)
                                   .s();
                     return fruit;
                   })
                   .collect(Collectors.toList());
  }

  public List<Fruit> add(Fruit fruit) throws Exception {
    dynamoDB.putItem(putRequest(fruit));
    return findAll();
  }

  public Fruit get(String name) throws Exception {
    Map<String, AttributeValue> f = dynamoDB.getItem(getRequest(name))
                                            .item();
    Fruit fruit = new Fruit();
    fruit.name = f.get(FRUIT_NAME_COL)
                  .s();
    fruit.season = f.get(FRUIT_SEASON)
                    .s();
    return fruit;
  }

  public void delete(String name) throws Exception {
    dynamoDB.deleteItem(deleteRequest(name));
  }
}
