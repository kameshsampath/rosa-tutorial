package dev.kameshs.rosa;

import java.util.HashMap;
import java.util.Map;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

public abstract class AbstractService {

  public final static String FRUIT_NAME_COL = "fruitName";
  public final static String FRUIT_SEASON = "fruitSeason";

  public String getTableName() {
    return "QuarkusFruits";
  }

  protected ScanRequest scanRequest() throws Exception {
    return ScanRequest.builder()
                      .tableName(getTableName())
                      .attributesToGet(FRUIT_NAME_COL, FRUIT_SEASON)
                      .build();
  }

  protected PutItemRequest putRequest(Fruit fruit) throws Exception {
    Map<String, AttributeValue> item = new HashMap<>();
    item.put(FRUIT_NAME_COL, AttributeValue.builder()
        .s(fruit.name)
                                           .build());
    item.put(FRUIT_SEASON, AttributeValue.builder()
        .s(fruit.season)
                                         .build());

    return PutItemRequest.builder()
                         .tableName(getTableName())
                         .item(item)
                         .build();
  }

  protected GetItemRequest getRequest(String name) throws Exception {
    Map<String, AttributeValue> key = new HashMap<>();
    key.put(FRUIT_NAME_COL, AttributeValue.builder()
                                          .s(name)
                                          .build());

    return GetItemRequest.builder()
                         .tableName(getTableName())
                         .key(key)
                         .attributesToGet(FRUIT_NAME_COL, FRUIT_SEASON)
                         .build();
  }

  protected DeleteItemRequest deleteRequest(String name) throws Exception {
    Map<String, AttributeValue> key = new HashMap<>();
    key.put(FRUIT_NAME_COL, AttributeValue.builder()
                                          .s(name)
                                          .build());

    return DeleteItemRequest.builder()
                            .tableName(getTableName())
                            .key(key)
                            .build();
  }
}
