package com.three.d.tic.tac.toe.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameBoardSpecification {

    @NotNull(message = "'n' is mandatory")
    private Integer n; // game board will be: n * n * n
}
