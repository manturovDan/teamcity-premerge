import java.util.ArrayList;
import java.util.List;
import jetbrains.buildServer.vcs.VcsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import premerge.PremergeBranchSupport;

public class MockPremergeBranchSupportSuccess implements PremergeBranchSupport {
  private final MockPremergeBuildProcess myProcess;
  private final List<String> actionSequence = new ArrayList<>();

  MockPremergeBranchSupportSuccess(MockPremergeBuildProcess process) {
    myProcess = process;
  }

  @Override
  public void fetch(String branch) throws VcsException {
    actionSequence.add("fetched_" + branch);
  }

  @Override
  public void checkout(String branch) throws VcsException {
    actionSequence.add("checkouted_to_" + branch);
  }

  @Override
  public void createBranch(String branch) throws VcsException {
    actionSequence.add("branch_" + branch + "_created");
  }

  @Override
  public void merge(String branch) throws VcsException {
    actionSequence.add("merged_" + branch);
  }

  @NotNull
  @Override
  public String constructBranchName() {
    return "premerge_branch";
  }

  @Nullable
  @Override
  public String getParameter(String parameter) throws VcsException {
    actionSequence.add("asked_parameter_" + parameter);
    myProcess.setTestStatus(
      String.join(",", actionSequence)
    );
    return parameter + "_answer";
  }
}
