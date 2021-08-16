import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeBranchSupport;
import premerge.PremergeBuildProcess;

public class MockPremergeBuildProcess extends PremergeBuildProcess {
  public MockPremergeBuildProcess(@NotNull PluginConfigFactory configFactory,
                                  @NotNull GitAgentSSHService sshService,
                                  @NotNull GitMetaFactory gitMetaFactory,
                                  @NotNull MirrorManager mirrorManager,
                                  @NotNull AgentRunningBuild build,
                                  @NotNull BuildRunnerContext runner) {
    super(configFactory, sshService, gitMetaFactory, mirrorManager, build, runner);
  }

  @Override
  protected PremergeBranchSupport createPremergeBranchSupport(VcsRoot root) throws VcsException {
    return new MockPremergeBranchSupport();
  }
}
