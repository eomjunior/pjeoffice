/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.PrimitiveSink;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ByteSource
/*     */ {
/*     */   public CharSource asCharSource(Charset charset) {
/*  94 */     return new AsCharSource(charset);
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
/*     */   public abstract InputStream openStream() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream openBufferedStream() throws IOException {
/* 120 */     InputStream in = openStream();
/* 121 */     return (in instanceof BufferedInputStream) ? 
/* 122 */       in : 
/* 123 */       new BufferedInputStream(in);
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
/*     */   public ByteSource slice(long offset, long length) {
/* 136 */     return new SlicedByteSource(offset, length);
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
/*     */   public boolean isEmpty() throws IOException {
/* 153 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 154 */     if (sizeIfKnown.isPresent()) {
/* 155 */       return (((Long)sizeIfKnown.get()).longValue() == 0L);
/*     */     }
/* 157 */     Closer closer = Closer.create();
/*     */     try {
/* 159 */       InputStream in = closer.<InputStream>register(openStream());
/* 160 */       return (in.read() == -1);
/* 161 */     } catch (Throwable e) {
/* 162 */       throw closer.rethrow(e);
/*     */     } finally {
/* 164 */       closer.close();
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
/*     */   public Optional<Long> sizeIfKnown() {
/* 183 */     return Optional.absent();
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
/*     */   public long size() throws IOException {
/* 206 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 207 */     if (sizeIfKnown.isPresent()) {
/* 208 */       return ((Long)sizeIfKnown.get()).longValue();
/*     */     }
/*     */     
/* 211 */     Closer closer = Closer.create();
/*     */     try {
/* 213 */       InputStream in = closer.<InputStream>register(openStream());
/* 214 */       return countBySkipping(in);
/* 215 */     } catch (IOException iOException) {
/*     */     
/*     */     } finally {
/* 218 */       closer.close();
/*     */     } 
/*     */     
/* 221 */     closer = Closer.create();
/*     */     try {
/* 223 */       InputStream in = closer.<InputStream>register(openStream());
/* 224 */       return ByteStreams.exhaust(in);
/* 225 */     } catch (Throwable e) {
/* 226 */       throw closer.rethrow(e);
/*     */     } finally {
/* 228 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private long countBySkipping(InputStream in) throws IOException {
/* 234 */     long count = 0L;
/*     */     long skipped;
/* 236 */     while ((skipped = ByteStreams.skipUpTo(in, 2147483647L)) > 0L) {
/* 237 */       count += skipped;
/*     */     }
/* 239 */     return count;
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
/*     */   public long copyTo(OutputStream output) throws IOException {
/* 252 */     Preconditions.checkNotNull(output);
/*     */     
/* 254 */     Closer closer = Closer.create();
/*     */     try {
/* 256 */       InputStream in = closer.<InputStream>register(openStream());
/* 257 */       return ByteStreams.copy(in, output);
/* 258 */     } catch (Throwable e) {
/* 259 */       throw closer.rethrow(e);
/*     */     } finally {
/* 261 */       closer.close();
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
/*     */   public long copyTo(ByteSink sink) throws IOException {
/* 274 */     Preconditions.checkNotNull(sink);
/*     */     
/* 276 */     Closer closer = Closer.create();
/*     */     try {
/* 278 */       InputStream in = closer.<InputStream>register(openStream());
/* 279 */       OutputStream out = closer.<OutputStream>register(sink.openStream());
/* 280 */       return ByteStreams.copy(in, out);
/* 281 */     } catch (Throwable e) {
/* 282 */       throw closer.rethrow(e);
/*     */     } finally {
/* 284 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read() throws IOException {
/* 294 */     Closer closer = Closer.create();
/*     */     try {
/* 296 */       InputStream in = closer.<InputStream>register(openStream());
/* 297 */       Optional<Long> size = sizeIfKnown();
/* 298 */       return size.isPresent() ? 
/* 299 */         ByteStreams.toByteArray(in, ((Long)size.get()).longValue()) : 
/* 300 */         ByteStreams.toByteArray(in);
/* 301 */     } catch (Throwable e) {
/* 302 */       throw closer.rethrow(e);
/*     */     } finally {
/* 304 */       closer.close();
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
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 320 */     Preconditions.checkNotNull(processor);
/*     */     
/* 322 */     Closer closer = Closer.create();
/*     */     try {
/* 324 */       InputStream in = closer.<InputStream>register(openStream());
/* 325 */       return (T)ByteStreams.readBytes(in, (ByteProcessor)processor);
/* 326 */     } catch (Throwable e) {
/* 327 */       throw closer.rethrow(e);
/*     */     } finally {
/* 329 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hash(HashFunction hashFunction) throws IOException {
/* 339 */     Hasher hasher = hashFunction.newHasher();
/* 340 */     copyTo(Funnels.asOutputStream((PrimitiveSink)hasher));
/* 341 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contentEquals(ByteSource other) throws IOException {
/* 351 */     Preconditions.checkNotNull(other);
/*     */     
/* 353 */     byte[] buf1 = ByteStreams.createBuffer();
/* 354 */     byte[] buf2 = ByteStreams.createBuffer();
/*     */     
/* 356 */     Closer closer = Closer.create();
/*     */     try {
/* 358 */       InputStream in1 = closer.<InputStream>register(openStream());
/* 359 */       InputStream in2 = closer.<InputStream>register(other.openStream());
/*     */       while (true) {
/* 361 */         int read1 = ByteStreams.read(in1, buf1, 0, buf1.length);
/* 362 */         int read2 = ByteStreams.read(in2, buf2, 0, buf2.length);
/* 363 */         if (read1 != read2 || !Arrays.equals(buf1, buf2))
/* 364 */           return false; 
/* 365 */         if (read1 != buf1.length) {
/* 366 */           return true;
/*     */         }
/*     */       } 
/* 369 */     } catch (Throwable e) {
/* 370 */       throw closer.rethrow(e);
/*     */     } finally {
/* 372 */       closer.close();
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
/*     */   public static ByteSource concat(Iterable<? extends ByteSource> sources) {
/* 388 */     return new ConcatenatedByteSource(sources);
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
/*     */   public static ByteSource concat(Iterator<? extends ByteSource> sources) {
/* 410 */     return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf(sources));
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
/*     */   public static ByteSource concat(ByteSource... sources) {
/* 426 */     return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf((Object[])sources));
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
/*     */   public static ByteSource wrap(byte[] b) {
/* 441 */     return new ByteArrayByteSource(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource empty() {
/* 450 */     return EmptyByteSource.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   class AsCharSource
/*     */     extends CharSource
/*     */   {
/*     */     final Charset charset;
/*     */ 
/*     */     
/*     */     AsCharSource(Charset charset) {
/* 461 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource asByteSource(Charset charset) {
/* 466 */       if (charset.equals(this.charset)) {
/* 467 */         return ByteSource.this;
/*     */       }
/* 469 */       return super.asByteSource(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() throws IOException {
/* 474 */       return new InputStreamReader(ByteSource.this.openStream(), this.charset);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String read() throws IOException {
/* 486 */       return new String(ByteSource.this.read(), this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 491 */       return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private final class SlicedByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     final long offset;
/*     */     final long length;
/*     */     
/*     */     SlicedByteSource(long offset, long length) {
/* 502 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 503 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/* 504 */       this.offset = offset;
/* 505 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 510 */       return sliceStream(ByteSource.this.openStream());
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException {
/* 515 */       return sliceStream(ByteSource.this.openBufferedStream());
/*     */     }
/*     */     
/*     */     private InputStream sliceStream(InputStream in) throws IOException {
/* 519 */       if (this.offset > 0L) {
/*     */         long skipped;
/*     */         try {
/* 522 */           skipped = ByteStreams.skipUpTo(in, this.offset);
/* 523 */         } catch (Throwable e) {
/* 524 */           Closer closer = Closer.create();
/* 525 */           closer.register(in);
/*     */           try {
/* 527 */             throw closer.rethrow(e);
/*     */           } finally {
/* 529 */             closer.close();
/*     */           } 
/*     */         } 
/*     */         
/* 533 */         if (skipped < this.offset) {
/*     */           
/* 535 */           in.close();
/* 536 */           return new ByteArrayInputStream(new byte[0]);
/*     */         } 
/*     */       } 
/* 539 */       return ByteStreams.limit(in, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource slice(long offset, long length) {
/* 544 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 545 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/* 546 */       long maxLength = this.length - offset;
/* 547 */       return (maxLength <= 0L) ? 
/* 548 */         ByteSource.empty() : 
/* 549 */         ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 554 */       return (this.length == 0L || super.isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 559 */       Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
/* 560 */       if (optionalUnslicedSize.isPresent()) {
/* 561 */         long unslicedSize = ((Long)optionalUnslicedSize.get()).longValue();
/* 562 */         long off = Math.min(this.offset, unslicedSize);
/* 563 */         return Optional.of(Long.valueOf(Math.min(this.length, unslicedSize - off)));
/*     */       } 
/* 565 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 570 */       return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ByteArrayByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     final byte[] bytes;
/*     */     final int offset;
/*     */     final int length;
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes) {
/* 583 */       this(bytes, 0, bytes.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes, int offset, int length) {
/* 588 */       this.bytes = bytes;
/* 589 */       this.offset = offset;
/* 590 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() {
/* 595 */       return new ByteArrayInputStream(this.bytes, this.offset, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openBufferedStream() {
/* 600 */       return openStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 605 */       return (this.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() {
/* 610 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 615 */       return Optional.of(Long.valueOf(this.length));
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() {
/* 620 */       return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 627 */       processor.processBytes(this.bytes, this.offset, this.length);
/* 628 */       return processor.getResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public long copyTo(OutputStream output) throws IOException {
/* 633 */       output.write(this.bytes, this.offset, this.length);
/* 634 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash(HashFunction hashFunction) throws IOException {
/* 639 */       return hashFunction.hashBytes(this.bytes, this.offset, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource slice(long offset, long length) {
/* 644 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 645 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/*     */       
/* 647 */       offset = Math.min(offset, this.length);
/* 648 */       length = Math.min(length, this.length - offset);
/* 649 */       int newOffset = this.offset + (int)offset;
/* 650 */       return new ByteArrayByteSource(this.bytes, newOffset, (int)length);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 655 */       return "ByteSource.wrap(" + 
/* 656 */         Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyByteSource
/*     */     extends ByteArrayByteSource
/*     */   {
/* 663 */     static final EmptyByteSource INSTANCE = new EmptyByteSource();
/*     */     
/*     */     EmptyByteSource() {
/* 666 */       super(new byte[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSource asCharSource(Charset charset) {
/* 671 */       Preconditions.checkNotNull(charset);
/* 672 */       return CharSource.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() {
/* 677 */       return this.bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 682 */       return "ByteSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedByteSource
/*     */     extends ByteSource {
/*     */     final Iterable<? extends ByteSource> sources;
/*     */     
/*     */     ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
/* 691 */       this.sources = (Iterable<? extends ByteSource>)Preconditions.checkNotNull(sources);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 696 */       return new MultiInputStream(this.sources.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 701 */       for (ByteSource source : this.sources) {
/* 702 */         if (!source.isEmpty()) {
/* 703 */           return false;
/*     */         }
/*     */       } 
/* 706 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 711 */       if (!(this.sources instanceof java.util.Collection))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 717 */         return Optional.absent();
/*     */       }
/* 719 */       long result = 0L;
/* 720 */       for (ByteSource source : this.sources) {
/* 721 */         Optional<Long> sizeIfKnown = source.sizeIfKnown();
/* 722 */         if (!sizeIfKnown.isPresent()) {
/* 723 */           return Optional.absent();
/*     */         }
/* 725 */         result += ((Long)sizeIfKnown.get()).longValue();
/* 726 */         if (result < 0L)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 732 */           return Optional.of(Long.valueOf(Long.MAX_VALUE));
/*     */         }
/*     */       } 
/* 735 */       return Optional.of(Long.valueOf(result));
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 740 */       long result = 0L;
/* 741 */       for (ByteSource source : this.sources) {
/* 742 */         result += source.size();
/* 743 */         if (result < 0L)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 749 */           return Long.MAX_VALUE;
/*     */         }
/*     */       } 
/* 752 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 757 */       return "ByteSource.concat(" + this.sources + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/ByteSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */