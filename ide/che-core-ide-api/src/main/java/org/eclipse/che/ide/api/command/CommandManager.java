/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.api.command;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.command.ContextualCommand.ApplicableContext;

import java.util.List;

/**
 * Facade for command management.
 * Allows to easily perform CRUD operations on {@link ContextualCommand}s
 * and provides possibility to add listeners to be notified about commands modifications.
 *
 * @author Artem Zatsarynnyi
 * @see ContextualCommand
 * @see CommandLoadedListener
 * @see CommandChangedListener
 */
public interface CommandManager {

    /** Returns all commands. */
    List<ContextualCommand> getCommands();

    /** Returns command by the specified name or {@code null} if none. */
    @Nullable
    ContextualCommand getCommand(String name);

    /** Returns commands which are applicable to the current IDE context. */
    List<ContextualCommand> getApplicableCommands();

    /** Checks whether the given {@code command} is applicable to the current IDE context or not. */
    boolean isCommandApplicable(ContextualCommand command);

    /**
     * Creates new command of the specified type with the given {@link ApplicableContext}.
     * <p><b>Note</b> that command's name will be generated by {@link CommandManager}.
     */
    Promise<ContextualCommand> createCommand(String goalId,
                                             String commandTypeId,
                                             ApplicableContext applicableContext);

    /**
     * Creates copy of the given {@code command}.
     * <p><b>Note</b> that name of the created command may differ from
     * the given {@code command}'s name in order to prevent name duplication.
     */
    Promise<ContextualCommand> createCommand(ContextualCommand command);

    /**
     * Updates the command with the specified {@code name} by replacing it with the given {@code command}.
     * <p><b>Note</b> that name of the updated command may differ from the name provided by the given {@code command}
     * in order to prevent name duplication.
     */
    Promise<ContextualCommand> updateCommand(String name, ContextualCommand command);

    /** Removes command with the specified {@code commandName}. */
    Promise<Void> removeCommand(String commandName);

    void addCommandLoadedListener(CommandLoadedListener listener);

    void removeCommandLoadedListener(CommandLoadedListener listener);

    void addCommandChangedListener(CommandChangedListener listener);

    void removeCommandChangedListener(CommandChangedListener listener);

    /** Listener to notify when all commands have been loaded. */
    interface CommandLoadedListener {

        /** Called when all commands have been loaded. */
        void onCommandsLoaded();
    }

    /** Listener to notify when command has been changed. */
    interface CommandChangedListener {

        /** Called when command has been added. */
        void onCommandAdded(ContextualCommand command);

        /** Called when command has been updated. */
        void onCommandUpdated(ContextualCommand command);

        /** Called when command has been removed. */
        void onCommandRemoved(ContextualCommand command);
    }
}
