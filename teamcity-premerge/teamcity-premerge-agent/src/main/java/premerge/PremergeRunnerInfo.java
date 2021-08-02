package premerge;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import org.jetbrains.annotations.NotNull;

public class PremergeRunnerInfo implements AgentBuildRunnerInfo {
  @NotNull
  @Override
  public String getType() {
    return "premergeRunner";
  }

  @Override
  public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
    return true;
  }
}
