
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;
import trains.PullRequestEntity;
import trains.impl.github.GitHubPullRequestDeserializer;

public class PullRequestsFetcherTest {
  @Test
  public void pullRequestsParserTest() {
    String json = readFile("resources/pullRequests.json");
    GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Map.class, new GitHubPullRequestDeserializer());
    Gson myGson = builder.create();
    Map<String, PullRequestEntity> pullRequests = myGson.fromJson(json, Map.class);
    Assert.assertEquals(6, pullRequests.size());
  }

  private static String readFile(@NotNull final String path) {
    StringBuilder builder = new StringBuilder();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
      int val = 0;
      while ((val = bufferedReader.read()) != -1) {
        char c = (char) val;
        builder.append(c);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return builder.toString();
  }
}

