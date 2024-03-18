package TestSuites;

import GameEngine.GameEngineTest;
import Models.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for testing issue and execution of order functionality and various
 * player services of adding players, assigning armies and countries
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ AdvanceTest.class, BombTest.class, ContinentTest.class, DeployTest.class, DiplomacyTest.class, MapTest.class, PlayerTest.class})
public class ModelsTestSuite {
}