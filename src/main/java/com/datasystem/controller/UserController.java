package com.datasystem.controller;

import com.datasystem.dto.LoginFormDTO;
import com.datasystem.entity.Result;
import com.datasystem.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pauluooooo
 * Date:2023/3/15 17:40
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

  @Resource
  private IUserService userService;

  @PostMapping("/register")
  public Result register(@RequestBody LoginFormDTO loginForm, HttpSession session) {
    return userService.register(loginForm,session);
  }

  @PostMapping("/login")
  public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
    return userService.login(loginForm,session);
  }
}
