package org.eclipse.che.ide.resources.management;

import org.eclipse.che.ide.api.resources.Folder;
import org.eclipse.che.ide.api.resources.Project;

/**
 * Created by vlad on 29.11.16.
 */
public class FolderImpl extends ContainerImpl implements Folder {
    public FolderImpl(ResourceManager delegate) {
        super(delegate);
    }

    @Override
    public Project.ProjectRequest toProject() {
        return null;
    }

    @Override
    public int getResourceType() {
        return FOLDER;
    }
}
