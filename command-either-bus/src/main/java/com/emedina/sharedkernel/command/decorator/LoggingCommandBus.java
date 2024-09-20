package com.emedina.sharedkernel.command.decorator;

import io.vavr.control.Either;
import com.emedina.sharedkernel.command.Command;
import com.emedina.sharedkernel.command.core.CommandBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command bus decorator which logs every execution of a command with its elapsed time.
 *
 * @author Enrique Medina Montenegro
 */
public class LoggingCommandBus implements CommandBus {

    static final Logger LOG = LoggerFactory.getLogger(LoggingCommandBus.class);

    private CommandBus decorated;

    /**
     * Creates a new command bus that logs timing, and delegates the execution to the given command bus.
     *
     * @param decorated command bus to decorate
     */
    public LoggingCommandBus(final CommandBus decorated) {
        this.decorated = decorated;
    }

    @Override
    public <C extends Command> Either<?, Void> execute(final C command) {
        LOG.info("started execution of command {}", command.getClass().getSimpleName());

        Timer timer = new Timer();
        try {
            this.decorated.execute(command);
        } finally {
            LOG.info("finished execution of command {} in {}", command.getClass().getSimpleName(), timer);
        }

        return Either.right(null);
    }

}
