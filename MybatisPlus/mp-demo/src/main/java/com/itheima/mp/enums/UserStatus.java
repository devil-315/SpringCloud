package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * ClassName：UserStatus
 *
 * @author: Devil
 * @Date: 2024/12/28
 * @Description:
 * @version: 1.0
 */
@Getter
public enum UserStatus {
    NORMAL(1,"正常"),
    FROZEN(2,"冻结"),
    ;
    @EnumValue
    private final int value;
    @JsonValue
    private final String desc;

    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
