package com.itextpdf.text.api;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

public interface WriterOperation {
  void write(PdfWriter paramPdfWriter, Document paramDocument) throws DocumentException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/api/WriterOperation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */