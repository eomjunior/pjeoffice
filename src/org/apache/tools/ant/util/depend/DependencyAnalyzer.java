package org.apache.tools.ant.util.depend;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import org.apache.tools.ant.types.Path;

public interface DependencyAnalyzer {
  void addSourcePath(Path paramPath);
  
  void addClassPath(Path paramPath);
  
  void addRootClass(String paramString);
  
  Enumeration<File> getFileDependencies();
  
  Enumeration<String> getClassDependencies();
  
  void reset();
  
  void config(String paramString, Object paramObject);
  
  void setClosure(boolean paramBoolean);
  
  File getClassContainer(String paramString) throws IOException;
  
  File getSourceContainer(String paramString) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/depend/DependencyAnalyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */