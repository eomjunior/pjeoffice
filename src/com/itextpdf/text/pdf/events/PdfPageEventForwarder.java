/*     */ package com.itextpdf.text.pdf.events;
/*     */ 
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.Paragraph;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.pdf.PdfPageEvent;
/*     */ import com.itextpdf.text.pdf.PdfWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfPageEventForwarder
/*     */   implements PdfPageEvent
/*     */ {
/*  64 */   protected ArrayList<PdfPageEvent> events = new ArrayList<PdfPageEvent>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPageEvent(PdfPageEvent event) {
/*  71 */     this.events.add(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onOpenDocument(PdfWriter writer, Document document) {
/*  83 */     for (PdfPageEvent event : this.events) {
/*  84 */       event.onOpenDocument(writer, document);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onStartPage(PdfWriter writer, Document document) {
/* 100 */     for (PdfPageEvent event : this.events) {
/* 101 */       event.onStartPage(writer, document);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEndPage(PdfWriter writer, Document document) {
/* 115 */     for (PdfPageEvent event : this.events) {
/* 116 */       event.onEndPage(writer, document);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCloseDocument(PdfWriter writer, Document document) {
/* 132 */     for (PdfPageEvent event : this.events) {
/* 133 */       event.onCloseDocument(writer, document);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {
/* 153 */     for (PdfPageEvent event : this.events) {
/* 154 */       event.onParagraph(writer, document, paragraphPosition);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {
/* 173 */     for (PdfPageEvent event : this.events) {
/* 174 */       event.onParagraphEnd(writer, document, paragraphPosition);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
/* 195 */     for (PdfPageEvent event : this.events) {
/* 196 */       event.onChapter(writer, document, paragraphPosition, title);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChapterEnd(PdfWriter writer, Document document, float position) {
/* 213 */     for (PdfPageEvent event : this.events) {
/* 214 */       event.onChapterEnd(writer, document, position);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {
/* 237 */     for (PdfPageEvent event : this.events) {
/* 238 */       event.onSection(writer, document, paragraphPosition, depth, title);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSectionEnd(PdfWriter writer, Document document, float position) {
/* 255 */     for (PdfPageEvent event : this.events) {
/* 256 */       event.onSectionEnd(writer, document, position);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
/* 279 */     for (PdfPageEvent element : this.events) {
/* 280 */       PdfPageEvent event = element;
/* 281 */       event.onGenericTag(writer, document, rect, text);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/events/PdfPageEventForwarder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */