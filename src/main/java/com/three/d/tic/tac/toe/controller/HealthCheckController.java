package com.three.d.tic.tac.toe.controller;

import static com.three.d.tic.tac.toe.controller.HealthCheckController.SELF_LINK;

import com.three.d.tic.tac.toe.common.Constants.UriParts;
import com.three.d.tic.tac.toe.dto.Health;
import com.three.d.tic.tac.toe.dto.Health.Status;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = SELF_LINK)
public class HealthCheckController {
    public static final String SELF_LINK = UriParts.API_PREFIX + "/healthcheck";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Health healthcheck() {
        return Health.builder()
                .status(Status.UP)
                .build();
    }
}
