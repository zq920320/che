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
import {IEnvironmentManagerMachine} from './environment-manager-machine';

/**
 * This is base class, which describes the environment manager.
 * It's aim is to handle machines retrieval and editing, based on the type of environment.
 */

const WS_AGENT_NAME: string = 'org.eclipse.che.ws-agent';
const TERMINAL_AGENT_NAME: string = 'org.eclipse.che.terminal';
const SSH_AGENT_NAME: string = 'org.eclipse.che.ssh';

export abstract class EnvironmentManager {

  constructor() { }

  get editorMode(): string {
    return '';
  }

  canRenameMachine(machine: any): boolean {
    return false;
  }

  canDeleteMachine(machine: any): boolean {
    return false;
  }

  canAddMachine(machine: any): boolean {
    return false;
  }

  canEditEnvVariables(machine: any): boolean {
    return false;
  }

  /**
   * Retrieves the list of machines.
   *
   * @param {che.IWorkspaceEnvironment} environment
   * @returns {IEnvironmentManagerMachine[]} list of machines defined in environment
   */
  abstract getMachines(environment: che.IWorkspaceEnvironment): IEnvironmentManagerMachine[];

  /**
   * Renames machine.
   *
   * @param {che.IWorkspaceEnvironment} environment
   * @param {string} oldName
   * @param {string} newName
   */
  renameMachine(environment: che.IWorkspaceEnvironment, oldName: string, newName: string): void {
    throw new TypeError('EnvironmentManager: cannot rename machine.');
  }

  /**
   * Removes machine.
   *
   * @param {che.IWorkspaceEnvironment} environment
   * @param {string} name name of machine
   */
  deleteMachine(environment: che.IWorkspaceEnvironment, name: string): void {
    throw new TypeError('EnvironmentManager: cannot delete machine.');
  }

  /**
   * Provides the environment configuration based on machines format.
   *
   * @param {che.IWorkspaceEnvironment} environment origin of the environment to be edited
   * @param {IEnvironmentManagerMachine} machines the list of machines
   * @returns {che.IWorkspaceEnvironment} environment's configuration
   */
  getEnvironment(environment: che.IWorkspaceEnvironment, machines: IEnvironmentManagerMachine[]): che.IWorkspaceEnvironment {
    let newEnvironment = angular.copy(environment);

    machines.forEach((machine: IEnvironmentManagerMachine) => {
      let machineName = machine.name;

      if (angular.isUndefined(newEnvironment.machines)) {
        newEnvironment.machines = {};
      }
      if (angular.isUndefined(newEnvironment.machines[machineName])) {
        newEnvironment.machines[machineName] = {'attributes': {}};
      }
      newEnvironment.machines[machineName].attributes.memoryLimitBytes = machine.attributes.memoryLimitBytes;
      newEnvironment.machines[machineName].agents = angular.copy(machine.agents);
      newEnvironment.machines[machineName].servers = angular.copy(machine.servers);
    });

    return newEnvironment;
  }

  /**
   * Returns whether machine is developer or not.
   *
   * @param {IEnvironmentManagerMachine} machine
   * @returns {boolean}
   */
  isDev(machine: IEnvironmentManagerMachine): boolean {
    return machine.agents && machine.agents.indexOf(WS_AGENT_NAME) >= 0;
  }

  /**
   * Set machine as developer one - contains 'ws-agent' agent.
   *
   * @param {IEnvironmentManagerMachine} machine machine to edit
   * @param isDev defined whether machine is developer or not
   */
  setDev(machine: IEnvironmentManagerMachine, isDev: boolean): void {
    let hasWsAgent = this.isDev(machine);
    if (isDev) {
      machine.agents = machine.agents ? machine.agents : [];
      if (!hasWsAgent) {
        machine.agents.push(WS_AGENT_NAME);
      }
      if (machine.agents.indexOf(SSH_AGENT_NAME) < 0) {
        machine.agents.push(SSH_AGENT_NAME);
      }
      if (machine.agents.indexOf(TERMINAL_AGENT_NAME) < 0) {
        machine.agents.push(TERMINAL_AGENT_NAME);
      }
      return;
    }

    if (!isDev && hasWsAgent) {
      machine.agents.splice(machine.agents.indexOf(WS_AGENT_NAME), 1);
    }
  }

  getServers(machine: any): any {
    return machine.servers || {};
  }

  setServers(machine: any, servers: any): void {
    machine.servers = angular.copy(servers);
  }

  getAgents(machine: any): any[] {
    return machine.agents || [];
  }

  setAgents(machine: any, agents: any[]): void {
    machine.agents = angular.copy(agents);
  }

  /**
   * Returns memory limit from machine's attributes
   *
   * @param {IEnvironmentManagerMachine} machine
   * @returns {number|string} memory limit in bytes
   */
  getMemoryLimit(machine: IEnvironmentManagerMachine): number|string {
    if (machine && machine.attributes && machine.attributes.memoryLimitBytes) {
      return machine.attributes.memoryLimitBytes;
    }

    return -1;
  }

  /**
   * Sets the memory limit of the pointed machine.
   * Value in attributes has the highest priority,
   *
   * @param {IEnvironmentManagerMachine} machine machine to change memory limit
   * @param limit memory limit
   */
  setMemoryLimit(machine: IEnvironmentManagerMachine, limit: number): void {
    machine.attributes = machine.attributes ? machine.attributes : {};
    machine.attributes.memoryLimitBytes = limit;
  }

  getEnvVariables(machine: IEnvironmentManagerMachine): any {
    return null;
  }
}
