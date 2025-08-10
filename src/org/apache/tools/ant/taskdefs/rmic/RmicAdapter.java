package org.apache.tools.ant.taskdefs.rmic;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Rmic;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileNameMapper;

public interface RmicAdapter {
  void setRmic(Rmic paramRmic);
  
  boolean execute() throws BuildException;
  
  FileNameMapper getMapper();
  
  Path getClasspath();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/rmic/RmicAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */