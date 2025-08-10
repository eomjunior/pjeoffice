package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;

public interface Regexp extends RegexpMatcher {
  public static final int REPLACE_FIRST = 1;
  
  public static final int REPLACE_ALL = 16;
  
  String substitute(String paramString1, String paramString2, int paramInt) throws BuildException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/regexp/Regexp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */