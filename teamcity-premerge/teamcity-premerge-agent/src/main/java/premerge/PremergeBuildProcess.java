package premerge;

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
  private String targetBranch;
  private ResultStatus status = ResultStatus.SKIPPED;

  public enum ResultStatus {SUCCESS, SKIPPED, FAILED}

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
    targetBranch = PremergeBranchSupport.getLogicalName(myRunner.getRunnerParameters().get(PremergeConstants.TARGET_BRANCH));
    for (VcsRootEntry entry : myBuild.getVcsRootEntries()) {
      VcsRoot root = entry.getVcsRoot();
      PremergeBranchSupport branchSupport = new PremergeBranchSupport(this, root);

      String currentBranch = PremergeBranchSupport.getLogicalName(branchSupport.getCurrentBranchName());

      if (currentBranch.equals(targetBranch)) {
        myBuild.getBuildLogger().warning("Can't make premerge of target branch '" + targetBranch + "', skip this Vcs Root");
        continue;
      }

      String premergeBranch = branchSupport.constructName();
      branchSupport.fetch(targetBranch);
      branchSupport.createBranch(premergeBranch, currentBranch);
      branchSupport.checkout(premergeBranch);
      branchSupport.merge(targetBranch);

      setSuccess();
    }
  }

  @NotNull
  @Override
  public BuildFinishedStatus waitFor() {
    if (getStatus() == ResultStatus.FAILED) {
      return BuildFinishedStatus.FINISHED_FAILED;
    }
    else {
      if (getStatus() == ResultStatus.SUCCESS) {
        assert targetBranch != null;
        myBuild.addSharedConfigParameter(PremergeConstants.SHARED_PARAM, targetBranch);
      }
      return BuildFinishedStatus.FINISHED_SUCCESS;
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

  ResultStatus getStatus() {
    return status;
  }

  void setSuccess() {
    status = ResultStatus.SUCCESS;
  }

  void setUnsuccess() {
    status = ResultStatus.FAILED;
  }
}
