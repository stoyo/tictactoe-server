package com.three.d.tic.tac.toe.scheduled.task;

import com.three.d.tic.tac.toe.common.Constants.ScheduledTaskNames;
import com.three.d.tic.tac.toe.service.GameService;

import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class FinishedGamesDeletionTask {

    @Autowired
    private GameService gameService;

    @Scheduled(fixedDelayString = "${finished.games.deletion.task.fixed-delay}")
    @SchedulerLock(name = ScheduledTaskNames.FINISHED_GAMES_DELETION_TASK)
    public void scheduleFixedDelayTask() {

        LockAssert.assertLocked();
        // null next player means either winner is set or blankTilesIndexes is empty
        gameService.deleteByNextTurnNull();
    }
}
