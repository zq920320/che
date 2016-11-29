package org.eclipse.che.ide.resources.management;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import org.eclipse.che.api.core.model.project.ProjectConfig;
import org.eclipse.che.api.project.shared.dto.SourceEstimation;
import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.api.promises.client.FunctionException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.resources.Container;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.Folder;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.resources.ResourceDelta;
import org.eclipse.che.ide.resource.Path;

/**
 * Created by vlad on 29.11.16.
 */
public abstract class ContainerImpl extends ResourceImpl implements Container {

    public ContainerImpl(ResourceManager delegate) {
        super(delegate);
    }

    @Override
    public Promise<Optional<File>> getFile(Path relativePath) {
        final Path absolutePath = getLocation().append(relativePath);

        return delegate.resolveResource(this, absolutePath)
                       .then(new Function<Resource, Optional<File>>() {
                           @Override
                           public Optional<File> apply(Resource resource) throws FunctionException {
                               Preconditions.checkArgument(resource.isFile());

                               return Optional.of((File)resource);
                           }
                       })
                       .catchError(new Function<PromiseError, Optional<File>>() {
                           @Override
                           public Optional<File> apply(PromiseError error) throws FunctionException {
                               return Optional.absent();
                           }
                       });
    }

    @Override
    public Promise<Optional<File>> getFile(String relativePath) {
        return getFile(Path.valueOf(relativePath));
    }

    @Override
    public Promise<Optional<Container>> getContainer(Path relativePath) {
        final Path absolutePath = getLocation().append(relativePath);

        return delegate.resolveResource(this, absolutePath)
                       .then(new Function<Resource, Optional<Container>>() {
                           @Override
                           public Optional<Container> apply(Resource resource) throws FunctionException {
                               Preconditions.checkArgument(resource instanceof Container);

                               return Optional.of((Container)resource);
                           }
                       })
                       .catchError(new Function<PromiseError, Optional<Container>>() {
                           @Override
                           public Optional<Container> apply(PromiseError error) throws FunctionException {
                               return Optional.absent();
                           }
                       });
    }

    @Override
    public Promise<Optional<Container>> getContainer(String relativePath) {
        return getContainer(Path.valueOf(relativePath));
    }

    @Override
    public Promise<Resource[]> getChildren() {
        return delegate.getChildren(this, this).then(new Function<Iterable<Resource>, Resource[]>() {
            @Override
            public Resource[] apply(Iterable<Resource> arg) throws FunctionException {
                return Iterables.toArray(arg, Resource.class);
            }
        });
    }

    @Override
    public Promise<Resource[]> getChildren(boolean force) {
        return delegate.getChildren(this, this).then(new Function<Iterable<Resource>, Resource[]>() {
            @Override
            public Resource[] apply(Iterable<Resource> arg) throws FunctionException {
                return Iterables.toArray(arg, Resource.class);
            }
        });
    }

    @Override
    public Project.ProjectRequest newProject() {
        return new Project.ProjectRequest() {

            ProjectConfig config = null;

            @Override
            public Request<Project, ProjectConfig> withBody(ProjectConfig config) {
                this.config = config;
                return this;
            }

            @Override
            public ProjectConfig getBody() {
                return config;
            }

            @Override
            public Promise<Project> send() {
                Preconditions.checkNotNull(config);

                return delegate.createProject(ContainerImpl.this, config);
            }
        };
    }

    @Override
    public Project.ProjectRequest importProject() {
        return new Project.ProjectRequest() {

            ProjectConfig config = null;

            @Override
            public Request<Project, ProjectConfig> withBody(ProjectConfig config) {
                this.config = config;
                return this;
            }

            @Override
            public ProjectConfig getBody() {
                return config;
            }

            @Override
            public Promise<Project> send() {
                Preconditions.checkNotNull(config);

                return delegate.importProject(ContainerImpl.class, config);
            }
        };
    }

    @Override
    public Promise<Folder> newFolder(String name) {
        final Path absolutePath = getLocation().append(name);

        return delegate.createFolder(this, absolutePath);
    }

    @Override
    public Promise<File> newFile(String name, String content) {
        final Path absolutePath = getLocation().append(name);

        return delegate.createFile(this, absolutePath, content);
    }

    @Override
    public Promise<Resource[]> synchronize() {
        return null;
    }

    @Override
    public Promise<ResourceDelta[]> synchronize(ResourceDelta... deltas) {
        return null;
    }

    @Override
    public Promise<Resource[]> search(String fileMask, String contentMask) {
        return null;
    }

    @Override
    public Promise<Resource[]> getTree(int depth) {
        return delegate.getPlainTree(this, this, depth)
                       .then(new Function<Iterable<Resource>, Resource[]>() {
                           @Override
                           public Resource[] apply(Iterable<Resource> resources) throws FunctionException {
                               return Iterables.toArray(resources, Resource.class);
                           }
                       });
    }

    @Override
    public Promise<SourceEstimation> estimate(String projectType) {
        return null;
    }
}
