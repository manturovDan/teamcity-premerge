package trainFinish;

import java.util.HashSet;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.http.HttpApi;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeBuildProcess;
import premerge.PremergeConstants;
import trains.PullRequestsFetcherProvider;

public class TrainFinishBuildRunner implements AgentBuildRunner, AgentBuildRunnerInfo {
  @NotNull private final GitMetaFactory myGitMetaFactory;
  @NotNull private final GitAgentSSHService mySshService;
  @NotNull private final PluginConfigFactory myConfigFactory;
  @NotNull private final MirrorManager myMirrorManager;
  @NotNull private final HttpApi myHttpApi;
  HashSet<PullRequestsFetcherProvider> myPullRequestFetcherProviders = new HashSet<>();

  public TrainFinishBuildRunner(@NotNull GitMetaFactory gitMetaFactory,
                                @NotNull GitAgentSSHService sshService,
                                @NotNull PluginConfigFactory configFactory,
                                @NotNull MirrorManager mirrorManager, @NotNull HttpApi httpApi) {
    myGitMetaFactory = gitMetaFactory;
    mySshService = sshService;
    myConfigFactory = configFactory;
    myMirrorManager = mirrorManager;
    myHttpApi = httpApi;
  }

  @NotNull
  @Override
  public BuildProcess createBuildProcess(@NotNull AgentRunningBuild runningBuild, @NotNull BuildRunnerContext context) throws RunBuildException {
    String currentRunnerType = context.getRunnerParameters().get("providerType");
    for (PullRequestsFetcherProvider provider : myPullRequestFetcherProviders) {
      if (provider.getType().equals(currentRunnerType)) {
        return new TrainFinishBuildProcess(myConfigFactory,
                                                  mySshService,
                                                  myGitMetaFactory,
                                                  myMirrorManager,
                                                  runningBuild,
                                                  context,
                                                  myHttpApi,
                                                  provider);
      }
    }
    throw new RuntimeException(currentRunnerType + " is unsupported");
  }

  @NotNull
  @Override
  public AgentBuildRunnerInfo getRunnerInfo() {
    return this;
  }

  @NotNull
  @Override
  public String getType() {
    return PremergeConstants.TYPE_FINISH;
  }

  @Override
  public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
    return true;
  }

  public void registerPullRequestFetcherProvider(PullRequestsFetcherProvider pullRequestFetcherProvider) {
    myPullRequestFetcherProviders.add(pullRequestFetcherProvider);
  }
}
