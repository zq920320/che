/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.resources.management;

import com.google.common.annotations.Beta;

import org.eclipse.che.api.core.model.project.ProjectConfig;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.project.ProjectServiceClient;
import org.eclipse.che.ide.api.resources.Container;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.Folder;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.resources.ResourceChangedEvent;
import org.eclipse.che.ide.resource.Path;


/**
 * Intermediate layer for operating with file based resources. Main goal of this component is to create
 * layer that communicates between interface based objects that represents virtual file system and low level
 * interface which actually communicates with server based on the data transfer objects.
 *
 * @author Vlad Zhukovskyi
 * @since 5.0.0
 */
@Beta
public interface ResourceManager {

    /**
     * Resolve an instance of {@link Resource} by given {@code path}.
     * <p>
     * Usage:
     * <pre><code>
     * manager.resolveResource(null, Path.valueOf("/path/to/the/resource"))
     *        .then(new{@code Operation<Resource>}() {
     *           {@code @Override}
     *            public void apply(Resource resource) throws OperationException {
     *                //handle resolved resource
     *            }
     *        })
     *        .catchError(new{@code Operation<PromiseError>}() {
     *           {@code @Override}
     *            public void apply(PromiseError arg) throws OperationException {
     *                //handle exception
     *            }
     *        });
     * </code></pre>
     * <p>
     * Given {@code path} should be an absolute path.
     *
     * @param requestor
     *         any object to control who called this method. See {@link ResourceChangedEvent#getRequestor()}
     * @param path
     *         desired path, which should be resolved
     * @return
     */
    Promise<Resource> resolveResource(Object requestor, Path path);

    /**
     * Creates new directory by given {@code path}.
     * <p>
     * Usage:
     * <pre><code>
     * manager.createFolder(null, Path.valueOf("/path/to/new/folder"))
     *        .then(new{@code Operation<Folder>}() {
     *           {@code @Override}
     *            public void apply(Folder folder) throws OperationException {
     *                //handle folder creation
     *            }
     *        })
     *        .catchError(new{@code Operation<PromiseError>}() {
     *           {@code @Override}
     *            public void apply(PromiseError error) throws OperationException {
     *                //handle exception
     *            }
     *        });
     * </code></pre>
     * <p>
     * Given {@code path} should be an absolute path.
     *
     * @param requestor
     *         any object to control who called this method. See {@link ResourceChangedEvent#getRequestor()}
     * @param path
     *         desired path, where new folder should be created.
     * @return the {@link Promise} object with the instance of newly created folder or reject with detailed description of the problem
     * @see Folder
     * @see Promise
     * @see Path
     * @since 5.0.0
     */
    Promise<Folder> createFolder(Object requestor, Path path);

    Promise<File> createFile(Object requestor, Path path, String content);

    Promise<String> updateFile(Object requestor, File file, String content);

    Promise<Void> deleteResource(Object requestor, Resource resource);

    Promise<String> getFileContent(Object requestor, File file);

    <R extends Resource> Promise<R> moveResource(Object requestor, R resource, Path destination, boolean force);

    <R extends Resource> Promise<R> copyResource(Object requestor, R resource, Path destination, boolean force);

    Container getWorkspaceRoot();

    Promise<Iterable<Resource>> getChildren(Object requestor, Container container);

    Promise<Iterable<Resource>> getPlainTree(Object requestor, Container container, int depth);

    Promise<Project> createProject(Object requestor, ProjectConfig config);

    Promise<Iterable<Project>> createProjects(Object requestor, ProjectConfig... configs);

    Promise<Project> updateProject(Object requestor, ProjectConfig config);

    Promise<Project> importProject(Object requestor, ProjectConfig config);

    Container getParent(Object requestor, Resource resource);

    Path resolvePath(Object requestor, Resource resource);

    Project getProject(Resource resource);

    ProjectConfig getProjectConfig(Project project);
}
