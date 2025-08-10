/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ class PdfReaderInstance
/*     */ {
/*  57 */   static final PdfLiteral IDENTITYMATRIX = new PdfLiteral("[1 0 0 1 0 0]");
/*  58 */   static final PdfNumber ONE = new PdfNumber(1);
/*     */   int[] myXref;
/*     */   PdfReader reader;
/*     */   RandomAccessFileOrArray file;
/*  62 */   HashMap<Integer, PdfImportedPage> importedPages = new HashMap<Integer, PdfImportedPage>();
/*     */   PdfWriter writer;
/*  64 */   HashSet<Integer> visited = new HashSet<Integer>();
/*  65 */   ArrayList<Integer> nextRound = new ArrayList<Integer>();
/*     */   
/*     */   PdfReaderInstance(PdfReader reader, PdfWriter writer) {
/*  68 */     this.reader = reader;
/*  69 */     this.writer = writer;
/*  70 */     this.file = reader.getSafeFile();
/*  71 */     this.myXref = new int[reader.getXrefSize()];
/*     */   }
/*     */   
/*     */   PdfReader getReader() {
/*  75 */     return this.reader;
/*     */   }
/*     */   
/*     */   PdfImportedPage getImportedPage(int pageNumber) {
/*  79 */     if (!this.reader.isOpenedWithFullPermissions())
/*  80 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0])); 
/*  81 */     if (pageNumber < 1 || pageNumber > this.reader.getNumberOfPages())
/*  82 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", pageNumber)); 
/*  83 */     Integer i = Integer.valueOf(pageNumber);
/*  84 */     PdfImportedPage pageT = this.importedPages.get(i);
/*  85 */     if (pageT == null) {
/*  86 */       pageT = new PdfImportedPage(this, this.writer, pageNumber);
/*  87 */       this.importedPages.put(i, pageT);
/*     */     } 
/*  89 */     return pageT;
/*     */   }
/*     */   
/*     */   int getNewObjectNumber(int number, int generation) {
/*  93 */     if (this.myXref[number] == 0) {
/*  94 */       this.myXref[number] = this.writer.getIndirectReferenceNumber();
/*  95 */       this.nextRound.add(Integer.valueOf(number));
/*     */     } 
/*  97 */     return this.myXref[number];
/*     */   }
/*     */   
/*     */   RandomAccessFileOrArray getReaderFile() {
/* 101 */     return this.file;
/*     */   }
/*     */   
/*     */   PdfObject getResources(int pageNumber) {
/* 105 */     PdfObject obj = PdfReader.getPdfObjectRelease(this.reader.getPageNRelease(pageNumber).get(PdfName.RESOURCES));
/* 106 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfStream getFormXObject(int pageNumber, int compressionLevel) throws IOException {
/*     */     PRStream stream;
/* 117 */     PdfDictionary page = this.reader.getPageNRelease(pageNumber);
/* 118 */     PdfObject contents = PdfReader.getPdfObjectRelease(page.get(PdfName.CONTENTS));
/* 119 */     PdfDictionary dic = new PdfDictionary();
/* 120 */     byte[] bout = null;
/* 121 */     if (contents != null) {
/* 122 */       if (contents.isStream()) {
/* 123 */         dic.putAll((PRStream)contents);
/*     */       } else {
/* 125 */         bout = this.reader.getPageContent(pageNumber, this.file);
/*     */       } 
/*     */     } else {
/* 128 */       bout = new byte[0];
/* 129 */     }  dic.put(PdfName.RESOURCES, PdfReader.getPdfObjectRelease(page.get(PdfName.RESOURCES)));
/* 130 */     dic.put(PdfName.TYPE, PdfName.XOBJECT);
/* 131 */     dic.put(PdfName.SUBTYPE, PdfName.FORM);
/* 132 */     PdfImportedPage impPage = this.importedPages.get(Integer.valueOf(pageNumber));
/* 133 */     dic.put(PdfName.BBOX, new PdfRectangle(impPage.getBoundingBox()));
/* 134 */     PdfArray matrix = impPage.getMatrix();
/* 135 */     if (matrix == null) {
/* 136 */       dic.put(PdfName.MATRIX, IDENTITYMATRIX);
/*     */     } else {
/* 138 */       dic.put(PdfName.MATRIX, matrix);
/* 139 */     }  dic.put(PdfName.FORMTYPE, ONE);
/*     */     
/* 141 */     if (bout == null) {
/* 142 */       stream = new PRStream((PRStream)contents, dic);
/*     */     } else {
/*     */       
/* 145 */       stream = new PRStream(this.reader, bout, compressionLevel);
/* 146 */       stream.putAll(dic);
/*     */     } 
/* 148 */     return stream;
/*     */   }
/*     */   
/*     */   void writeAllVisited() throws IOException {
/* 152 */     while (!this.nextRound.isEmpty()) {
/* 153 */       ArrayList<Integer> vec = this.nextRound;
/* 154 */       this.nextRound = new ArrayList<Integer>();
/* 155 */       for (int k = 0; k < vec.size(); k++) {
/* 156 */         Integer i = vec.get(k);
/* 157 */         if (!this.visited.contains(i)) {
/* 158 */           this.visited.add(i);
/* 159 */           int n = i.intValue();
/* 160 */           this.writer.addToBody(this.reader.getPdfObjectRelease(n), this.myXref[n]);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeAllPages() throws IOException {
/*     */     try {
/* 168 */       this.file.reOpen();
/* 169 */       for (PdfImportedPage element : this.importedPages.values()) {
/* 170 */         PdfImportedPage ip = element;
/* 171 */         if (ip.isToCopy()) {
/* 172 */           this.writer.addToBody(ip.getFormXObject(this.writer.getCompressionLevel()), ip.getIndirectReference());
/* 173 */           ip.setCopied();
/*     */         } 
/*     */       } 
/* 176 */       writeAllVisited();
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 182 */         this.file.close();
/*     */       }
/* 184 */       catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfReaderInstance.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */