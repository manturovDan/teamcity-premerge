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

public class MultipleRootsTest {
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
  public void multipleRootsSuccess() {
    AgentRunningBuild runningBuild = new MockRunnerBuildBuilder().setVcsRootsCount(3).setBuildId(780).build();
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();
    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    runningBuild,
                                                                    runnerContext);

    process.setBranchSupportClass(MockPremergeBranchSupport.class);
    process.start();
    Assert.assertEquals(process.waitFor().toString(), "FINISHED_SUCCESS");
    Assert.assertEquals(process.getStatus().toString(), "SUCCESS");
    Assert.assertEquals(process.getSupports().size(), 3);

    for (int i = 0; i < process.getSupports().size(); ++i) {
      List<String> statuses = process.getSupports().get(i).getBuilder().getSequence();
      Assert.assertEquals(statuses.size(), 4);
      Assert.assertEquals(statuses.get(0), "fetching");
      Assert.assertEquals(statuses.get(1), "branchCreation");
      Assert.assertEquals(statuses.get(2), "checkouting");
      Assert.assertEquals(statuses.get(3), "merging");
    }
  }

  @Test
  public void multipleFetchError() {
    AgentRunningBuild runningBuild = new MockRunnerBuildBuilder().setVcsRootsCount(3).setBuildId(780).build();
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();
    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    runningBuild,
                                                                    runnerContext);

    process.setFetchSuccess(false, 1);
    process.setBranchSupportClass(MockPremergeBranchSupport.class);
    process.start();
    Assert.assertEquals(process.waitFor().toString(), "FINISHED_FAILED");
    Assert.assertEquals(process.getStatus().toString(), "FAILED");
    Assert.assertEquals(process.getSupports().size(), 2);

    List<String> statuses0 = process.getSupports().get(0).getBuilder().getSequence();
    Assert.assertEquals(statuses0.size(), 4);
    Assert.assertEquals(statuses0.get(0), "fetching");
    Assert.assertEquals(statuses0.get(1), "branchCreation");
    Assert.assertEquals(statuses0.get(2), "checkouting");
    Assert.assertEquals(statuses0.get(3), "merging");

    Assert.assertEquals(process.getSupports().get(1).getBuilder().getSequence().size(), 0);
  }

  @Test
  public void multipleConflictError() {
    AgentRunningBuild runningBuild = new MockRunnerBuildBuilder().setVcsRootsCount(3).setBuildId(780).build();
    BuildRunnerContext runnerContext = new MockBuildRunnerCtx();
    MockPremergeBuildProcess process = new MockPremergeBuildProcess(configFactory,
                                                                    sshService,
                                                                    gitMetaFactory,
                                                                    mirrorManager,
                                                                    runningBuild,
                                                                    runnerContext);

    process.setMergeSuccess(false, 1);
    process.setBranchSupportClass(MockPremergeBranchSupport.class);
    process.start();
    Assert.assertEquals(process.waitFor().toString(), "FINISHED_FAILED");
    Assert.assertEquals(process.getStatus().toString(), "FAILED");
    Assert.assertEquals(process.getSupports().size(), 3);

    for (int i = 0; i < process.getSupports().size(); i++) {
      List<String> statuses = process.getSupports().get(i).getBuilder().getSequence();
      Assert.assertEquals(statuses.get(0), "fetching");
      Assert.assertEquals(statuses.get(1), "branchCreation");
      Assert.assertEquals(statuses.get(2), "checkouting");
      if (i == 1) {
        Assert.assertEquals(statuses.size(), 5);
        Assert.assertEquals(statuses.get(3), "verif_MERGE_HEAD");
        Assert.assertEquals(statuses.get(4), "merge_aborting");
      }
      else {
        Assert.assertEquals(statuses.size(), 4);
        Assert.assertEquals(statuses.get(3), "merging");
      }
    }
  }
}
