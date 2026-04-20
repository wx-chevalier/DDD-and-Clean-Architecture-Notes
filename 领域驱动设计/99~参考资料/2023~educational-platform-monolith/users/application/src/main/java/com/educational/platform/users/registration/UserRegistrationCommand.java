package com.educational.platform.users.registration;

import com.educational.platform.users.RoleDTO;
import com.educational.platform.users.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents User Registration Command.
 */
@Builder
@Data
@AllArgsConstructor
public class UserRegistrationCommand {

    @NotNull
    private final RoleDTO role;

    @Size(min = 4, max = 255)
    @NotBlank
    private final String username;

    @Email
    @NotBlank
    private final String email;

    @ValidPassword
    @NotBlank
    private final String password;

}
