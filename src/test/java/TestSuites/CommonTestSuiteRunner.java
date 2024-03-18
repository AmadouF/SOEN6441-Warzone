package TestSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ GameEngineTestSuite.class, HelpersTestSuite.class, ModelsTestSuite.class })
public class CommonTestSuiteRunner {

}
