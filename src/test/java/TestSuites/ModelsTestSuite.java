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
@SuiteClasses({ AdvanceTest.class, BombTest.class, CardTest.class, ContinentTest.class, CountryTest.class, DeployTest.class, DiplomacyTest.class, GameEngineTest.class, MapTest.class, OrderTest.class, PlayerTest.class})
public class ModelsTestSuite {
}