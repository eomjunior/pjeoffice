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
/*    */ public class AndSelector
/*    */   extends BaseSelectorContainer
/*    */ {
/*    */   public String toString() {
/* 36 */     StringBuilder buf = new StringBuilder();
/* 37 */     if (hasSelectors()) {
/* 38 */       buf.append("{andselect: ");
/* 39 */       buf.append(super.toString());
/* 40 */       buf.append("}");
/*    */     } 
/* 42 */     return buf.toString();
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
/* 56 */     validate();
/* 57 */     return Stream.<FileSelector>of(getSelectors(getProject()))
/* 58 */       .allMatch(s -> s.isSelected(basedir, filename, file));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/AndSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */