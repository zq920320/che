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
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.resources.Container;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.resources.ResourceInterceptor;
import org.eclipse.che.ide.resource.Path;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Iterables.unmodifiableIterable;

/**
 * Memory based implementation of {@link ResourceStorage}.
 *
 * @author Vlad Zhukovskyi
 * @since 5.0.0
 */
@Beta
public class MemoryResourceStorage implements ResourceStorage {

    /**
     * Map which contains records about cached resources. As keys, resources id are used which can be obtained by calling
     * internal implementation of {@link PathKeyProvider}. Resource id is unique identifier which based on the resource's hashcode. For
     * more
     * information about resource identifier see {@link PathKeyProvider}. As values, resource's meta data are used. Meta data
     * contains information about resource itself and bound children. For more information about meta data see {@link ResourceMetaData}.
     *
     * @see ResourceMetaData
     */
    private Map<Integer, ResourceMetaData> storageRecords;

    /**
     * Resource interceptor set which is used to modify specific resource before it has been registered in the internal storage.
     *
     * @see ResourceInterceptor
     */
    private final Set<ResourceInterceptor> resourceInterceptors;

    /**
     * Unique key provider for storing resource in internal storage.
     *
     * @see PathKeyProvider
     */
    private final PathKeyProvider keyProvider;

    @Inject
    public MemoryResourceStorage(Set<ResourceInterceptor> resourceInterceptors, PathKeyProvider keyProvider) {
        storageRecords = new HashMap<>();
        this.resourceInterceptors = resourceInterceptors;
        this.keyProvider = keyProvider;
    }

    @Override
    public void add(Resource... resources) {
        checkNotNull(resources);

        for (Resource resource : resources) {
            add(resource);
        }
    }

    @Override
    public void add(Resource resource) {

        interceptResource(resource);

        if (containsRecord(resource)) {
            updateExistedRecord(resource);
        } else {
            createNewRecord(resource);
        }
    }

    private void updateExistedRecord(Resource resource) {
        final ResourceMetaData existedMetaData = getResourceMetaData(getUniqueKey(resource));

        existedMetaData.setResource(resource);
    }

    private boolean containsRecord(Resource resource) {
        checkResourceWithIdNotNull(resource);

        try {
            return getResourceMetaData(getUniqueKey(resource)) != null;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private void createNewRecord(Resource resource) {
        checkResourceWithIdNotNull(resource);

        final ResourceMetaData resourceMetaData = new ResourceMetaData(resource);
        storageRecords.put(getUniqueKey(resourceMetaData.getResource()), resourceMetaData);

        if (!resource.getLocation().isRoot()) {
            final ResourceMetaData parentResourceMetaData = getParentResourceMetaData(resource);

            parentResourceMetaData.addChild(getUniqueKey(resource));
        }
    }

    private void interceptResource(Resource resource) {
        for (ResourceInterceptor resourceInterceptor : resourceInterceptors) {
            resourceInterceptor.intercept(resource);
        }
    }

    @Override
    public void delete(Path path) {
        checkPathNotNull(path);

        removeResourceMetaData(getUniqueKey(path));
    }

    @Override
    public void delete(Resource resource) {
        checkResourceWithIdNotNull(resource);

        removeResourceMetaData(getUniqueKey(resource));
    }

    @Override
    public void deleteRecursively(Path path) {
        checkPathNotNull(path);

        deleteRecursively(getUniqueKey(path));
    }

    @Override
    public void deleteRecursively(Resource resource) {
        checkResourceWithIdNotNull(resource);

        deleteRecursively(getUniqueKey(resource));
    }

    private void deleteRecursively(int id) {
        final ResourceMetaData resourceMetaData = getResourceMetaData(id);

        for (Resource resourceToDelete : getChildren(resourceMetaData.getResource())) {
            deleteRecursively(getUniqueKey(resourceToDelete));
        }

        removeResourceMetaData(id);
    }

    @Override
    public Resource getParent(Resource resource) {
        return getParentResourceMetaData(resource).getResource();
    }

    @Override
    public void setParent(Resource resource, Container newParent) {
        final ResourceMetaData parentResourceMetaData = getParentResourceMetaData(resource);

        final int resourceIdToMove = getUniqueKey(resource);

        parentResourceMetaData.removeChild(resourceIdToMove);

        final ResourceMetaData resourceMetaData = getResourceMetaData(getUniqueKey(newParent));

        resourceMetaData.addChild(resourceIdToMove);
    }

    private ResourceMetaData getParentResourceMetaData(Resource resource) {
        checkResourceWithIdNotNull(resource);

        return getResourceMetaData(calculateParentId(resource));
    }

    @Override
    public FluentIterable<Resource> getChildren(Path path) {
        checkPathNotNull(path);

        return getChildren(getUniqueKey(path));
    }

    @Override
    public FluentIterable<Resource> getChildren(Resource resource) {
        checkResourceWithIdNotNull(resource);

        return getChildren(getUniqueKey(resource));
    }

    private FluentIterable<Resource> getChildren(int parentId) {
        checkRecordExists(parentId);

        final ResourceMetaData resourceMetaData = getResourceMetaData(parentId);

        final FluentIterable<Integer> childrenIds = resourceMetaData.getChildrenIds();

        if (childrenIds.isEmpty()) {
            return FluentIterable.of(new Resource[0]);
        }

        final List<Resource> children = new ArrayList<>(childrenIds.size());

        for (int childId : childrenIds) {
            final ResourceMetaData childMetaData = getResourceMetaData(childId);

            children.add(childMetaData.getResource());
        }

        return FluentIterable.from(unmodifiableIterable(children));
    }

    @Override
    public FluentIterable<Resource> getChildrenRecursively(Path path) {
        checkPathNotNull(path);

        return getChildrenRecursively(getUniqueKey(path));
    }

    @Override
    public FluentIterable<Resource> getChildrenRecursively(Resource resource) {
        checkResourceWithIdNotNull(resource);

        return getChildrenRecursively(getUniqueKey(resource));
    }

    private FluentIterable<Resource> getChildrenRecursively(int id) {
        final List<Resource> children = new ArrayList<>();

        doGetChildrenRecursively(id, children);

        return FluentIterable.from(unmodifiableIterable(children));
    }

    private void doGetChildrenRecursively(int parentId, List<Resource> collector) {
        final ResourceMetaData resourceMetaData = getResourceMetaData(parentId);

        for (Resource child : getChildren(resourceMetaData.getResource())) {
            collector.add(child);

            doGetChildrenRecursively(getUniqueKey(child), collector);
        }
    }

    @Override
    public Resource getResource(Path path) {
        checkPathNotNull(path);

        return getResource(getUniqueKey(path));
    }

    private Resource getResource(int id) {
        return getResourceMetaData(id).getResource();
    }

    private ResourceMetaData getResourceMetaData(int id) {
        checkRecordExists(id);

        return storageRecords.get(id);
    }

    private void removeResourceMetaData(int id) {
        checkState(storageRecords.remove(id) != null, "Record with id %d does not exists", id);
    }

    private void checkRecordExists(int id) {
        checkState(storageRecords.containsKey(id), "Record with id %s was not found in storage", id);
    }

    private void checkPathNotNull(Path path) {
        checkNotNull(path, "Null path occurred");
    }

    private void checkResourceWithIdNotNull(Resource resource) {
        checkNotNull(resource, "Null resource occurred");
    }

    @Override
    public int size() {
        return storageRecords.size();
    }

    @Override
    public FluentIterable<Resource> getAll() {
        final Iterable<Resource> resources =
                transform(storageRecords.values(), new Function<ResourceMetaData, Resource>() {
                    @Nullable
                    @Override
                    public Resource apply(@Nullable ResourceMetaData input) {
                        checkNotNull(input);

                        return input.getResource();
                    }
                });

        return FluentIterable.from(unmodifiableIterable(resources));
    }

    private int getUniqueKey(Resource resource) {
        return keyProvider.getKey(resource.getLocation());
    }

    private int getUniqueKey(Path path) {
        return keyProvider.getKey(path);
    }

    private int calculateParentId(Resource resource) {
        final Path resourceLocation = resource.getLocation();

        checkState(!resourceLocation.isRoot(), "Parent can not be obtained for root resource");

        return getUniqueKey(resourceLocation.parent());
    }

    @Override
    public Resource tryFind(Path path) {
        checkPathNotNull(path);

        return doFind(path);
    }

    private Resource doFind(Path path) {
        if (path.isRoot()) {
            return getResource(getUniqueKey(Path.ROOT));
        }

        while (!path.isRoot()) {
            try {
                return getResource(getUniqueKey(path));
            } catch (IllegalStateException e) {
                // resource not found, take path.parent()
                path = path.parent();
            }
        }

        return null;
    }
}
