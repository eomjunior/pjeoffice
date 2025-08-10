package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

public interface PdfViewerPreferences {
  void setViewerPreferences(int paramInt);
  
  void addViewerPreference(PdfName paramPdfName, PdfObject paramPdfObject);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/interfaces/PdfViewerPreferences.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */