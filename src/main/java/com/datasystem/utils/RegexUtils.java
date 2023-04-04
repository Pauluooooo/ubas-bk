package com.datasystem.utils;

import cn.hutool.core.util.ReUtil;
/**
 * @author Pauluooooo
 * Date:2023/4/4 14:19
 */

public class RegexUtils {

  /**
   * <p>判断手机号是否匹配</p>
   * @param phone 传入的手机号
   * @return true:匹配 false:不匹配
   * */
  public static boolean isPhoneMatch(String phone) {
    return ReUtil.isMatch(RegexPatterns.PHONE_REGEX,phone);
  }
}
