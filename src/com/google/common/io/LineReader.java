/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.nio.CharBuffer;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Queue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ public final class LineReader
/*    */ {
/*    */   private final Readable readable;
/*    */   @CheckForNull
/*    */   private final Reader reader;
/* 44 */   private final CharBuffer cbuf = CharStreams.createBuffer();
/* 45 */   private final char[] buf = this.cbuf.array();
/*    */   
/* 47 */   private final Queue<String> lines = new ArrayDeque<>();
/* 48 */   private final LineBuffer lineBuf = new LineBuffer()
/*    */     {
/*    */       protected void handleLine(String line, String end)
/*    */       {
/* 52 */         LineReader.this.lines.add(line);
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public LineReader(Readable readable) {
/* 58 */     this.readable = (Readable)Preconditions.checkNotNull(readable);
/* 59 */     this.reader = (readable instanceof Reader) ? (Reader)readable : null;
/*    */   }
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
/*    */   @CheckForNull
/*    */   @CanIgnoreReturnValue
/*    */   public String readLine() throws IOException {
/* 74 */     while (this.lines.peek() == null) {
/* 75 */       Java8Compatibility.clear(this.cbuf);
/*    */ 
/*    */       
/* 78 */       int read = (this.reader != null) ? this.reader.read(this.buf, 0, this.buf.length) : this.readable.read(this.cbuf);
/* 79 */       if (read == -1) {
/* 80 */         this.lineBuf.finish();
/*    */         break;
/*    */       } 
/* 83 */       this.lineBuf.add(this.buf, 0, read);
/*    */     } 
/* 85 */     return this.lines.poll();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/LineReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */