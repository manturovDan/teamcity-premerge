package premerge;

public class PremergeConstants {
  private PremergeConstants() {

  }

  public static final String TYPE = "premergeRunner";
  public static final String DISPLAY_NAME = "Preliminary Merge";
  public static final String DESCRIPTION = "Makes preliminary merge commits with " +
                                           "\"Target branch\" when a source branch is updated (source branch must be set in 'Build condition')";
  public static final String TARGET_BRANCH = "tar.br";
  public static final String PRELIMINARY_MERGE_BRANCH_PREFIX = "premerge";
  public static final String TARGET_BRANCH_SHARED_PARAM = "teamcity.build.premerge.target";
  public static final String TARGET_SHA_SHARED_PARAM = "teamcity.build.premerge.targetSHA";
}
