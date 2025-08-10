package org.apache.tools.ant.types.selectors;

public interface SelectorScanner {
  void setSelectors(FileSelector[] paramArrayOfFileSelector);
  
  String[] getDeselectedDirectories();
  
  String[] getDeselectedFiles();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/SelectorScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */