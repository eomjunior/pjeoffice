package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

public interface IPdfStructureElement {
  PdfObject getAttribute(PdfName paramPdfName);
  
  void setAttribute(PdfName paramPdfName, PdfObject paramPdfObject);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/interfaces/IPdfStructureElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */