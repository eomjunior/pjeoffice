package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocListener;
import com.itextpdf.text.Image;
import java.util.Map;

@Deprecated
public interface ImageProcessor {
  boolean process(Image paramImage, Map<String, String> paramMap, ChainedProperties paramChainedProperties, DocListener paramDocListener);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/ImageProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */