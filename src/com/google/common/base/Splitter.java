/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Splitter
/*     */ {
/*     */   private final CharMatcher trimmer;
/*     */   private final boolean omitEmptyStrings;
/*     */   private final Strategy strategy;
/*     */   private final int limit;
/*     */   
/*     */   private Splitter(Strategy strategy) {
/* 111 */     this(strategy, false, CharMatcher.none(), 2147483647);
/*     */   }
/*     */   
/*     */   private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
/* 115 */     this.strategy = strategy;
/* 116 */     this.omitEmptyStrings = omitEmptyStrings;
/* 117 */     this.trimmer = trimmer;
/* 118 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Splitter on(char separator) {
/* 129 */     return on(CharMatcher.is(separator));
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
/*     */   public static Splitter on(final CharMatcher separatorMatcher) {
/* 143 */     Preconditions.checkNotNull(separatorMatcher);
/*     */     
/* 145 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 149 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 int separatorStart(int start) {
/* 152 */                   return separatorMatcher.indexIn(this.toSplit, start);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 int separatorEnd(int separatorPosition) {
/* 157 */                   return separatorPosition + 1;
/*     */                 }
/*     */               };
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
/*     */   public static Splitter on(final String separator) {
/* 173 */     Preconditions.checkArgument((separator.length() != 0), "The separator may not be the empty string.");
/* 174 */     if (separator.length() == 1) {
/* 175 */       return on(separator.charAt(0));
/*     */     }
/* 177 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 181 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 184 */                   int separatorLength = separator.length();
/*     */ 
/*     */                   
/* 187 */                   for (int p = start, last = this.toSplit.length() - separatorLength; p <= last; p++) {
/* 188 */                     int i = 0; while (true) { if (i < separatorLength) {
/* 189 */                         if (this.toSplit.charAt(i + p) != separator.charAt(i))
/*     */                           break;  i++;
/*     */                         continue;
/*     */                       } 
/* 193 */                       return p; }
/*     */                   
/* 195 */                   }  return -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 200 */                   return separatorPosition + separator.length();
/*     */                 }
/*     */               };
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static Splitter on(Pattern separatorPattern) {
/* 220 */     return onPatternInternal(new JdkPattern(separatorPattern));
/*     */   }
/*     */ 
/*     */   
/*     */   static Splitter onPatternInternal(final CommonPattern separatorPattern) {
/* 225 */     Preconditions.checkArgument(
/* 226 */         !separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", separatorPattern);
/*     */ 
/*     */ 
/*     */     
/* 230 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 234 */             final CommonMatcher matcher = separatorPattern.matcher(toSplit);
/* 235 */             return new Splitter.SplittingIterator(this, splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 238 */                   return matcher.find(start) ? matcher.start() : -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 243 */                   return matcher.end();
/*     */                 }
/*     */               };
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static Splitter onPattern(String separatorPattern) {
/* 265 */     return onPatternInternal(Platform.compilePattern(separatorPattern));
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
/*     */   public static Splitter fixedLength(final int length) {
/* 287 */     Preconditions.checkArgument((length > 0), "The length may not be less than 1");
/*     */     
/* 289 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 293 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 296 */                   int nextChunkStart = start + length;
/* 297 */                   return (nextChunkStart < this.toSplit.length()) ? nextChunkStart : -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 302 */                   return separatorPosition;
/*     */                 }
/*     */               };
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
/*     */ 
/*     */   
/*     */   public Splitter omitEmptyStrings() {
/* 326 */     return new Splitter(this.strategy, true, this.trimmer, this.limit);
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
/*     */   public Splitter limit(int maxItems) {
/* 346 */     Preconditions.checkArgument((maxItems > 0), "must be greater than zero: %s", maxItems);
/* 347 */     return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, maxItems);
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
/*     */   public Splitter trimResults() {
/* 360 */     return trimResults(CharMatcher.whitespace());
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
/*     */   public Splitter trimResults(CharMatcher trimmer) {
/* 375 */     Preconditions.checkNotNull(trimmer);
/* 376 */     return new Splitter(this.strategy, this.omitEmptyStrings, trimmer, this.limit);
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
/*     */   public Iterable<String> split(final CharSequence sequence) {
/* 388 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 390 */     return new Iterable<String>()
/*     */       {
/*     */         public Iterator<String> iterator() {
/* 393 */           return Splitter.this.splittingIterator(sequence);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 398 */           return Joiner.on(", ")
/* 399 */             .appendTo((new StringBuilder()).append('['), this)
/* 400 */             .append(']')
/* 401 */             .toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private Iterator<String> splittingIterator(CharSequence sequence) {
/* 407 */     return this.strategy.iterator(this, sequence);
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
/*     */   public List<String> splitToList(CharSequence sequence) {
/* 419 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 421 */     Iterator<String> iterator = splittingIterator(sequence);
/* 422 */     List<String> result = new ArrayList<>();
/*     */     
/* 424 */     while (iterator.hasNext()) {
/* 425 */       result.add(iterator.next());
/*     */     }
/*     */     
/* 428 */     return Collections.unmodifiableList(result);
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
/*     */   public Stream<String> splitToStream(CharSequence sequence) {
/* 442 */     return StreamSupport.stream(split(sequence).spliterator(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapSplitter withKeyValueSeparator(String separator) {
/* 452 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapSplitter withKeyValueSeparator(char separator) {
/* 462 */     return withKeyValueSeparator(on(separator));
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
/*     */   public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter) {
/* 485 */     return new MapSplitter(this, keyValueSplitter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class MapSplitter
/*     */   {
/*     */     private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
/*     */ 
/*     */     
/*     */     private final Splitter outerSplitter;
/*     */ 
/*     */     
/*     */     private final Splitter entrySplitter;
/*     */ 
/*     */     
/*     */     private MapSplitter(Splitter outerSplitter, Splitter entrySplitter) {
/* 502 */       this.outerSplitter = outerSplitter;
/* 503 */       this.entrySplitter = Preconditions.<Splitter>checkNotNull(entrySplitter);
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
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, String> split(CharSequence sequence) {
/* 518 */       Map<String, String> map = new LinkedHashMap<>();
/* 519 */       for (String entry : this.outerSplitter.split(sequence)) {
/* 520 */         Iterator<String> entryFields = this.entrySplitter.splittingIterator(entry);
/*     */         
/* 522 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 523 */         String key = entryFields.next();
/* 524 */         Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", key);
/*     */         
/* 526 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 527 */         String value = entryFields.next();
/* 528 */         map.put(key, value);
/*     */         
/* 530 */         Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/*     */       } 
/* 532 */       return Collections.unmodifiableMap(map);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface Strategy
/*     */   {
/*     */     Iterator<String> iterator(Splitter param1Splitter, CharSequence param1CharSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class SplittingIterator
/*     */     extends AbstractIterator<String>
/*     */   {
/*     */     final CharSequence toSplit;
/*     */ 
/*     */     
/*     */     final CharMatcher trimmer;
/*     */ 
/*     */     
/*     */     final boolean omitEmptyStrings;
/*     */ 
/*     */     
/* 557 */     int offset = 0;
/*     */     int limit;
/*     */     
/*     */     protected SplittingIterator(Splitter splitter, CharSequence toSplit) {
/* 561 */       this.trimmer = splitter.trimmer;
/* 562 */       this.omitEmptyStrings = splitter.omitEmptyStrings;
/* 563 */       this.limit = splitter.limit;
/* 564 */       this.toSplit = toSplit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     protected String computeNext() {
/* 575 */       int nextStart = this.offset;
/* 576 */       while (this.offset != -1) {
/* 577 */         int end, start = nextStart;
/*     */ 
/*     */         
/* 580 */         int separatorPosition = separatorStart(this.offset);
/* 581 */         if (separatorPosition == -1) {
/* 582 */           end = this.toSplit.length();
/* 583 */           this.offset = -1;
/*     */         } else {
/* 585 */           end = separatorPosition;
/* 586 */           this.offset = separatorEnd(separatorPosition);
/*     */         } 
/* 588 */         if (this.offset == nextStart) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 595 */           this.offset++;
/* 596 */           if (this.offset > this.toSplit.length()) {
/* 597 */             this.offset = -1;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 602 */         while (start < end && this.trimmer.matches(this.toSplit.charAt(start))) {
/* 603 */           start++;
/*     */         }
/* 605 */         while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 606 */           end--;
/*     */         }
/*     */         
/* 609 */         if (this.omitEmptyStrings && start == end) {
/*     */           
/* 611 */           nextStart = this.offset;
/*     */           
/*     */           continue;
/*     */         } 
/* 615 */         if (this.limit == 1) {
/*     */ 
/*     */ 
/*     */           
/* 619 */           end = this.toSplit.length();
/* 620 */           this.offset = -1;
/*     */           
/* 622 */           while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 623 */             end--;
/*     */           }
/*     */         } else {
/* 626 */           this.limit--;
/*     */         } 
/*     */         
/* 629 */         return this.toSplit.subSequence(start, end).toString();
/*     */       } 
/* 631 */       return endOfData();
/*     */     }
/*     */     
/*     */     abstract int separatorStart(int param1Int);
/*     */     
/*     */     abstract int separatorEnd(int param1Int);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Splitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */