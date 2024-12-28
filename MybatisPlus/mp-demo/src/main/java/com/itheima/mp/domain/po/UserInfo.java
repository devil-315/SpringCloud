package com.itheima.mp.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName：UserInfo
 *
 * @author: Devil
 * @Date: 2024/12/28
 * @Description:
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserInfo {
    private Integer age;
    private String intro;
    private String gender;
}
