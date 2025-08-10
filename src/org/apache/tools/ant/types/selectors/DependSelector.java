/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
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
/*    */ public class DependSelector
/*    */   extends MappingSelector
/*    */ {
/*    */   public String toString() {
/* 37 */     StringBuilder buf = new StringBuilder("{dependselector targetdir: ");
/* 38 */     if (this.targetdir == null) {
/* 39 */       buf.append("NOT YET SET");
/*    */     } else {
/* 41 */       buf.append(this.targetdir.getName());
/*    */     } 
/* 43 */     buf.append(" granularity: ").append(this.granularity);
/* 44 */     if (this.map != null) {
/* 45 */       buf.append(" mapper: ");
/* 46 */       buf.append(this.map.toString());
/* 47 */     } else if (this.mapperElement != null) {
/* 48 */       buf.append(" mapper: ");
/* 49 */       buf.append(this.mapperElement.toString());
/*    */     } 
/* 51 */     buf.append("}");
/* 52 */     return buf.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean selectionTest(File srcfile, File destfile) {
/* 62 */     return SelectorUtils.isOutOfDate(srcfile, destfile, this.granularity);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/DependSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */