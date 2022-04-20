package trainFinish;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProcessAdapter;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.http.HttpApi;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeConstants;
import trains.PullRequestEntity;
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
  private boolean success = false;

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
    PullRequestsFetcher fetcher = new GitHubPullRequestsFetcher(myHttpApi);
    if (isBuildFailed()) { //check if canceled (just rerurn)
      fetcher.setUnsuccess(currentPRNumber); //and rerun builds later TODO maybe?
      myBuild.getBuildLogger().message("INVALID");
    }
    else if (isTrainBroken(fetcher)) {
      //else rerun build
      myBuild.getBuildLogger().message("Should rerun build");
    }
    else {
      success = true;
      myBuild.getBuildLogger().message("OKAY");
    }
  }

  @NotNull
  @Override
  public BuildFinishedStatus waitFor() throws RunBuildException {
    return success ? BuildFinishedStatus.FINISHED_SUCCESS : BuildFinishedStatus.FINISHED_FAILED;
  }

  private boolean isTrainBroken(PullRequestsFetcher fetcher) {
    Map<String, PullRequestEntity> allPullRequests = fetcher.fetchPRs();
    Set<String> involvedPRs = new HashSet<>(Arrays.asList(myBuild.getSharedConfigParameters().get(PremergeConstants.MERGE_TRAIN_PULL_REQUESTS).split(",")));

    for (String involved : involvedPRs) {
      if (!allPullRequests.get(involved).isValid())
        return true;
    }
    return false;
  }

  private boolean isBuildFailed() {
    try {
      return myBuild.isBuildFailingOnServer();
    } catch (InterruptedException e) {
      throw new RuntimeException("Fail"); //TODO noraml
    }
  }
}
