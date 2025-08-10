/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(serializable = true)
/*    */ class ImmutableEntry<K, V>
/*    */   extends AbstractMapEntry<K, V>
/*    */   implements Serializable
/*    */ {
/*    */   @ParametricNullness
/*    */   final K key;
/*    */   @ParametricNullness
/*    */   final V value;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ImmutableEntry(@ParametricNullness K key, @ParametricNullness V value) {
/* 36 */     this.key = key;
/* 37 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   public final K getKey() {
/* 43 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   public final V getValue() {
/* 49 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   public final V setValue(@ParametricNullness V value) {
/* 55 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */