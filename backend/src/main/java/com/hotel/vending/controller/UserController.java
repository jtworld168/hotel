package com.hotel.vending.controller;

import com.hotel.vending.common.Result;
import com.hotel.vending.dto.LoginDTO;
import com.hotel.vending.dto.RegisterDTO;
import com.hotel.vending.entity.User;
import com.hotel.vending.service.UserService;
import com.hotel.vending.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 */
@Tag(name = "User Management")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "User registration")
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterDTO registerDTO) {
        User user = userService.register(registerDTO);
        return Result.success(user);
    }
    
    @Operation(summary = "User login")
    @PostMapping("/login")
    public Result<UserInfoVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserInfoVO userInfo = userService.login(loginDTO);
        return Result.success(userInfo);
    }
    
    @Operation(summary = "User logout")
    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }
    
    @Operation(summary = "Get current user info")
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        User user = userService.getCurrentUser();
        return Result.success(user);
    }
}
