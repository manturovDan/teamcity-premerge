package premerge;

import jetbrains.buildServer.vcs.VcsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  public void setUser() throws VcsException;
  @NotNull String constructBranchName();
  @Nullable public String getParameter(String parameter) throws VcsException;
}
