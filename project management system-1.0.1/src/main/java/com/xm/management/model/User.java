package com.xm.management.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

@Data
@Table(name = "user")
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> {
    @TableId
    private String username;
    private String password;
    private Integer role;

}
