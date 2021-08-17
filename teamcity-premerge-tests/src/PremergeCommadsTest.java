import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PremergeCommadsTest {
  @Test
  public void setUp() {
    Mockery context = new Mockery() {{
      setImposteriser(ClassImposteriser.INSTANCE);
    }};

    GitMetaFactory gitMetaFactory = context.mock(GitMetaFactory.class);
    GitAgentSSHService sshService = context.mock(GitAgentSSHService.class);
    PluginConfigFactory configFactory = context.mock(PluginConfigFactory.class);
    MirrorManager mirrorManager = context.mock(MirrorManager.class);

    AgentRunningBuild runningBuild = new MockRunnerBuildFactory().build();

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
}
