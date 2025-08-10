package org.apache.tools.ant.types.selectors.modifiedselector;

import java.util.Iterator;

public interface Cache {
  boolean isValid();
  
  void delete();
  
  void load();
  
  void save();
  
  Object get(Object paramObject);
  
  void put(Object paramObject1, Object paramObject2);
  
  Iterator<String> iterator();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/modifiedselector/Cache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */