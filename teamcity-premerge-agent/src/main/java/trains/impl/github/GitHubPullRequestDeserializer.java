package trains.impl.github;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GitHubPullRequestDeserializer implements JsonDeserializer<Map<String, GitHubPullRequestEntity>> {
  @Override
  public Map<String, GitHubPullRequestEntity> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    Map<String, GitHubPullRequestEntity> PRs = new HashMap<>();
    JsonArray jsonPRs = json.getAsJsonArray();
    for (JsonElement pr : jsonPRs) {
      JsonObject prObj = pr.getAsJsonObject();
      PRs.put(prObj.get("number").getAsString(),
              new GitHubPullRequestEntity(prObj.get("number").getAsString(),
                                          convertDate(prObj.get("updated_at").getAsString()),
                                          prObj.get("head").getAsJsonObject().get("ref").getAsString(),
                                          prObj.get("base").getAsJsonObject().get("ref").getAsString()));
    }
    return PRs;
  }

  private static Date convertDate(String date) {
    try {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
      return format.parse(date);
    } catch (ParseException e) {
      throw new RuntimeException("Date parsing error"); // TODO Normal
    }
  }
}
