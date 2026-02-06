package com.hotel.vending.service;

import com.hotel.vending.dto.LoginDTO;
import com.hotel.vending.dto.RegisterDTO;
import com.hotel.vending.entity.User;
import com.hotel.vending.vo.UserInfoVO;

/**
 * User Service Interface
 */
public interface UserService {
    
    /**
     * User registration
     */
    User register(RegisterDTO registerDTO);
    
    /**
     * User login
     */
    UserInfoVO login(LoginDTO loginDTO);
    
    /**
     * User logout
     */
    void logout();
    
    /**
     * Get current user info
     */
    User getCurrentUser();
}
