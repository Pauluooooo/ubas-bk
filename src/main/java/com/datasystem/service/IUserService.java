package com.datasystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datasystem.dto.LoginFormDTO;
import com.datasystem.entity.Result;
import com.datasystem.entity.User;
import jakarta.servlet.http.HttpSession;

/**
 * @author Pauluooooo
 * Date:2023/3/15 17:18
 */

public interface IUserService extends IService<User> {
  Result register(LoginFormDTO loginForm, HttpSession session);

  Result login(LoginFormDTO loginForm, HttpSession session);

}
