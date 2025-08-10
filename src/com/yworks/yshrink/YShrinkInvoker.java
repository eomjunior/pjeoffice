package com.yworks.yshrink;

import com.yworks.common.ShrinkBag;
import com.yworks.common.ant.EntryPointsSection;
import com.yworks.yguard.ant.ClassSection;
import com.yworks.yguard.ant.FieldSection;
import com.yworks.yguard.ant.MethodSection;
import java.io.File;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;

public interface YShrinkInvoker {
  void execute();
  
  void addPair(ShrinkBag paramShrinkBag);
  
  void setResourceClassPath(Path paramPath);
  
  void addClassSection(ClassSection paramClassSection);
  
  void addMethodSection(MethodSection paramMethodSection);
  
  void addFieldSection(FieldSection paramFieldSection);
  
  void setEntyPoints(EntryPointsSection paramEntryPointsSection);
  
  void setLogFile(File paramFile);
  
  void setContext(Task paramTask);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/YShrinkInvoker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */