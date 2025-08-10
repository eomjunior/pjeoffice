/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Beta
/*     */ public final class Funnels
/*     */ {
/*     */   public static Funnel<byte[]> byteArrayFunnel() {
/*  40 */     return ByteArrayFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum ByteArrayFunnel implements Funnel<byte[]> {
/*  44 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void funnel(byte[] from, PrimitiveSink into) {
/*  48 */       into.putBytes(from);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  53 */       return "Funnels.byteArrayFunnel()";
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
/*     */   public static Funnel<CharSequence> unencodedCharsFunnel() {
/*  65 */     return UnencodedCharsFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum UnencodedCharsFunnel implements Funnel<CharSequence> {
/*  69 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void funnel(CharSequence from, PrimitiveSink into) {
/*  73 */       into.putUnencodedChars(from);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  78 */       return "Funnels.unencodedCharsFunnel()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Funnel<CharSequence> stringFunnel(Charset charset) {
/*  89 */     return new StringCharsetFunnel(charset);
/*     */   }
/*     */   
/*     */   private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
/*     */     private final Charset charset;
/*     */     
/*     */     StringCharsetFunnel(Charset charset) {
/*  96 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public void funnel(CharSequence from, PrimitiveSink into) {
/* 101 */       into.putString(from, this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 106 */       return "Funnels.stringFunnel(" + this.charset.name() + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object o) {
/* 111 */       if (o instanceof StringCharsetFunnel) {
/* 112 */         StringCharsetFunnel funnel = (StringCharsetFunnel)o;
/* 113 */         return this.charset.equals(funnel.charset);
/*     */       } 
/* 115 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 120 */       return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 124 */       return new SerializedForm(this.charset);
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 128 */       throw new InvalidObjectException("Use SerializedForm");
/*     */     }
/*     */     
/*     */     private static class SerializedForm implements Serializable {
/*     */       private final String charsetCanonicalName;
/*     */       
/*     */       SerializedForm(Charset charset) {
/* 135 */         this.charsetCanonicalName = charset.name();
/*     */       }
/*     */       private static final long serialVersionUID = 0L;
/*     */       private Object readResolve() {
/* 139 */         return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
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
/*     */   public static Funnel<Integer> integerFunnel() {
/* 152 */     return IntegerFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum IntegerFunnel implements Funnel<Integer> {
/* 156 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void funnel(Integer from, PrimitiveSink into) {
/* 160 */       into.putInt(from.intValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 165 */       return "Funnels.integerFunnel()";
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
/*     */   public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> elementFunnel) {
/* 177 */     return new SequentialFunnel<>(elementFunnel);
/*     */   }
/*     */   
/*     */   private static class SequentialFunnel<E>
/*     */     implements Funnel<Iterable<? extends E>>, Serializable {
/*     */     private final Funnel<E> elementFunnel;
/*     */     
/*     */     SequentialFunnel(Funnel<E> elementFunnel) {
/* 185 */       this.elementFunnel = (Funnel<E>)Preconditions.checkNotNull(elementFunnel);
/*     */     }
/*     */ 
/*     */     
/*     */     public void funnel(Iterable<? extends E> from, PrimitiveSink into) {
/* 190 */       for (E e : from) {
/* 191 */         this.elementFunnel.funnel(e, into);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 197 */       return "Funnels.sequentialFunnel(" + this.elementFunnel + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object o) {
/* 202 */       if (o instanceof SequentialFunnel) {
/* 203 */         SequentialFunnel<?> funnel = (SequentialFunnel)o;
/* 204 */         return this.elementFunnel.equals(funnel.elementFunnel);
/*     */       } 
/* 206 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 211 */       return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Funnel<Long> longFunnel() {
/* 221 */     return LongFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LongFunnel implements Funnel<Long> {
/* 225 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void funnel(Long from, PrimitiveSink into) {
/* 229 */       into.putLong(from.longValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 234 */       return "Funnels.longFunnel()";
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
/*     */   public static OutputStream asOutputStream(PrimitiveSink sink) {
/* 249 */     return new SinkAsStream(sink);
/*     */   }
/*     */   
/*     */   private static class SinkAsStream extends OutputStream {
/*     */     final PrimitiveSink sink;
/*     */     
/*     */     SinkAsStream(PrimitiveSink sink) {
/* 256 */       this.sink = (PrimitiveSink)Preconditions.checkNotNull(sink);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/* 261 */       this.sink.putByte((byte)b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] bytes) {
/* 266 */       this.sink.putBytes(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] bytes, int off, int len) {
/* 271 */       this.sink.putBytes(bytes, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 276 */       return "Funnels.asOutputStream(" + this.sink + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Funnels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */