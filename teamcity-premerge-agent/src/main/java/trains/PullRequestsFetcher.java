package trains;

import java.util.Map;

public interface PullRequestsFetcher {
  public Map<String, PullRequestEntity> fetchPRs();
  public void setUnsuccess(String prNumber);
}
