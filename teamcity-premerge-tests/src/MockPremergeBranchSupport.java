import gitCommands.MockGitFacadeBuilder;
import java.io.File;
import java.util.Collection;
import jetbrains.buildServer.buildTriggers.vcs.git.AuthSettings;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVcsRoot;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVersion;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentGitFacade;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentGitVcsRoot;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentPluginConfig;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.FetchHeadsMode;
import jetbrains.buildServer.buildTriggers.vcs.git.command.GitExec;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import premerge.PremergeBranchSupportImpl;
import premerge.PremergeBuildProcess;

public class MockPremergeBranchSupport extends PremergeBranchSupportImpl {
  public MockPremergeBranchSupport(@NotNull PremergeBuildProcess process,
                                   @NotNull VcsRoot root) throws VcsException {
    super(process, root);
  }

  @Override
  protected AgentGitFacade getFacade() {
    return new MockGitFacadeBuilder().build();
  }

  @Override
  protected AgentPluginConfig createPluginConfig() throws VcsException {
    try {
      Class<?> c = Class.forName("jetbrains.buildServer.buildTriggers.vcs.git.agent.FetchHeadsMode");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new AgentPluginConfig() {
      @Override
      public boolean isUseNativeSSH() {
        return false;
      }

      @Override
      public boolean isUseGitSshCommand() {
        return false;
      }

      @Override
      public boolean isUseLocalMirrors(@NotNull GitVcsRoot root) {
        return false;
      }

      @Override
      public boolean isUseLocalMirrorsForSubmodules(@NotNull GitVcsRoot root) {
        return false;
      }

      @Override
      public boolean isUseAlternates(@NotNull GitVcsRoot root) {
        return false;
      }

      @Override
      public boolean isUseShallowClone(@NotNull GitVcsRoot root) {
        return false;
      }

      @Override
      public boolean isUseShallowCloneFromMirrorToCheckoutDir() {
        return false;
      }

      @Override
      public boolean isDeleteTempFiles() {
        return false;
      }

      @NotNull
      @Override
      public FetchHeadsMode getFetchHeadsMode() {
        return null;
      }

      @Nullable
      @Override
      public String getFetchAllHeadsModeStr() {
        return null;
      }

      @Override
      public boolean isUseMainRepoUserForSubmodules() {
        return false;
      }

      @NotNull
      @Override
      public GitVersion getGitVersion() {
        return new GitVersion(10, 10, 10);
      }

      @NotNull
      @Override
      public GitExec getGitExec() {
        return null;
      }

      @Override
      public int getCheckoutIdleTimeoutSeconds() {
        return 0;
      }

      @Override
      public boolean isUpdateSubmoduleOriginUrl() {
        return false;
      }

      @Override
      public boolean isUseSparseCheckout() {
        return false;
      }

      @Override
      public boolean isRunGitWithBuildEnv() {
        return false;
      }

      @Override
      public boolean isFailOnCleanCheckout() {
        return false;
      }

      @Override
      public boolean isFetchTags() {
        return false;
      }

      @Override
      public boolean isCredHelperMatchesAllUrls() {
        return false;
      }

      @NotNull
      @Override
      public GitProgressMode getGitProgressMode() {
        return null;
      }

      @Override
      public boolean isExcludeUsernameFromHttpUrl() {
        return false;
      }

      @Override
      public boolean isCleanCredHelperScript() {
        return false;
      }

      @Override
      public boolean isProvideCredHelper() {
        return false;
      }

      @Nullable
      @Override
      public String getGitOutputCharsetName() {
        return null;
      }

      @Override
      public int getLsRemoteTimeoutSeconds() {
        return 0;
      }

      @Override
      public int getSubmoduleUpdateTimeoutSeconds() {
        return 0;
      }

      @Nullable
      @Override
      public String getSshRequestToken() {
        return null;
      }

      @Override
      public boolean isCleanCommandRespectsOtherRoots() {
        return false;
      }

      @NotNull
      @Override
      public Collection<String> getCustomConfig() {
        return null;
      }

      @Override
      public int getRemoteOperationAttempts() {
        return 0;
      }

      @Override
      public boolean isDebugSsh() {
        return false;
      }

      @Override
      public boolean isNoFetchRequiredIfRevisionInRepo() {
        return false;
      }

      @NotNull
      @Override
      public File getCachesDir() {
        return null;
      }

      @Override
      public int getIdleTimeoutSeconds() {
        return 0;
      }

      @Override
      public String getPathToGit() {
        return null;
      }
    };
  }

  @Override
  protected AgentGitVcsRoot createGitVcsRoot(VcsRoot root) throws VcsException {
    return new Mockery(){{
      setImposteriser(ClassImposteriser.INSTANCE);
    }}.mock(AgentGitVcsRoot.class);
  }

  @Override
  protected AuthSettings retrieveAuthSettings() {
    return new Mockery(){{
      setImposteriser(ClassImposteriser.INSTANCE);
    }}.mock(AuthSettings.class);
  }
}
