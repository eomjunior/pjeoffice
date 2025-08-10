package org.apache.tools.ant;

public interface TypeAdapter {
  void setProject(Project paramProject);
  
  Project getProject();
  
  void setProxy(Object paramObject);
  
  Object getProxy();
  
  void checkProxyClass(Class<?> paramClass);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/TypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */