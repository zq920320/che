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

import {EnvironmentManager} from './environment-manager';
import {EnvironmentManagerMachine, IEnvironmentManagerMachine} from './environment-manager-machine';

/**
 * This is the implementation of environment manager that handles the docker image format of environment.
 *
 * Format sample and specific description:
 * <code>
 *     condevy/ubuntu_jdk8
 * </code>
 *
 * The recipe type is <code>dockerimage</code>. This environment can contain only one machine.
 * Machine is described by image and in machines attribute of the environment (machine configs).
 * The machine configs contain memoryLimitBytes in attributes, servers and agent.
 * Environment variables can not be set.
 *
 * @author Ann Shumilova
 */
export class DockerImageEnvironmentManager extends EnvironmentManager {

  constructor() {
    super();
  }

  /**
   * Retrieves the list of machines.
   *
   * @param {che.IWorkspaceEnvironment} environment environment's configuration
   * @returns {IEnvironmentManagerMachine[]} list of machines defined in environment
   */
  getMachines(environment: che.IWorkspaceEnvironment): IEnvironmentManagerMachine[] {
    let machines: IEnvironmentManagerMachine[] = [];

    Object.keys(environment.machines).forEach((machineName: string) => {
      let envMachine = angular.copy(environment.machines[machineName]),
          machine = new EnvironmentManagerMachine(machineName, environment.recipe);
      if (envMachine.attributes) {
        machine.attributes = angular.copy(envMachine.attributes);
      }
      if (envMachine.agents) {
        machine.agents = angular.copy(envMachine.agents);
      }
      if (envMachine.servers) {
        machine.servers = angular.copy(envMachine.servers);
      }

      machines.push(machine);
    });

    return machines;
  }

  /**
   * Provides the environment configuration based on machines format.
   *
   * @param {che.IWorkspaceEnvironment} environment origin of the environment to be edited
   * @param {IEnvironmentManagerMachine[]} machines the list of machines
   * @returns {che.IWorkspaceEnvironment} environment's configuration
   */
  getEnvironment(environment: che.IWorkspaceEnvironment, machines: IEnvironmentManagerMachine[]): che.IWorkspaceEnvironment {
    return super.getEnvironment(environment, machines);
  }

  /**
   * Returns a dockerimage.
   *
   * @param {IEnvironmentManagerMachine} machine
   * @returns {{image: string}}
   */
  getSource(machine: IEnvironmentManagerMachine): any {
    return {image: machine.recipe.location};
  }}
