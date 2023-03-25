package com.datasystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datasystem.dto.LoginFormDTO;
import com.datasystem.entity.Result;
import com.datasystem.entity.User;
import com.datasystem.mapper.UserMapper;
import com.datasystem.service.IUserService;
import com.datasystem.utils.PasswordEncoder;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Pauluooooo
 * Date:2023/3/15 17:35
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

  @Override
  public Result register(LoginFormDTO loginForm, HttpSession session) {
    String username = loginForm.getUsername();
    String password = loginForm.getPassword();

    User user = query().eq("user_name", username).one();

    if (user == null) {
      try {
        user = createUserWithUsernameAndPassword(username, password);

      } catch (Exception e) {
        return Result.fail("Failed to create user");
      }
    }

    return Result.ok(user);
  }

  @Override
  public Result login(LoginFormDTO loginForm, HttpSession session) {
    return null;
  }

  private User createUserWithUsernameAndPassword(String userName, String passWord){
    // 数据校验
    if (StringUtils.isBlank(userName) || StringUtils.isBlank(passWord)) {
      throw new IllegalArgumentException("Invalid input data");
    }

    // 创建用户
    User user = new User();
    UUID uuid = UUID.randomUUID();
    user.setUserId(String.valueOf(uuid));
    user.setUsername(userName);
    user.setPassword(PasswordEncoder.encode(passWord));
    save(user);

    return user;
  }

}
