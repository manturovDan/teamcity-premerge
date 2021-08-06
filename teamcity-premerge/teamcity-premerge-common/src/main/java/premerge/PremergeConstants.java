package premerge;

public class PremergeConstants {
  private PremergeConstants() {

  }

  public static final String TYPE = "premergeRunner";
  public static final String DISPLAY_NAME = "Preliminary Merge";
  public static final String DESCRIPTION = "Makes preliminary merge commits of branches which " +
                                           "satisfy \"Source branch filter\" and \"Target branch\" when a source branch is updated";
  public static final String SOURCE_BRANCHES_FILTER = "src.br.filter";
  public static final String TARGET_BRANCH = "tar.br";
  public static final String PRELIMINARY_MERGE_BRANCH_PREFIX = "premerge";
}
