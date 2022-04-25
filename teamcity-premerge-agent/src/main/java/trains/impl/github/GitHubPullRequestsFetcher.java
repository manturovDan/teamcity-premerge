package trains.impl.github;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import jetbrains.buildServer.http.HttpApi;
import trains.PullRequestEntity;
import trains.RESTHelper;
import trains.PullRequestsFetcher;

public class GitHubPullRequestsFetcher implements PullRequestsFetcher {
  private final RESTHelper myHttpHelper;
  private final Gson myGson;

  public GitHubPullRequestsFetcher(HttpApi httpApi, String repoUrl, String accesToken) {
    //myHttpHelper = new RESTHelper(httpApi, "https://api.github.com/repos/manturovDan/delay2", "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");
    myHttpHelper = new RESTHelper(httpApi, getApiURL(repoUrl), accesToken);
    GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Map.class, new GitHubPullRequestDeserializer());
    myGson = builder.create();
  }

  public Map<String, PullRequestEntity> fetchPRs() {
    HttpApi.Response prResp = myHttpHelper.get("pulls");
    return myGson.fromJson(prResp.getBody(), Map.class);
  }

  @Override
  public void setUnsuccess(String prNumber) {
    HttpApi.Response setLabelResp = myHttpHelper.post("issues/" + prNumber + "/labels", "{\"labels\":[\"invalid\"]}", new HttpApi.HeaderPair("Accept", "application/vnd.github.v3+json"));
    if (setLabelResp.getStatusCode() / 100 != 2) {
      throw new RuntimeException("set label error"); //TODO Normal
    }
  }


  public static String getApiURL(String repo) {
    try {
      URL repoUrl = new URL(repo);
      return repoUrl.getProtocol() + "://api." + repoUrl.getHost() + "/repos" + repoUrl.getPath();
    } catch (MalformedURLException e) {
      throw new RuntimeException("repo url error"); //TODO NORMAL
    }
  }
}
