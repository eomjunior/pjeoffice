/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class CollectionFuture<V, C>
/*     */   extends AggregateFuture<V, C>
/*     */ {
/*     */   @CheckForNull
/*     */   private List<Present<V>> values;
/*     */   
/*     */   CollectionFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
/*  44 */     super(futures, allMustSucceed, true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     List<Present<V>> values = futures.isEmpty() ? Collections.<Present<V>>emptyList() : Lists.newArrayListWithCapacity(futures.size());
/*     */ 
/*     */     
/*  52 */     for (int i = 0; i < futures.size(); i++) {
/*  53 */       values.add(null);
/*     */     }
/*     */     
/*  56 */     this.values = values;
/*     */   }
/*     */ 
/*     */   
/*     */   final void collectOneValue(int index, @ParametricNullness V returnValue) {
/*  61 */     List<Present<V>> localValues = this.values;
/*  62 */     if (localValues != null) {
/*  63 */       localValues.set(index, new Present<>(returnValue));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   final void handleAllCompleted() {
/*  69 */     List<Present<V>> localValues = this.values;
/*  70 */     if (localValues != null) {
/*  71 */       set(combine(localValues));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void releaseResources(AggregateFuture.ReleaseResourcesReason reason) {
/*  77 */     super.releaseResources(reason);
/*  78 */     this.values = null;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract C combine(List<Present<V>> paramList);
/*     */ 
/*     */   
/*     */   static final class ListFuture<V>
/*     */     extends CollectionFuture<V, List<V>>
/*     */   {
/*     */     ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
/*  89 */       super(futures, allMustSucceed);
/*  90 */       init();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> combine(List<CollectionFuture.Present<V>> values) {
/*  95 */       List<V> result = Lists.newArrayListWithCapacity(values.size());
/*  96 */       for (CollectionFuture.Present<V> element : values) {
/*  97 */         result.add((element != null) ? element.value : null);
/*     */       }
/*  99 */       return Collections.unmodifiableList(result);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Present<V> {
/*     */     @ParametricNullness
/*     */     final V value;
/*     */     
/*     */     Present(@ParametricNullness V value) {
/* 108 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/CollectionFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */