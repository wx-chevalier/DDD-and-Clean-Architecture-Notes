package com.educational.platform.users.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents Sign In Command.
 */
@Builder
@Data
@AllArgsConstructor
public class SignInCommand {

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

}
