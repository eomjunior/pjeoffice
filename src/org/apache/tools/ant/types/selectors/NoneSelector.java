/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.stream.Stream;
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
/*    */ public class NoneSelector
/*    */   extends BaseSelectorContainer
/*    */ {
/*    */   public String toString() {
/* 37 */     StringBuilder buf = new StringBuilder();
/* 38 */     if (hasSelectors()) {
/* 39 */       buf.append("{noneselect: ");
/* 40 */       buf.append(super.toString());
/* 41 */       buf.append("}");
/*    */     } 
/* 43 */     return buf.toString();
/*    */   }
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
/*    */   public boolean isSelected(File basedir, String filename, File file) {
/* 57 */     validate();
/* 58 */     return Stream.<FileSelector>of(getSelectors(getProject()))
/* 59 */       .noneMatch(s -> s.isSelected(basedir, filename, file));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/NoneSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */