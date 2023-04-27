package com.datasystem.service;

import com.datasystem.entity.Result;

/**
 * @author Pauluooooo
 * Date:2023/4/7 18:24
 */


public interface IChartService {

//  Result countUserAndProduct();
//
//  Result countProductTypeAndPv();

  Result countBehavior();

  Result countTime();
  Result countProduct();

}
