package com.itextpdf.xmp.properties;

import com.itextpdf.xmp.options.PropertyOptions;

public interface XMPPropertyInfo extends XMPProperty {
  String getNamespace();
  
  String getPath();
  
  String getValue();
  
  PropertyOptions getOptions();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/properties/XMPPropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */