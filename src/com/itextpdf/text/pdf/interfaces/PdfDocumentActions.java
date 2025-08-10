package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfName;

public interface PdfDocumentActions {
  void setOpenAction(String paramString);
  
  void setOpenAction(PdfAction paramPdfAction);
  
  void setAdditionalAction(PdfName paramPdfName, PdfAction paramPdfAction) throws DocumentException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/interfaces/PdfDocumentActions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */