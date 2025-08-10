/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ abstract class LineBuffer
/*     */ {
/*  38 */   private StringBuilder line = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sawReturn;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void add(char[] cbuf, int off, int len) throws IOException {
/*  53 */     int pos = off;
/*  54 */     if (this.sawReturn && len > 0)
/*     */     {
/*  56 */       if (finishLine((cbuf[pos] == '\n'))) {
/*  57 */         pos++;
/*     */       }
/*     */     }
/*     */     
/*  61 */     int start = pos;
/*  62 */     for (int end = off + len; pos < end; pos++) {
/*  63 */       switch (cbuf[pos]) {
/*     */         case '\r':
/*  65 */           this.line.append(cbuf, start, pos - start);
/*  66 */           this.sawReturn = true;
/*  67 */           if (pos + 1 < end && 
/*  68 */             finishLine((cbuf[pos + 1] == '\n'))) {
/*  69 */             pos++;
/*     */           }
/*     */           
/*  72 */           start = pos + 1;
/*     */           break;
/*     */         
/*     */         case '\n':
/*  76 */           this.line.append(cbuf, start, pos - start);
/*  77 */           finishLine(true);
/*  78 */           start = pos + 1;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/*  85 */     this.line.append(cbuf, start, off + len - start);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private boolean finishLine(boolean sawNewline) throws IOException {
/*  91 */     String separator = this.sawReturn ? (sawNewline ? "\r\n" : "\r") : (sawNewline ? "\n" : "");
/*  92 */     handleLine(this.line.toString(), separator);
/*  93 */     this.line = new StringBuilder();
/*  94 */     this.sawReturn = false;
/*  95 */     return sawNewline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finish() throws IOException {
/* 105 */     if (this.sawReturn || this.line.length() > 0)
/* 106 */       finishLine(false); 
/*     */   }
/*     */   
/*     */   protected abstract void handleLine(String paramString1, String paramString2) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/LineBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */