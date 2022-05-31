package trains;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import jetbrains.buildServer.http.HttpApi;
import jetbrains.buildServer.serverSide.IOGuard;
import org.jetbrains.annotations.NotNull;

public class RESTHelper {
  @NotNull private final HttpApi myHttpApi;
  @NotNull private final String myApiUrl;
  @NotNull private final String myToken;
  @NotNull private final Gson myGson = new GsonBuilder().create();

  public RESTHelper(@NotNull HttpApi httpApi, @NotNull String apiUrl, @NotNull String token) {
    myHttpApi = httpApi;
    myApiUrl = apiUrl.endsWith("/") ? apiUrl : apiUrl + "/";
    myToken = token;
  }

  public HttpApi.Response get(String path) {
    AtomicReference<HttpApi.Response> respGet = new AtomicReference<>();
    try {
      IOGuard.allowNetworkCall(() -> {
        respGet.set(
          myHttpApi.get(myApiUrl + path, new HttpApi.HeaderPair("Authorization", "Bearer " + myToken)));
      });
      return respGet.get();
    } catch (IOException e) {
      System.out.println("get API request error"); //TODO normal
      return null;
    }
  }

  public HttpApi.Response post(String path, String body, HttpApi.HeaderPair... additionalHeraders) {
    AtomicReference<HttpApi.Response> respPost = new AtomicReference<>();
    try {
      IOGuard.allowNetworkCall(() -> {
        HttpApi.HeaderPair[] headers = new HttpApi.HeaderPair[additionalHeraders.length+1];
        System.arraycopy(additionalHeraders, 0, headers, 0, additionalHeraders.length);
        headers[additionalHeraders.length] = new HttpApi.HeaderPair("Authorization", "Bearer " + myToken);
        respPost.set(
          myHttpApi.post(myApiUrl + path, body, "application/json", "UTF-8", headers)
        );
      });
      return respPost.get();
    } catch (IOException e) {
      System.out.println("post API request error"); //TODO normal
      return null;
    }
  }
}
