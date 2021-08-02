package premerge;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildServer.util.TCStreamUtil;
import org.jetbrains.annotations.NotNull;

public class PremergeRunner extends BuildServiceAdapter {
  private final Set<File> myFilesToDelete = new HashSet<File>();

  @NotNull
  @Override
  public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
    System.out.println("Simple Build Step");

    String script = getScript();
    setExecutableAttribute(script);

    return new SimpleProgramCommandLine(getRunnerContext(), script, Collections.emptyList());
  }

  @NotNull
  protected String getScript() {
    final String commandContent = "git log --graph --all";
    try {
      final File scriptFile = File.createTempFile("premerge_script", "", getAgentTempDirectory());
      FileUtil.writeFile(scriptFile, commandContent, Charset.defaultCharset());
      myFilesToDelete.add(scriptFile);

      return scriptFile.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace(); //todo
      return null;
    }
  }

  private void setExecutableAttribute(@NotNull final String script) throws RunBuildException {
    try {
      TCStreamUtil.setFileMode(new File(script), "a+x");
    } catch (Throwable t) {
      throw new RunBuildException("Failed to set executable attribute for custom script '" + script + "'", t);
    }
  }
}
