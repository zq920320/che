package org.eclipse.che.ide.resources.management;

import com.google.common.collect.FluentIterable;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.resources.ResourceInterceptor;
import org.eclipse.che.ide.resource.Path;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for the {@link MemoryResourceStorage}.
 *
 * @author Vlad Zhukovskyi
 */
@RunWith(GwtMockitoTestRunner.class)
public class MemoryResourceStorageTest {

    @Mock
    ResourceInterceptor resourceInterceptorMock;
    @Mock
    PathKeyProvider     pathKeyProvider;

    @Mock
    Resource rootResourceMock;
    @Mock
    Resource resourceAMock;
    @Mock
    Resource resourceAChildMock;
    @Mock
    Resource resourceAAChildMock;
    @Mock
    Resource resourceABChildMock;
    @Mock
    Resource resourceAUpdatedMock;
    @Mock
    Resource resourceBMock;

    Path rootResourcePath    = Path.ROOT;
    Path resourceAPath       = Path.valueOf("/a");
    Path resourceAChildPath  = Path.valueOf("/a/child");
    Path resourceAAChildPath = Path.valueOf("/a/child/sub_1");
    Path resourceABChildPath = Path.valueOf("/a/child/sub_2");
    Path resourceBPath       = Path.valueOf("/b");

    private MemoryResourceStorage resourceStorage;

    @Before
    public void setUp() throws Exception {
        resourceStorage = new MemoryResourceStorage(newHashSet(resourceInterceptorMock), new HashCodeBasedKeyProvider());

        when(resourceAMock.getLocation()).thenReturn(resourceAPath);
        when(resourceBMock.getLocation()).thenReturn(resourceBPath);

        when(resourceAUpdatedMock.getLocation()).thenReturn(resourceAPath);

        when(resourceAChildMock.getLocation()).thenReturn(resourceAChildPath);
        when(resourceAAChildMock.getLocation()).thenReturn(resourceAAChildPath);
        when(resourceABChildMock.getLocation()).thenReturn(resourceABChildPath);

        when(rootResourceMock.getLocation()).thenReturn(rootResourcePath);

        resourceStorage.add(rootResourceMock);
    }

    @Test
    public void testAddResource() throws Exception {
        resourceStorage.add(resourceAMock);

        verify(resourceInterceptorMock).intercept(eq(resourceAMock));

        assertEquals(resourceStorage.size(), 2);

        final FluentIterable<Resource> allResources = resourceStorage.getAll();

        assertTrue(allResources.contains(rootResourceMock));
        assertTrue(allResources.contains(resourceAMock));
    }

    @Test
    public void testUpdateExistedRecordWithNewResource() throws Exception {
        resourceStorage.add(resourceAMock);

        assertEquals(resourceStorage.size(), 2);

        FluentIterable<Resource> allResources = resourceStorage.getAll();

        assertTrue(allResources.contains(rootResourceMock));
        assertTrue(allResources.contains(resourceAMock));

        resourceStorage.add(resourceAUpdatedMock);

        assertEquals(resourceStorage.size(), 2);

        allResources = resourceStorage.getAll();

        assertTrue(allResources.contains(rootResourceMock));
        assertTrue(allResources.contains(resourceAUpdatedMock));
    }

    @Test
    public void testDeleteByPath() throws Exception {
        resourceStorage.add(resourceAMock);

        assertEquals(resourceStorage.size(), 2);

        resourceStorage.delete(resourceAPath);

        assertEquals(resourceStorage.size(), 1);

        FluentIterable<Resource> allResources = resourceStorage.getAll();

        assertTrue(allResources.contains(rootResourceMock));
    }

    @Test
    public void testDeleteByResourceWithId() throws Exception {
        resourceStorage.add(resourceAMock);

        assertEquals(resourceStorage.size(), 2);

        resourceStorage.delete(resourceAMock);

        assertEquals(resourceStorage.size(), 1);

        FluentIterable<Resource> allResources = resourceStorage.getAll();

        assertTrue(allResources.contains(rootResourceMock));
    }

    @Test
    public void testDeleteRecursivelyByPath() throws Exception {
        resourceStorage.add(resourceAMock);
        resourceStorage.add(resourceAChildMock);

        assertEquals(resourceStorage.size(), 3);

        resourceStorage.deleteRecursively(resourceAPath);

        assertEquals(resourceStorage.size(), 1);

        FluentIterable<Resource> allResources = resourceStorage.getAll();

        assertTrue(allResources.contains(rootResourceMock));
    }

    @Test
    public void testDeleteRecursivelyByResourceWithId() throws Exception {
        resourceStorage.add(resourceAMock);
        resourceStorage.add(resourceAChildMock);

        assertEquals(resourceStorage.size(), 3);

        resourceStorage.deleteRecursively(resourceAMock);

        assertEquals(resourceStorage.size(), 1);

        FluentIterable<Resource> allResources = resourceStorage.getAll();

        assertTrue(allResources.contains(rootResourceMock));
    }

    @Test
    public void testGetParent() throws Exception {
        resourceStorage.add(resourceAMock);
        resourceStorage.add(resourceAChildMock);

        assertEquals(resourceStorage.size(), 3);

        final Resource parent = resourceStorage.getParent(resourceAChildMock);

        assertEquals(parent, resourceAMock);
    }

    @Test
    public void testGetChildrenByPath() throws Exception {
        resourceStorage.add(resourceAMock);
        resourceStorage.add(resourceAChildMock);

        assertEquals(resourceStorage.size(), 3);

        final FluentIterable<Resource> children = resourceStorage.getChildren(resourceAPath);

        assertEquals(children.size(), 1);
        assertTrue(children.contains(resourceAChildMock));
    }

    @Test
    public void testGetChildrenByResourceWithId() throws Exception {
        resourceStorage.add(resourceAMock);
        resourceStorage.add(resourceAChildMock);

        assertEquals(resourceStorage.size(), 3);

        final FluentIterable<Resource> children = resourceStorage.getChildren(resourceAMock);

        assertEquals(children.size(), 1);
        assertTrue(children.contains(resourceAChildMock));
    }

    @Test
    public void testGetResource() throws Exception {
        resourceStorage.add(resourceAMock);

        assertEquals(resourceStorage.size(), 2);

        final Resource resource = resourceStorage.getResource(resourceAPath);
        final Resource rootResource = resourceStorage.getResource(Path.ROOT);

        assertEquals(resource, resourceAMock);
        assertEquals(rootResource, rootResourceMock);
    }

    @Test
    public void testCorrectSize() throws Exception {
        assertEquals(resourceStorage.size(), 1);
    }

    @Test
    public void testGetAll() throws Exception {
        resourceStorage.add(resourceAMock);
        resourceStorage.add(resourceBMock);

        final FluentIterable<Resource> all = resourceStorage.getAll();

        assertEquals(all.size(), 3);
    }

    @Test
    public void testGetChildrenRecursivelyWithResource() throws Exception {
        resourceStorage.add(resourceAMock);
        resourceStorage.add(resourceBMock);
        resourceStorage.add(resourceAChildMock);
        resourceStorage.add(resourceAAChildMock);
        resourceStorage.add(resourceABChildMock);

        final FluentIterable<Resource> all = resourceStorage.getAll();

        assertEquals(all.size(), 6);

        final FluentIterable<Resource> allChildren = resourceStorage.getChildrenRecursively(resourceAMock);

        assertEquals(allChildren.size(), 3);
        assertTrue(allChildren.contains(resourceAChildMock));
        assertTrue(allChildren.contains(resourceAAChildMock));
        assertTrue(allChildren.contains(resourceABChildMock));
    }

    @Test
    public void testGetChildrenRecursivelyByPath() throws Exception {
        resourceStorage.add(resourceAMock);
        resourceStorage.add(resourceBMock);
        resourceStorage.add(resourceAChildMock);
        resourceStorage.add(resourceAAChildMock);
        resourceStorage.add(resourceABChildMock);

        final FluentIterable<Resource> all = resourceStorage.getAll();

        assertEquals(all.size(), 6);

        final FluentIterable<Resource> allChildren = resourceStorage.getChildrenRecursively(resourceAPath);

        assertEquals(allChildren.size(), 3);
        assertTrue(allChildren.contains(resourceAChildMock));
        assertTrue(allChildren.contains(resourceAAChildMock));
        assertTrue(allChildren.contains(resourceABChildMock));
    }

    @Test
    public void testFindResourceByNonExistPath() throws Exception {
        resourceStorage.add(resourceAMock);

        final Resource resource = resourceStorage.tryFind(resourceAPath.append("some/nonexisted/path"));

        assertEquals(resource, resourceAMock);
    }
}