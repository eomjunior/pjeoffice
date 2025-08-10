package org.apache.tools.ant;

public interface SubBuildListener extends BuildListener {
  void subBuildStarted(BuildEvent paramBuildEvent);
  
  void subBuildFinished(BuildEvent paramBuildEvent);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/SubBuildListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */