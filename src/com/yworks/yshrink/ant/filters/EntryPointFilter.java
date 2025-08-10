package com.yworks.yshrink.ant.filters;

import com.yworks.yshrink.model.ClassDescriptor;
import com.yworks.yshrink.model.FieldDescriptor;
import com.yworks.yshrink.model.MethodDescriptor;
import com.yworks.yshrink.model.Model;

public interface EntryPointFilter {
  boolean isEntryPointClass(Model paramModel, ClassDescriptor paramClassDescriptor);
  
  boolean isEntryPointMethod(Model paramModel, ClassDescriptor paramClassDescriptor, MethodDescriptor paramMethodDescriptor);
  
  boolean isEntryPointField(Model paramModel, ClassDescriptor paramClassDescriptor, FieldDescriptor paramFieldDescriptor);
  
  void setRetainAttribute(ClassDescriptor paramClassDescriptor);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/EntryPointFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */