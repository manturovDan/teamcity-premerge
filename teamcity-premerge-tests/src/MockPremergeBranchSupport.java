import jetbrains.buildServer.vcs.VcsException;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeBranchSupport;

public class MockPremergeBranchSupport implements PremergeBranchSupport {
  @Override
  public void fetch(String branch) throws VcsException {

  }

  @Override
  public void checkout(String branch) throws VcsException {

  }

  @Override
  public void createBranch(String branch) throws VcsException {

  }

  @Override
  public void merge(String branch) throws VcsException {

  }

  @NotNull
  @Override
  public String constructBranchName() {
    return "hello";
  }
}
