package org.eclipse.che.ide.resources.management;

import org.eclipse.che.api.core.model.project.ProjectConfig;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.resources.Project;

/**
 *
 */
public interface ProjectManager {

    Promise<Project> createProject(Object requestor, ProjectConfig config);

    Promise<Iterable<Project>> createProjects(Object requestor, ProjectConfig... configs);

    Promise<Project> updateProject(Object requestor, ProjectConfig config);

    Promise<Project> importProject(Object requestor, ProjectConfig config);

}
