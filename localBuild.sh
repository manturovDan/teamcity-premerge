# this script configures the plugin to run within the local TeamCity project in IntelliJ IDEA

plugin_name="teamcity-premerge"
assembled_plugin="target/${plugin_name}"
wd="external-repos/${plugin_name}/"
web_deployment_debug="../../.idea_artifacts/web_deployment_debug/WEB-INF/plugins"
agent_deployment_debug="../../.idea_artifacts/agent_deployment_debug/plugins"

pwd

cd $wd

mvn clean package

if [ -e "${assembled_plugin}.zip" ]
then
  echo "pre-merge plugin is built"
  rm -rf "$assembled_plugin"
  unzip -d "$assembled_plugin" "${assembled_plugin}.zip"

  #server
  rm -rf "${web_deployment_debug}/${plugin_name}"
  mkdir "${web_deployment_debug}/${plugin_name}"
  cp -R "${assembled_plugin}/kotlin-dsl" "${web_deployment_debug}/${plugin_name}"
  cp -R "${assembled_plugin}/server" "${web_deployment_debug}/${plugin_name}"
  cp -R "${assembled_plugin}/teamcity-plugin.xml" "${web_deployment_debug}/${plugin_name}"

  #agent
  unzip -d "${assembled_plugin}/agent" "${assembled_plugin}/agent/${plugin_name}-agent.zip"
  # rm -rf ${assembled_plugin}/agent/lib/git-*.jar
  rm -rf "${agent_deployment_debug}/${plugin_name}"
  mkdir "${agent_deployment_debug}/${plugin_name}"
  cp -R "${assembled_plugin}/agent/lib" "${agent_deployment_debug}/${plugin_name}"
  cp -R "${assembled_plugin}/agent/teamcity-plugin.xml" "${agent_deployment_debug}/${plugin_name}"

else
  echo "error: pre-merge plugin is not built"
fi

cd ../..