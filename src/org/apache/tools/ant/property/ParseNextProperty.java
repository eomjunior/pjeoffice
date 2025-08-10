package org.apache.tools.ant.property;

import java.text.ParsePosition;
import org.apache.tools.ant.Project;

public interface ParseNextProperty {
  Project getProject();
  
  Object parseNextProperty(String paramString, ParsePosition paramParsePosition);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/property/ParseNextProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */