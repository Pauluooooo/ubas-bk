package com.datasystem.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Pauluooooo
 * Date:2023/3/25 12:54
 */

public class PasswordEncoder {
  private static final String ALGORITHM = "SHA-256";

  /**
   * 对密码进行加密，返回加密后的字符串
   * @param password 明文密码
   * @return 加密后的字符串
   */
  public static String encode(String password) {
    try {
      // 获取SHA-256摘要实例
      MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
      // 将密码转换为byte数组并进行摘要计算
      byte[] digest = messageDigest.digest(password.getBytes());
      // 将byte数组转换为十六进制字符串
      return toHexString(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Failed to encode password", e);
    }
  }

  /**
   * 验证密码是否正确
   *
   * @param rawPassword     原始密码
   * @param encodedPassword 编码后的密码
   * @return 如果密码正确则返回true，否则返回false
   */
  public static boolean matcher(String rawPassword, String encodedPassword) {
    return encode(rawPassword).equals(encodedPassword);
  }

  /**
   * 将byte数组转换为十六进制字符串
   * @param bytes byte数组
   * @return 十六进制字符串
   */
  private static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
