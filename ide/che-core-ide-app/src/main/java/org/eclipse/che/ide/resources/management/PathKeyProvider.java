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

import com.google.gwt.view.client.ProvidesKey;

import org.eclipse.che.ide.resource.Path;

/**
 * Unique key provider for the given path.
 * Default implementation is based on the generating unique key identifier using path hashcode value.
 *
 * @author Vlad Zhukovskyi
 * @see HashCodeBasedKeyProvider
 * @see ProvidesKey
 * @see Path
 * @since 5.0.0
 */
public interface PathKeyProvider extends ProvidesKey<Path> {
    @Override
    Integer getKey(Path item);
}
