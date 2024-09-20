package com.emedina.sharedkernel.command.core;

import io.vavr.control.Either;
import com.emedina.sharedkernel.command.Command;

/**
 * A command bus is able to execute commands, by passing the command object to its appropriate handler, hence
 * decoupling the requester from the executor using best practices.
 *
 * @author Enrique Medina Montenegro
 */
public interface CommandBus {

    /**
     * Looks up the handler and passes the command to it.
     *
     * @param command command object
     * @param <C>     type of command
     */
    <C extends Command> Either<?, Void> execute(final C command);

}