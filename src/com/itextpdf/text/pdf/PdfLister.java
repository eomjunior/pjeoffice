/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ public class PdfLister
/*     */ {
/*     */   PrintStream out;
/*     */   
/*     */   public PdfLister(PrintStream out) {
/*  64 */     this.out = out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listAnyObject(PdfObject object) {
/*  73 */     switch (object.type()) {
/*     */       case 5:
/*  75 */         listArray((PdfArray)object);
/*     */         return;
/*     */       case 6:
/*  78 */         listDict((PdfDictionary)object);
/*     */         return;
/*     */       case 3:
/*  81 */         this.out.println("(" + object.toString() + ")");
/*     */         return;
/*     */     } 
/*  84 */     this.out.println(object.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listDict(PdfDictionary dictionary) {
/*  94 */     this.out.println("<<");
/*     */     
/*  96 */     for (PdfName key : dictionary.getKeys()) {
/*  97 */       PdfObject value = dictionary.get(key);
/*  98 */       this.out.print(key.toString());
/*  99 */       this.out.print(' ');
/* 100 */       listAnyObject(value);
/*     */     } 
/* 102 */     this.out.println(">>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listArray(PdfArray array) {
/* 111 */     this.out.println('[');
/* 112 */     for (Iterator<PdfObject> i = array.listIterator(); i.hasNext(); ) {
/* 113 */       PdfObject item = i.next();
/* 114 */       listAnyObject(item);
/*     */     } 
/* 116 */     this.out.println(']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listStream(PRStream stream, PdfReaderInstance reader) {
/*     */     try {
/* 126 */       listDict(stream);
/* 127 */       this.out.println("startstream");
/* 128 */       byte[] b = PdfReader.getStreamBytes(stream);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 138 */       int len = b.length - 1;
/* 139 */       for (int k = 0; k < len; k++) {
/* 140 */         if (b[k] == 13 && b[k + 1] != 10)
/* 141 */           b[k] = 10; 
/*     */       } 
/* 143 */       this.out.println(new String(b));
/* 144 */       this.out.println("endstream");
/* 145 */     } catch (IOException e) {
/* 146 */       System.err.println("I/O exception: " + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listPage(PdfImportedPage iPage) {
/*     */     Iterator<PdfObject> i;
/* 157 */     int pageNum = iPage.getPageNumber();
/* 158 */     PdfReaderInstance readerInst = iPage.getPdfReaderInstance();
/* 159 */     PdfReader reader = readerInst.getReader();
/*     */     
/* 161 */     PdfDictionary page = reader.getPageN(pageNum);
/* 162 */     listDict(page);
/* 163 */     PdfObject obj = PdfReader.getPdfObject(page.get(PdfName.CONTENTS));
/* 164 */     if (obj == null)
/*     */       return; 
/* 166 */     switch (obj.type) {
/*     */       case 7:
/* 168 */         listStream((PRStream)obj, readerInst);
/*     */         break;
/*     */       case 5:
/* 171 */         for (i = ((PdfArray)obj).listIterator(); i.hasNext(); ) {
/* 172 */           PdfObject o = PdfReader.getPdfObject(i.next());
/* 173 */           listStream((PRStream)o, readerInst);
/* 174 */           this.out.println("-----------");
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfLister.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */