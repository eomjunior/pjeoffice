package org.apache.tools.ant.taskdefs.optional.j2ee;

import org.apache.tools.ant.BuildException;

public interface HotDeploymentTool {
  public static final String ACTION_DELETE = "delete";
  
  public static final String ACTION_DEPLOY = "deploy";
  
  public static final String ACTION_LIST = "list";
  
  public static final String ACTION_UNDEPLOY = "undeploy";
  
  public static final String ACTION_UPDATE = "update";
  
  void validateAttributes() throws BuildException;
  
  void deploy() throws BuildException;
  
  void setTask(ServerDeploy paramServerDeploy);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/j2ee/HotDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */