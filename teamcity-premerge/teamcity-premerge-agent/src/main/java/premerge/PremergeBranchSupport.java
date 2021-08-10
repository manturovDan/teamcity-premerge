package premerge;

import java.util.HashMap;
import java.util.Map;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVersion;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.*;
import jetbrains.buildServer.util.StringUtil;
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
  public static String cutRefsHeads(@NotNull String branchName) {
    if (branchName.startsWith("refs/heads/")) {
      return branchName.substring("refs/heads/".length());
    }
    return branchName;
  }

  public void fetch(String branch) throws VcsException {
    try {
      myFacade.fetch()
              .setAuthSettings(myVcsRoot.getAuthSettings())
              .setUseNativeSsh(myConfig.isUseNativeSSH())
              .setTimeout(getTimeout())
              .setRefspec("+" + branch + ":" + branch)
              .setFetchTags(myConfig.isFetchTags())
              .setQuite(true)
              .call();
    } catch (Exception e) {
      myProcess.getBuild().getBuildLogger().error("Fetching '" + branch + "' error");
      myProcess.setUnsuccess();
      throw new VcsException(e);
    }
    myProcess.getBuild().getBuildLogger().message("'" + branch + "' fetched");
  }

  public void checkout(String branch) throws VcsException {
    try {
      myFacade.checkout()
              .setAuthSettings(myVcsRoot.getAuthSettings())
              .setUseNativeSsh(myConfig.isUseNativeSSH())
              .setBranch(branch)
              .setTimeout(myConfig.getCheckoutIdleTimeoutSeconds())
              .call();
    } catch (Exception e) {
      myProcess.getBuild().getBuildLogger().error("Checkout to '" + branch + "' error");
      myProcess.setUnsuccess();
      throw new VcsException(e);
    }
    myProcess.getBuild().getBuildLogger().message("Checkout to '" + branch + "'");
  }

  public void createBranch(String branch) throws VcsException {
    try {
      myFacade.createBranch()
              .setName(branch)
              .call();
    } catch (Exception e) {
      myProcess.getBuild().getBuildLogger().error("Creating '" + branch + "'");
      myProcess.setUnsuccess();
      throw new VcsException(e);
    }
    myProcess.getBuild().getBuildLogger().message("Created '" + branch + "'");
  }

  public void merge(String branch) throws VcsException {
    try {
      myFacade.merge()
              .setBranches(branch)
              .setQuite(true)
              .call();
      myProcess.setSuccess();
    } catch (VcsException vcsException) {
      String mergeCommits = getParameter("MERGE_HEAD");
      if (!StringUtil.isEmpty(mergeCommits)) {
        myProcess.getBuild().getBuildLogger().warning("Preliminary merge conflict with branch '" + branch + "'");
        myProcess.setUnsuccess();
        myFacade.merge()
                .setAbort(true)
                .call();
        return;
      }
    } catch (Exception e) {
      myProcess.getBuild().getBuildLogger().error("Merging '" + branch +"' error");
      myProcess.setUnsuccess();
      throw new VcsException(e);
    }
    myProcess.getBuild().getBuildLogger().message("'" + branch + "' was merged");
  }

  public String getParameter(String parameter) throws VcsException {
    return myFacade.revParse()
            .verify(parameter)
            .call();
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
