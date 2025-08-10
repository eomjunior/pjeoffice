package org.apache.tools.ant.taskdefs.optional.jsp;

import java.io.File;

public interface JspMangler {
  String mapJspToJavaName(File paramFile);
  
  String mapPath(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jsp/JspMangler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */