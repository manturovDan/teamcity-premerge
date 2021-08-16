import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.parameters.ValueResolver;
import org.jetbrains.annotations.NotNull;
import premerge.PremergeConstants;

public class MockBuildRunnerCtx implements BuildRunnerContext {
  private Map<String, String> mockRunnerParameters = new HashMap<String, String>() {{
    put(PremergeConstants.TARGET_BRANCH, "main");
  }};

  @Override
  public String getId() {
    return null;
  }

  @NotNull
  @Override
  public AgentRunningBuild getBuild() {
    return null;
  }

  @NotNull
  @Override
  public File getWorkingDirectory() {
    return null;
  }

  @NotNull
  @Override
  public String getRunType() {
    return null;
  }

  @NotNull
  @Override
  public String getName() {
    return null;
  }

  @NotNull
  @Override
  public BuildParametersMap getBuildParameters() {
    return null;
  }

  @NotNull
  @Override
  public Map<String, String> getConfigParameters() {
    return null;
  }

  @NotNull
  @Override
  public Map<String, String> getRunnerParameters() {
    return mockRunnerParameters;
  }

  @Override
  public void addSystemProperty(@NotNull String key, @NotNull String value) {

  }

  @Override
  public void addEnvironmentVariable(@NotNull String key, @NotNull String value) {

  }

  @Override
  public void addConfigParameter(@NotNull String key, @NotNull String value) {

  }

  @Override
  public void addRunnerParameter(@NotNull String key, @NotNull String value) {

  }

  @NotNull
  @Override
  public ValueResolver getParametersResolver() {
    return null;
  }

  @NotNull
  @Override
  public String getToolPath(@NotNull String toolName) throws ToolCannotBeFoundException {
    return null;
  }

  @Override
  public boolean parametersHaveReferencesTo(@NotNull Collection<String> keys) {
    return false;
  }

  @Override
  public boolean isVirtualContext() {
    return false;
  }

  @NotNull
  @Override
  public VirtualContext getVirtualContext() {
    return null;
  }
}
