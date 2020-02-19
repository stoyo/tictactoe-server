package com.three.d.tic.tac.toe.scheduled;

import javax.sql.DataSource;

import com.three.d.tic.tac.toe.scheduled.task.FinishedGamesDeletionTask;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Tasks {

    @Bean
    @ConditionalOnProperty(
            value = "finished.games.deletion.task.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public FinishedGamesDeletionTask finishedGamesDeletionTask() {
        return new FinishedGamesDeletionTask();
    }

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }
}
