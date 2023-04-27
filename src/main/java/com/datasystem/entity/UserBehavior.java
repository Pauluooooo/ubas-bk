package com.datasystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Pauluooooo
 * Date:2023/4/7 18:20
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Document(collation = "behavior_min")
@AllArgsConstructor
public class UserBehavior {
  private Integer userId;
  private Integer productId;
  private Integer commodityCategoryId;
  private String pv;
  private Date date;

}
