/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Iterator;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class CharSink
/*     */ {
/*     */   public abstract Writer openStream() throws IOException;
/*     */   
/*     */   public Writer openBufferedStream() throws IOException {
/*  83 */     Writer writer = openStream();
/*  84 */     return (writer instanceof BufferedWriter) ? 
/*  85 */       writer : 
/*  86 */       new BufferedWriter(writer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(CharSequence charSequence) throws IOException {
/*  95 */     Preconditions.checkNotNull(charSequence);
/*     */     
/*  97 */     Closer closer = Closer.create();
/*     */     try {
/*  99 */       Writer out = closer.<Writer>register(openStream());
/* 100 */       out.append(charSequence);
/* 101 */       out.flush();
/* 102 */     } catch (Throwable e) {
/* 103 */       throw closer.rethrow(e);
/*     */     } finally {
/* 105 */       closer.close();
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
/*     */   public void writeLines(Iterable<? extends CharSequence> lines) throws IOException {
/* 117 */     writeLines(lines, System.getProperty("line.separator"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws IOException {
/* 128 */     writeLines(lines.iterator(), lineSeparator);
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
/*     */   public void writeLines(Stream<? extends CharSequence> lines) throws IOException {
/* 140 */     writeLines(lines, System.getProperty("line.separator"));
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
/*     */   public void writeLines(Stream<? extends CharSequence> lines, String lineSeparator) throws IOException {
/* 152 */     writeLines(lines.iterator(), lineSeparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeLines(Iterator<? extends CharSequence> lines, String lineSeparator) throws IOException {
/* 157 */     Preconditions.checkNotNull(lineSeparator);
/*     */     
/* 159 */     Writer out = openBufferedStream(); try {
/* 160 */       while (lines.hasNext()) {
/* 161 */         out.append(lines.next()).append(lineSeparator);
/*     */       }
/* 163 */       if (out != null) out.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (out != null)
/*     */         try {
/*     */           out.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   @CanIgnoreReturnValue
/*     */   public long writeFrom(Readable readable) throws IOException {
/* 176 */     Preconditions.checkNotNull(readable);
/*     */     
/* 178 */     Closer closer = Closer.create();
/*     */     try {
/* 180 */       Writer out = closer.<Writer>register(openStream());
/* 181 */       long written = CharStreams.copy(readable, out);
/* 182 */       out.flush();
/* 183 */       return written;
/* 184 */     } catch (Throwable e) {
/* 185 */       throw closer.rethrow(e);
/*     */     } finally {
/* 187 */       closer.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/CharSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */