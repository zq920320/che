package org.eclipse.che.ide.resources.management;

import com.google.common.base.Preconditions;

import org.eclipse.che.api.core.model.project.ProjectConfig;
import org.eclipse.che.api.core.model.project.SourceStorage;
import org.eclipse.che.api.project.shared.dto.SourceEstimation;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.api.resources.Resource;

import java.util.List;
import java.util.Map;

/**
 * Created by vlad on 29.11.16.
 */
public class ProjectImpl extends ContainerImpl implements Project {

    public ProjectImpl(ResourceManager delegate) {
        super(delegate);
    }

    @Override
    public String getPath() {
        return getLocation().toString();
    }

    @Override
    public String getDescription() {
        return delegate.getProjectConfig(this).getDescription();
    }

    @Override
    public String getType() {
        return delegate.getProjectConfig(this).getType();
    }

    @Override
    public List<String> getMixins() {
        return delegate.getProjectConfig(this).getMixins();
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return delegate.getProjectConfig(this).getAttributes();
    }

    @Override
    public SourceStorage getSource() {
        return delegate.getProjectConfig(this).getSource();
    }

    @Override
    public ProjectRequest update() {
        return new ProjectRequest() {

            private ProjectConfig config = null;

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

                return delegate.updateProject(ProjectImpl.this, config);
            }
        };
    }

    @Override
    public boolean isProblem() {
        return false;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public Promise<List<SourceEstimation>> resolve() {
        return null;
    }

    @Override
    public boolean isTypeOf(String type) {
        return false;
    }

    @Override
    public String getAttribute(String key) {
        return null;
    }

    @Override
    public List<String> getAttributes(String key) {
        return null;
    }

    @Override
    public int getResourceType() {
        return PROJECT;
    }
}
