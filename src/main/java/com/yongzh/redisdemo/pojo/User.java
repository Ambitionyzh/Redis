package com.yongzh.redisdemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @version 1.0
 * @program: redis-demo
 * @description:
 * @date 2023/5/27 16:53
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String name;
    private Integer age;

}
