package org.apache.tools.ant;

import java.util.EventListener;

public interface BuildListener extends EventListener {
  void buildStarted(BuildEvent paramBuildEvent);
  
  void buildFinished(BuildEvent paramBuildEvent);
  
  void targetStarted(BuildEvent paramBuildEvent);
  
  void targetFinished(BuildEvent paramBuildEvent);
  
  void taskStarted(BuildEvent paramBuildEvent);
  
  void taskFinished(BuildEvent paramBuildEvent);
  
  void messageLogged(BuildEvent paramBuildEvent);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/BuildListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */