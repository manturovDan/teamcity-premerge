import java.util.ArrayList;
import java.util.List;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.buildTriggers.vcs.git.MirrorManager;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitAgentSSHService;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.GitMetaFactory;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.PluginConfigFactory;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeBranchSupport;
import premerge.PremergeBuildProcess;

public class MockPremergeBuildProcess extends PremergeBuildProcess {
  private Class<? extends PremergeBranchSupport> myBranchSupportClass = MockPremergeBranchSupportSuccess.class;
  private String myTestStatus = "NOT_STARTED";
  private final List<MockPremergeBranchSupport> supports = new ArrayList<>();

  public MockPremergeBuildProcess(@NotNull PluginConfigFactory configFactory,
                                  @NotNull GitAgentSSHService sshService,
                                  @NotNull GitMetaFactory gitMetaFactory,
                                  @NotNull MirrorManager mirrorManager,
                                  @NotNull AgentRunningBuild build,
                                  @NotNull BuildRunnerContext runner) {
    super(configFactory, sshService, gitMetaFactory, mirrorManager, build, runner);
  }

  public List<MockPremergeBranchSupport> getSupports() {
    return supports;
  }

  protected void setBranchSuppoerClass(Class<? extends PremergeBranchSupport> branchSupportClass) {
    myBranchSupportClass = branchSupportClass;
  }

  public void setTestStatus(String status) {
    myTestStatus = status;
  }

  public String getTestStatus() {
    return myTestStatus;
  }

  @Override
  protected PremergeBranchSupport createPremergeBranchSupport(VcsRoot root) throws VcsException {
    if (myBranchSupportClass.equals(MockPremergeBranchSupportSuccess.class)) {
      return new MockPremergeBranchSupportSuccess(this);
    }
    if (myBranchSupportClass.equals(MockPremergeBranchSupport.class)) {
      MockPremergeBranchSupport support = new MockPremergeBranchSupport(this, root);
      supports.add(support);
      return support;
    }
    return null;
  }
}