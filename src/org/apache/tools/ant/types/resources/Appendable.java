package org.apache.tools.ant.types.resources;

import java.io.IOException;
import java.io.OutputStream;

public interface Appendable {
  OutputStream getAppendOutputStream() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Appendable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */