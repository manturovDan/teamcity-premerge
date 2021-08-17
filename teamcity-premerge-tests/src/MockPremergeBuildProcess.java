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
  private final List<Boolean> myFetchSuccess = new ArrayList<Boolean>();
  private final List<Boolean> myMergeSuccess = new ArrayList<Boolean>();
  private final List<Boolean> myAbortSuccess = new ArrayList<Boolean>();
  private int branchSupportCounter = 0;

  private final List<MockPremergeBranchSupport> supports = new ArrayList<>();

  public MockPremergeBuildProcess(@NotNull PluginConfigFactory configFactory,
                                  @NotNull GitAgentSSHService sshService,
                                  @NotNull GitMetaFactory gitMetaFactory,
                                  @NotNull MirrorManager mirrorManager,
                                  @NotNull AgentRunningBuild build,
                                  @NotNull BuildRunnerContext runner) {
    super(configFactory, sshService, gitMetaFactory, mirrorManager, build, runner);
  }

  public void setFetchSuccess(boolean fetchSuccess, int num) {
    for (int i = myFetchSuccess.size(); i < num; ++i) {
      myFetchSuccess.add(true);
    }

    myFetchSuccess.add(num, fetchSuccess);
  }

  public void setMergeSuccess(boolean mergeSuccess, int num) {
    for (int i = myMergeSuccess.size(); i < num; ++i) {
      myMergeSuccess.add(true);
    }
    myMergeSuccess.add(num, mergeSuccess);
  }

  public void setAbortSuccess(boolean abortSuccess, int num) {
    for (int i = myAbortSuccess.size(); i < num; ++i) {
      myAbortSuccess.add(true);
    }

    myAbortSuccess.add(num, abortSuccess);
  }

  public List<MockPremergeBranchSupport> getSupports() {
    return supports;
  }

  protected void setBranchSupportClass(Class<? extends PremergeBranchSupport> branchSupportClass) {
    myBranchSupportClass = branchSupportClass;
  }

  public void setTestStatus(String status) {
    myTestStatus = status;
  }

  public String getTestStatus() {
    return myTestStatus;
  }

  protected PremergeBranchSupport createPremergeBranchSupport(VcsRoot root) throws VcsException {
    if (myBranchSupportClass.equals(MockPremergeBranchSupportSuccess.class)) {
      branchSupportCounter++;
      return new MockPremergeBranchSupportSuccess(this);
    }
    if (myBranchSupportClass.equals(MockPremergeBranchSupport.class)) {
      MockPremergeBranchSupport support = new MockPremergeBranchSupport(this, root);
      support.getBuilder().setFetchSuccess(myFetchSuccess.size() > branchSupportCounter ? myFetchSuccess.get(branchSupportCounter) : true);
      support.getBuilder().setMergeSuccess(myMergeSuccess.size() > branchSupportCounter ? myMergeSuccess.get(branchSupportCounter) : true);
      support.getBuilder().setAbortSuccess(myAbortSuccess.size() > branchSupportCounter ? myAbortSuccess.get(branchSupportCounter) : true);
      supports.add(support);
      branchSupportCounter++;
      return support;
    }
    branchSupportCounter++;
    return null;
  }
}
