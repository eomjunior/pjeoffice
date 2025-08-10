/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LazyResourceCollectionWrapper
/*     */   extends AbstractResourceCollectionWrapper
/*     */ {
/*  36 */   private final List<Resource> cachedResources = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private Iterator<Resource> filteringIterator;
/*     */   
/*     */   private final Supplier<Iterator<Resource>> filteringIteratorSupplier = () -> new FilteringIterator(getResourceCollection().iterator());
/*     */ 
/*     */   
/*     */   protected Iterator<Resource> createIterator() {
/*  45 */     if (isCache()) {
/*  46 */       if (this.filteringIterator == null)
/*     */       {
/*  48 */         this.filteringIterator = this.filteringIteratorSupplier.get();
/*     */       }
/*  50 */       return new CachedIterator(this.filteringIterator);
/*     */     } 
/*  52 */     return this.filteringIteratorSupplier.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getSize() {
/*  59 */     Iterator<Resource> it = createIterator();
/*  60 */     int size = 0;
/*  61 */     while (it.hasNext()) {
/*  62 */       it.next();
/*  63 */       size++;
/*     */     } 
/*  65 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean filterResource(Resource r) {
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   private class FilteringIterator
/*     */     implements Iterator<Resource> {
/*  81 */     Resource next = null;
/*     */     
/*     */     boolean ended = false;
/*     */     
/*     */     protected final Iterator<Resource> it;
/*     */     
/*     */     FilteringIterator(Iterator<Resource> it) {
/*  88 */       this.it = it;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  93 */       if (this.ended) {
/*  94 */         return false;
/*     */       }
/*  96 */       while (this.next == null) {
/*  97 */         if (!this.it.hasNext()) {
/*  98 */           this.ended = true;
/*  99 */           return false;
/*     */         } 
/* 101 */         this.next = this.it.next();
/* 102 */         if (LazyResourceCollectionWrapper.this.filterResource(this.next)) {
/* 103 */           this.next = null;
/*     */         }
/*     */       } 
/* 106 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Resource next() {
/* 111 */       if (!hasNext()) {
/* 112 */         throw new UnsupportedOperationException();
/*     */       }
/* 114 */       Resource r = this.next;
/* 115 */       this.next = null;
/* 116 */       return r;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class CachedIterator
/*     */     implements Iterator<Resource>
/*     */   {
/* 127 */     int cursor = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Iterator<Resource> it;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CachedIterator(Iterator<Resource> it) {
/* 139 */       this.it = it;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 144 */       synchronized (LazyResourceCollectionWrapper.this.cachedResources) {
/*     */         
/* 146 */         if (LazyResourceCollectionWrapper.this.cachedResources.size() > this.cursor) {
/* 147 */           return true;
/*     */         }
/*     */         
/* 150 */         if (!this.it.hasNext()) {
/* 151 */           return false;
/*     */         }
/*     */         
/* 154 */         Resource r = this.it.next();
/* 155 */         LazyResourceCollectionWrapper.this.cachedResources.add(r);
/*     */       } 
/* 157 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Resource next() {
/* 163 */       if (!hasNext()) {
/* 164 */         throw new NoSuchElementException();
/*     */       }
/* 166 */       synchronized (LazyResourceCollectionWrapper.this.cachedResources) {
/*     */ 
/*     */         
/* 169 */         return LazyResourceCollectionWrapper.this.cachedResources.get(this.cursor++);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 175 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/LazyResourceCollectionWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */