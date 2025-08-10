package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

public interface UnrecognizedElementHandler {
  boolean parseUnrecognizedElement(Element paramElement, Properties paramProperties) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/xml/UnrecognizedElementHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */