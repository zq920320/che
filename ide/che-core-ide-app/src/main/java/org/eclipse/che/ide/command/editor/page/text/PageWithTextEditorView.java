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
package org.eclipse.che.ide.command.editor.page.text;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;

import org.eclipse.che.ide.api.mvp.View;

/**
 * Contract of the view for {@link AbstractPageWithTextEditor}.
 *
 * @author Artem Zatsarynnyi
 */
public interface PageWithTextEditorView extends View<PageWithTextEditorView.ActionDelegate> {

    /** Returns the container where the editor should be placed. */
    SimpleLayoutPanel getEditorContainer();

    /** The action delegate for this view. */
    interface ActionDelegate {

        /** Called when exploring macros is requested. */
        void onExploreMacros();
    }
}
