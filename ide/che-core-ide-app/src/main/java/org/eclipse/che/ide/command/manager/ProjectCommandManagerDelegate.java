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
package org.eclipse.che.ide.command.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.api.machine.shared.dto.CommandDto;
import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.api.promises.client.FunctionException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseProvider;
import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.command.CommandManager;
import org.eclipse.che.ide.api.command.CommandType;
import org.eclipse.che.ide.api.project.MutableProjectConfig;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.dto.DtoFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eclipse.che.api.project.shared.Constants.COMMANDS_ATTRIBUTE_NAME;

/**
 * Responsible for managing the commands which are stored with projects.
 *
 * @author Artem Zatsarynnyi
 */
@Singleton
class ProjectCommandManagerDelegate {

    private final DtoFactory      dtoFactory;
    private final PromiseProvider promiseProvider;

    @Inject
    ProjectCommandManagerDelegate(DtoFactory dtoFactory, PromiseProvider promiseProvider) {
        this.dtoFactory = dtoFactory;
        this.promiseProvider = promiseProvider;
    }

    /**
     * Returns commands.
     *
     * @param project
     */
    List<CommandImpl> getCommands(Project project) {
        List<String> attributeValues = project.getAttributes(COMMANDS_ATTRIBUTE_NAME);
        if (attributeValues == null) {
            return new ArrayList<>();
        }

        Map<String, CommandImpl> commands = new HashMap<>(attributeValues.size());

        for (String commandJson : attributeValues) {
            final CommandDto command = dtoFactory.createDtoFromJson(commandJson, CommandDto.class);

            commands.put(command.getName(), new CommandImpl(command));
        }

        return new ArrayList<>(commands.values());
    }

    /**
     * Creates new command of the specified type.
     * <p><b>Note</b> that command's name will be generated by {@link CommandManager}
     * and command line will be provided by an appropriate {@link CommandType}.
     */
    Promise<CommandImpl> createCommand(Project project, final CommandImpl newCommand) {
        final List<CommandImpl> projectCommands = getCommands(project);

        for (CommandImpl projectCommand : projectCommands) {
            if (projectCommand.getName().equals(newCommand.getName())) {
                return promiseProvider.reject(new Exception("Command '" + newCommand.getName() +
                                                            "' is already associated to the project '" + project.getName() + "'"));
            }
        }

        projectCommands.add(newCommand);

        return updateProject(project, projectCommands).then(new Function<Void, CommandImpl>() {
            @Override
            public CommandImpl apply(Void arg) throws FunctionException {
                return newCommand;
            }
        });
    }

    private Promise<Void> updateProject(Project project, List<CommandImpl> commands) {
        MutableProjectConfig config = new MutableProjectConfig(project);
        Map<String, List<String>> attributes = config.getAttributes();

        List<String> commandsList = new ArrayList<>(attributes.size());
        for (CommandImpl command : commands) {
            final CommandDto commandDto = dtoFactory.createDto(CommandDto.class)
                                                    .withName(command.getName())
                                                    .withType(command.getType())
                                                    .withCommandLine(command.getCommandLine())
                                                    .withAttributes(command.getAttributes());
            commandsList.add(dtoFactory.toJson(commandDto));
        }

        attributes.put(COMMANDS_ATTRIBUTE_NAME, commandsList);

        return project.update().withBody(config).send().then(new Function<Project, Void>() {
            @Override
            public Void apply(Project arg) throws FunctionException {
                return null;
            }
        });
    }

    /**
     * Updates the command with the specified {@code name} by replacing it with the given {@code command}.
     * <p><b>Note</b> that name of the updated command may differ from the name provided by the given {@code command}
     * in order to prevent name duplication.
     */
    Promise<CommandImpl> updateCommand(Project project, final CommandImpl command) {
        final List<CommandImpl> projectCommands = getCommands(project);

        if (projectCommands.isEmpty()) {
            return promiseProvider.reject(new Exception("Command '" + command.getName() + "' is not associated with the project '" +
                                                        project.getName() + "'"));
        }

        final List<CommandImpl> commandsToUpdate = new ArrayList<>();
        for (CommandImpl projectCommand : projectCommands) {
            // skip existed command with the same name
            if (!command.getName().equals(projectCommand.getName())) {
                commandsToUpdate.add(projectCommand);
            }
        }

        commandsToUpdate.add(command);

        return updateProject(project, new ArrayList<>(commandsToUpdate)).then(new Function<Void, CommandImpl>() {
            @Override
            public CommandImpl apply(Void arg) throws FunctionException {
                return command;
            }
        });
    }

    /** Removes the command with the specified {@code commandName}. */
    Promise<Void> removeCommand(Project project, String commandName) {
        final List<CommandImpl> projectCommands = getCommands(project);

        if (projectCommands.isEmpty()) {
            return promiseProvider.reject(new Exception("Command '" + commandName + "' is not associated with the project '" +
                                                        project.getName() + "'"));
        }

        final List<CommandImpl> commandsToUpdate = new ArrayList<>();
        for (CommandImpl projectCommand : projectCommands) {
            if (!commandName.equals(projectCommand.getName())) {
                commandsToUpdate.add(projectCommand);
            }
        }

        return updateProject(project, new ArrayList<>(commandsToUpdate));
    }
}
