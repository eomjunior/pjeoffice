/*    */ package org.apache.tools.ant.types.selectors;
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
/*    */ public class NotSelector
/*    */   extends NoneSelector
/*    */ {
/*    */   public NotSelector() {}
/*    */   
/*    */   public NotSelector(FileSelector other) {
/* 44 */     this();
/* 45 */     appendSelector(other);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     StringBuilder buf = new StringBuilder();
/* 53 */     if (hasSelectors()) {
/* 54 */       buf.append("{notselect: ");
/* 55 */       buf.append(super.toString());
/* 56 */       buf.append("}");
/*    */     } 
/* 58 */     return buf.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void verifySettings() {
/* 66 */     if (selectorCount() != 1)
/* 67 */       setError("One and only one selector is allowed within the <not> tag"); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/NotSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */