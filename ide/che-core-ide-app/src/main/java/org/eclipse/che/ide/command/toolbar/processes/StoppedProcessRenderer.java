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
package org.eclipse.che.ide.command.toolbar.processes;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import org.eclipse.che.ide.ui.dropdown.DropDownListItemRenderer;

import static com.google.gwt.dom.client.Style.Float.LEFT;
import static com.google.gwt.dom.client.Style.Float.RIGHT;

/**
 * Renders widget for representing {@link StoppedProcess}.
 */
class StoppedProcessRenderer implements DropDownListItemRenderer<StoppedProcess> {

    private final ReRunProcessHandler handler;

    StoppedProcessRenderer(ReRunProcessHandler handler) {
        this.handler = handler;
    }

    @Override
    public Widget render(final StoppedProcess process) {
        final String labelText = process.getMachine().getConfig().getName() + ": <b>" + process.getName() + "</b>";
        final Label nameLabel = new InlineHTML(labelText);
        nameLabel.setWidth("230px");
        nameLabel.getElement().getStyle().setFloat(LEFT);
        nameLabel.setTitle(process.getCommandLine());

        final Button reRunButton = new ReRunButton();
        reRunButton.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handler.onReRunProcess(process);
            }
        }, ClickEvent.getType());

        final FlowPanel panel = new FlowPanel();
        panel.setHeight("25px");
        panel.add(nameLabel);
        panel.add(reRunButton);

        return panel;
    }

    interface ReRunProcessHandler {
        void onReRunProcess(StoppedProcess process);
    }

    /** Button for rerunning process. */
    public class ReRunButton extends Button {

        ReRunButton() {
            super();

            getElement().getStyle().setFloat(RIGHT);
            setHTML("re-run");
        }
    }
}