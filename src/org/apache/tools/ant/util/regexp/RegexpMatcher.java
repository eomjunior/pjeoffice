package org.apache.tools.ant.util.regexp;

import java.util.Vector;
import org.apache.tools.ant.BuildException;

public interface RegexpMatcher {
  public static final int MATCH_DEFAULT = 0;
  
  public static final int MATCH_CASE_INSENSITIVE = 256;
  
  public static final int MATCH_MULTILINE = 4096;
  
  public static final int MATCH_SINGLELINE = 65536;
  
  void setPattern(String paramString) throws BuildException;
  
  String getPattern() throws BuildException;
  
  boolean matches(String paramString) throws BuildException;
  
  Vector<String> getGroups(String paramString) throws BuildException;
  
  boolean matches(String paramString, int paramInt) throws BuildException;
  
  Vector<String> getGroups(String paramString, int paramInt) throws BuildException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/regexp/RegexpMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */