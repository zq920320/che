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
import com.google.common.collect.FluentIterable;

import org.eclipse.che.ide.api.resources.Container;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.resources.ResourceInterceptor;
import org.eclipse.che.ide.resource.Path;

/**
 * Memory resource storage component responses for loading file based resource from the server and caching them the client side.
 * This component is part of core resource management and is not intended to be used by third-party components.
 *
 * @author Vlad Zhukovskyi
 * @since 5.0.0
 */
@Beta
public interface ResourceStorage {

    /**
     * Register given {@code resources} in the resource storage.
     * <p>
     * Before registering, resource performs intercepting by set of {@link ResourceInterceptor}s to allow third-party components
     * modify resource by adding specific meta data, i.e. markers.
     *
     * @param resources
     *         non null resources array
     * @throws NullPointerException
     *         in case id given {@code resource} is null, reason includes:
     *         <ul>
     *         <li>Null resource occurred</li>
     *         </ul>
     * @see ResourceInterceptor
     * @since 5.0.0
     */
    void add(Resource... resources);

    /**
     * Register given {@code resource} in the resource storage.
     * <p>
     * Before registering, resource performs intercepting by set of {@link ResourceInterceptor}s to allow third-party components
     * modify resource by adding specific meta data, i.e. markers.
     *
     * @param resource
     *         non null resource
     * @throws NullPointerException
     *         in case id given {@code resource} is null, reason includes:
     *         <ul>
     *         <li>Null resource occurred</li>
     *         </ul>
     * @see ResourceInterceptor
     * @since 5.0.0
     */
    void add(Resource resource);

    /**
     * Remove registered resource by specific given {@code path}.
     *
     * @param path
     *         non null path for the registered resource
     * @throws NullPointerException
     *         in case if given {@code path} is null, reason includes:
     *         <ul>
     *         <li>Null path occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code path}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @see Path
     * @since 5.0.0
     */
    void delete(Path path);

    /**
     * Remove registered resource by specific given {@code resource}.
     *
     * @param resource
     *         non null instance of the resource to delete
     * @throws NullPointerException
     *         in case if given {@code resource} is null, reason includes:
     *         <ul>
     *         <li>Null resource occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code resource}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @since 5.0.0
     */
    void delete(Resource resource);

    /**
     * Remove registered resource by specific given {@code path} recursively.
     * <p>
     * Recursively means that if specific resource has children they will be removed also.
     *
     * @param path
     *         non null path for the registered resource
     * @throws NullPointerException
     *         in case if given {@code path} is null, reason includes:
     *         <ul>
     *         <li>Null path occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code path}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @see Path
     * @since 5.0.0
     */
    void deleteRecursively(Path path);

    /**
     * Remove registered resource by specific given {@code resource} recursively.
     * <p>
     * Recursively means that if specific resource has children they will be removed also.
     *
     * @param resource
     *         non null instance of the resource to delete
     * @throws NullPointerException
     *         in case if given {@code resource} is null, reason includes:
     *         <ul>
     *         <li>Null resource occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code resource}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @since 5.0.0
     */
    void deleteRecursively(Resource resource);

    /**
     * Returns the parent resource for given {@code resource}.
     *
     * @param resource
     *         non null instance of the resource
     * @return the parent resource for given {@code resource}
     * @throws NullPointerException
     *         in case if given {@code resource} is null, reason includes:
     *         <ul>
     *         <li>Null resource occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code resource}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @since 5.0.0
     */
    Resource getParent(Resource resource);

    void setParent(Resource resource, Container newParent);

    /**
     * Returns the children for the given resource by {@code path}.
     *
     * @param path
     *         non null path to the resource which children should be retrieved
     * @return immutable instance of {@link FluentIterable} with the children iterator
     * @throws NullPointerException
     *         in case if given {@code path} is null, reason includes:
     *         <ul>
     *         <li>Null path occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code path}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @see Path
     * @since 5.0.0
     */
    FluentIterable<Resource> getChildren(Path path);

    /**
     * Returns the children for the given {@code resource}.
     *
     * @param resource
     *         non null instance of resource which children should be retrieved
     * @return immutable instance of {@link FluentIterable} with the children iterator
     * @throws NullPointerException
     *         in case if given {@code resource} is null, reason includes:
     *         <ul>
     *         <li>Null resource occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code resource}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @since 5.0.0
     */
    FluentIterable<Resource> getChildren(Resource resource);

    /**
     * Returns the children for the given resource by {@code path} recursively.
     *
     * @param path
     *         non null path to the resource which children should be retrieved
     * @return immutable instance of {@link FluentIterable} with the children iterator
     * @throws NullPointerException
     *         in case if given {@code resource} is null, reason includes:
     *         <ul>
     *         <li>Null resource occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code resource}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @since 5.0.0
     */
    FluentIterable<Resource> getChildrenRecursively(Path path);

    /**
     * Returns the children for the given {@code resource} recursively.
     *
     * @param resource
     *         non null instance of resource which children should be retrieved
     * @return immutable instance of {@link FluentIterable} with the children iterator
     * @throws NullPointerException
     *         in case if given {@code resource} is null, reason includes:
     *         <ul>
     *         <li>Null resource occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code resource}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @since 5.0.0
     */
    FluentIterable<Resource> getChildrenRecursively(Resource resource);

    /**
     * Returns the resource by given {@code path}.
     *
     * @param path
     *         non null path to the resource
     * @return instance of the resource stored by given {@code path}
     * @throws NullPointerException
     *         in case if given {@code path} is null, reason includes:
     *         <ul>
     *         <li>Null path occurred</li>
     *         </ul>
     * @throws IllegalStateException
     *         in case if storage doesn't contain resource with given {@code path}, reason includes:
     *         <ul>
     *         <li>Record with id %d does not exists</li>
     *         </ul>
     * @see Path
     * @since 5.0.0
     */
    Resource getResource(Path path);

    /**
     * Returns the size of the stored resource in the storage.
     *
     * @return the size of stored resources
     * @since 5.0.0
     */
    int size();

    /**
     * Returns all stored resources in the storage.
     *
     * @return immutable instance of {@link FluentIterable} with the children iterator
     * @since 5.0.0
     */
    FluentIterable<Resource> getAll();

    /**
     * Tries to find resource with given {@code path} or nearest parent with the path which has sub-path of given {@code path}.
     * <p>
     * In other words method works in two ways:
     * <p>
     * 1. When resource with given {@code path} exists - it returns.
     * 2. When resource with given {@code path} doesn't exist - method works in follow workflow:
     * 2.1 Get parent path from given {@code path}
     * 2.2 Check if path is not a root
     * 2.3 Get resource if it exists, otherwise go to #2.1
     *
     * @param path
     *         to the resource which is looking for
     * @return instance of the resource which is stores by given {@code path}, otherwise nearest parent
     * @throws NullPointerException
     *         in case if given {@code path} is null, reason includes:
     *         <ul>
     *         <li>Null path occurred</li>
     *         </ul>
     * @since 5.0.0
     */
    Resource tryFind(Path path);
}
