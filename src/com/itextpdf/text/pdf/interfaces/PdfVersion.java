package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfName;

public interface PdfVersion {
  void setPdfVersion(char paramChar);
  
  void setAtLeastPdfVersion(char paramChar);
  
  void setPdfVersion(PdfName paramPdfName);
  
  void addDeveloperExtension(PdfDeveloperExtension paramPdfDeveloperExtension);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/interfaces/PdfVersion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */