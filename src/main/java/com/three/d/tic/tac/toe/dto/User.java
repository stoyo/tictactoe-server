package com.three.d.tic.tac.toe.dto;

import java.util.Collection;
import java.util.Collections;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({
        "authorities",
        "accountNonExpired",
        "accountNonLocked",
        "credentialsNonExpired",
        "enabled"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {

    private Long id;

    @NotBlank(message = "'username' is mandatory")
    @Size(min = 3, max = 20, message = "'username' must be between 6 and 20 characters")
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotBlank(message = "'password' is mandatory")
    @Size(min = 6, max = 255, message = "'password' must be between 6 and 255 characters")
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
