package com.samwagg.gravity.main_game_module;

/**
 * May need to rethink testibility. MainGameFacade is in charge of creating and integrating the top level game objects
 * (in addition to providing a high level interface to the module once created) so by definition it has dependencies
 * that it creates rather than having injected...
 *
 * It seems necessary for something to be the "factory" for this core module, I need to understand if it's even appropriate
 * to have unit tests for it. Perhaps I am violating SRP and the "facade" should be separate from (and created by) the "factory"
 */
public class MainGameFacadeTest {

}
