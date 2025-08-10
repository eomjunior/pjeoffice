package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfAcroForm;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfFormField;

public interface PdfAnnotations {
  PdfAcroForm getAcroForm();
  
  void addAnnotation(PdfAnnotation paramPdfAnnotation);
  
  void addCalculationOrder(PdfFormField paramPdfFormField);
  
  void setSigFlags(int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/interfaces/PdfAnnotations.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */