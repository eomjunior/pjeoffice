/*    */ package org.apache.tools.ant.types.resources.comparators;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.Optional;
/*    */ import java.util.Stack;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.types.Resource;
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
/*    */ public class Reverse
/*    */   extends ResourceComparator
/*    */ {
/*    */   private static final String ONE_NESTED = "You must not nest more than one ResourceComparator for reversal.";
/*    */   private ResourceComparator nested;
/*    */   
/*    */   public Reverse() {}
/*    */   
/*    */   public Reverse(ResourceComparator c) {
/* 50 */     add(c);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(ResourceComparator c) {
/* 58 */     if (this.nested != null) {
/* 59 */       throw new BuildException("You must not nest more than one ResourceComparator for reversal.");
/*    */     }
/* 61 */     this.nested = c;
/* 62 */     setChecked(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int resourceCompare(Resource foo, Resource bar) {
/* 73 */     return ((Comparator<Resource>)Optional.<Comparator<Resource>>ofNullable(this.nested)
/* 74 */       .orElseGet(Comparator::naturalOrder)).reversed().compare(foo, bar);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 79 */     if (isChecked()) {
/*    */       return;
/*    */     }
/* 82 */     if (isReference()) {
/* 83 */       super.dieOnCircularReference(stk, p);
/*    */     } else {
/* 85 */       if (this.nested != null) {
/* 86 */         pushAndInvokeCircularReferenceCheck(this.nested, stk, p);
/*    */       }
/* 88 */       setChecked(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/comparators/Reverse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */