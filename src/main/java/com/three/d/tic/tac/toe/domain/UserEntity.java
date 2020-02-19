package com.three.d.tic.tac.toe.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.three.d.tic.tac.toe.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.NaturalId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NaturalId
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = {
            CascadeType.ALL }, orphanRemoval = true)
    private List<GameEntity> gameEntities;

    public static User toDto(UserEntity ue) {
        return User.builder()
                .id(ue.getId())
                .username(ue.getUsername())
                .password(ue.getPassword())
                .build();
    }

    public static UserEntity fromDto(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
