package com.cyq.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String salt;
    private Integer status;
    private Integer type;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

}
