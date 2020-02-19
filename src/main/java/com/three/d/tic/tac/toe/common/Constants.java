package com.three.d.tic.tac.toe.common;

public interface Constants {

    interface ScheduledTaskNames {

        String SCHEDULED_TASK_PREFIX = "TaskScheduler_";
        String FINISHED_GAMES_DELETION_TASK = SCHEDULED_TASK_PREFIX + "finishedGamesDeletionTask";
    }

    interface CacheNames {

        String POSSIBLE_WINS = "possibleWinsMap";
    }

    interface UriParts {

        String API_PREFIX = "/tic-tac-toe/api";
    }
}
