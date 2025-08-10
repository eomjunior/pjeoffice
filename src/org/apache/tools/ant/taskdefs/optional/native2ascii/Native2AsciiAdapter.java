package org.apache.tools.ant.taskdefs.optional.native2ascii;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.Native2Ascii;

public interface Native2AsciiAdapter {
  boolean convert(Native2Ascii paramNative2Ascii, File paramFile1, File paramFile2) throws BuildException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/native2ascii/Native2AsciiAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */