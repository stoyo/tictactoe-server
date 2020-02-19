package com.three.d.tic.tac.toe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Health {

    private Status status;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Status {
        public static final Status UP = new Status("UP");

        private final String code;

        Status(String code) {
            this.code = code;
        }

        public String toString() {
            return this.code;
        }
    }
}
