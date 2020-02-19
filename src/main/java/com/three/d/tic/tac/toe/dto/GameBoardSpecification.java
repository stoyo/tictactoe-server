package com.three.d.tic.tac.toe.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameBoardSpecification {

    @NotNull(message = "'n' is mandatory")
    @Range(min = 3, max = 5)
    private Integer n; // game board will be: n * n * n
}
