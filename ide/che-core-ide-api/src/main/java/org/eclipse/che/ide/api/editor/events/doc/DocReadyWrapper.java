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
package org.eclipse.che.ide.api.editor.events.doc;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.editor.document.DocumentHandle;
import org.eclipse.che.ide.api.editor.events.DocumentReadyEvent;
import org.eclipse.che.ide.api.editor.events.DocumentReadyHandler;

/**
 * Wrapper around components that need to wait for documents to be ready.
 */
public class DocReadyWrapper<T> {

    private       DocReadyInit<T>     docReadyInit;
    private final T                   wrapped;
    private       HandlerRegistration docReadyRegistration;
    private       DocumentHandle      documentHandle;

    public DocReadyWrapper(final EventBus generalEventBus, final T wrapped) {
        this(generalEventBus, null, wrapped);
    }

    public DocReadyWrapper(final EventBus generalEventBus, @Nullable final DocReadyInit<T> init, final T wrapped) {
        this.docReadyInit = init;
        this.wrapped = wrapped;
        this.docReadyRegistration = generalEventBus.addHandler(DocumentReadyEvent.TYPE,
                                                               new DocumentReadyHandler() {

                                                                   @Override
                                                                   public void onDocumentReady(final DocumentReadyEvent event) {
                                                                       if (event == null) {
                                                                           return;
                                                                       }

                                                                       // stop listening DocReady events
                                                                       if (docReadyRegistration != null) {
                                                                           docReadyRegistration.removeHandler();
                                                                       }
                       documentHandle = event.getDocument().getDocumentHandle();

                       final DocReadyInit<T> initializer = DocReadyWrapper.this.docReadyInit;
                       if (initializer != null) {
                           initializer.initialize(documentHandle, DocReadyWrapper.this.wrapped);
                       }
                   }
               });
    }

    /**
     * Remove all active handlers.
     */
    public void release() {
        if (this.docReadyRegistration != null) {
            docReadyRegistration.removeHandler();
            docReadyRegistration = null;
        }
    }
    
    protected void setDocReadyInit(final DocReadyInit<T> initializer) {
        this.docReadyInit = initializer;
    }

    /** Interface for initialization that occurs when the document is ready. */
    public interface DocReadyInit<T> {
        /** Triggered when the document is ready. */
        public void initialize(DocumentHandle documentHandle, T wrapped);
    }
}
