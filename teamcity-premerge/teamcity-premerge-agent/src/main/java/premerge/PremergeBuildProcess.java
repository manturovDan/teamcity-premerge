package premerge;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProcessAdapter;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.*;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import jetbrains.buildServer.vcs.VcsRootEntry;
import org.jetbrains.annotations.NotNull;

public class PremergeBuildProcess extends BuildProcessAdapter {
  @NotNull private final PluginConfigFactory myConfigFactory;
  @NotNull private final GitAgentSSHService mySshService;
  @NotNull private final GitMetaFactory myGitMetaFactory;
  @NotNull private final MirrorManager myMirrorManager;
  @NotNull private final AgentRunningBuild myBuild;
  @NotNull private final BuildRunnerContext myRunner;
  private boolean success = true;

  public PremergeBuildProcess(@NotNull PluginConfigFactory configFactory,
                              @NotNull GitAgentSSHService sshService,
                              @NotNull GitMetaFactory gitMetaFactory,
                              @NotNull MirrorManager mirrorManager,
                              @NotNull AgentRunningBuild build,
                              @NotNull BuildRunnerContext runner) {
    myConfigFactory = configFactory;
    mySshService = sshService;
    myGitMetaFactory = gitMetaFactory;
    myMirrorManager = mirrorManager;
    myBuild = build;
    myRunner = runner;
  }

  @Override
  public void start() {
    myBuild.getBuildLogger().message("Preliminary merge fearture:");
    try {
      preliminaryMerge();
    }
    catch (VcsException vcsException) {
      myBuild.getBuildLogger().error("Error while build step execution");
    }
  }

  protected void preliminaryMerge() throws VcsException {
    for (VcsRootEntry entry : myBuild.getVcsRootEntries()) {
      VcsRoot root = entry.getVcsRoot();
      PremergeBranchSupport branchSupport = new PremergeBranchSupport(this, root);

      String currentBranch = branchSupport.getCurrentBranchName();
      String targetBranch = myRunner.getRunnerParameters().get(PremergeConstants.TARGET_BRANCH);

      if (currentBranch.equals(targetBranch)) {
        myBuild.getBuildLogger().error("Trying to make premerge of target branch '" + targetBranch + "'");
        setUnsuccess();
        return;
      }

      String premergeBranch = branchSupport.constructName();
      branchSupport.fetch(targetBranch);
      branchSupport.createBranch(premergeBranch, currentBranch);
      branchSupport.checkout(premergeBranch);
      branchSupport.merge(targetBranch);
    }
  }

  @NotNull
  @Override
  public BuildFinishedStatus waitFor() {
    if (getSuccess()) {
      return BuildFinishedStatus.FINISHED_SUCCESS;
    }
    else {
      return BuildFinishedStatus.FINISHED_FAILED;
    }
  }

  @NotNull
  PluginConfigFactory getConfigFactory() {
    return myConfigFactory;
  }

  @NotNull
  GitAgentSSHService getSshService() {
    return mySshService;
  }

  @NotNull
  GitMetaFactory getGitMetaFactory() {
    return myGitMetaFactory;
  }

  @NotNull
  MirrorManager getMirrorManager() {
    return myMirrorManager;
  }

  @NotNull
  AgentRunningBuild getBuild() {
    return myBuild;
  }

  @NotNull
  BuildRunnerContext getRunner() {
    return myRunner;
  }

  boolean getSuccess() {
    return success;
  }

  void setUnsuccess() {
    success = false;
  }
}
