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

import org.eclipse.che.ide.api.resources.Resource;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.unmodifiableIterable;

/**
 * Meta data object that describes relation about given resource and it's children.
 *
 * @author Vlad Zhukovskyi
 * @since 5.0.0
 */
@Beta
public class ResourceMetaData {

    private       Resource     resource;
    private final Set<Integer> childrenIds;

    public ResourceMetaData(Resource resource) {
        this.resource = checkNotNull(resource);
        this.childrenIds = new HashSet<>();
    }

    /**
     * Returns the resource which is reserves by current meta data.
     *
     * @return the resource
     * @see Resource
     * @since 5.0.0
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the new resource to the current meta data object.
     *
     * @param resource
     *         the new resource
     * @throws IllegalArgumentException
     *         in case if current resource path is not equals to the new one, reason includes:
     *         <ul>
     *         <li>Paths should be equal on both resources</li>
     *         </ul>
     * @see Resource
     * @since 5.0.0
     */
    public void setResource(Resource resource) {
        checkArgument(this.resource.getLocation().equals(resource.getLocation()), "Paths should be equal on both resources");

        this.resource = resource;
    }

    /**
     * Returns the children identifiers.
     *
     * @return immutable iterable of the children identifiers
     * @throws IllegalStateException
     *         in case if method tries to be called on the file based resource, reason includes:
     *         <ul>
     *         <li>Children is not provided for file based resources</li>
     *         </ul>
     * @since 5.0.0
     */
    public FluentIterable<Integer> getChildrenIds() {
        checkState(!resource.isFile(), "Children is not provided for file based resources");

        return FluentIterable.from(unmodifiableIterable(childrenIds));
    }

    /**
     * Adds new child identifier to the current meta data.
     *
     * @param entryId
     *         child identifier
     * @throws IllegalStateException
     *         in case if method tries to be called on the file based resource, reason includes:
     *         <ul>
     *         <li>Children is not provided for file based resources</li>
     *         </ul>
     * @since 5.0.0
     */
    public void addChild(int entryId) {
        checkState(!resource.isFile(), "Children is not provided for file based resources");

        childrenIds.add(entryId);
    }

    /**
     * Removes given child with {@code entryId} from the current meta data.
     *
     * @param entryId
     *         child identifier
     * @throws IllegalStateException
     *         in case if method tries to be called on the file based resource, reason includes:
     *         <ul>
     *         <li>Children is not provided for file based resources</li>
     *         </ul>
     * @since 5.0.0
     */
    public void removeChild(int entryId) {
        checkState(!resource.isFile(), "Children is not provided for file based resources");

        childrenIds.remove(entryId);
    }
}
