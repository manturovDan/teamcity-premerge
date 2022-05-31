package trainFinish;

import java.util.*;
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
import teamcityREST.BuildRerunner;
import trains.PullRequestEntity;
import trains.PullRequestsFetcher;
import trains.PullRequestsFetcherProvider;

public class TrainFinishBuildProcess extends BuildProcessAdapter {
  @NotNull private final PluginConfigFactory myConfigFactory;
  @NotNull private final GitAgentSSHService mySshService;
  @NotNull private final GitMetaFactory myGitMetaFactory;
  @NotNull private final MirrorManager myMirrorManager;
  @NotNull private final AgentRunningBuild myBuild;
  @NotNull private final BuildRunnerContext myRunner;
  @NotNull private final HttpApi myHttpApi;
  private boolean success = false;
  private String trainPRs;
  @NotNull private final PullRequestsFetcherProvider myPullRequestsFetcherProvider;

  public TrainFinishBuildProcess(@NotNull PluginConfigFactory configFactory,
                                 @NotNull GitAgentSSHService sshService,
                                 @NotNull GitMetaFactory gitMetaFactory,
                                 @NotNull MirrorManager mirrorManager,
                                 @NotNull AgentRunningBuild build,
                                 @NotNull BuildRunnerContext runner,
                                 @NotNull HttpApi httpApi,
                                 @NotNull PullRequestsFetcherProvider provider) {
    myConfigFactory = configFactory;
    mySshService = sshService;
    myGitMetaFactory = gitMetaFactory;
    myMirrorManager = mirrorManager;
    myBuild = build;
    myRunner = runner;
    myHttpApi = httpApi;
    myPullRequestsFetcherProvider = provider;
  }

  @Override
  public void start() throws RunBuildException {
    myBuild.getBuildLogger().message("Merge Train finish build step:");
    trainPRs = myBuild.getSharedConfigParameters().get(PremergeConstants.MERGE_TRAIN_PULL_REQUESTS);
    if (trainPRs == null) {
      success = true;
      myBuild.getBuildLogger().message("There is no teamcity.build.mergetrains.pullrequests param. Skipping.");
      return;
    }

    String currentPRNumber = myBuild.getSharedConfigParameters().get(PremergeConstants.PULL_REQUEST_NUMBER_SHARED_PARAM);
    PullRequestsFetcher fetcher = myPullRequestsFetcherProvider.getFetcher(myHttpApi,
                                                                myBuild.getVcsRootEntries().get(0).getVcsRoot().getProperties().get("url"),
                                                                myRunner.getRunnerParameters().get(PremergeConstants.ACCESS_TOKEN));
    //restartBuild();
    if (isTrainBroken(fetcher)) {
      myBuild.getBuildLogger().message("Should rerun build");
      restartBuild();
    }
    else if (isBuildFailed()) {
      //set parameter and check - optimization - maybe todo later
      fetcher.setUnsuccess(currentPRNumber);
      myBuild.getBuildLogger().message("INVALID");
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
    Set<String> involvedPRs = new HashSet<>(Arrays.asList(trainPRs.split(",")));

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
      throw new RuntimeException("Fail");
    }
  }

  private void restartBuild() {
    BuildRerunner rerunner = new BuildRerunner(myRunner.getRunnerParameters().get(PremergeConstants.TEAMCITY_ACCESS_TOKEN),
                                               myRunner.getRunnerParameters().get("SERVER_URL"),
                                               myHttpApi);

    rerunner.restartBuild(myBuild.getBuildTypeExternalId(), myBuild.getSharedConfigParameters().get("teamcity.build.branch"));

  }
}
