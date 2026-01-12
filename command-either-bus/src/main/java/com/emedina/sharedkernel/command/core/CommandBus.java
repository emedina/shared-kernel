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
     * @param <E>     type of error
     * @param <C>     type of command
     * @return either success, or an error if anything goes wrong
     */
    <E, C extends Command> Either<E, Void> execute(final C command);

}