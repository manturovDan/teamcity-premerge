package premerge;

import java.util.Map;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PremergeRunType extends RunType {
  @NotNull
  private final PluginDescriptor myPluginDescriptor;


  public PremergeRunType(@NotNull RunTypeRegistry runTypeRegistry, @NotNull PluginDescriptor pluginDescriptor) {
    runTypeRegistry.registerRunType(this);
    myPluginDescriptor = pluginDescriptor;
  }

  @NotNull
  @Override
  public String getType() {
    return "premergeRunner";
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Preliminary Merge";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Makes preliminary merge builds";
  }

  @Nullable
  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    return null;
  }

  @Nullable
  @Override
  public String getEditRunnerParamsJspFilePath() {
    return null;
  }

  @Nullable
  @Override
  public String getViewRunnerParamsJspFilePath() {
    return null;
  }

  @Nullable
  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    return null;
  }
}
