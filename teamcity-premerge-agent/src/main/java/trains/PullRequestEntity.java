package trains;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

public interface PullRequestEntity {
  @NotNull public String getNumber();
  @NotNull public Date getUpdateTime();
  @NotNull public String getSourceBranch();
  @NotNull public String getTargetBranch();
}
