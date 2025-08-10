package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import java.util.HashMap;

public interface IAccessibleElement {
  PdfObject getAccessibleAttribute(PdfName paramPdfName);
  
  void setAccessibleAttribute(PdfName paramPdfName, PdfObject paramPdfObject);
  
  HashMap<PdfName, PdfObject> getAccessibleAttributes();
  
  PdfName getRole();
  
  void setRole(PdfName paramPdfName);
  
  AccessibleElementId getId();
  
  void setId(AccessibleElementId paramAccessibleElementId);
  
  boolean isInline();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/interfaces/IAccessibleElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */