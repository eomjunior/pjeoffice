/*    */ package org.apache.tools.ant.types;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class Comparison
/*    */   extends EnumeratedAttribute
/*    */ {
/* 32 */   private static final String[] VALUES = new String[] { "equal", "greater", "less", "ne", "ge", "le", "eq", "gt", "lt", "more" };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final Comparison EQUAL = new Comparison("equal");
/*    */ 
/*    */   
/* 40 */   public static final Comparison NOT_EQUAL = new Comparison("ne");
/*    */ 
/*    */   
/* 43 */   public static final Comparison GREATER = new Comparison("greater");
/*    */ 
/*    */   
/* 46 */   public static final Comparison LESS = new Comparison("less");
/*    */ 
/*    */   
/* 49 */   public static final Comparison GREATER_EQUAL = new Comparison("ge");
/*    */ 
/*    */   
/* 52 */   public static final Comparison LESS_EQUAL = new Comparison("le");
/*    */   
/* 54 */   private static final int[] EQUAL_INDEX = new int[] { 0, 4, 5, 6 };
/* 55 */   private static final int[] LESS_INDEX = new int[] { 2, 3, 5, 8 };
/* 56 */   private static final int[] GREATER_INDEX = new int[] { 1, 3, 4, 7, 9 };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Comparison() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Comparison(String value) {
/* 69 */     setValue(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getValues() {
/* 77 */     return VALUES;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean evaluate(int comparisonResult) {
/* 86 */     if (getIndex() == -1) {
/* 87 */       throw new BuildException("Comparison value not set.");
/*    */     }
/*    */     
/* 90 */     int[] i = (comparisonResult < 0) ? LESS_INDEX : ((comparisonResult > 0) ? GREATER_INDEX : EQUAL_INDEX);
/* 91 */     return (Arrays.binarySearch(i, getIndex()) >= 0);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Comparison.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */