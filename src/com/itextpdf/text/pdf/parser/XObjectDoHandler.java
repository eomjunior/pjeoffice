package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfStream;
import java.util.Stack;

public interface XObjectDoHandler {
  void handleXObject(PdfContentStreamProcessor paramPdfContentStreamProcessor, PdfStream paramPdfStream, PdfIndirectReference paramPdfIndirectReference);
  
  void handleXObject(PdfContentStreamProcessor paramPdfContentStreamProcessor, PdfStream paramPdfStream, PdfIndirectReference paramPdfIndirectReference, Stack<MarkedContentInfo> paramStack);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/XObjectDoHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */