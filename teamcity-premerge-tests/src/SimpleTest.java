import java.io.IOException;
import java.util.Map;
import jetbrains.buildServer.RunnerTest2Base;
import jetbrains.buildServer.TempFiles;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.MockAgentBuildRunner;
import jetbrains.buildServer.agent.impl.AgentRunningBuildImpl;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentVcsSupport;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.tests.AgentSupportBuilder;
import jetbrains.buildServer.buildTriggers.vcs.git.tests.builders.AgentRunningBuildBuilder;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jmock.Mockery;
import org.testng.Assert;
import org.testng.annotations.Test;
import premerge.PremergeBranchSupport;
import premerge.PremergeBuildProcess;
import premerge.PremergeBuildRunner;

public class SimpleTest {
  private MirrorManager myMirrorManager;
  private PluginConfigFactory myPluginConfigFactory;
  private GitMetaFactory myGitMetaFactory;
  private GitAgentSSHService mySshService;
  private TempFiles myTempFiles;
  private AgentSupportBuilder myAgentSupportBuilder;
  private GitAgentVcsSupport myVcsSupport;

  @Test
  public void simpleTest() throws IOException {
    myTempFiles = new TempFiles();
    myAgentSupportBuilder = new AgentSupportBuilder(myTempFiles);

    myVcsSupport = myAgentSupportBuilder.build();

    myPluginConfigFactory = myAgentSupportBuilder.getPluginConfigFactory();
    myMirrorManager = myAgentSupportBuilder.getMirrorManager();
    mySshService = myAgentSupportBuilder.getGitAgentSSHService();
    myGitMetaFactory = myAgentSupportBuilder.getGitMetaFactory();


    AgentRunningBuildBuilder builder = new AgentRunningBuildBuilder();
    builder.addRootEntry(new VcsRoot() {
      @Override
      public long getId() {
        return 0;
      }

      @NotNull
      @Override
      public String getName() {
        return null;
      }

      @NotNull
      @Override
      public String getVcsName() {
        return null;
      }

      @NotNull
      @Override
      public Map<String, String> getProperties() {
        return null;
      }

      @Nullable
      @Override
      public String getProperty(@NotNull String propertyName) {
        return null;
      }

      @Nullable
      @Override
      public String getProperty(@NotNull String propertyName, @Nullable String defaultValue) {
        return null;
      }

      @NotNull
      @Override
      public String getExternalId() {
        return null;
      }

      @NotNull
      @Override
      public String describe(boolean verbose) {
        return null;
      }
    }, "");
    AgentRunningBuild build = builder.build();
    BuildRunnerContext context = new MockBuildRunnerCtx();

    PremergeBuildProcess process = new MockPremergeBuildProcess(myPluginConfigFactory, mySshService, myGitMetaFactory, myMirrorManager, build, context);

    process.start();

    System.out.println(process.getStatus());
  }
}
