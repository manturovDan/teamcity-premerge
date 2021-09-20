import java.util.HashMap;
import java.util.Map;
import jetbrains.buildServer.buildTriggers.vcs.git.Constants;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MockVcsRoot implements VcsRoot {
  private Map<String, String> properties = new HashMap<String, String>() {{
                                                                            put(Constants.FETCH_URL, "https://");
                                                                          }};
  public VcsRoot setUrl(String url) {
    properties.put("url", url);
    return this;
  }

  @NotNull
  @Override
  public String describe(boolean verbose) {
    return null;
  }

  @Override
  public long getId() {
    return 0;
  }

  @NotNull
  @Override
  public String getName() {
    return "TestRoot";
  }

  @NotNull
  @Override
  public String getVcsName() {
    return null;
  }

  @NotNull
  @Override
  public Map<String, String> getProperties() {
    return properties;
  }

  @Nullable
  @Override
  public String getProperty(@NotNull String propertyName) {
    return properties.get(propertyName);
  }

  @Nullable
  @Override
  public String getProperty(@NotNull String propertyName, @Nullable String defaultValue) {
    return null;
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "ex_id";
  }
}
