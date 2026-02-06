package com.hotel.vending.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotel.vending.dto.LoginDTO;
import com.hotel.vending.dto.RegisterDTO;
import com.hotel.vending.entity.User;
import com.hotel.vending.mapper.UserMapper;
import com.hotel.vending.service.UserService;
import com.hotel.vending.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * User Service Implementation
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    
    @Override
    public User register(RegisterDTO registerDTO) {
        // Check if username exists
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            throw new RuntimeException("Username already exists");
        }
        
        // Create new user
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword()));
        user.setRole("customer");
        user.setStatus(1);
        userMapper.insert(user);
        
        return user;
    }
    
    @Override
    public UserInfoVO login(LoginDTO loginDTO) {
        // Find user by username
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        // Verify password
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("Account is disabled");
        }
        
        // Login with SaToken
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        // Build response
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        userInfoVO.setToken(token);
        
        return userInfoVO;
    }
    
    @Override
    public void logout() {
        StpUtil.logout();
    }
    
    @Override
    public User getCurrentUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        return userMapper.selectById(userId);
    }
}
