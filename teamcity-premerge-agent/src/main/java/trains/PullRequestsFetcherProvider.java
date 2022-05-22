package trains;

import jetbrains.buildServer.http.HttpApi;
import org.jetbrains.annotations.NotNull;

public interface PullRequestsFetcherProvider {
  public @NotNull String getType();
  public @NotNull PullRequestsFetcher getFetcher(HttpApi httpApi, String repoUrl, String credentials);
}
