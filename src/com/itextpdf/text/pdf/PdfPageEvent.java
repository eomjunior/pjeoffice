package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;

public interface PdfPageEvent {
  void onOpenDocument(PdfWriter paramPdfWriter, Document paramDocument);
  
  void onStartPage(PdfWriter paramPdfWriter, Document paramDocument);
  
  void onEndPage(PdfWriter paramPdfWriter, Document paramDocument);
  
  void onCloseDocument(PdfWriter paramPdfWriter, Document paramDocument);
  
  void onParagraph(PdfWriter paramPdfWriter, Document paramDocument, float paramFloat);
  
  void onParagraphEnd(PdfWriter paramPdfWriter, Document paramDocument, float paramFloat);
  
  void onChapter(PdfWriter paramPdfWriter, Document paramDocument, float paramFloat, Paragraph paramParagraph);
  
  void onChapterEnd(PdfWriter paramPdfWriter, Document paramDocument, float paramFloat);
  
  void onSection(PdfWriter paramPdfWriter, Document paramDocument, float paramFloat, int paramInt, Paragraph paramParagraph);
  
  void onSectionEnd(PdfWriter paramPdfWriter, Document paramDocument, float paramFloat);
  
  void onGenericTag(PdfWriter paramPdfWriter, Document paramDocument, Rectangle paramRectangle, String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPageEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */