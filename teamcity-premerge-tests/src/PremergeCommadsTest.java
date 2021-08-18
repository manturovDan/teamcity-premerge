import java.util.List;
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
import org.testng.annotations.Test;
import premerge.PremergeBranchSupport;
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
  public void processTest() {
    AgentRunningBuild runningBuild = new MockRunnerBuildBuilder().setBuildId(780).build();
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();
    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    runningBuild,
                                                                    runnerContext);

    process.setBranchSupportClass(MockPremergeBranchSupportSuccess.class);

    Assert.assertEquals(process.getTestStatus(), "NOT_STARTED");
    process.start();
    process.waitFor();
    Assert.assertEquals(process.getTestStatus(), "fetched_main,branch_premerge_branch_created,checkouted_to_premerge_branch,merged_main,asked_parameter_main");
  }

  @Test
  public void cutRefsHeadsTest() {
    Assert.assertEquals(PremergeBranchSupport.cutRefsHeads("refs/heads/master"), "master");
    Assert.assertEquals(PremergeBranchSupport.cutRefsHeads("master"), "master");
    Assert.assertEquals(PremergeBranchSupport.cutRefsHeads("refs/pull/123/head"), "refs/pull/123/head");
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

    process.setBranchSupportClass(MockPremergeBranchSupport.class);
    process.start();
    process.waitFor();
    Assert.assertEquals(process.getStatus().toString(), "SUCCESS");
    List<String> statuses = process.getSupports().get(0).getBuilder().getSequence();
    Assert.assertEquals(statuses.size(), 5);
    Assert.assertEquals(statuses.get(0), "fetching");
    Assert.assertEquals(statuses.get(1), "branchCreation");
    Assert.assertEquals(statuses.get(2), "checkouting");
    Assert.assertEquals(statuses.get(3), "merging");
    Assert.assertEquals(statuses.get(4), "verif_main");
  }

  @Test
  public void FetchErrorTest() {
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

    process.setBranchSupportClass(MockPremergeBranchSupport.class);
    process.setFetchSuccess(false, 0);

    process.start();
    process.waitFor();
    Assert.assertEquals(process.getStatus().toString(), "FAILED");
    List<String> statuses = process.getSupports().get(0).getBuilder().getSequence();
    Assert.assertEquals(statuses.size(), 0);
  }

  @Test
  public void conflictTest() {
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

    process.setBranchSupportClass(MockPremergeBranchSupport.class);
    process.setMergeSuccess(false, 0);

    process.start();
    Assert.assertEquals(process.waitFor().toString(), "FINISHED_FAILED");
    Assert.assertEquals(process.getStatus().toString(), "FAILED");
    List<String> statuses = process.getSupports().get(0).getBuilder().getSequence();
    Assert.assertEquals(statuses.size(), 6);
    Assert.assertEquals(statuses.get(0), "fetching");
    Assert.assertEquals(statuses.get(1), "branchCreation");
    Assert.assertEquals(statuses.get(2), "checkouting");
    Assert.assertEquals(statuses.get(3), "verif_MERGE_HEAD");
    Assert.assertEquals(statuses.get(4), "merge_aborting");
    Assert.assertEquals(statuses.get(5), "verif_main");
  }
}
