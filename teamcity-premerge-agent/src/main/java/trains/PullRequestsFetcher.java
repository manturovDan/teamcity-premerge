package trains;

import java.io.IOException;
import jetbrains.buildServer.pullRequests.HttpCredentials;
import jetbrains.buildServer.pullRequests.impl.HttpHelper;
import jetbrains.buildServer.serverSide.IOGuard;

public interface PullRequestsFetcher {
  public void fetchPRs();
}
