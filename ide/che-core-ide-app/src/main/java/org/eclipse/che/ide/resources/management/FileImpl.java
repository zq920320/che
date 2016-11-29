package org.eclipse.che.ide.resources.management;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.resources.File;

/**
 * Created by vlad on 29.11.16.
 */
public class FileImpl extends ResourceImpl implements File {

    public FileImpl(ResourceManager delegate) {
        super(delegate);
    }

    @Override
    public void setModificationStamp(String stamp) {

    }

    @Override
    public String getModificationStamp() {
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getMediaType() {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public String getContentUrl() {
        return null;
    }

    @Override
    public Promise<String> getContent() {
        return null;
    }

    @Override
    public Promise<Void> updateContent(String content) {
        return null;
    }

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public String getNameWithoutExtension() {
        return null;
    }

    @Override
    public int getResourceType() {
        return FILE;
    }
}
