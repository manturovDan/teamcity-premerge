/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package premerge;

public class PremergeConstants {
  private PremergeConstants() {

  }

  public static final String TYPE = "premergeRunner";
  public static final String DISPLAY_NAME_START = "Merge train (start)";
  public static final String DESCRIPTION = "Makes preliminary merge commits of " +
                                           "pull request target branch with all opened pull requests source branches.";
  public static final String TARGET_BRANCH = "tar.br";
  public static final String TEAMCITY_ACCESS_TOKEN = "secure:tc.token";
  public static final String GITHUB_ACCESS_TOKEN = "secure:github.token";
  public static final String PRELIMINARY_MERGE_BRANCH_PREFIX = "premerge";
  public static final String TARGET_BRANCH_SHARED_PARAM = "teamcity.build.premerge.target";
  public static final String TARGET_SHA_SHARED_PARAM = "teamcity.build.premerge.targetSHA";
  public static final String PULL_REQUEST_NUMBER_SHARED_PARAM = "teamcity.pullRequest.branch.pullrequests";
  public static final String MERGE_TRAIN_PULL_REQUESTS = "teamcity.build.mergetrains.pullrequests";
  public static final String TARGET_BRANCH_PR_FEATURE_PARAM = "teamcity.pullRequest.target.branch";
  public static final String TYPE_FINISH = "trainFinish";
  public static final String DISPLAY_NAME_FINISH = "Merge train (finish)";
}
