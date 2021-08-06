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
  public void start() throws RunBuildException {
    System.out.println("Build process run");
    myBuild.getBuildLogger().message("Write to log");
    try {
      preliminaryMerge();
    }
    catch (VcsException vcsException) {
      vcsException.printStackTrace();
    }
  }

  protected void preliminaryMerge() throws VcsException {
    for (VcsRootEntry entry : myBuild.getVcsRootEntries()) {
      VcsRoot root = entry.getVcsRoot();
      PremergeBranchSupport branchSupport = new PremergeBranchSupport(this, root);

      String premergeBranch = branchSupport.constructName();
      branchSupport.fetch(myRunner.getRunnerParameters().get(PremergeConstants.TARGET_BRANCH));
      branchSupport.createBranch(premergeBranch, branchSupport.getCurrentBranchName());
      branchSupport.checkout(premergeBranch);
    }
  }

  @NotNull
  @Override
  public BuildFinishedStatus waitFor() throws RunBuildException {
    System.out.println("Finished");
    return BuildFinishedStatus.FINISHED_SUCCESS;
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
