package premerge;

import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import org.jetbrains.annotations.NotNull;

public class PremergeBuildRunner implements AgentBuildRunner, AgentBuildRunnerInfo {
  @NotNull private final GitMetaFactory myGitMetaFactory;
  @NotNull private final GitAgentSSHService mySshService;
  @NotNull private final PluginConfigFactory myConfigFactory;
  @NotNull private final MirrorManager myMirrorManager;

  public PremergeBuildRunner(@NotNull GitMetaFactory gitMetaFactory,
                             @NotNull GitAgentSSHService sshService,
                             @NotNull PluginConfigFactory configFactory,
                             @NotNull MirrorManager mirrorManager) {
    myGitMetaFactory = gitMetaFactory;
    mySshService = sshService;
    myConfigFactory = configFactory;
    myMirrorManager = mirrorManager;
  }

  @NotNull
  @Override
  public BuildProcess createBuildProcess(@NotNull AgentRunningBuild runningBuild, @NotNull BuildRunnerContext context) {
    return new PremergeBuildProcess(myConfigFactory, mySshService, myGitMetaFactory, myMirrorManager, runningBuild, context);
  }

  @NotNull
  @Override
  public AgentBuildRunnerInfo getRunnerInfo() {
    return this;
  }

  @NotNull
  @Override
  public String getType() {
    return PremergeConstants.TYPE;
  }

  @Override
  public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
    return true;
  }
}
