/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ public enum BoundType
/*    */ {
/* 30 */   OPEN(false),
/* 31 */   CLOSED(true);
/*    */   
/*    */   final boolean inclusive;
/*    */   
/*    */   BoundType(boolean inclusive) {
/* 36 */     this.inclusive = inclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   static BoundType forBoolean(boolean inclusive) {
/* 41 */     return inclusive ? CLOSED : OPEN;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/BoundType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */