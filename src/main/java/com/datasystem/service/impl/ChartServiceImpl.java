package com.datasystem.service.impl;

import com.datasystem.entity.ProductIdResult;
import com.datasystem.entity.Result;
import com.datasystem.entity.UserBehavior;
import com.datasystem.service.IChartService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


/**
 * @author Pauluooooo
 * Date:2023/4/7 18:26
 */
@Service
public class ChartServiceImpl implements IChartService {
  @Resource
  private MongoTemplate mongoTemplate;
  @Resource
  private RedisTemplate<String, String> redisTemplate;

  @Override
  public Result countBehavior() {
    String redisKey = "countByBehavior";
    String redisValue = redisTemplate.opsForValue().get(redisKey);

    if (redisValue != null) {
      // 如果Redis中存在相应数据，则直接返回
      return Result.ok(redisValue);
    }
    GroupOperation groupOperation = group()
            .sum(ConditionalOperators
                    .when(Criteria.where("pv").is("pv"))
                    .then(1)
                    .otherwise(0)).as("pv")
            .sum(ConditionalOperators
                    .when(Criteria.where("pv").is("buy"))
                    .then(1)
                    .otherwise(0)).as("buy")
            .sum(ConditionalOperators
                    .when(Criteria.where("pv").is("cart"))
                    .then(1)
                    .otherwise(0)).as("cart")
            .sum(ConditionalOperators
                    .when(Criteria.where("pv").is("fav"))
                    .then(1)
                    .otherwise(0)).as("fav");
    Aggregation aggregation = newAggregation(groupOperation);
    var result = mongoTemplate.aggregate(aggregation, "myCollection", Document.class).getUniqueMappedResult();
    // 将查询结果存入Redis中，并设置过期时间
    redisTemplate.opsForValue().set("countByBehavior", result.toString(), Duration.ofHours(10));
    return Result.ok(result);
  }

  @Override
  public Result countTime() {
    String redisKey = "buyCountByDate";
    String redisValue = redisTemplate.opsForValue().get(redisKey);

    if (redisValue != null) {
      // 如果Redis中存在相应数据，则直接返回
      return Result.ok(redisValue);
    }

    MatchOperation match = match(Criteria.where("pv").is("buy").and("Date").gte("2017-11-25").lte("2017-12-03"));
    GroupOperation group = group("Date").count().as("count");
    Aggregation aggregation = newAggregation(match, group);
    AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "behavior_min", Document.class);
    List<Document> resultDocuments = results.getMappedResults();

    // 将查询结果存入Redis中，并设置过期时间
    redisTemplate.opsForValue().set("buyCountByDate", resultDocuments.toString(), Duration.ofHours(10));

    return Result.ok(resultDocuments);
  }

  @Override
  public Result countProduct() {
    String redisKey = "countProduct";
    String redisValue = redisTemplate.opsForValue().get(redisKey);

    if (redisValue != null) {
      // 如果Redis中存在相应数据，则直接返回
      return Result.ok(redisValue);
    }

    Aggregation aggregation = newAggregation(
            match(Criteria.where("pv").is("buy")),
            group("product_id").count().as("buyCount"),
            project("product_id", "buyCount").andExclude("_id"),
            sort(Sort.Direction.DESC, "buyCount"),
            limit(10)
    );

    AggregationResults<ProductIdResult> result = mongoTemplate.aggregate(aggregation, "behavior_min", ProductIdResult.class);
    List<ProductIdResult> mappedResult = result.getMappedResults();
    // 将查询结果存入Redis中，并设置过期时间
    redisTemplate.opsForValue().set("countProduct", mappedResult.toString(), Duration.ofHours(10));
    return Result.ok(mappedResult);
  }


}
