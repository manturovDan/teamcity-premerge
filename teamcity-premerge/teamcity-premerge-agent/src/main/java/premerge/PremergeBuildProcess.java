package premerge;

import java.util.HashMap;
import java.util.Map;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProcessAdapter;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVersion;
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

  public PremergeBuildProcess(@NotNull PluginConfigFactory configFactory,
                              @NotNull GitAgentSSHService sshService,
                              @NotNull GitMetaFactory gitMetaFactory,
                              @NotNull MirrorManager mirrorManager,
                              @NotNull AgentRunningBuild build) {
    myConfigFactory = configFactory;
    mySshService = sshService;
    myGitMetaFactory = gitMetaFactory;
    myMirrorManager = mirrorManager;
    myBuild = build;
  }

  @Override
  public void start() throws RunBuildException {
    System.out.println("Build process run");
    myBuild.getBuildLogger().message("Write to log");
    try {
      preliminaryMergeTmp();
    }
    catch (VcsException vcsException) {
      vcsException.printStackTrace();
    }
  }

  protected void preliminaryMergeTmp() throws VcsException {
    System.out.println(myBuild);
    for (VcsRootEntry entry : myBuild.getVcsRootEntries()) {
      VcsRoot root = entry.getVcsRoot();

      AgentPluginConfig config = myConfigFactory.createConfig(myBuild, root);
      Map<String, String> env = getGitCommandEnv(config, myBuild);
      GitFactory gitFactory = myGitMetaFactory.createFactory(mySshService, config, getLogger(myBuild, config), myBuild.getBuildTempDirectory(), env, new BuildContext(myBuild, config));
      GitFacade facade = gitFactory.create(myBuild.getCheckoutDirectory());
      //facade.createBranch()
      //      .setName("new_test_branch_from_master")
      //      .setStartPoint("master")
      //      .call();

      int timeout = config.getIdleTimeoutSeconds();
      GitVersion version = config.getGitVersion();
      if(version.isLessThan(new GitVersion(1, 7, 1, 0))) {
        timeout = 24 * 60 * 60; //24 hours
      }
      AgentGitVcsRoot vcsRoot = new AgentGitVcsRoot(myMirrorManager, myBuild.getCheckoutDirectory(), root);
      facade.fetch()
            .setAuthSettings(vcsRoot.getAuthSettings())
            .setUseNativeSsh(config.isUseNativeSSH())
            .setTimeout(timeout)
            .setRefspec("+untagged4:untagged4")
            .setFetchTags(config.isFetchTags())
            .setQuite(true)
            .call();


    }
    //GitFacade facade = myGitFactory.create(getAgentTempDirectory());
    //facade.createBranch()
    //      .setName("new_test_branch_from_master")
    //      .setStartPoint("master")
    //      .call();
  }

  @NotNull
  private Map<String, String> getGitCommandEnv(@NotNull AgentPluginConfig config, @NotNull AgentRunningBuild build) {
    if (config.isRunGitWithBuildEnv()) {
      return build.getBuildParameters().getEnvironmentVariables();
    } else {
      return new HashMap<>(0);
    }
  }

  @NotNull
  private GitBuildProgressLogger getLogger(@NotNull AgentRunningBuild build, @NotNull AgentPluginConfig config) {
    return new GitBuildProgressLogger(build.getBuildLogger().getFlowLogger("-1"), config.getGitProgressMode());
  }

  @NotNull
  @Override
  public BuildFinishedStatus waitFor() throws RunBuildException {
    System.out.println("Finished");
    return BuildFinishedStatus.FINISHED_SUCCESS;
  }
}
