/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.QuietWriter;
/*     */ import org.apache.log4j.spi.ErrorHandler;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WriterAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   protected boolean immediateFlush = true;
/*     */   protected String encoding;
/*     */   protected QuietWriter qw;
/*     */   
/*     */   public WriterAppender() {}
/*     */   
/*     */   public WriterAppender(Layout layout, OutputStream os) {
/*  84 */     this(layout, new OutputStreamWriter(os));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriterAppender(Layout layout, Writer writer) {
/*  95 */     this.layout = layout;
/*  96 */     setWriter(writer);
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
/*     */   public void setImmediateFlush(boolean value) {
/* 114 */     this.immediateFlush = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getImmediateFlush() {
/* 121 */     return this.immediateFlush;
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
/*     */   public void activateOptions() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LoggingEvent event) {
/* 153 */     if (!checkEntryConditions()) {
/*     */       return;
/*     */     }
/* 156 */     subAppend(event);
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
/*     */   protected boolean checkEntryConditions() {
/* 168 */     if (this.closed) {
/* 169 */       LogLog.warn("Not allowed to write to a closed appender.");
/* 170 */       return false;
/*     */     } 
/*     */     
/* 173 */     if (this.qw == null) {
/* 174 */       this.errorHandler.error("No output stream or file set for the appender named [" + this.name + "].");
/* 175 */       return false;
/*     */     } 
/*     */     
/* 178 */     if (this.layout == null) {
/* 179 */       this.errorHandler.error("No layout set for the appender named [" + this.name + "].");
/* 180 */       return false;
/*     */     } 
/* 182 */     return true;
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
/*     */   public synchronized void close() {
/* 195 */     if (this.closed)
/*     */       return; 
/* 197 */     this.closed = true;
/* 198 */     writeFooter();
/* 199 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeWriter() {
/* 206 */     if (this.qw != null) {
/*     */       try {
/* 208 */         this.qw.close();
/* 209 */       } catch (IOException e) {
/* 210 */         if (e instanceof java.io.InterruptedIOException) {
/* 211 */           Thread.currentThread().interrupt();
/*     */         }
/*     */ 
/*     */         
/* 215 */         LogLog.error("Could not close " + this.qw, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStreamWriter createWriter(OutputStream os) {
/* 227 */     OutputStreamWriter retval = null;
/*     */     
/* 229 */     String enc = getEncoding();
/* 230 */     if (enc != null) {
/*     */       try {
/* 232 */         retval = new OutputStreamWriter(os, enc);
/* 233 */       } catch (IOException e) {
/* 234 */         if (e instanceof java.io.InterruptedIOException) {
/* 235 */           Thread.currentThread().interrupt();
/*     */         }
/* 237 */         LogLog.warn("Error initializing output writer.");
/* 238 */         LogLog.warn("Unsupported encoding?");
/*     */       } 
/*     */     }
/* 241 */     if (retval == null) {
/* 242 */       retval = new OutputStreamWriter(os);
/*     */     }
/* 244 */     return retval;
/*     */   }
/*     */   
/*     */   public String getEncoding() {
/* 248 */     return this.encoding;
/*     */   }
/*     */   
/*     */   public void setEncoding(String value) {
/* 252 */     this.encoding = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setErrorHandler(ErrorHandler eh) {
/* 260 */     if (eh == null) {
/* 261 */       LogLog.warn("You have tried to set a null error-handler.");
/*     */     } else {
/* 263 */       this.errorHandler = eh;
/* 264 */       if (this.qw != null) {
/* 265 */         this.qw.setErrorHandler(eh);
/*     */       }
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
/*     */   public synchronized void setWriter(Writer writer) {
/* 287 */     reset();
/* 288 */     this.qw = new QuietWriter(writer, this.errorHandler);
/*     */     
/* 290 */     writeHeader();
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
/*     */   protected void subAppend(LoggingEvent event) {
/* 303 */     this.qw.write(this.layout.format(event));
/*     */     
/* 305 */     if (this.layout.ignoresThrowable()) {
/* 306 */       String[] s = event.getThrowableStrRep();
/* 307 */       if (s != null) {
/* 308 */         int len = s.length;
/* 309 */         for (int i = 0; i < len; i++) {
/* 310 */           this.qw.write(s[i]);
/* 311 */           this.qw.write(Layout.LINE_SEP);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 316 */     if (shouldFlush(event)) {
/* 317 */       this.qw.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 326 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reset() {
/* 335 */     closeWriter();
/* 336 */     this.qw = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeFooter() {
/* 345 */     if (this.layout != null) {
/* 346 */       String f = this.layout.getFooter();
/* 347 */       if (f != null && this.qw != null) {
/* 348 */         this.qw.write(f);
/* 349 */         this.qw.flush();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeader() {
/* 359 */     if (this.layout != null) {
/* 360 */       String h = this.layout.getHeader();
/* 361 */       if (h != null && this.qw != null) {
/* 362 */         this.qw.write(h);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldFlush(LoggingEvent event) {
/* 372 */     return this.immediateFlush;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/WriterAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */