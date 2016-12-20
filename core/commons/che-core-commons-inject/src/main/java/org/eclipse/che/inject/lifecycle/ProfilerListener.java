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

import com.google.inject.spi.ProvisionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sj on 20.12.16.
 */
public class ProfilerListener implements ProvisionListener {
    private static final Logger LOG = LoggerFactory.getLogger(ProfilerListener.class);

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        long time = System.currentTimeMillis();
        try {
            provision.provision();
        } finally {
            LOG.info("Provision {} time {} ms", provision.getBinding().getKey(), System.currentTimeMillis() - time);
        }
    }
}
