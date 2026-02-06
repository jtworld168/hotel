package com.hotel.vending.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * User Register DTO
 */
@Data
public class RegisterDTO {
    
    @NotBlank(message = "Username cannot be empty")
    private String username;
    
    @NotBlank(message = "Password cannot be empty")
    private String password;
    
    private String nickname;
    
    private String phone;
    
    private String email;
}
