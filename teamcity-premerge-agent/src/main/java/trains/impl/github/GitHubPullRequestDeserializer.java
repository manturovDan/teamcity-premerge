package trains.impl.github;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GitHubPullRequestDeserializer implements JsonDeserializer<List<GitHubPullRequestEntity>> {
  @Override
  public List<GitHubPullRequestEntity> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    List<GitHubPullRequestEntity> PRs = new ArrayList<>();
    JsonArray jsonPRs = json.getAsJsonArray();
    for (JsonElement pr : jsonPRs) {
      JsonObject prObj = pr.getAsJsonObject();
      PRs.add(new GitHubPullRequestEntity(prObj.get("number").getAsString(),
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
