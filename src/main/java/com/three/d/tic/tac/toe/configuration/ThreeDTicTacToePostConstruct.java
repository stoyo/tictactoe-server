package com.three.d.tic.tac.toe.configuration;

import java.util.Collections;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.three.d.tic.tac.toe.common.Constants.CacheNames;
import com.three.d.tic.tac.toe.util.ThreeDTicTacToePossibleWinsGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ThreeDTicTacToePostConstruct {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ThreeDTicTacToePossibleWinsGenerator threeDTicTacToePossibleWinsGenerator;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(
                Collections.singletonList(new ConcurrentMapCache(CacheNames.POSSIBLE_WINS)));
        return simpleCacheManager;
    }

    @PostConstruct
    public void initPossibleWinsCache() {
        threeDTicTacToePossibleWinsGenerator.getPossibleWinsMap()
                .forEach((n, possibleWins) ->
                    Objects.requireNonNull(cacheManager.getCache(CacheNames.POSSIBLE_WINS))
                            .put(n, possibleWins)
                );
    }
}
