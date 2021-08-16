package premerge;

import jetbrains.buildServer.vcs.VcsException;
import org.jetbrains.annotations.NotNull;

public interface PremergeBranchSupport {
  @NotNull
  public static String cutRefsHeads(@NotNull String branchName) {
    if (branchName.startsWith("refs/heads/")) {
      return branchName.substring("refs/heads/".length());
    }
    return branchName;
  }

  public void fetch(String branch) throws VcsException;
  public void checkout(String branch) throws VcsException;
  public void createBranch(String branch) throws VcsException;
  public void merge(String branch) throws VcsException;
  @NotNull String constructBranchName();
}
