package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocListener;
import com.itextpdf.text.Image;
import java.util.Map;

@Deprecated
public interface ImageProvider {
  Image getImage(String paramString, Map<String, String> paramMap, ChainedProperties paramChainedProperties, DocListener paramDocListener);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/ImageProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */