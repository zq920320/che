package org.eclipse.che.ide.resources.management;

import com.google.common.annotations.Beta;

import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.project.ProjectServiceClient;
import org.eclipse.che.ide.api.resources.Container;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.Folder;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.resources.ResourceChangedEvent;
import org.eclipse.che.ide.resource.Path;


/**
 * Intermediate layer for operating with file based resources. Main goal of this component is to create
 * layer that communicates between interface based objects that represents virtual file system and low level
 * interface which actually communicates with server based on the data transfer objects.
 *
 * @author Vlad Zhukovskyi
 * @since 5.0.0
 */
@Beta
public interface ResourceManager {

    /**
     * Resolve an instance of {@link Resource} by given {@code path}.
     * <p>
     * Usage:
     * <pre><code>
     * manager.resolve(null, Path.valueOf("/path/to/the/resource"))
     *        .then(new{@code Operation<Resource>}() {
     *           {@code @Override}
     *            public void apply(Resource arg) throws OperationException {
     *                //handle resolved resource
     *            }
     *        })
     *        .catchError(new{@code Operation<PromiseError>}() {
     *           {@code @Override}
     *            public void apply(PromiseError arg) throws OperationException {
     *                //handle exception
     *            }
     *        });
     * </code></pre>
     *
     * @param requestor
     * @param path
     * @return
     */
    Promise<Resource> resolve(Object requestor, Path path);

    /**
     * Creates new directory by given {@code path}.
     * <p>
     * Usage:
     * <pre><code>
     * manager.mkdir(null, Path.valueOf("/path/to/new/folder"))
     *        .then(new{@code Operation<Folder>}() {
     *           {@code @Override}
     *            public void apply(Folder folder) throws OperationException {
     *                //handle folder creation
     *            }
     *        })
     *        .catchError(new{@code Operation<PromiseError>}() {
     *           {@code @Override}
     *            public void apply(PromiseError error) throws OperationException {
     *                //handle exception
     *            }
     *        });
     * </code></pre>
     * <p>
     * Given {@code path} may be an relative, then path will be result of operation {@link Resource#getLocation()} + {@code path}.
     *
     * @param requestor
     *         any object to control who called this method. See {@link ResourceChangedEvent#getRequestor()}
     * @param path
     *         desired path where new folder should be created.
     * @return the {@link Promise} object with the instance of newly created folder or reject with detailed description of the problem
     * @throws ResourceAlreadyExistsException
     *         in case if folder with given {@code path} already exists
     * @see Folder
     * @see Promise
     * @see Path
     * @since 5.0.0
     */
    Promise<Folder> mkdir(Object requestor, Path path);

    Promise<File> touch(Object requestor, Path path, String content);

    Promise<Void> rm(Object requestor, Resource resource);

    <R extends Resource> Promise<R> move(Object requestor, R resource, Path target);

    <R extends Resource> Promise<R> copy(Object requestor, R resource, Path target);

    Container root();

    Promise<Iterable<Resource>> ls(Container container);

    Promise<Iterable<Resource>> ls(Container container, int depth);

    Promise<String> cat(File file);

//    class Foo {
//        void bar() {
//
//            ResourceManager manager = ...;
//
//manager.resolve(null, Path.valueOf("/path/to/the/resource"))
//       .then(new Operation<Resource>() {
//           @Override
//           public void apply(Resource arg) throws OperationException {
//               //handle resolved resource
//           }
//       })
//       .catchError(new Operation<PromiseError>() {
//           @Override
//           public void apply(PromiseError arg) throws OperationException {
//               //handle exception
//           }
//       });
//
//
//        }
//    }
}
