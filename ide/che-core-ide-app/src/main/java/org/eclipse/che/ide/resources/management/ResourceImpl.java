package org.eclipse.che.ide.resources.management;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.resources.Container;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.resources.marker.Marker;
import org.eclipse.che.ide.resource.Path;

/**
 * Created by vlad on 29.11.16.
 */
public abstract class ResourceImpl implements Resource {

    protected ResourceManager delegate;

    public ResourceImpl(ResourceManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isFile() {
        return getResourceType() == FILE;
    }

    @Override
    public boolean isFolder() {
        return getResourceType() == FOLDER;
    }

    @Override
    public boolean isProject() {
        return getResourceType() == PROJECT;
    }

    @Override
    public Promise<Resource> copy(Path destination) {
        return copy(destination, false);
    }

    @Override
    public Promise<Resource> copy(Path destination, boolean force) {
        return delegate.<Resource>copyResource(this, this, destination, force);
    }

    @Override
    public Promise<Resource> move(Path destination) {
        return move(destination, false);
    }

    @Override
    public Promise<Resource> move(Path destination, boolean force) {
        return delegate.<Resource>moveResource(this, this, destination, force);
    }

    @Override
    public Promise<Void> delete() {
        return delegate.deleteResource(null, this);
    }

    @Override
    public Path getLocation() {
        return delegate.resolvePath(this, this);
    }

    @Override
    public String getName() {
        return getLocation().lastSegment();
    }

    @Override
    public Optional<Container> getParent() {
        return Optional.fromNullable(delegate.getParent(this, this));
    }

    @Override
    public Optional<Project> getRelatedProject() {
        return Optional.fromNullable(delegate.getProject(this));
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public Optional<Marker> getMarker(String type) {
        return null;
    }

    @Override
    public Marker[] getMarkers() {
        return new Marker[0];
    }

    @Override
    public void addMarker(Marker marker) {

    }

    @Override
    public boolean deleteMarker(String type) {
        return false;
    }

    @Override
    public boolean deleteAllMarkers() {
        return false;
    }

    @Override
    public Optional<Resource> getParentWithMarker(String type) {
        return null;
    }

    @Override
    public int compareTo(Resource o) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceImpl resource = (ResourceImpl)o;
        return Objects.equal(getLocation(), resource.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLocation());
    }
}
