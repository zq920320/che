package org.eclipse.che.ide.resources.management;

import org.eclipse.che.ide.resource.Path;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link HashCodeBasedKeyProvider}.
 *
 * @author Vlad Zhukovskyi
 */
public class PathKeyProviderImplTest {

    private PathKeyProvider pathKeyProvider;

    @Before
    public void setUp() throws Exception {
        pathKeyProvider = new HashCodeBasedKeyProvider();
    }

    @Test
    public void testShouldCheckUniqueValue() throws Exception {
        final Path checkedPath = Path.valueOf("/foo/bar/zxc");

        assertEquals((int)pathKeyProvider.getKey(checkedPath), checkedPath.hashCode());
    }
}