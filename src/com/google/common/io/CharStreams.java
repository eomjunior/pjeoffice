/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class CharStreams
/*     */ {
/*     */   private static final int DEFAULT_BUF_SIZE = 2048;
/*     */   
/*     */   static CharBuffer createBuffer() {
/*  56 */     return CharBuffer.allocate(2048);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(Readable from, Appendable to) throws IOException {
/*  74 */     if (from instanceof Reader) {
/*     */       
/*  76 */       if (to instanceof StringBuilder) {
/*  77 */         return copyReaderToBuilder((Reader)from, (StringBuilder)to);
/*     */       }
/*  79 */       return copyReaderToWriter((Reader)from, asWriter(to));
/*     */     } 
/*     */ 
/*     */     
/*  83 */     Preconditions.checkNotNull(from);
/*  84 */     Preconditions.checkNotNull(to);
/*  85 */     long total = 0L;
/*  86 */     CharBuffer buf = createBuffer();
/*  87 */     while (from.read(buf) != -1) {
/*  88 */       Java8Compatibility.flip(buf);
/*  89 */       to.append(buf);
/*  90 */       total += buf.remaining();
/*  91 */       Java8Compatibility.clear(buf);
/*     */     } 
/*  93 */     return total;
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
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long copyReaderToBuilder(Reader from, StringBuilder to) throws IOException {
/* 116 */     Preconditions.checkNotNull(from);
/* 117 */     Preconditions.checkNotNull(to);
/* 118 */     char[] buf = new char[2048];
/*     */     
/* 120 */     long total = 0L; int nRead;
/* 121 */     while ((nRead = from.read(buf)) != -1) {
/* 122 */       to.append(buf, 0, nRead);
/* 123 */       total += nRead;
/*     */     } 
/* 125 */     return total;
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
/*     */   @CanIgnoreReturnValue
/*     */   static long copyReaderToWriter(Reader from, Writer to) throws IOException {
/* 144 */     Preconditions.checkNotNull(from);
/* 145 */     Preconditions.checkNotNull(to);
/* 146 */     char[] buf = new char[2048];
/*     */     
/* 148 */     long total = 0L; int nRead;
/* 149 */     while ((nRead = from.read(buf)) != -1) {
/* 150 */       to.write(buf, 0, nRead);
/* 151 */       total += nRead;
/*     */     } 
/* 153 */     return total;
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
/*     */   public static String toString(Readable r) throws IOException {
/* 165 */     return toStringBuilder(r).toString();
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
/*     */   private static StringBuilder toStringBuilder(Readable r) throws IOException {
/* 177 */     StringBuilder sb = new StringBuilder();
/* 178 */     if (r instanceof Reader) {
/* 179 */       copyReaderToBuilder((Reader)r, sb);
/*     */     } else {
/* 181 */       copy(r, sb);
/*     */     } 
/* 183 */     return sb;
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
/*     */   public static List<String> readLines(Readable r) throws IOException {
/* 198 */     List<String> result = new ArrayList<>();
/* 199 */     LineReader lineReader = new LineReader(r);
/*     */     String line;
/* 201 */     while ((line = lineReader.readLine()) != null) {
/* 202 */       result.add(line);
/*     */     }
/* 204 */     return result;
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
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(Readable readable, LineProcessor<T> processor) throws IOException {
/* 220 */     Preconditions.checkNotNull(readable);
/* 221 */     Preconditions.checkNotNull(processor);
/*     */     
/* 223 */     LineReader lineReader = new LineReader(readable); String line; do {
/*     */     
/* 225 */     } while ((line = lineReader.readLine()) != null && 
/* 226 */       processor.processLine(line));
/*     */ 
/*     */ 
/*     */     
/* 230 */     return processor.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static long exhaust(Readable readable) throws IOException {
/* 241 */     long total = 0L;
/*     */     
/* 243 */     CharBuffer buf = createBuffer(); long read;
/* 244 */     while ((read = readable.read(buf)) != -1L) {
/* 245 */       total += read;
/* 246 */       Java8Compatibility.clear(buf);
/*     */     } 
/* 248 */     return total;
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
/*     */   public static void skipFully(Reader reader, long n) throws IOException {
/* 261 */     Preconditions.checkNotNull(reader);
/* 262 */     while (n > 0L) {
/* 263 */       long amt = reader.skip(n);
/* 264 */       if (amt == 0L) {
/* 265 */         throw new EOFException();
/*     */       }
/* 267 */       n -= amt;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Writer nullWriter() {
/* 277 */     return NullWriter.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class NullWriter
/*     */     extends Writer {
/* 282 */     private static final NullWriter INSTANCE = new NullWriter();
/*     */ 
/*     */     
/*     */     public void write(int c) {}
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf) {
/* 289 */       Preconditions.checkNotNull(cbuf);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf, int off, int len) {
/* 294 */       Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str) {
/* 299 */       Preconditions.checkNotNull(str);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str, int off, int len) {
/* 304 */       Preconditions.checkPositionIndexes(off, off + len, str.length());
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(@CheckForNull CharSequence csq) {
/* 309 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(@CheckForNull CharSequence csq, int start, int end) {
/* 314 */       Preconditions.checkPositionIndexes(start, end, (csq == null) ? "null".length() : csq.length());
/* 315 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(char c) {
/* 320 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() {}
/*     */ 
/*     */     
/*     */     public void close() {}
/*     */ 
/*     */     
/*     */     public String toString() {
/* 331 */       return "CharStreams.nullWriter()";
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
/*     */   public static Writer asWriter(Appendable target) {
/* 344 */     if (target instanceof Writer) {
/* 345 */       return (Writer)target;
/*     */     }
/* 347 */     return new AppendableWriter(target);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/CharStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */