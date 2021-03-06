import jetbrains.buildServer.buildTriggers.vcs.git.AuthSettings;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVersion;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentGitFacade;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentGitVcsRoot;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentPluginConfig;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import premerge.PremergeBranchSupportImpl;
import premerge.PremergeBuildProcess;

public class MockPremergeBranchSupport extends PremergeBranchSupportImpl {
  private MockGitFacadeBuilder myBuilder;

  public MockPremergeBranchSupport(@NotNull PremergeBuildProcess process,
                                   @NotNull VcsRoot root) throws VcsException {
    super(process, root, "");
  }

  @Override
  protected AgentGitFacade getFacade() {
    return getBuilder().build();
  }

  @Override
  protected AgentGitFacade getFacade(String repoRelativePath) {
    return getBuilder().build();
  }

  public MockGitFacadeBuilder getBuilder() {
    if (myBuilder == null)
      myBuilder = new MockGitFacadeBuilder();
    return myBuilder;
  }

  @Override
  protected AgentPluginConfig createPluginConfig() throws VcsException {
    Mockery context = new Mockery();
    AgentPluginConfig config = context.mock(AgentPluginConfig.class);

    context.checking(new Expectations() {{
      allowing(config).isUseNativeSSH(); will(returnValue(false));
      allowing(config).getCheckoutIdleTimeoutSeconds(); will(returnValue(0));
      allowing(config).getIdleTimeoutSeconds(); will(returnValue(0));
      allowing(config).getGitVersion(); will(returnValue(new GitVersion(10, 10, 10)));
      allowing(config).isFetchTags(); will(returnValue(false));
      allowing(config).getGitProgressMode(); will(returnValue(AgentPluginConfig.GitProgressMode.DEBUG));
    }});

    return config;
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
