package premerge;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVcsRoot;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.*;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildServer.util.TCStreamUtil;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import jetbrains.buildServer.vcs.VcsRootEntry;
import org.jetbrains.annotations.NotNull;

public class PremergeRunner extends BuildServiceAdapter {
  private final Set<File> myFilesToDelete = new HashSet<>();
  private final PluginConfigFactory myConfigFactory;
  private final GitAgentSSHService mySshService;
  private final GitMetaFactory myGitMetaFactory;
  private final MirrorManager myMirrorManager;
  private final AgentPluginConfig myPluginConfig;


  public PremergeRunner(@NotNull PluginConfigFactory configFactory,
                        @NotNull GitAgentSSHService sshService,
                        @NotNull GitMetaFactory gitMetaFactory,
                        @NotNull MirrorManager mirrorManager,
                        AgentPluginConfig pluginConfig) {
    myConfigFactory = configFactory;
    mySshService = sshService;
    myGitMetaFactory = gitMetaFactory;
    myMirrorManager = mirrorManager;
    myPluginConfig = pluginConfig;
  }


  @NotNull
  @Override
  public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
    System.out.println("Simple Build Step");

    try {
      preliminaryMerge();
    }
    catch (VcsException vcsException) {
      vcsException.printStackTrace();
    }

    String script = getScript();
    setExecutableAttribute(script);

    return new SimpleProgramCommandLine(getRunnerContext(), script, Collections.emptyList());
  }

  protected void preliminaryMerge() throws VcsException {
    System.out.println(getBuild());
    AgentRunningBuild build = getBuild();
    for (VcsRootEntry entry : build.getVcsRootEntries()) {
      VcsRoot root = entry.getVcsRoot();

      AgentPluginConfig config = myConfigFactory.createConfig(build, root);
      Map<String, String> env = getGitCommandEnv(config, build);
      GitFactory gitFactory = myGitMetaFactory.createFactory(mySshService, config, getLogger(build, config), build.getBuildTempDirectory(), env, new BuildContext(build, config));
      GitFacade facade = gitFactory.create(build.getCheckoutDirectory());
      //facade.createBranch()
      //      .setName("new_test_branch_from_master")
      //      .setStartPoint("master")
      //      .call();

      //todo upd
      AgentGitVcsRoot vcsRoot = new AgentGitVcsRoot(myMirrorManager, build.getCheckoutDirectory(), root);
      facade.fetch()
            .setAuthSettings(vcsRoot.getAuthSettings())
            .setUseNativeSsh(false)
            .setTimeout(10)
            .setRefspec("+untagged:untagged")
            .setFetchTags(false)
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
  protected String getScript() {
    //if my currect branch satisfy branch spec: fetch target, create branch, merge target into it
    //final String commandContent = "git branch\ngit fetch origin premerge/new_merge7/to/master\ngit checkout premerge/new_merge7/to/master ";

    final String commandContent = "git branch";
    try {
      final File scriptFile = File.createTempFile("premerge_script", "", getAgentTempDirectory());
      FileUtil.writeFile(scriptFile, commandContent, Charset.defaultCharset());
      myFilesToDelete.add(scriptFile);

      return scriptFile.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace(); //todo normal exception
      return null;
    }
  }

  private void setExecutableAttribute(@NotNull final String script) throws RunBuildException {
    try {
      TCStreamUtil.setFileMode(new File(script), "a+x");
    } catch (Throwable t) {
      throw new RunBuildException("Failed to set executable attribute for custom script '" + script + "'", t);
    }
  }
}
