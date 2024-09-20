package com.emedina.sharedkernel.command.core;

import com.emedina.sharedkernel.command.Command;

/**
 * A handler for a {@link Command}. Notice that it does not return any value.
 *
 * @param <C> type of command
 * @author Enrique Medina Montenegro
 * @see <a href="https://martinfowler.com/bliki/CommandQuerySeparation.html">CQS - Command-Query Separation</a>
 */
public interface CommandHandler<C extends Command> {

    /**
     * Handles the command.
     *
     * @param command command to handle
     * @return an optional return value as specified in {@link Command}
     */
    void handle(final C command);

}