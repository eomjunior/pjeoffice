/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.pdf.OutputStreamCounter;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
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
/*     */ public abstract class DocWriter
/*     */   implements DocListener
/*     */ {
/*     */   public static final byte NEWLINE = 10;
/*     */   public static final byte TAB = 9;
/*     */   public static final byte LT = 60;
/*     */   public static final byte SPACE = 32;
/*     */   public static final byte EQUALS = 61;
/*     */   public static final byte QUOTE = 34;
/*     */   public static final byte GT = 62;
/*     */   public static final byte FORWARD = 47;
/*     */   protected Rectangle pageSize;
/*     */   protected Document document;
/*     */   protected OutputStreamCounter os;
/*     */   protected boolean open = false;
/*     */   protected boolean pause = false;
/*     */   protected boolean closeStream = true;
/*     */   
/*     */   protected DocWriter() {}
/*     */   
/*     */   protected DocWriter(Document document, OutputStream os) {
/* 129 */     this.document = document;
/* 130 */     this.os = new OutputStreamCounter(new BufferedOutputStream(os));
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
/*     */   public boolean add(Element element) throws DocumentException {
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() {
/* 155 */     this.open = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setPageSize(Rectangle pageSize) {
/* 166 */     this.pageSize = pageSize;
/* 167 */     return true;
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
/*     */   public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
/* 183 */     return false;
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
/*     */   public boolean newPage() {
/* 195 */     if (!this.open) {
/* 196 */       return false;
/*     */     }
/* 198 */     return true;
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
/*     */   public void resetPageCount() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPageCount(int pageN) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 231 */     this.open = false;
/*     */     try {
/* 233 */       this.os.flush();
/* 234 */       if (this.closeStream) {
/* 235 */         this.os.close();
/*     */       }
/* 237 */     } catch (IOException ioe) {
/* 238 */       throw new ExceptionConverter(ioe);
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
/*     */   public static final byte[] getISOBytes(String text) {
/* 252 */     if (text == null)
/* 253 */       return null; 
/* 254 */     int len = text.length();
/* 255 */     byte[] b = new byte[len];
/* 256 */     for (int k = 0; k < len; k++)
/* 257 */       b[k] = (byte)text.charAt(k); 
/* 258 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pause() {
/* 266 */     this.pause = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPaused() {
/* 276 */     return this.pause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resume() {
/* 284 */     this.pause = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {
/*     */     try {
/* 293 */       this.os.flush();
/*     */     }
/* 295 */     catch (IOException ioe) {
/* 296 */       throw new ExceptionConverter(ioe);
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
/*     */   protected void write(String string) throws IOException {
/* 308 */     this.os.write(getISOBytes(string));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTabs(int indent) throws IOException {
/* 319 */     this.os.write(10);
/* 320 */     for (int i = 0; i < indent; i++) {
/* 321 */       this.os.write(9);
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
/*     */   protected void write(String key, String value) throws IOException {
/* 335 */     this.os.write(32);
/* 336 */     write(key);
/* 337 */     this.os.write(61);
/* 338 */     this.os.write(34);
/* 339 */     write(value);
/* 340 */     this.os.write(34);
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
/*     */   protected void writeStart(String tag) throws IOException {
/* 352 */     this.os.write(60);
/* 353 */     write(tag);
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
/*     */   protected void writeEnd(String tag) throws IOException {
/* 365 */     this.os.write(60);
/* 366 */     this.os.write(47);
/* 367 */     write(tag);
/* 368 */     this.os.write(62);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeEnd() throws IOException {
/* 378 */     this.os.write(32);
/* 379 */     this.os.write(47);
/* 380 */     this.os.write(62);
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
/*     */   protected boolean writeMarkupAttributes(Properties markup) throws IOException {
/* 392 */     if (markup == null) return false; 
/* 393 */     Iterator<Object> attributeIterator = markup.keySet().iterator();
/*     */     
/* 395 */     while (attributeIterator.hasNext()) {
/* 396 */       String name = String.valueOf(attributeIterator.next());
/* 397 */       write(name, markup.getProperty(name));
/*     */     } 
/* 399 */     markup.clear();
/* 400 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCloseStream() {
/* 408 */     return this.closeStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCloseStream(boolean closeStream) {
/* 416 */     this.closeStream = closeStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setMarginMirroring(boolean MarginMirroring) {
/* 425 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setMarginMirroringTopBottom(boolean MarginMirroring) {
/* 435 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/DocWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */