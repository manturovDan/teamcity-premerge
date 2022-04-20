package trainFinish;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildProcessAdapter;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.http.HttpApi;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeConstants;
import trains.PullRequestsFetcher;
import trains.impl.github.GitHubPullRequestsFetcher;

public class TrainFinishBuildProcess extends BuildProcessAdapter {
  @NotNull private final PluginConfigFactory myConfigFactory;
  @NotNull private final GitAgentSSHService mySshService;
  @NotNull private final GitMetaFactory myGitMetaFactory;
  @NotNull private final MirrorManager myMirrorManager;
  @NotNull private final AgentRunningBuild myBuild;
  @NotNull private final BuildRunnerContext myRunner;
  @NotNull private final HttpApi myHttpApi;

  public TrainFinishBuildProcess(@NotNull PluginConfigFactory configFactory,
                                 @NotNull GitAgentSSHService sshService,
                                 @NotNull GitMetaFactory gitMetaFactory,
                                 @NotNull MirrorManager mirrorManager,
                                 @NotNull AgentRunningBuild build,
                                 @NotNull BuildRunnerContext runner,
                                 @NotNull HttpApi httpApi) {
    myConfigFactory = configFactory;
    mySshService = sshService;
    myGitMetaFactory = gitMetaFactory;
    myMirrorManager = mirrorManager;
    myBuild = build;
    myRunner = runner;
    myHttpApi = httpApi;
  }

  @Override
  public void start() throws RunBuildException {
    myBuild.getBuildLogger().message("Merge Train finish build step:");
    String currentPRNumber = myBuild.getSharedConfigParameters().get(PremergeConstants.PULL_REQUEST_NUMBER_SHARED_PARAM);
    if (isBuildFailed()) {
      PullRequestsFetcher fetcher = new GitHubPullRequestsFetcher(myHttpApi);
      fetcher.setUnsuccess(currentPRNumber);
    }
    else {
      //get all build PRs and check statuses
      //if nothing is invalid PRINT MESSAGE OK
      //else rerun build
    }
  }

  private boolean isBuildFailed() {
    try {
      return myBuild.isBuildFailingOnServer();
    } catch (InterruptedException e) {
      throw new RuntimeException("Fail"); //TODO noraml
    }
  }
}
