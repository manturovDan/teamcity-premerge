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
    AtomicReference<HttpApi.Response> respPRsRef = new AtomicReference<>();
    try {
      IOGuard.allowNetworkCall(() -> {
        respPRsRef.set(
          myHttpApi.get(myApiUrl + path, new HttpApi.HeaderPair("Authentification", "bearer " + myToken)));
      });
      return respPRsRef.get();
    } catch (IOException e) {
      System.out.println("API request error"); //TODO normal
      return null;
    }
  }
}
