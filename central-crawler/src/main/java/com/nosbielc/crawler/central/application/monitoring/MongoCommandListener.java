package com.nosbielc.crawler.central.application.monitoring;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MongoCommandListener implements CommandListener {

    private static final Logger logger = LogManager.getLogger(MongoCommandListener.class);

    @Value("${app.mongo.command.listener.enabled}")
    private boolean enabled;
    @Value("${app.mongo.command.listener.log.events}")
    private boolean logEventsEnabled;
    @Value("${app.mongo.command.listener.log.timers}")
    private boolean logTimersEnabled;

    @Override
    public void commandStarted(CommandStartedEvent event) {
        if (enabled && logEventsEnabled) {
            String message = String.format("Sent command '%s:%s' with id %s to mongo '%s' "
                            + "on connection '%s' to server '%s'",
                    event.getCommandName(),
                    event.getCommand().get(event.getCommandName()),
                    event.getRequestId(),
                    event.getDatabaseName(),
                    event.getConnectionDescription()
                            .getConnectionId(),
                    event.getConnectionDescription().getServerAddress());

            logger.info(message);
        }
    }

    @Override
    public void commandSucceeded(CommandSucceededEvent event) {
        if (enabled) {
            if (logEventsEnabled) {
                String message = String.format("Successfully executed command '%s' with id %s "
                                + "on connection '%s' to server '%s'",
                        event.getCommandName(),
                        event.getRequestId(),
                        event.getConnectionDescription()
                                .getConnectionId(),
                        event.getConnectionDescription().getServerAddress());

                logger.info(message);
            }
            if (logTimersEnabled) {
                String timeMessage = String.format("Command=[%s] elapsedTime=[%s ms or %s ns]",
                        event.getCommandName(),
                        event.getElapsedTime(TimeUnit.MILLISECONDS),
                        event.getElapsedTime(TimeUnit.NANOSECONDS));
                logger.info(timeMessage);
            }
        }
    }

    @Override
    public void commandFailed(CommandFailedEvent event) {
        if (enabled) {
            if (logEventsEnabled) {
                String message = String.format("Failed execution of command '%s' with id %s "
                                + "on connection '%s' to server '%s' with exception '%s'",
                        event.getCommandName(),
                        event.getRequestId(),
                        event.getConnectionDescription()
                                .getConnectionId(),
                        event.getConnectionDescription().getServerAddress(),
                        event.getThrowable());

                logger.error(message);
            }
            if (logTimersEnabled) {
                String timeMessage = String.format("Command=[%s] elapsedTime=[%s ms or %s ns]",
                        event.getCommandName(),
                        event.getElapsedTime(TimeUnit.MILLISECONDS),
                        event.getElapsedTime(TimeUnit.NANOSECONDS));
                logger.info(timeMessage);
            }
        }
    }
}
