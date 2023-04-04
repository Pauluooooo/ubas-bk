package com.datasystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datasystem.dto.LoginFormDTO;
import com.datasystem.entity.Result;
import com.datasystem.entity.User;
import com.datasystem.mapper.UserMapper;
import com.datasystem.service.IUserService;
import com.datasystem.utils.PasswordEncoder;
import com.datasystem.utils.RegexUtils;
import com.datasystem.utils.SendPhoneCode;
import com.datasystem.utils.SystemConstants;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @author Pauluooooo
 * Date:2023/3/15 17:35
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
  @Resource
  private StringRedisTemplate stringRedisTemplate;

  /**
   * 用户注册
   * @param loginForm 注册表单
   * @param session   Session
   * @return 返回结果 用户为空则注册
   * 用户不为空返回错误
   */
  @Override
  public Result register(LoginFormDTO loginForm, HttpSession session) {
    String username = loginForm.getUsername();
    String password = loginForm.getPassword();
    String confirmPassword = loginForm.getConfirmPassword();

    if (!password.equals(confirmPassword)) {
      return Result.fail("The passwords do not match!");
    }

    User user = getUserByUsername(username);

    if (user == null) {
      // 用户不存在，注册
      try {
        user = createUserWithUsernameAndPassword(username, password);
        return Result.ok(user);
      } catch (Exception e) {
        return Result.fail("Failed to create user");
      }
    } else {
      // 用户存在
      return Result.fail("The user already exists");
    }
  }

  /**
   * 用户名密码登录
   * @param loginForm 登录表单
   * @param session   Session
   * @return 返回结果：验证成功返回用户
   * 验证失败返回错误信息
   */
  @Override
  public Result login(LoginFormDTO loginForm, HttpSession session) {
    String username = loginForm.getUsername();
    String rawPassword = loginForm.getPassword();

    User user = getUserByUsername(username);
    // 用户为空，直接返回
    if (user == null) {
      // 用户不存在
      return Result.fail("The user not found");
    }

    if (PasswordEncoder.matcher(rawPassword, user.getPassword())) {
      // 密码匹配，登录
      session.setAttribute("user", user);
      return Result.ok(user);
    }else {
      // 密码不匹配
      return Result.fail("The Password not match");
    }
  }

  /**
   * 发送手机验证码
   * @param loginFormDTO 手机表单
   * @return 返回结果 手机号非法返回错误
   * 合法正常返回
   */
  @Override
  public Result sendPhoneCode(LoginFormDTO loginFormDTO) throws Exception {
    String phone = loginFormDTO.getPhone();
    // 验证手机号是否合法
    if (!RegexUtils.isPhoneMatch(phone)) {
      return Result.fail("Invalid phone number");
    }
    // 生成验证码
    Random random = new Random();
    int verificationCode = random.nextInt(9000) + 1000;
    // 将验证码保存到 Redis，设置过期时间
    stringRedisTemplate.opsForValue()
            .set(SystemConstants.LOGIN_CODE_KEY + phone,
                    String.valueOf(verificationCode),
                    SystemConstants.LOGIN_CODE_TTL,
                    TimeUnit.MINUTES);
    // 发送验证码并返回结果
    Result result = SendPhoneCode.sendCode(phone, verificationCode);
    String errorMsg = result.getErrorMsg();
    // 查看是否发送成功
    if (errorMsg == null) {
      return Result.ok();
    }else{
      // 不成功，返回错误信息
      return Result.fail(errorMsg);
    }
  }

  /**
   * 手机号登录
   * @param loginForm 手机号验证码表单
   */
  @Override
  public Result phoneLogin(LoginFormDTO loginForm) {
    String phone = loginForm.getPhone();
    if (!RegexUtils.isPhoneMatch(phone)) {
      return Result.fail("Error Phone Format");
    }
    // 判断验证码
    String cacheCode = stringRedisTemplate.opsForValue().get(SystemConstants.LOGIN_CODE_KEY + phone);
    String phoneCode = loginForm.getPhoneCode();
    if (cacheCode == null || !cacheCode.equals(phoneCode)) {
      return Result.fail("Phone Code Error");
    }
    User user = query().eq("user_phone", phone).one();
    // 随机生成 Token，作为登录令牌
    String token = cn.hutool.core.lang.UUID.randomUUID().toString(true);
    if (user == null) {
      // 创建用户
      creatUserWithPhone(phone);
      return Result.ok(token);
    }else {
      // 用户存在，直接返回 token
      return Result.ok(token);
    }
  }


  private User createUserWithUsernameAndPassword(String userName, String passWord) {
    // 数据校验
    if (StringUtils.isBlank(userName) || StringUtils.isBlank(passWord)) {
      throw new IllegalArgumentException("The username or password cannot be empty!");
    }
    // 创建用户
    User user = new User();
    // 使用UUID作为主键
    UUID uuid = UUID.randomUUID();
    user.setUserId(String.valueOf(uuid));
    user.setUsername(userName);
    // 密码加密
    user.setPassword(PasswordEncoder.encode(passWord));
    save(user);
    return user;
  }

  private User creatUserWithPhone(String phone){
    // 创建用户
    User user = new User();
    // 使用UUID作为主键
    UUID uuid = UUID.randomUUID();
    user.setUserId(String.valueOf(uuid));
    user.setUsername(phone);
    user.setPhone(phone);
    save(user);
    return user;
  }


  private User getUserByUsername(String username) {
    return query().eq("user_name", username).one();
  }

}
