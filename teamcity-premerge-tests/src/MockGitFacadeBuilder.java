import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jetbrains.buildServer.buildTriggers.vcs.git.AuthSettings;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.AgentGitFacade;
import jetbrains.buildServer.buildTriggers.vcs.git.agent.command.*;
import jetbrains.buildServer.buildTriggers.vcs.git.command.FetchCommand;
import jetbrains.buildServer.buildTriggers.vcs.git.command.LsRemoteCommand;
import jetbrains.buildServer.buildTriggers.vcs.git.command.RemoteCommand;
import jetbrains.buildServer.buildTriggers.vcs.git.command.VersionCommand;
import jetbrains.buildServer.buildTriggers.vcs.git.command.credentials.ScriptGen;
import jetbrains.buildServer.vcs.VcsException;
import org.jetbrains.annotations.NotNull;

public class MockGitFacadeBuilder {
  private boolean myFetchSuccess = true;
  private boolean myMergeSuccess = true;
  private boolean myAbortSuccess = true;

  private final List<String> sequence = new ArrayList<>();

  public List<String> getSequence() {
    return sequence;
  }

  public void setFetchSuccess(boolean fetchSuccess) {
    myFetchSuccess = fetchSuccess;
  }

  public void setMergeSuccess(boolean mergeSuccess) {
    myMergeSuccess = mergeSuccess;
  }

  public void setAbortSuccess(boolean abortSuccess) {
    myAbortSuccess = abortSuccess;
  }

  public AgentGitFacade build() {
    return new AgentGitFacade() {
      @NotNull
      @Override
      public InitCommand init() {
        return null;
      }

      @NotNull
      @Override
      public CreateBranchCommand createBranch() {
        return new CreateBranchCommand() {
          @NotNull
          @Override
          public CreateBranchCommand setName(@NotNull String name) {
            return this;
          }

          @NotNull
          @Override
          public CreateBranchCommand setStartPoint(@NotNull String startPoint) {
            return this;
          }

          @NotNull
          @Override
          public CreateBranchCommand setTrack(boolean track) {
            return this;
          }

          @Override
          public void call() throws VcsException {
            sequence.add("branchCreation");
          }

          @Override
          public void addConfig(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void setEnv(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void addPostAction(@NotNull Runnable action) {

          }
        };
      }

      @NotNull
      @Override
      public DeleteBranchCommand deleteBranch() {
        return null;
      }

      @NotNull
      @Override
      public DeleteTagCommand deleteTag() {
        return null;
      }

      @NotNull
      @Override
      public AddRemoteCommand addRemote() {
        return null;
      }

      @NotNull
      @Override
      public CleanCommand clean() {
        return null;
      }

      @NotNull
      @Override
      public ResetCommand reset() {
        return null;
      }

      @NotNull
      @Override
      public UpdateRefCommand updateRef() {
        return null;
      }

      @NotNull
      @Override
      public UpdateRefBatchCommand updateRefBatch() {
        return null;
      }

      @NotNull
      @Override
      public CheckoutCommand checkout() {
        return new CheckoutCommand() {
          @NotNull
          @Override
          public CheckoutCommand setForce(boolean force) {
            return this;
          }

          @NotNull
          @Override
          public CheckoutCommand setBranch(@NotNull String branch) {
            return this;
          }

          @Override
          public void call() throws VcsException {
            sequence.add("checkouting");
          }

          @NotNull
          @Override
          public CheckoutCommand setAuthSettings(@NotNull AuthSettings authSettings) {
            return this;
          }

          @NotNull
          @Override
          public CheckoutCommand setUseNativeSsh(boolean useNativeSsh) {
            return this;
          }

          @NotNull
          @Override
          public CheckoutCommand setTimeout(int timeout) {
            return this;
          }

          @Override
          public CheckoutCommand addPreAction(@NotNull Runnable action) {
            return null;
          }

          @Override
          public CheckoutCommand setRetryAttempts(int num) {
            return null;
          }

          @Override
          public CheckoutCommand trace(@NotNull Map<String, String> gitTraceEnv) {
            return null;
          }

          @Override
          public void addConfig(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void setEnv(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void addPostAction(@NotNull Runnable action) {

          }
        };
      }

      @NotNull
      @Override
      public GetConfigCommand getConfig() {
        return null;
      }

      @NotNull
      @Override
      public SetConfigCommand setConfig() {
        return null;
      }

      @NotNull
      @Override
      public ListConfigCommand listConfig() {
        return null;
      }

      @NotNull
      @Override
      public LogCommand log() {
        return null;
      }

      @NotNull
      @Override
      public LsTreeCommand lsTree() {
        return null;
      }

      @NotNull
      @Override
      public RevParseCommand revParse() {
        return new RevParseCommand() {
          private String verified = "";
          @NotNull
          @Override
          public RevParseCommand setRef(String ref) {
            return this;
          }

          @NotNull
          @Override
          public RevParseCommand setShallow(boolean isShallow) {
            return this;
          }

          @NotNull
          @Override
          public RevParseCommand verify(String param) {
            verified = param;
            return this;
          }

          @Override
          public String call() throws VcsException {
            sequence.add("verif_" + verified);
            return "sha";
          }

          @Override
          public void addConfig(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void setEnv(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void addPostAction(@NotNull Runnable action) {

          }
        };
      }

      @NotNull
      @Override
      public SubmoduleInitCommand submoduleInit() {
        return null;
      }

      @Override
      public SubmoduleSyncCommand submoduleSync() {
        return null;
      }

      @NotNull
      @Override
      public SubmoduleUpdateCommand submoduleUpdate() {
        return null;
      }

      @NotNull
      @Override
      public ShowRefCommand showRef() {
        return null;
      }

      @NotNull
      @Override
      public PackRefs packRefs() {
        return null;
      }

      @NotNull
      @Override
      public GcCommand gc() {
        return null;
      }

      @NotNull
      @Override
      public RepackCommand repack() {
        return null;
      }

      @NotNull
      @Override
      public Branches listBranches(boolean all) throws VcsException {
        return null;
      }

      @NotNull
      @Override
      public SetUpstreamCommand setUpstream(@NotNull String localBranch, @NotNull String upstreamBranch) throws VcsException {
        return null;
      }

      @NotNull
      @Override
      public String resolvePath(@NotNull File f) throws VcsException {
        return null;
      }

      @NotNull
      @Override
      public ScriptGen getScriptGen() {
        return null;
      }

      @NotNull
      @Override
      public UpdateIndexCommand updateIndex() {
        return null;
      }

      @NotNull
      @Override
      public DiffCommand diff() {
        return null;
      }

      @NotNull
      @Override
      public MergeCommand merge() {
        return new MergeCommand() {
          private boolean isAbort = false;

          @NotNull
          @Override
          public MergeCommand setBranches(String... mergeBranches) {
            return this;
          }

          @NotNull
          @Override
          public MergeCommand setAbort(boolean abort) {
            isAbort = true;
            return this;
          }

          @NotNull
          @Override
          public MergeCommand setQuiet(boolean quite) {
            return this;
          }

          @Override
          public void call() throws VcsException {
            if (isAbort) {
              if (myAbortSuccess) {
                sequence.add("merge_aborting");
              }
              else {
                throw new VcsException("Abort error");
              }
            }
            else {
              if (myMergeSuccess) {
                sequence.add("merging");
              }
              else {
                throw new VcsException("Conflict err");
              }
            }
          }

          @Override
          public void addConfig(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void setEnv(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void addPostAction(@NotNull Runnable action) {

          }
        };
      }

      @NotNull
      @Override
      public VersionCommand version() {
        return null;
      }

      @NotNull
      @Override
      public FetchCommand fetch() {
        return new FetchCommand() {
          @NotNull
          @Override
          public FetchCommand setRefspec(@NotNull String refspec) {
            return this;
          }

          @NotNull
          @Override
          public FetchCommand setQuite(boolean quite) {
            return this;
          }

          @NotNull
          @Override
          public FetchCommand setShowProgress(boolean showProgress) {
            return this;
          }

          @NotNull
          @Override
          public FetchCommand setDepth(int depth) {
            return this;
          }

          @NotNull
          @Override
          public FetchCommand setFetchTags(boolean fetchTags) {
            return this;
          }

          @NotNull
          @Override
          public FetchCommand setRemote(@NotNull String remoteUrl) {
            return this;
          }

          @Override
          public void call() throws VcsException {
            if (myFetchSuccess) {
              sequence.add("fetching");
            }
            else {
              throw new VcsException("Mock connection error");
            }
          }

          @NotNull
          @Override
          public FetchCommand setAuthSettings(@NotNull AuthSettings authSettings) {
            return this;
          }

          @NotNull
          @Override
          public FetchCommand setUseNativeSsh(boolean useNativeSsh) {
            return this;
          }

          @NotNull
          @Override
          public FetchCommand setTimeout(int timeout) {
            return this;
          }

          @Override
          public FetchCommand addPreAction(@NotNull Runnable action) {
            return null;
          }

          @Override
          public FetchCommand setRetryAttempts(int num) {
            return null;
          }

          @Override
          public FetchCommand trace(@NotNull Map<String, String> gitTraceEnv) {
            return null;
          }

          @Override
          public void addConfig(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void setEnv(@NotNull String name, @NotNull String value) {

          }

          @Override
          public void addPostAction(@NotNull Runnable action) {

          }
        };
      }

      @NotNull
      @Override
      public LsRemoteCommand lsRemote() {
        return null;
      }

      @NotNull
      @Override
      public RemoteCommand remote() {
        return null;
      }
    };
  }
}
