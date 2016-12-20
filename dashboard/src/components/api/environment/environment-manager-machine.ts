/*
 * Copyright (c) 2015-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 */
'use strict';

/**
 * @author Oleksii Kurinnyi
 */

export interface IEnvironmentManagerMachine extends che.IWorkspaceEnvironmentMachine {
  name: string;
  recipe: any;
}

export class EnvironmentManagerMachine implements IEnvironmentManagerMachine {
  name: string;
  recipe: any;
  attributes: {
    [attrName: string]: any
  } = {};
  agents: string[] = [];
  servers: {
    [serverName: string]: che.IWorkspaceEnvironmentMachineServer
  } = {};

  constructor(name: string, recipe: any) {
    this.name = name;
    this.recipe = angular.copy(recipe);
  }
}

