package com.datasystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用来向前端返回结果信息
 * @author Pauluooooo
 * Date:2023/3/15 17:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
  private Boolean success;
  private String errorMsg;
  private Object data;
  private Long total;

  public static Result ok(){
    return new Result(true, null, null, null);
  }
  public static Result ok(Object data){
    return new Result(true, null, data, null);
  }
  public static Result ok(List<?> data, Long total){
    return new Result(true, null, data, total);
  }
  public static Result fail(String errorMsg){
    return new Result(false, errorMsg, null, null);
  }
}
