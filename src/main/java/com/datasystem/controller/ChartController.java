package com.datasystem.controller;

import com.datasystem.entity.Result;
import com.datasystem.service.IChartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pauluooooo
 * Date:2023/4/7 18:24
 */
@Slf4j
@RestController
@RequestMapping("/chart")
public class ChartController {
  @Resource
  private IChartService chartService;

  @PostMapping("/behavior")
  public Result countBehavior() {
    return Result.ok(chartService.countBehavior());
  }

  @PostMapping("/time")
  public Result countTime() {
    return Result.ok(chartService.countTime());
  }

  @PostMapping("/product")
  public Result countProduct() {
    return Result.ok(chartService.countProduct());
  }
}
