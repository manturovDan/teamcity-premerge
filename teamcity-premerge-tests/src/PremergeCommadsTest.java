import java.util.List;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.GitUtils;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVersion;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentPluginConfig;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.command.Context;
import jetbrains.buildServer.http.HttpApi;
import jetbrains.buildServer.vcs.CheckoutRules;
import jetbrains.buildServer.vcs.VcsRootEntry;
import org.jmock.Expectations;
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
  private HttpApi httpApi;


  @BeforeMethod
  public void initMocs() {
    context = new Mockery() {{
      setImposteriser(ClassImposteriser.INSTANCE);
    }};

    gitMetaFactory = context.mock(GitMetaFactory.class);
    sshService = context.mock(GitAgentSSHService.class);
    configFactory = context.mock(PluginConfigFactory.class);
    mirrorManager = context.mock(MirrorManager.class);
    httpApi = context.mock(HttpApi.class);
  }

  @Test
  public void noPRTest() {
    AgentRunningBuild runningBuild = new MockRunnerBuildBuilder().setBuildId(780).build();
    runningBuild.addSharedConfigParameter(GitUtils.getGitRootBranchParamName(
      new VcsRootEntry(new MockVcsRoot().setUrl("git@...0"), new CheckoutRules(".")).getVcsRoot()), "refs/heads/feature_X");
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();
    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    httpApi,
                                                                    runningBuild,
                                                                    runnerContext);

    process.setBranchSupportClass(MockPremergeBranchSupportSuccess.class);

    Assert.assertEquals(process.getTestStatus(), "NOT_STARTED");
    process.start();
    BuildFinishedStatus status = process.waitFor();
    Assert.assertEquals(process.getTestStatus(), "NOT_STARTED");
    Assert.assertEquals(status.toString(), "FINISHED_SUCCESS");
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
    runningBuild.addSharedConfigParameter(GitUtils.getGitRootBranchParamName(
      new VcsRootEntry(new MockVcsRoot().setUrl("git@...0"), new CheckoutRules(".")).getVcsRoot()), "refs/heads/feature_X");
    runningBuild.addSharedConfigParameter("teamcity.pullRequest.target.branch", "refs/heads/main");
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();

    Assert.assertEquals(runningBuild.getBuildId(), 780);
    Assert.assertEquals(runnerContext.getRunnerParameters().get(PremergeConstants.TARGET_BRANCH), "main");

    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    httpApi,
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
    runningBuild.addSharedConfigParameter(GitUtils.getGitRootBranchParamName(
      new VcsRootEntry(new MockVcsRoot().setUrl("git@...0"), new CheckoutRules(".")).getVcsRoot()), "refs/heads/feature_X");
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();

    Assert.assertEquals(runningBuild.getBuildId(), 780);
    Assert.assertEquals(runnerContext.getRunnerParameters().get(PremergeConstants.TARGET_BRANCH), "main");

    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    httpApi,
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
    runningBuild.addSharedConfigParameter(GitUtils.getGitRootBranchParamName(
      new VcsRootEntry(new MockVcsRoot().setUrl("git@...0"), new CheckoutRules(".")).getVcsRoot()), "refs/heads/feature_X");
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();

    Assert.assertEquals(runningBuild.getBuildId(), 780);
    Assert.assertEquals(runnerContext.getRunnerParameters().get(PremergeConstants.TARGET_BRANCH), "main");

    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    httpApi,
                                                                    runningBuild,
                                                                    runnerContext);

    process.setBranchSupportClass(MockPremergeBranchSupport.class);
    process.setMergeSuccess(false, 0);

    process.start();
    Assert.assertEquals(process.waitFor().toString(), "FINISHED_FAILED");
    Assert.assertEquals(process.getStatus().toString(), "FAILED");
    List<String> statuses = process.getSupports().get(0).getBuilder().getSequence();
    Assert.assertEquals(statuses.size(), 5);
    Assert.assertEquals(statuses.get(0), "fetching");
    Assert.assertEquals(statuses.get(1), "branchCreation");
    Assert.assertEquals(statuses.get(2), "checkouting");
    Assert.assertEquals(statuses.get(3), "verif_MERGE_HEAD");
    Assert.assertEquals(statuses.get(4), "merge_aborting");
  }
}
