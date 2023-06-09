package com.datasystem.dto;

import lombok.Data;

/**
 * @author Pauluooooo
 * Date:2023/3/15 17:31
 */
@Data
public class LoginFormDTO {

  private String username;
  private String password;

  private String phone;

  private String phoneCode;
  private String confirmPassword;

  private String newPassword;
  private String role;
}
