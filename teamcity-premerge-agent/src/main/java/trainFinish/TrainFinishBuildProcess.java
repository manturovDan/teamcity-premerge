package trainFinish;

import java.util.*;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProcessAdapter;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.impl.AgentRunningBuildImpl;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.http.HttpApi;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRootEntry;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeConstants;
import teamcityREST.BuildRerunner;
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
    PullRequestsFetcher fetcher = new GitHubPullRequestsFetcher(myHttpApi,
                                                                myBuild.getVcsRootEntries().get(0).getVcsRoot().getProperties().get("url"),
                                                                myRunner.getRunnerParameters().get(PremergeConstants.GITHUB_ACCESS_TOKEN));
    if (isBuildFailed()) {
      //set parameter and check - optimization - todo later
      fetcher.setUnsuccess(currentPRNumber);
      myBuild.getBuildLogger().message("INVALID");
    }
    else if (isTrainBroken(fetcher)) {
      myBuild.getBuildLogger().message("Should rerun build");
      restartBuild();
    }
    else {
      success = true;
      myBuild.getBuildLogger().message("OKAY");
      //restartBuild();
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
      PullRequestEntity entity = allPullRequests.get(involved);
      if (entity != null && !entity.isValid())
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

  private void restartBuild() {
    BuildRerunner rerunner = new BuildRerunner(myRunner.getRunnerParameters().get(PremergeConstants.TEAMCITY_ACCESS_TOKEN),
                                               "http://localhost:8111/bs/",
                                               myHttpApi);

    rerunner.restartBuild(myBuild.getBuildTypeExternalId(), myBuild.getSharedConfigParameters().get("teamcity.build.branch"));

  }
}
