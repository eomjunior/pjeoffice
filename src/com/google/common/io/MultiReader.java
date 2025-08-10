/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.util.Iterator;
/*    */ import javax.annotation.CheckForNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ class MultiReader
/*    */   extends Reader
/*    */ {
/*    */   private final Iterator<? extends CharSource> it;
/*    */   @CheckForNull
/*    */   private Reader current;
/*    */   
/*    */   MultiReader(Iterator<? extends CharSource> readers) throws IOException {
/* 41 */     this.it = readers;
/* 42 */     advance();
/*    */   }
/*    */ 
/*    */   
/*    */   private void advance() throws IOException {
/* 47 */     close();
/* 48 */     if (this.it.hasNext()) {
/* 49 */       this.current = ((CharSource)this.it.next()).openStream();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 55 */     Preconditions.checkNotNull(cbuf);
/* 56 */     if (this.current == null) {
/* 57 */       return -1;
/*    */     }
/* 59 */     int result = this.current.read(cbuf, off, len);
/* 60 */     if (result == -1) {
/* 61 */       advance();
/* 62 */       return read(cbuf, off, len);
/*    */     } 
/* 64 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public long skip(long n) throws IOException {
/* 69 */     Preconditions.checkArgument((n >= 0L), "n is negative");
/* 70 */     if (n > 0L) {
/* 71 */       while (this.current != null) {
/* 72 */         long result = this.current.skip(n);
/* 73 */         if (result > 0L) {
/* 74 */           return result;
/*    */         }
/* 76 */         advance();
/*    */       } 
/*    */     }
/* 79 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean ready() throws IOException {
/* 84 */     return (this.current != null && this.current.ready());
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 89 */     if (this.current != null)
/*    */       try {
/* 91 */         this.current.close();
/*    */       } finally {
/* 93 */         this.current = null;
/*    */       }  
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/MultiReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */