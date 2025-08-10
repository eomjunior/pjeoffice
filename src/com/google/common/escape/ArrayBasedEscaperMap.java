/*    */ package com.google.common.escape;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collections;
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
/*    */ public final class ArrayBasedEscaperMap
/*    */ {
/*    */   private final char[][] replacementArray;
/*    */   
/*    */   public static ArrayBasedEscaperMap create(Map<Character, String> replacements) {
/* 48 */     return new ArrayBasedEscaperMap(createReplacementArray(replacements));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ArrayBasedEscaperMap(char[][] replacementArray) {
/* 56 */     this.replacementArray = replacementArray;
/*    */   }
/*    */ 
/*    */   
/*    */   char[][] getReplacementArray() {
/* 61 */     return this.replacementArray;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/*    */   static char[][] createReplacementArray(Map<Character, String> map) {
/* 69 */     Preconditions.checkNotNull(map);
/* 70 */     if (map.isEmpty()) {
/* 71 */       return EMPTY_REPLACEMENT_ARRAY;
/*    */     }
/* 73 */     char max = ((Character)Collections.<Character>max(map.keySet())).charValue();
/* 74 */     char[][] replacements = new char[max + 1][];
/* 75 */     for (Character c : map.keySet()) {
/* 76 */       replacements[c.charValue()] = ((String)map.get(c)).toCharArray();
/*    */     }
/* 78 */     return replacements;
/*    */   }
/*    */ 
/*    */   
/* 82 */   private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/escape/ArrayBasedEscaperMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */