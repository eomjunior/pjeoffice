/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
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
/*    */ class EmptyImmutableSetMultimap
/*    */   extends ImmutableSetMultimap<Object, Object>
/*    */ {
/* 30 */   static final EmptyImmutableSetMultimap INSTANCE = new EmptyImmutableSetMultimap();
/*    */   
/*    */   private EmptyImmutableSetMultimap() {
/* 33 */     super(ImmutableMap.of(), 0, (Comparator<? super Object>)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */ 
/*    */ 
/*    */   
/*    */   public ImmutableMap<Object, Collection<Object>> asMap() {
/* 44 */     return super.asMap();
/*    */   }
/*    */   
/*    */   private Object readResolve() {
/* 48 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/EmptyImmutableSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */