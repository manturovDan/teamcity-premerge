package trains.impl.github;

import jetbrains.buildServer.http.HttpApi;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeBuildRunner;
import trains.PullRequestsFetcherProvider;
import trains.PullRequestsFetcher;

public class GitHubPullRequestsFetcherProvider implements PullRequestsFetcherProvider {
  public GitHubPullRequestsFetcherProvider(PremergeBuildRunner premergeRunner) {
    premergeRunner.registerPullRequestFetcherProvider(this);
  }

  @NotNull
  @Override
  public String getType() {
    return "github";
  }

  @NotNull
  @Override
  public PullRequestsFetcher getFetcher(HttpApi httpApi, String repoUrl, String credentials) {
    return new GitHubPullRequestsFetcher(httpApi, repoUrl, credentials);
  }
}
