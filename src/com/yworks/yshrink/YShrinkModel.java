package com.yworks.yshrink;

import com.yworks.common.ShrinkBag;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;

public interface YShrinkModel {
  void createSimpleModel(List<ShrinkBag> paramList) throws IOException;
  
  Set<String> getAllAncestorClasses(String paramString);
  
  Set<String> getAllImplementedInterfaces(String paramString);
  
  Collection<String> getAllClassNames();
  
  void setResourceClassPath(Path paramPath, Task paramTask);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/YShrinkModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */