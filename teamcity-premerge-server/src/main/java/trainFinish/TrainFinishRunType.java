package trainFinish;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import premerge.PremergeConstants;

public class TrainFinishRunType extends RunType {
  @NotNull
  private final PluginDescriptor myPluginDescriptor;

  public TrainFinishRunType(@NotNull RunTypeRegistry runTypeRegistry, @NotNull PluginDescriptor pluginDescriptor) {
    runTypeRegistry.registerRunType(this);
    myPluginDescriptor = pluginDescriptor;
  }

  @NotNull
  @Override
  public String getType() {
    return PremergeConstants.TYPE_FINISH;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return PremergeConstants.DISPLAY_NAME_FINISH;
  }

  @NotNull
  @Override
  public String getDescription() {
    return "last step of merge train"; //TODO Write
  }

  @Nullable
  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    return new PropertiesProcessor() {
      @Override
      public Collection<InvalidProperty> process(Map<String, String> properties) {
        final HashSet<InvalidProperty> invalid = new HashSet<>();
        String gitHubToken = properties.get(PremergeConstants.GITHUB_ACCESS_TOKEN);
        if (StringUtil.isEmpty(gitHubToken)) {
          invalid.add(new InvalidProperty(PremergeConstants.GITHUB_ACCESS_TOKEN, "GitHub Access Token must be specified"));
        }

        String teamcityToken = properties.get(PremergeConstants.TEAMCITY_ACCESS_TOKEN);
        if (StringUtil.isEmpty(teamcityToken)) {
          invalid.add(new InvalidProperty(PremergeConstants.TEAMCITY_ACCESS_TOKEN, "TeamCity Access Token must be specified"));
        }

        return invalid;
      }
    };
  }

  @Nullable
  @Override
  public String getEditRunnerParamsJspFilePath() {
    return myPluginDescriptor.getPluginResourcesPath("trainFinishParams.jsp");
  }

  @Nullable
  @Override
  public String getViewRunnerParamsJspFilePath() {
    return myPluginDescriptor.getPluginResourcesPath("viewPremergeParams.jsp");
  }

  @Nullable
  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    return new HashMap<>();
  }
}
