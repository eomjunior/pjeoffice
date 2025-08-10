package org.apache.tools.ant.taskdefs.optional.ejb;

import javax.xml.parsers.SAXParser;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public interface EJBDeploymentTool {
  void processDescriptor(String paramString, SAXParser paramSAXParser) throws BuildException;
  
  void validateConfigured() throws BuildException;
  
  void setTask(Task paramTask);
  
  void configure(EjbJar.Config paramConfig);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/EJBDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */