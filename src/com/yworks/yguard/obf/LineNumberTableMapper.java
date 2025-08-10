package com.yworks.yguard.obf;

import com.yworks.yguard.obf.classfile.LineNumberTableAttrInfo;
import java.io.PrintWriter;

public interface LineNumberTableMapper {
  boolean mapLineNumberTable(String paramString1, String paramString2, String paramString3, LineNumberTableAttrInfo paramLineNumberTableAttrInfo);
  
  void logProperties(PrintWriter paramPrintWriter);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/LineNumberTableMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */