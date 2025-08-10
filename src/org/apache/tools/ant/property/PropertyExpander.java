package org.apache.tools.ant.property;

import java.text.ParsePosition;
import org.apache.tools.ant.PropertyHelper;

public interface PropertyExpander extends PropertyHelper.Delegate {
  String parsePropertyName(String paramString, ParsePosition paramParsePosition, ParseNextProperty paramParseNextProperty);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/property/PropertyExpander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */