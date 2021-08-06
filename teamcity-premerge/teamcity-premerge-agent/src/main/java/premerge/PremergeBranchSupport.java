package premerge;

import java.util.HashMap;
import java.util.Map;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVersion;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.*;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;

public class PremergeBranchSupport {
  @NotNull private final GitFacade myFacade;
  @NotNull private final VcsRoot myRoot;
  @NotNull private final AgentPluginConfig myConfig;
  @NotNull private final PremergeBuildProcess myProcess;
  @NotNull private final AgentGitVcsRoot myVcsRoot;

  public PremergeBranchSupport(@NotNull PremergeBuildProcess process,
                               @NotNull VcsRoot root) throws VcsException {
    myRoot = root;
    myProcess = process;
    myConfig = myProcess.getConfigFactory().createConfig(myProcess.getBuild(), myRoot);
    myVcsRoot = new AgentGitVcsRoot(myProcess.getMirrorManager(), myProcess.getBuild().getCheckoutDirectory(), root);
    myFacade = getFacade();
  }

  protected GitFacade getFacade() {
    Map<String, String> env = getGitCommandEnv();
    GitFactory gitFactory = myProcess.getGitMetaFactory().createFactory(myProcess.getSshService(),
                                                                        myConfig,
                                                                        getLogger(),
                                                                        myProcess.getBuild().getBuildTempDirectory(),
                                                                        env,
                                                                        new BuildContext(myProcess.getBuild(), myConfig));
    return gitFactory.create(myProcess.getBuild().getCheckoutDirectory());
  }

  @NotNull
  protected Map<String, String> getGitCommandEnv() {
    if (myConfig.isRunGitWithBuildEnv()) {
      return myProcess.getBuild().getBuildParameters().getEnvironmentVariables();
    } else {
      return new HashMap<>(0);
    }
  }

  @NotNull
  protected GitBuildProgressLogger getLogger() {
    return new GitBuildProgressLogger(myProcess.getBuild().getBuildLogger().getFlowLogger("-1"), myConfig.getGitProgressMode());
  }

  @NotNull
  public String constructName() {
    return PremergeConstants.PRELIMINARY_MERGE_BRANCH_PREFIX + "/" + myProcess.getBuild().getBuildId();
  }

  @NotNull
  private String getLogicalName(@NotNull String branchName) {
    if (branchName.startsWith("refs/heads/")) {
      return branchName.substring("refs/heads/".length());
    }
    return branchName;
  }

  @NotNull
  public String getCurrentBranchName() throws VcsException {
    String currentBranch = myFacade.revParse()
                                   .setRef("HEAD")
                                   .setParams("--abbrev-ref")
                                   .call();
    if (currentBranch == null) {
      myProcess.getBuild().getBuildLogger().error("Smt went wrong. Current branch is null");
      myProcess.setUnsuccess();
      throw new IllegalStateException("Current branch is null");
    }
    return currentBranch;
  }

  public void fetch(String branch) {
    String logicalBranch = getLogicalName(branch);

    try {
      myFacade.fetch()
              .setAuthSettings(myVcsRoot.getAuthSettings())
              .setUseNativeSsh(myConfig.isUseNativeSSH())
              .setTimeout(getTimeout())
              .setRefspec("+" + logicalBranch + ":" + logicalBranch)
              .setFetchTags(myConfig.isFetchTags())
              .setQuite(true)
              .call();
    } catch (Exception e) {
      myProcess.getBuild().getBuildLogger().error("Fetching '" + branch + "' error");
      myProcess.setUnsuccess();
      return;
    }

    myProcess.getBuild().getBuildLogger().message("'" + branch + "' fetched");
  }

  public void checkout(String branch) {
    try {
      myFacade.checkout()
              .setAuthSettings(myVcsRoot.getAuthSettings())
              .setUseNativeSsh(myConfig.isUseNativeSSH())
              .setBranch(getLogicalName(branch))
              .setTimeout(myConfig.getCheckoutIdleTimeoutSeconds())
              .call();
    } catch (Exception e) {
      myProcess.getBuild().getBuildLogger().error("Checkout to '" + branch + "' error");
      myProcess.setUnsuccess();
      return;
    }

    myProcess.getBuild().getBuildLogger().message("Checkout to '" + branch + "'");

  }

  public void createBranch(String branch, String startPoint) {
    try {
      myFacade.createBranch()
              .setName(getLogicalName(branch))
              .setStartPoint(getLogicalName(startPoint))
              .call();
    } catch (Exception e) {
      myProcess.getBuild().getBuildLogger().error("Creating '" + branch + "' from '" + startPoint + "' error");
      myProcess.setUnsuccess();
      return;
    }

    myProcess.getBuild().getBuildLogger().message("Created '" + branch + "' from '" + startPoint + "'");
  }

  public int getTimeout() {
    int timeout = myConfig.getIdleTimeoutSeconds();
    GitVersion version = myConfig.getGitVersion();
    if(version.isLessThan(new GitVersion(1, 7, 1, 0))) {
      timeout = 24 * 60 * 60; //24 hours
    }
    return timeout;
  }
}
