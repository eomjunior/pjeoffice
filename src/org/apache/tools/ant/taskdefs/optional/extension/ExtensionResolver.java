package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public interface ExtensionResolver {
  File resolve(Extension paramExtension, Project paramProject) throws BuildException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/ExtensionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */