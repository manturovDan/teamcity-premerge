import java.io.IOException;
import jetbrains.buildServer.TempFiles;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentVcsSupport;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.tests.AgentSupportBuilder;
import jetbrains.buildServer.buildTriggers.vcs.git.tests.builders.AgentRunningBuildBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SequenceTest {
  private MirrorManager myMirrorManager;
  private PluginConfigFactory myPluginConfigFactory;
  private GitMetaFactory myGitMetaFactory;
  private GitAgentSSHService mySshService;
  private TempFiles myTempFiles;
  private AgentSupportBuilder myAgentSupportBuilder;
  private GitAgentVcsSupport myVcsSupport;
  private MockPremergeBuildProcess process;

  private void initMocks() throws IOException {
    myTempFiles = new TempFiles();
    myAgentSupportBuilder = new AgentSupportBuilder(myTempFiles);

    myVcsSupport = myAgentSupportBuilder.build();

    myPluginConfigFactory = myAgentSupportBuilder.getPluginConfigFactory();
    myMirrorManager = myAgentSupportBuilder.getMirrorManager();
    mySshService = myAgentSupportBuilder.getGitAgentSSHService();
    myGitMetaFactory = myAgentSupportBuilder.getGitMetaFactory();


    AgentRunningBuildBuilder builder = new AgentRunningBuildBuilder();
    builder.addRootEntry(new MockVcsRoot(), "");
    AgentRunningBuild build = builder.build();
    BuildRunnerContext context = new MockBuildRunnerCtx();
    process = new MockPremergeBuildProcess(myPluginConfigFactory, mySshService, myGitMetaFactory, myMirrorManager, build, context);

  }

  @Test
  public void successfulSequenceTest() throws IOException {
    initMocks();

    process.setBranchSuppoerClass(MockPremergeBranchSupportSuccess.class);
    process.start();
    process.waitFor();

    Assert.assertEquals(process.getTestStatus(),
                        "fetched_main,branch_premerge_branch_created,checkouted_to_premerge_branch,merged_main");
  }
}
