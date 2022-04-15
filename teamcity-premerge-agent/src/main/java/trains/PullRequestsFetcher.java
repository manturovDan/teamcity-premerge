package trains;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import jetbrains.buildServer.pullRequests.HttpCredentials;
import jetbrains.buildServer.pullRequests.impl.HttpHelper;
import jetbrains.buildServer.serverSide.IOGuard;

public interface PullRequestsFetcher {
  public Map<String, PullRequestEntity> fetchPRs();
}
