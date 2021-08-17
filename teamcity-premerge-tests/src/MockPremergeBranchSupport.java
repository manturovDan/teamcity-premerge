import gitCommands.MockGitFacadeBuilder;
import java.io.File;
import java.util.Collection;
import jetbrains.buildServer.buildTriggers.vcs.git.AuthSettings;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVcsRoot;
import jetbrains.buildServer.buildTriggers.vcs.git.GitVersion;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentGitFacade;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentGitVcsRoot;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentPluginConfig;
import jetbrains.buildServer.buildTriggers.vcs.git.command.GitExec;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Expectation;
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

    Mockery context = new Mockery();
    AgentPluginConfig config = context.mock(AgentPluginConfig.class);

    context.checking(new Expectations() {{
      allowing(config).isUseNativeSSH(); will(returnValue(false));
      allowing(config).getCheckoutIdleTimeoutSeconds(); will(returnValue(0));
      allowing(config).getIdleTimeoutSeconds(); will(returnValue(0));
      allowing(config).getGitVersion(); will(returnValue(new GitVersion(10, 10, 10)));
      allowing(config).isFetchTags(); will(returnValue(false));
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
