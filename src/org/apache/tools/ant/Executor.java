package org.apache.tools.ant;

public interface Executor {
  void executeTargets(Project paramProject, String[] paramArrayOfString) throws BuildException;
  
  Executor getSubProjectExecutor();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/Executor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */