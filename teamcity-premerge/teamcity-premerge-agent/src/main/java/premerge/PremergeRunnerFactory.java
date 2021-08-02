package premerge;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import org.jetbrains.annotations.NotNull;

public class PremergeRunnerFactory implements CommandLineBuildServiceFactory, AgentBuildRunnerInfo {
  @NotNull
  @Override
  public String getType() {
    return "premergeRunner";
  }

  @Override
  public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
    return true;
  }

  @NotNull
  @Override
  public CommandLineBuildService createService() {
    return new PremergeRunner();
  }

  @NotNull
  @Override
  public AgentBuildRunnerInfo getBuildRunnerInfo() {
    return this;
  }
}
