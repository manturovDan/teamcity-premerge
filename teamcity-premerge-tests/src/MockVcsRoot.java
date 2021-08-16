import java.util.Map;
import jetbrains.buildServer.vcs.VcsRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MockVcsRoot implements VcsRoot {
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
    return null;
  }

  @NotNull
  @Override
  public String getVcsName() {
    return null;
  }

  @NotNull
  @Override
  public Map<String, String> getProperties() {
    return null;
  }

  @Nullable
  @Override
  public String getProperty(@NotNull String propertyName) {
    return null;
  }

  @Nullable
  @Override
  public String getProperty(@NotNull String propertyName, @Nullable String defaultValue) {
    return null;
  }

  @NotNull
  @Override
  public String getExternalId() {
    return null;
  }
}
