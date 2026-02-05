package com.hotel.vending.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * User Login DTO
 */
@Data
public class LoginDTO {
    
    @NotBlank(message = "Username cannot be empty")
    private String username;
    
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
