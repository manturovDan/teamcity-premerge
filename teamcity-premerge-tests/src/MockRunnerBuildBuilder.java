import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.agentServer.AgentBuild;
import jetbrains.buildServer.artifacts.ArtifactDependencyInfo;
import jetbrains.buildServer.parameters.ValueResolver;
import jetbrains.buildServer.util.Option;
import jetbrains.buildServer.util.PasswordReplacer;
import jetbrains.buildServer.vcs.CheckoutRules;
import jetbrains.buildServer.vcs.VcsChangeInfo;
import jetbrains.buildServer.vcs.VcsRoot;
import jetbrains.buildServer.vcs.VcsRootEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MockRunnerBuildBuilder {
  private int myBuildId = 0;
  private int myVcsRootsCount = 1;

  public MockRunnerBuildBuilder setBuildId(int buildId) {
    myBuildId = buildId;
    return this;
  }

  public MockRunnerBuildBuilder setVcsRootsCount(int vcsRootsCount) {
    myVcsRootsCount = vcsRootsCount;
    return this;
  }

  AgentRunningBuild build() {
    return new AgentRunningBuild() {
      @NotNull
      @Override
      public File getCheckoutDirectory() {
        return null;
      }

      @Nullable
      @Override
      public AgentCheckoutMode getEffectiveCheckoutMode() {
        return null;
      }

      @NotNull
      @Override
      public File getWorkingDirectory() {
        return null;
      }

      @Nullable
      @Override
      public String getArtifactsPaths() {
        return null;
      }

      @Override
      public boolean getFailBuildOnExitCode() {
        return false;
      }

      @NotNull
      @Override
      public BuildParametersMap getBuildParameters() {
        return null;
      }

      @NotNull
      @Override
      public Map<String, String> getRunnerParameters() {
        return null;
      }

      @NotNull
      @Override
      public String getBuildNumber() {
        return null;
      }

      @NotNull
      @Override
      public Map<String, String> getSharedConfigParameters() {
        return null;
      }

      @Override
      public void addSharedConfigParameter(@NotNull String key, @NotNull String value) {

      }

      @Override
      public void addSharedSystemProperty(@NotNull String key, @NotNull String value) {

      }

      @Override
      public void addSharedEnvironmentVariable(@NotNull String key, @NotNull String value) {

      }

      @NotNull
      @Override
      public BuildParametersMap getSharedBuildParameters() {
        return null;
      }

      @NotNull
      @Override
      public ValueResolver getSharedParametersResolver() {
        return null;
      }

      @NotNull
      @Override
      public Collection<AgentBuildFeature> getBuildFeatures() {
        return null;
      }

      @NotNull
      @Override
      public Collection<AgentBuildFeature> getBuildFeaturesOfType(@NotNull String type) {
        return null;
      }

      @Override
      public void stopBuild(@NotNull String reason) {

      }

      @Nullable
      @Override
      public BuildInterruptReason getInterruptReason() {
        return null;
      }

      @Override
      public void interruptBuild(@NotNull String comment, boolean reQueue) {

      }

      @Override
      public boolean isBuildFailingOnServer() throws InterruptedException {
        return false;
      }

      @Override
      public boolean isInAlwaysExecutingStage() {
        return false;
      }

      @NotNull
      @Override
      public PasswordReplacer getPasswordReplacer() {
        return null;
      }

      @NotNull
      @Override
      public Map<String, String> getArtifactStorageSettings() {
        return null;
      }

      @Override
      public String getProjectName() {
        return null;
      }

      @NotNull
      @Override
      public String getBuildTypeId() {
        return null;
      }

      @NotNull
      @Override
      public String getBuildTypeExternalId() {
        return null;
      }

      @Override
      public String getBuildTypeName() {
        return null;
      }

      @Override
      public long getBuildId() {
        return myBuildId;
      }

      @Override
      public boolean isCleanBuild() {
        return false;
      }

      @Override
      public boolean isPersonal() {
        return false;
      }

      @Override
      public boolean isPersonalPatchAvailable() {
        return false;
      }

      @Override
      public boolean isCheckoutOnAgent() {
        return false;
      }

      @Override
      public boolean isCheckoutOnServer() {
        return false;
      }

      @NotNull
      @Override
      public AgentBuild.CheckoutType getCheckoutType() {
        return null;
      }

      @Override
      public long getExecutionTimeoutMinutes() {
        return 0;
      }

      @NotNull
      @Override
      public List<ArtifactDependencyInfo> getArtifactDependencies() {
        return null;
      }

      @NotNull
      @Override
      public String getAccessUser() {
        return null;
      }

      @NotNull
      @Override
      public String getAccessCode() {
        return null;
      }

      @NotNull
      @Override
      public List<VcsRootEntry> getVcsRootEntries() {
        return new ArrayList<VcsRootEntry>() {{
          for (int i = 0; i < myVcsRootsCount; ++i) {
            add(new VcsRootEntry(new MockVcsRoot(), new CheckoutRules("no rules")));
          }
        }};
      }

      @Override
      public String getBuildCurrentVersion(@NotNull VcsRoot vcsRoot) {
        return null;
      }

      @Override
      public String getBuildPreviousVersion(@NotNull VcsRoot vcsRoot) {
        return null;
      }

      @Override
      public boolean isCustomCheckoutDirectory() {
        return false;
      }

      @NotNull
      @Override
      public List<VcsChangeInfo> getVcsChanges() {
        return null;
      }

      @NotNull
      @Override
      public List<VcsChangeInfo> getPersonalVcsChanges() {
        return null;
      }

      @NotNull
      @Override
      public File getBuildTempDirectory() {
        return null;
      }

      @NotNull
      @Override
      public File getAgentTempDirectory() {
        return null;
      }

      @NotNull
      @Override
      public BuildProgressLogger getBuildLogger() {
        return new NullBuildProgressLogger();
      }

      @NotNull
      @Override
      public BuildAgentConfiguration getAgentConfiguration() {
        return null;
      }

      @Override
      public <T> T getBuildTypeOptionValue(@NotNull Option<T> option) {
        return null;
      }

      @NotNull
      @Override
      public File getDefaultCheckoutDirectory() {
        return null;
      }

      @NotNull
      @Override
      public String getVcsSettingsHashForCheckoutMode(AgentCheckoutMode agentCheckoutMode) {
        return null;
      }

      @NotNull
      @Override
      public List<BuildRunnerSettings> getBuildRunners() {
        return null;
      }

      @NotNull
      @Override
      public String describe(boolean verbose) {
        return null;
      }
    };
  }
}
