package trains.impl.github;

import java.util.Date;
import org.jetbrains.annotations.NotNull;
import trains.PullRequestEntity;

public class GitHubPullRequestEntity implements PullRequestEntity {
  @NotNull final private String myNumber;
  @NotNull final private Date myUpdatedAt;
  @NotNull final private String mySourceBranch;
  @NotNull final private String myTargetBranch;
  @NotNull final private boolean myIsValid;

  public GitHubPullRequestEntity(@NotNull String number,
                                 @NotNull Date updatedAt,
                                 @NotNull String sourceBranch,
                                 @NotNull String targetBranch,
                                 boolean isValid) {
    myNumber = number;
    myUpdatedAt = updatedAt;
    mySourceBranch = sourceBranch;
    myTargetBranch = targetBranch;
    myIsValid = isValid;
  }

  @NotNull
  @Override
  public String getNumber() {
    return myNumber;
  }

  @NotNull
  @Override
  public Date getUpdateTime() {
    return myUpdatedAt;
  }

  @NotNull
  @Override
  public String getSourceBranch() {
    return mySourceBranch;
  }

  @NotNull
  @Override
  public String getTargetBranch() {
    return myTargetBranch;
  }

  @Override
  public boolean isValid() {
    return myIsValid;
  }
}
