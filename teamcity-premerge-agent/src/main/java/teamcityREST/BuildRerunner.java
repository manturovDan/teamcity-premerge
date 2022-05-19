package teamcityREST;

import jetbrains.buildServer.http.HttpApi;
import org.jetbrains.annotations.NotNull;
import trains.RESTHelper;

public class BuildRerunner {
  @NotNull private final String myTeamCityToken;
  @NotNull private final String myTeamCityServer;
  @NotNull private final RESTHelper myHttpHelper;
  @NotNull private final HttpApi myHttpApi;

  public BuildRerunner(@NotNull String teamCityToken, @NotNull String teamCityServer, @NotNull HttpApi httpApi) {
    myTeamCityToken = teamCityToken;
    myTeamCityServer = teamCityServer;
    myHttpApi = httpApi;
    myHttpHelper = new RESTHelper(myHttpApi, getRunBuildUrl(), myTeamCityToken);
  }

  private String getRunBuildUrl() {
    return (myTeamCityServer.endsWith("/") ? myTeamCityServer : myTeamCityServer + "/");
  }

  private String getBody(@NotNull String buildTypeId, @NotNull String branchName) {
    return "{ \"branchName\": \"" + branchName + "\", \"buildType\": { \"id\": \"" + buildTypeId + "\" }, \"comment\": { \"text\": \"Restarted from merge train\" }, \"personal\": false }";
  }

  public void restartBuild(@NotNull String buildTypeId, @NotNull String branchName) {
    HttpApi.Response restartResp = myHttpHelper.post("app/rest/buildQueue?moveToTop=true", getBody(buildTypeId, branchName), new HttpApi.HeaderPair("Accept", "*/*"));
    if (restartResp == null || restartResp.getStatusCode() / 100 != 2) {
      throw new RuntimeException("Restart build error." + (restartResp != null ?
                                                           "code: " + restartResp.getStatusCode() + ", body: " + restartResp.getBody() : ""));
    }
  }
}
