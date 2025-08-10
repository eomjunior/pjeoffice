/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public class Joiner
/*     */ {
/*     */   private final String separator;
/*     */   
/*     */   public static Joiner on(String separator) {
/*  72 */     return new Joiner(separator);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Joiner on(char separator) {
/*  77 */     return new Joiner(String.valueOf(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Joiner(String separator) {
/*  83 */     this.separator = Preconditions.<String>checkNotNull(separator);
/*     */   }
/*     */   
/*     */   private Joiner(Joiner prototype) {
/*  87 */     this.separator = prototype.separator;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Object> parts) throws IOException {
/* 103 */     return appendTo(appendable, parts.iterator());
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
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Object> parts) throws IOException {
/* 115 */     Preconditions.checkNotNull(appendable);
/* 116 */     if (parts.hasNext()) {
/* 117 */       appendable.append(toString(parts.next()));
/* 118 */       while (parts.hasNext()) {
/* 119 */         appendable.append(this.separator);
/* 120 */         appendable.append(toString(parts.next()));
/*     */       } 
/*     */     } 
/* 123 */     return appendable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
/* 134 */     List<?> partsList = Arrays.asList(parts);
/* 135 */     return appendTo(appendable, (Iterable)partsList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <A extends Appendable> A appendTo(A appendable, @CheckForNull Object first, @CheckForNull Object second, Object... rest) throws IOException {
/* 146 */     return appendTo(appendable, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterable<? extends Object> parts) {
/* 157 */     return appendTo(builder, parts.iterator());
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
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterator<? extends Object> parts) {
/*     */     try {
/* 171 */       appendTo(builder, parts);
/* 172 */     } catch (IOException impossible) {
/* 173 */       throw new AssertionError(impossible);
/*     */     } 
/* 175 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
/* 186 */     List<?> partsList = Arrays.asList(parts);
/* 187 */     return appendTo(builder, (Iterable)partsList);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, @CheckForNull Object first, @CheckForNull Object second, Object... rest) {
/* 201 */     return appendTo(builder, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterable<? extends Object> parts) {
/* 209 */     return join(parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterator<? extends Object> parts) {
/* 219 */     return appendTo(new StringBuilder(), parts).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Object[] parts) {
/* 228 */     List<?> partsList = Arrays.asList(parts);
/* 229 */     return join((Iterable)partsList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(@CheckForNull Object first, @CheckForNull Object second, Object... rest) {
/* 238 */     return join(iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Joiner useForNull(final String nullText) {
/* 246 */     Preconditions.checkNotNull(nullText);
/* 247 */     return new Joiner(this)
/*     */       {
/*     */         CharSequence toString(@CheckForNull Object part) {
/* 250 */           return (part == null) ? nullText : Joiner.this.toString(part);
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner useForNull(String nullText) {
/* 255 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner skipNulls() {
/* 260 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Joiner skipNulls() {
/* 270 */     return new Joiner(this)
/*     */       {
/*     */         public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Object> parts) throws IOException
/*     */         {
/* 274 */           Preconditions.checkNotNull(appendable, "appendable");
/* 275 */           Preconditions.checkNotNull(parts, "parts");
/* 276 */           while (parts.hasNext()) {
/* 277 */             Object part = parts.next();
/* 278 */             if (part != null) {
/* 279 */               appendable.append(Joiner.this.toString(part));
/*     */               break;
/*     */             } 
/*     */           } 
/* 283 */           while (parts.hasNext()) {
/* 284 */             Object part = parts.next();
/* 285 */             if (part != null) {
/* 286 */               appendable.append(Joiner.this.separator);
/* 287 */               appendable.append(Joiner.this.toString(part));
/*     */             } 
/*     */           } 
/* 290 */           return appendable;
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner useForNull(String nullText) {
/* 295 */           throw new UnsupportedOperationException("already specified skipNulls");
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner.MapJoiner withKeyValueSeparator(String kvs) {
/* 300 */           throw new UnsupportedOperationException("can't use .skipNulls() with maps");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapJoiner withKeyValueSeparator(char keyValueSeparator) {
/* 312 */     return withKeyValueSeparator(String.valueOf(keyValueSeparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
/* 320 */     return new MapJoiner(this, keyValueSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class MapJoiner
/*     */   {
/*     */     private final Joiner joiner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String keyValueSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MapJoiner(Joiner joiner, String keyValueSeparator) {
/* 346 */       this.joiner = joiner;
/* 347 */       this.keyValueSeparator = Preconditions.<String>checkNotNull(keyValueSeparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
/* 356 */       return appendTo(appendable, map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
/* 366 */       return appendTo(builder, map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Map.Entry<?, ?>> entries) throws IOException {
/* 378 */       return appendTo(appendable, entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Map.Entry<?, ?>> parts) throws IOException {
/* 390 */       Preconditions.checkNotNull(appendable);
/* 391 */       if (parts.hasNext()) {
/* 392 */         Map.Entry<?, ?> entry = parts.next();
/* 393 */         appendable.append(this.joiner.toString(entry.getKey()));
/* 394 */         appendable.append(this.keyValueSeparator);
/* 395 */         appendable.append(this.joiner.toString(entry.getValue()));
/* 396 */         while (parts.hasNext()) {
/* 397 */           appendable.append(this.joiner.separator);
/* 398 */           Map.Entry<?, ?> e = parts.next();
/* 399 */           appendable.append(this.joiner.toString(e.getKey()));
/* 400 */           appendable.append(this.keyValueSeparator);
/* 401 */           appendable.append(this.joiner.toString(e.getValue()));
/*     */         } 
/*     */       } 
/* 404 */       return appendable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Map.Entry<?, ?>> entries) {
/* 416 */       return appendTo(builder, entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterator<? extends Map.Entry<?, ?>> entries) {
/*     */       try {
/* 429 */         appendTo(builder, entries);
/* 430 */       } catch (IOException impossible) {
/* 431 */         throw new AssertionError(impossible);
/*     */       } 
/* 433 */       return builder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String join(Map<?, ?> map) {
/* 441 */       return join(map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String join(Iterable<? extends Map.Entry<?, ?>> entries) {
/* 451 */       return join(entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String join(Iterator<? extends Map.Entry<?, ?>> entries) {
/* 461 */       return appendTo(new StringBuilder(), entries).toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MapJoiner useForNull(String nullText) {
/* 469 */       return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
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
/*     */ 
/*     */ 
/*     */   
/*     */   CharSequence toString(@CheckForNull Object part) {
/* 491 */     Objects.requireNonNull(part);
/* 492 */     return (part instanceof CharSequence) ? (CharSequence)part : part.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Iterable<Object> iterable(@CheckForNull final Object first, @CheckForNull final Object second, final Object[] rest) {
/* 497 */     Preconditions.checkNotNull(rest);
/* 498 */     return new AbstractList()
/*     */       {
/*     */         public int size() {
/* 501 */           return rest.length + 2;
/*     */         }
/*     */ 
/*     */         
/*     */         @CheckForNull
/*     */         public Object get(int index) {
/* 507 */           switch (index) {
/*     */             case 0:
/* 509 */               return first;
/*     */             case 1:
/* 511 */               return second;
/*     */           } 
/* 513 */           return rest[index - 2];
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Joiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */