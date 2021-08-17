import gitCommands.MockGitFacadeBuilder;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import premerge.PremergeConstants;

public class PremergeCommadsTest {
  private Mockery context;
  private GitMetaFactory gitMetaFactory;
  private GitAgentSSHService sshService;
  private PluginConfigFactory configFactory;
  private MirrorManager mirrorManager;


  @BeforeMethod
  public void initMocs() {
    context = new Mockery() {{
      setImposteriser(ClassImposteriser.INSTANCE);
    }};

    gitMetaFactory = context.mock(GitMetaFactory.class);
    sshService = context.mock(GitAgentSSHService.class);
    configFactory = context.mock(PluginConfigFactory.class);
    mirrorManager = context.mock(MirrorManager.class);
  }

  @Test
  public void setUp() {
    AgentRunningBuild runningBuild = new MockRunnerBuildBuilder().setBuildId(780).build();
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();
    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    runningBuild,
                                                                    runnerContext);

    process.setBranchSuppoerClass(MockPremergeBranchSupportSuccess.class);

    Assert.assertEquals(process.getTestStatus(), "NOT_STARTED");
    process.start();
    process.waitFor();
    Assert.assertEquals(process.getTestStatus(), "fetched_main,branch_premerge_branch_created,checkouted_to_premerge_branch,merged_main");
  }

  @Test
  public void successfulPremergeTest() {
    AgentRunningBuild runningBuild = new MockRunnerBuildBuilder().setBuildId(780).build();
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();

    Assert.assertEquals(runningBuild.getBuildId(), 780);
    Assert.assertEquals(runnerContext.getRunnerParameters().get(PremergeConstants.TARGET_BRANCH), "main");

    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    runningBuild,
                                                                    runnerContext);

    process.setBranchSuppoerClass(MockPremergeBranchSupport.class);
    process.start();
    process.waitFor();
    Assert.assertEquals(process.getStatus().toString(), "SUCCESS");
  }
}
