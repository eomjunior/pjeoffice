package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfTransition;

public interface PdfPageActions {
  void setPageAction(PdfName paramPdfName, PdfAction paramPdfAction) throws DocumentException;
  
  void setDuration(int paramInt);
  
  void setTransition(PdfTransition paramPdfTransition);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/interfaces/PdfPageActions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */