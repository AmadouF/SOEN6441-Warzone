package TestSuites;

import Helpers.MapHelper;
import Helpers.PlayerHelperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suit for running all MapHelper and PlayerHelper related tests
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MapHelper.class, PlayerHelperTest.class })
public class HelpersTestSuite {
}