/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.inject.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/** @author andrew00x */
public final class Destroyer {

    private static final Logger LOG = LoggerFactory.getLogger(Destroyer.class);

    // Don't prevent instance from being discarded by the garbage collector.
    private final WeakHashMap<Object, Method[]> map;
    private final DestroyErrorHandler           errorHandler;

    public Destroyer(DestroyErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        map = new WeakHashMap<>();
    }

    public void add(Object instance, Method[] m) {
        synchronized (map) {
            map.put(instance, m);
        }
    }

    public void destroy() {
        synchronized (map) {
            for (Map.Entry<Object, Method[]> entry : map.entrySet()) {
                final Object instance = entry.getKey();
                final Method[] methods = entry.getValue();
                for (Method method : methods) {
                    long time = System.currentTimeMillis();
                    try {
                        method.invoke(instance);
                    } catch (IllegalArgumentException e) {
                        // method MUST NOT have any parameters
                        errorHandler.onError(instance, method, e);
                    } catch (IllegalAccessException e) {
                        errorHandler.onError(instance, method, e);
                    } catch (InvocationTargetException e) {
                        errorHandler.onError(instance, method, e.getTargetException());
                    } finally {
                        LOG.info("Destroy time for class {} method {} is {} msec", methods.getClass().getName(), method.getName(),
                                 System.currentTimeMillis() - time);

                    }
                }
            }
        }
    }
}
