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

import com.google.gwtmockito.GwtMockitoTestRunner;

import org.eclipse.che.ide.api.resources.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ResourceMetaData}.
 *
 * @author Vlad Zhukovskyi
 */
@RunWith(GwtMockitoTestRunner.class)
public class ResourceMetaDataTest {

    @Mock
    Resource resource;

    private ResourceMetaData resourceMetaData;

    @Before
    public void setUp() throws Exception {
        when(resource.isFile()).thenReturn(false);

        resourceMetaData = new ResourceMetaData(resource);
    }

    @Test
    public void testAddChild() throws Exception {
        resourceMetaData.addChild(1);

        assertTrue(resourceMetaData.getChildrenIds().contains(1));
    }

    @Test
    public void testRemoveChild() throws Exception {
        resourceMetaData.addChild(1);
        resourceMetaData.addChild(2);
        resourceMetaData.addChild(3);

        assertTrue(resourceMetaData.getChildrenIds().contains(2));

        resourceMetaData.removeChild(2);

        assertFalse(resourceMetaData.getChildrenIds().contains(2));
    }
}