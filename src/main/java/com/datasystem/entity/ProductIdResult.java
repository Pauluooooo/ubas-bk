package com.datasystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Pauluooooo
 * Date:2023/4/20 15:11
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Document(collation = "behavior_min")
@AllArgsConstructor
public class ProductIdResult {
  @Field("product_id")
  private String productId;

  private long buyCount;
}
