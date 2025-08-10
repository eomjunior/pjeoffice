/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Streams;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.MustBeClosed;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class CharSource
/*     */ {
/*     */   public ByteSource asByteSource(Charset charset) {
/* 104 */     return new AsByteSource(charset);
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
/*     */   public BufferedReader openBufferedStream() throws IOException {
/* 126 */     Reader reader = openStream();
/* 127 */     return (reader instanceof BufferedReader) ? 
/* 128 */       (BufferedReader)reader : 
/* 129 */       new BufferedReader(reader);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @MustBeClosed
/*     */   public Stream<String> lines() throws IOException {
/* 160 */     BufferedReader reader = openBufferedStream();
/* 161 */     return reader
/* 162 */       .lines()
/* 163 */       .onClose(() -> {
/*     */           
/*     */           try {
/*     */             reader.close();
/* 167 */           } catch (IOException e) {
/*     */             throw new UncheckedIOException(e);
/*     */           } 
/*     */         });
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
/*     */   public Optional<Long> lengthIfKnown() {
/* 188 */     return Optional.absent();
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
/*     */   
/*     */   public long length() throws IOException {
/* 211 */     Optional<Long> lengthIfKnown = lengthIfKnown();
/* 212 */     if (lengthIfKnown.isPresent()) {
/* 213 */       return ((Long)lengthIfKnown.get()).longValue();
/*     */     }
/*     */     
/* 216 */     Closer closer = Closer.create();
/*     */     try {
/* 218 */       Reader reader = closer.<Reader>register(openStream());
/* 219 */       return countBySkipping(reader);
/* 220 */     } catch (Throwable e) {
/* 221 */       throw closer.rethrow(e);
/*     */     } finally {
/* 223 */       closer.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private long countBySkipping(Reader reader) throws IOException {
/* 228 */     long count = 0L;
/*     */     long read;
/* 230 */     while ((read = reader.skip(Long.MAX_VALUE)) != 0L) {
/* 231 */       count += read;
/*     */     }
/* 233 */     return count;
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
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(Appendable appendable) throws IOException {
/* 246 */     Preconditions.checkNotNull(appendable);
/*     */     
/* 248 */     Closer closer = Closer.create();
/*     */     try {
/* 250 */       Reader reader = closer.<Reader>register(openStream());
/* 251 */       return CharStreams.copy(reader, appendable);
/* 252 */     } catch (Throwable e) {
/* 253 */       throw closer.rethrow(e);
/*     */     } finally {
/* 255 */       closer.close();
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
/*     */   public long copyTo(CharSink sink) throws IOException {
/* 268 */     Preconditions.checkNotNull(sink);
/*     */     
/* 270 */     Closer closer = Closer.create();
/*     */     try {
/* 272 */       Reader reader = closer.<Reader>register(openStream());
/* 273 */       Writer writer = closer.<Writer>register(sink.openStream());
/* 274 */       return CharStreams.copy(reader, writer);
/* 275 */     } catch (Throwable e) {
/* 276 */       throw closer.rethrow(e);
/*     */     } finally {
/* 278 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String read() throws IOException {
/* 288 */     Closer closer = Closer.create();
/*     */     try {
/* 290 */       Reader reader = closer.<Reader>register(openStream());
/* 291 */       return CharStreams.toString(reader);
/* 292 */     } catch (Throwable e) {
/* 293 */       throw closer.rethrow(e);
/*     */     } finally {
/* 295 */       closer.close();
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
/*     */   @CheckForNull
/*     */   public String readFirstLine() throws IOException {
/* 311 */     Closer closer = Closer.create();
/*     */     try {
/* 313 */       BufferedReader reader = closer.<BufferedReader>register(openBufferedStream());
/* 314 */       return reader.readLine();
/* 315 */     } catch (Throwable e) {
/* 316 */       throw closer.rethrow(e);
/*     */     } finally {
/* 318 */       closer.close();
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
/*     */   public ImmutableList<String> readLines() throws IOException {
/* 334 */     Closer closer = Closer.create();
/*     */     try {
/* 336 */       BufferedReader reader = closer.<BufferedReader>register(openBufferedStream());
/* 337 */       List<String> result = Lists.newArrayList();
/*     */       String line;
/* 339 */       while ((line = reader.readLine()) != null) {
/* 340 */         result.add(line);
/*     */       }
/* 342 */       return ImmutableList.copyOf(result);
/* 343 */     } catch (Throwable e) {
/* 344 */       throw closer.rethrow(e);
/*     */     } finally {
/* 346 */       closer.close();
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
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 367 */     Preconditions.checkNotNull(processor);
/*     */     
/* 369 */     Closer closer = Closer.create();
/*     */     try {
/* 371 */       Reader reader = closer.<Reader>register(openStream());
/* 372 */       return (T)CharStreams.readLines(reader, (LineProcessor)processor);
/* 373 */     } catch (Throwable e) {
/* 374 */       throw closer.rethrow(e);
/*     */     } finally {
/* 376 */       closer.close();
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
/*     */   public void forEachLine(Consumer<? super String> action) throws IOException {
/*     */     
/* 394 */     try { Stream<String> lines = lines();
/*     */       
/* 396 */       try { lines.forEachOrdered(action);
/* 397 */         if (lines != null) lines.close();  } catch (Throwable throwable) { if (lines != null) try { lines.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (UncheckedIOException e)
/* 398 */     { throw e.getCause(); }
/*     */   
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
/*     */   public boolean isEmpty() throws IOException {
/* 415 */     Optional<Long> lengthIfKnown = lengthIfKnown();
/* 416 */     if (lengthIfKnown.isPresent()) {
/* 417 */       return (((Long)lengthIfKnown.get()).longValue() == 0L);
/*     */     }
/* 419 */     Closer closer = Closer.create();
/*     */     try {
/* 421 */       Reader reader = closer.<Reader>register(openStream());
/* 422 */       return (reader.read() == -1);
/* 423 */     } catch (Throwable e) {
/* 424 */       throw closer.rethrow(e);
/*     */     } finally {
/* 426 */       closer.close();
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
/*     */   public static CharSource concat(Iterable<? extends CharSource> sources) {
/* 442 */     return new ConcatenatedCharSource(sources);
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
/*     */   public static CharSource concat(Iterator<? extends CharSource> sources) {
/* 464 */     return concat((Iterable<? extends CharSource>)ImmutableList.copyOf(sources));
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
/*     */   public static CharSource concat(CharSource... sources) {
/* 480 */     return concat((Iterable<? extends CharSource>)ImmutableList.copyOf((Object[])sources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource wrap(CharSequence charSequence) {
/* 491 */     return (charSequence instanceof String) ? 
/* 492 */       new StringCharSource((String)charSequence) : 
/* 493 */       new CharSequenceCharSource(charSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource empty() {
/* 502 */     return EmptyCharSource.INSTANCE;
/*     */   }
/*     */   
/*     */   public abstract Reader openStream() throws IOException;
/*     */   
/*     */   private final class AsByteSource extends ByteSource {
/*     */     final Charset charset;
/*     */     
/*     */     AsByteSource(Charset charset) {
/* 511 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSource asCharSource(Charset charset) {
/* 516 */       if (charset.equals(this.charset)) {
/* 517 */         return CharSource.this;
/*     */       }
/* 519 */       return super.asCharSource(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 524 */       return new ReaderInputStream(CharSource.this.openStream(), this.charset, 8192);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 529 */       return CharSource.this.toString() + ".asByteSource(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CharSequenceCharSource
/*     */     extends CharSource {
/* 535 */     private static final Splitter LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
/*     */     
/*     */     protected final CharSequence seq;
/*     */     
/*     */     protected CharSequenceCharSource(CharSequence seq) {
/* 540 */       this.seq = (CharSequence)Preconditions.checkNotNull(seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() {
/* 545 */       return new CharSequenceReader(this.seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public String read() {
/* 550 */       return this.seq.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 555 */       return (this.seq.length() == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public long length() {
/* 560 */       return this.seq.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> lengthIfKnown() {
/* 565 */       return Optional.of(Long.valueOf(this.seq.length()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Iterator<String> linesIterator() {
/* 573 */       return (Iterator<String>)new AbstractIterator<String>() {
/* 574 */           Iterator<String> lines = CharSource.CharSequenceCharSource.LINE_SPLITTER.split(CharSource.CharSequenceCharSource.this.seq).iterator();
/*     */ 
/*     */           
/*     */           @CheckForNull
/*     */           protected String computeNext() {
/* 579 */             if (this.lines.hasNext()) {
/* 580 */               String next = this.lines.next();
/*     */               
/* 582 */               if (this.lines.hasNext() || !next.isEmpty()) {
/* 583 */                 return next;
/*     */               }
/*     */             } 
/* 586 */             return (String)endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<String> lines() {
/* 593 */       return Streams.stream(linesIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public String readFirstLine() {
/* 599 */       Iterator<String> lines = linesIterator();
/* 600 */       return lines.hasNext() ? lines.next() : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<String> readLines() {
/* 605 */       return ImmutableList.copyOf(linesIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 611 */       Iterator<String> lines = linesIterator(); do {  }
/* 612 */       while (lines.hasNext() && 
/* 613 */         processor.processLine(lines.next()));
/*     */ 
/*     */ 
/*     */       
/* 617 */       return processor.getResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 622 */       return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
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
/*     */   private static class StringCharSource
/*     */     extends CharSequenceCharSource
/*     */   {
/*     */     protected StringCharSource(String seq) {
/* 643 */       super(seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() {
/* 648 */       return new StringReader((String)this.seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public long copyTo(Appendable appendable) throws IOException {
/* 653 */       appendable.append(this.seq);
/* 654 */       return this.seq.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public long copyTo(CharSink sink) throws IOException {
/* 659 */       Preconditions.checkNotNull(sink);
/* 660 */       Closer closer = Closer.create();
/*     */       try {
/* 662 */         Writer writer = closer.<Writer>register(sink.openStream());
/* 663 */         writer.write((String)this.seq);
/* 664 */         return this.seq.length();
/* 665 */       } catch (Throwable e) {
/* 666 */         throw closer.rethrow(e);
/*     */       } finally {
/* 668 */         closer.close();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyCharSource
/*     */     extends StringCharSource {
/* 675 */     private static final EmptyCharSource INSTANCE = new EmptyCharSource();
/*     */     
/*     */     private EmptyCharSource() {
/* 678 */       super("");
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 683 */       return "CharSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedCharSource
/*     */     extends CharSource {
/*     */     private final Iterable<? extends CharSource> sources;
/*     */     
/*     */     ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
/* 692 */       this.sources = (Iterable<? extends CharSource>)Preconditions.checkNotNull(sources);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() throws IOException {
/* 697 */       return new MultiReader(this.sources.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 702 */       for (CharSource source : this.sources) {
/* 703 */         if (!source.isEmpty()) {
/* 704 */           return false;
/*     */         }
/*     */       } 
/* 707 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> lengthIfKnown() {
/* 712 */       long result = 0L;
/* 713 */       for (CharSource source : this.sources) {
/* 714 */         Optional<Long> lengthIfKnown = source.lengthIfKnown();
/* 715 */         if (!lengthIfKnown.isPresent()) {
/* 716 */           return Optional.absent();
/*     */         }
/* 718 */         result += ((Long)lengthIfKnown.get()).longValue();
/*     */       } 
/* 720 */       return Optional.of(Long.valueOf(result));
/*     */     }
/*     */ 
/*     */     
/*     */     public long length() throws IOException {
/* 725 */       long result = 0L;
/* 726 */       for (CharSource source : this.sources) {
/* 727 */         result += source.length();
/*     */       }
/* 729 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 734 */       return "CharSource.concat(" + this.sources + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/CharSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */