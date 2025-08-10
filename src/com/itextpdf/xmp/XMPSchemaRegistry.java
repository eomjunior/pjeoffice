package com.itextpdf.xmp;

import com.itextpdf.xmp.properties.XMPAliasInfo;
import java.util.Map;

public interface XMPSchemaRegistry {
  String registerNamespace(String paramString1, String paramString2) throws XMPException;
  
  String getNamespacePrefix(String paramString);
  
  String getNamespaceURI(String paramString);
  
  Map getNamespaces();
  
  Map getPrefixes();
  
  void deleteNamespace(String paramString);
  
  XMPAliasInfo resolveAlias(String paramString1, String paramString2);
  
  XMPAliasInfo[] findAliases(String paramString);
  
  XMPAliasInfo findAlias(String paramString);
  
  Map getAliases();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/XMPSchemaRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */