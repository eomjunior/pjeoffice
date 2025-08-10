/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ByteSink
/*     */ {
/*     */   public CharSink asCharSink(Charset charset) {
/*  62 */     return new AsCharSink(charset);
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
/*     */   public abstract OutputStream openStream() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream openBufferedStream() throws IOException {
/*  88 */     OutputStream out = openStream();
/*  89 */     return (out instanceof BufferedOutputStream) ? 
/*  90 */       out : 
/*  91 */       new BufferedOutputStream(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] bytes) throws IOException {
/* 100 */     Preconditions.checkNotNull(bytes);
/*     */     
/* 102 */     Closer closer = Closer.create();
/*     */     try {
/* 104 */       OutputStream out = closer.<OutputStream>register(openStream());
/* 105 */       out.write(bytes);
/* 106 */       out.flush();
/* 107 */     } catch (Throwable e) {
/* 108 */       throw closer.rethrow(e);
/*     */     } finally {
/* 110 */       closer.close();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long writeFrom(InputStream input) throws IOException {
/* 123 */     Preconditions.checkNotNull(input);
/*     */     
/* 125 */     Closer closer = Closer.create();
/*     */     try {
/* 127 */       OutputStream out = closer.<OutputStream>register(openStream());
/* 128 */       long written = ByteStreams.copy(input, out);
/* 129 */       out.flush();
/* 130 */       return written;
/* 131 */     } catch (Throwable e) {
/* 132 */       throw closer.rethrow(e);
/*     */     } finally {
/* 134 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class AsCharSink
/*     */     extends CharSink
/*     */   {
/*     */     private final Charset charset;
/*     */ 
/*     */     
/*     */     private AsCharSink(Charset charset) {
/* 147 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer openStream() throws IOException {
/* 152 */       return new OutputStreamWriter(ByteSink.this.openStream(), this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 157 */       return ByteSink.this.toString() + ".asCharSink(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/ByteSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */