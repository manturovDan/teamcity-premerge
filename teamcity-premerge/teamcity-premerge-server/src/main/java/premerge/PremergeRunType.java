package premerge;

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
    return PremergeConstants.TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return PremergeConstants.DISPLAY_NAME;
  }

  @NotNull
  @Override
  public String getDescription() {
    return PremergeConstants.DESCRIPTION;
  }

  @Nullable
  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    return new PropertiesProcessor() {
      @Override
      public Collection<InvalidProperty> process(Map<String, String> properties) {
        //check branches are valid
        final HashSet<InvalidProperty> invalid = new HashSet<>();
        String srcFilter = properties.get(PremergeConstants.SOURCE_BRANCHES_FILTER);
        if (StringUtil.isEmpty(srcFilter)) {
          invalid.add(new InvalidProperty(PremergeConstants.SOURCE_BRANCHES_FILTER, "Source branches filter must be specified"));
        }

        String targetBranch = properties.get(PremergeConstants.TARGET_BRANCH);
        if (StringUtil.isEmpty(targetBranch)) {
          invalid.add(new InvalidProperty(PremergeConstants.TARGET_BRANCH, "Target branch must be specified"));
        }

        return invalid;
      }
    };
  }

  @Nullable
  @Override
  public String getEditRunnerParamsJspFilePath() {
    return myPluginDescriptor.getPluginResourcesPath("premergeParams.jsp");
  }

  @Nullable
  @Override
  public String getViewRunnerParamsJspFilePath() {
    return myPluginDescriptor.getPluginResourcesPath("premergeParams.jsp");
  }

  @Nullable
  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    return new HashMap<>();
  }

  @NotNull
  @Override
  public String describeParameters(@NotNull Map<String, String> parameters) {
    StringBuilder builder = new StringBuilder();
    builder.append("Source branches filter: ").append(StringUtil.emptyIfNull(parameters.get(PremergeConstants.SOURCE_BRANCHES_FILTER))).append("\n");
    builder.append("Target branch: ").append(StringUtil.emptyIfNull(parameters.get(PremergeConstants.TARGET_BRANCH)));
    return builder.toString();
  }
}


