package org.apache.tools.ant.util;

public interface FileNameMapper {
  void setFrom(String paramString);
  
  void setTo(String paramString);
  
  String[] mapFileName(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/FileNameMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */