package trains.impl.github;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import jetbrains.buildServer.http.HttpApi;
import trains.RESTHelper;
import trains.PullRequestsFetcher;

public class GitHubPullRequestsFetcher implements PullRequestsFetcher {
  private final RESTHelper myHttpHelper;
  private final Gson myGson;

  public GitHubPullRequestsFetcher(HttpApi httpApi) {
    myHttpHelper = new RESTHelper(httpApi, "https://api.github.com/repos/manturovDan/Sake/", "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");
    GsonBuilder builder = new GsonBuilder().registerTypeAdapter(List.class, new GitHubPullRequestDeserializer());
    myGson = builder.create();
  }

  public void fetchPRs() {
    HttpApi.Response prResp = myHttpHelper.get("pulls");
    List<GitHubPullRequestEntity> recievedPRs = myGson.fromJson(prResp.getBody(), List.class);
    recievedPRs.size();
  }

}
