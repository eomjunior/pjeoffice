/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class PdfReaderContentParser
/*     */ {
/*     */   private final PdfReader reader;
/*     */   
/*     */   public PdfReaderContentParser(PdfReader reader) {
/*  65 */     this.reader = reader;
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
/*     */   public <E extends RenderListener> E processContent(int pageNumber, E renderListener, Map<String, ContentOperator> additionalContentOperators) throws IOException {
/*  80 */     PdfDictionary pageDic = this.reader.getPageN(pageNumber);
/*  81 */     PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
/*     */     
/*  83 */     PdfContentStreamProcessor processor = new PdfContentStreamProcessor((RenderListener)renderListener);
/*  84 */     for (Map.Entry<String, ContentOperator> entry : additionalContentOperators.entrySet()) {
/*  85 */       processor.registerContentOperator(entry.getKey(), entry.getValue());
/*     */     }
/*  87 */     processor.processContent(ContentByteUtils.getContentBytesForPage(this.reader, pageNumber), resourcesDic);
/*  88 */     return renderListener;
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
/*     */   public <E extends RenderListener> E processContent(int pageNumber, E renderListener) throws IOException {
/* 104 */     return processContent(pageNumber, renderListener, new HashMap<String, ContentOperator>());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/PdfReaderContentParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */