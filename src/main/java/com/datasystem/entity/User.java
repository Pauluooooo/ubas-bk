package com.datasystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author Pauluooooo
 * Date:2023/3/15 16:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @TableId(value = "user_id")
  private String userId;
  @TableField(value = "user_name")
  private String username;

  @TableField(value = "user_phone")
  private String phone;
  @TableField(value = "user_password")
  private String password;

}
