/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Dictionary;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class CollectionUtils
/*     */ {
/*     */   @Deprecated
/*  44 */   public static final List EMPTY_LIST = Collections.EMPTY_LIST;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static boolean equals(Vector<?> v1, Vector<?> v2) {
/*  56 */     return Objects.equals(v1, v2);
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
/*     */   @Deprecated
/*     */   public static boolean equals(Dictionary<?, ?> d1, Dictionary<?, ?> d2) {
/*  72 */     if (d1 == d2) {
/*  73 */       return true;
/*     */     }
/*     */     
/*  76 */     if (d1 == null || d2 == null) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (d1.size() != d2.size()) {
/*  81 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  86 */     return StreamUtils.enumerationAsStream(d1.keys())
/*  87 */       .allMatch(key -> d1.get(key).equals(d2.get(key)));
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
/*     */   @Deprecated
/*     */   public static String flattenToString(Collection<?> c) {
/* 101 */     return c.stream().map(String::valueOf).collect(Collectors.joining(","));
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
/*     */   @Deprecated
/*     */   public static <K, V> void putAll(Dictionary<? super K, ? super V> m1, Dictionary<? extends K, ? extends V> m2) {
/* 116 */     StreamUtils.enumerationAsStream(m2.keys()).forEach(key -> m1.put(key, m2.get(key)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static final class EmptyEnumeration<E>
/*     */     implements Enumeration<E>
/*     */   {
/*     */     public boolean hasMoreElements() {
/* 131 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E nextElement() throws NoSuchElementException {
/* 140 */       throw new NoSuchElementException();
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
/*     */   @Deprecated
/*     */   public static <E> Enumeration<E> append(Enumeration<E> e1, Enumeration<E> e2) {
/* 157 */     return new CompoundEnumeration<>(e1, e2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <E> Enumeration<E> asEnumeration(final Iterator<E> iter) {
/* 169 */     return new Enumeration<E>()
/*     */       {
/*     */         public boolean hasMoreElements() {
/* 172 */           return iter.hasNext();
/*     */         }
/*     */         
/*     */         public E nextElement() {
/* 176 */           return iter.next();
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
/*     */   
/*     */   @Deprecated
/*     */   public static <E> Iterator<E> asIterator(final Enumeration<E> e) {
/* 190 */     return new Iterator<E>()
/*     */       {
/*     */         public boolean hasNext() {
/* 193 */           return e.hasMoreElements();
/*     */         }
/*     */         
/*     */         public E next() {
/* 197 */           return e.nextElement();
/*     */         }
/*     */         
/*     */         public void remove() {
/* 201 */           throw new UnsupportedOperationException();
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <T> Collection<T> asCollection(Iterator<? extends T> iter) {
/* 217 */     List<T> l = new ArrayList<>();
/* 218 */     Objects.requireNonNull(l); iter.forEachRemaining(l::add);
/* 219 */     return l;
/*     */   }
/*     */   
/*     */   private static final class CompoundEnumeration<E> implements Enumeration<E> {
/*     */     private final Enumeration<E> e1;
/*     */     private final Enumeration<E> e2;
/*     */     
/*     */     public CompoundEnumeration(Enumeration<E> e1, Enumeration<E> e2) {
/* 227 */       this.e1 = e1;
/* 228 */       this.e2 = e2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasMoreElements() {
/* 233 */       return (this.e1.hasMoreElements() || this.e2.hasMoreElements());
/*     */     }
/*     */ 
/*     */     
/*     */     public E nextElement() throws NoSuchElementException {
/* 238 */       if (this.e1.hasMoreElements()) {
/* 239 */         return this.e1.nextElement();
/*     */       }
/* 241 */       return this.e2.nextElement();
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
/*     */   @Deprecated
/*     */   public static int frequency(Collection<?> c, Object o) {
/* 257 */     return (c == null) ? 0 : Collections.frequency(c, o);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/CollectionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */