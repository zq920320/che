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
import com.google.inject.Singleton;

import org.eclipse.che.ide.resource.Path;

/**
 * Default implementation for the {@link PathKeyProvider}.
 *
 * @author Vlad Zhukovskyi
 * @see PathKeyProvider
 * @since 5.0.0
 */
@Beta
public class HashCodeBasedKeyProvider implements PathKeyProvider {
    @Override
    public Integer getKey(Path item) {
        return item.hashCode();
    }
}
