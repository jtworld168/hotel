package com.hotel.vending.vo;

import lombok.Data;

/**
 * User Info VO
 */
@Data
public class UserInfoVO {
    
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private String role;
    private String token;
}
