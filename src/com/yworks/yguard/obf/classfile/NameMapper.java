package com.yworks.yguard.obf.classfile;

public interface NameMapper {
  String[] getAttrsToKeep(String paramString);
  
  String mapClass(String paramString);
  
  String mapMethod(String paramString1, String paramString2, String paramString3);
  
  String mapAnnotationField(String paramString1, String paramString2);
  
  String mapField(String paramString1, String paramString2);
  
  String mapDescriptor(String paramString);
  
  String mapSignature(String paramString);
  
  String mapSourceFile(String paramString1, String paramString2);
  
  boolean mapLineNumberTable(String paramString1, String paramString2, String paramString3, LineNumberTableAttrInfo paramLineNumberTableAttrInfo);
  
  String mapLocalVariable(String paramString1, String paramString2, String paramString3, String paramString4);
  
  String mapPackage(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/NameMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */