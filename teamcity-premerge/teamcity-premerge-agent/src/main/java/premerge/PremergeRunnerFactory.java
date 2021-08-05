package premerge;

import java.util.HashMap;
import java.util.Map;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.*;
import org.jetbrains.annotations.NotNull;

public class PremergeRunnerFactory implements CommandLineBuildServiceFactory, AgentBuildRunnerInfo {
  private final GitMetaFactory myGitMetaFactory;
  private final GitAgentSSHService mySshService;
  private final PluginConfigFactory myConfigFactory;
  private final MirrorManager myMirrorManager;

  public PremergeRunnerFactory(@NotNull GitMetaFactory gitMetaFactory,
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
  public String getType() {
    return PremergeConstants.TYPE;
  }

  @Override
  public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
    return true;
  }

  @NotNull
  @Override
  public CommandLineBuildService createService() {
    return new PremergeRunner(myConfigFactory, mySshService, myGitMetaFactory, myMirrorManager);
  }

  @NotNull
  private Map<String, String> getGitCommandEnv(@NotNull AgentPluginConfig config, @NotNull AgentRunningBuild build) {
    if (config.isRunGitWithBuildEnv()) {
      return build.getBuildParameters().getEnvironmentVariables();
    } else {
      return new HashMap<>(0);
    }
  }

  @NotNull
  private GitBuildProgressLogger getLogger(@NotNull AgentRunningBuild build, @NotNull AgentPluginConfig config) {
    return new GitBuildProgressLogger(build.getBuildLogger().getFlowLogger("-1"), config.getGitProgressMode());
  }

  @NotNull
  @Override
  public AgentBuildRunnerInfo getBuildRunnerInfo() {
    return this;
  }
}
