/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ final class Hashing
/*    */ {
/*    */   private static final long C1 = -862048943L;
/*    */   private static final long C2 = 461845907L;
/*    */   private static final int MAX_TABLE_SIZE = 1073741824;
/*    */   
/*    */   static int smear(int hashCode) {
/* 51 */     return (int)(461845907L * Integer.rotateLeft((int)(hashCode * -862048943L), 15));
/*    */   }
/*    */   
/*    */   static int smearedHash(@CheckForNull Object o) {
/* 55 */     return smear((o == null) ? 0 : o.hashCode());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static int closedTableSize(int expectedEntries, double loadFactor) {
/* 63 */     expectedEntries = Math.max(expectedEntries, 2);
/* 64 */     int tableSize = Integer.highestOneBit(expectedEntries);
/*    */     
/* 66 */     if (expectedEntries > (int)(loadFactor * tableSize)) {
/* 67 */       tableSize <<= 1;
/* 68 */       return (tableSize > 0) ? tableSize : 1073741824;
/*    */     } 
/* 70 */     return tableSize;
/*    */   }
/*    */   
/*    */   static boolean needsResizing(int size, int tableSize, double loadFactor) {
/* 74 */     return (size > loadFactor * tableSize && tableSize < 1073741824);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */