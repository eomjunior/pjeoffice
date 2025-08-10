/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Collections;
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
/*    */ public class MajoritySelector
/*    */   extends BaseSelectorContainer
/*    */ {
/*    */   private boolean allowtie = true;
/*    */   
/*    */   public String toString() {
/* 43 */     StringBuilder buf = new StringBuilder();
/* 44 */     if (hasSelectors()) {
/* 45 */       buf.append("{majorityselect: ");
/* 46 */       buf.append(super.toString());
/* 47 */       buf.append("}");
/*    */     } 
/* 49 */     return buf.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAllowtie(boolean tiebreaker) {
/* 60 */     this.allowtie = tiebreaker;
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
/*    */   
/*    */   public boolean isSelected(File basedir, String filename, File file) {
/* 75 */     validate();
/* 76 */     int yesvotes = 0;
/* 77 */     int novotes = 0;
/*    */     
/* 79 */     for (FileSelector fs : Collections.<FileSelector>list(selectorElements())) {
/* 80 */       if (fs.isSelected(basedir, filename, file)) {
/* 81 */         yesvotes++; continue;
/*    */       } 
/* 83 */       novotes++;
/*    */     } 
/*    */     
/* 86 */     if (yesvotes > novotes) {
/* 87 */       return true;
/*    */     }
/* 89 */     if (novotes > yesvotes) {
/* 90 */       return false;
/*    */     }
/*    */     
/* 93 */     return this.allowtie;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/MajoritySelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */