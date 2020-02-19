package com.three.d.tic.tac.toe.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginRequest {

    @NotBlank(message = "'username' is mandatory")
    @Size(min = 3, max = 20, message = "'username' must be between 6 and 20 characters")
    private String username;

    @NotBlank(message = "'password' is mandatory")
    @Size(min = 6, max = 255, message = "'password' must be between 6 and 255 characters")
    private String password;
}
