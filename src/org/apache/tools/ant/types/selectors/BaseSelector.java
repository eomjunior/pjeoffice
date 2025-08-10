/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseSelector
/*     */   extends DataType
/*     */   implements FileSelector
/*     */ {
/*  36 */   private String errmsg = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private Throwable cause;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setError(String msg) {
/*  46 */     if (this.errmsg == null) {
/*  47 */       this.errmsg = msg;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setError(String msg, Throwable cause) {
/*  59 */     if (this.errmsg == null) {
/*  60 */       this.errmsg = msg;
/*  61 */       this.cause = cause;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getError() {
/*  71 */     return this.errmsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void verifySettings() {
/*  82 */     if (isReference()) {
/*  83 */       getRef().verifySettings();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate() {
/*  92 */     if (getError() == null) {
/*  93 */       verifySettings();
/*     */     }
/*  95 */     if (getError() != null) {
/*  96 */       throw new BuildException(this.errmsg, this.cause);
/*     */     }
/*  98 */     if (!isReference()) {
/*  99 */       dieOnCircularReference();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isSelected(File paramFile1, String paramString, File paramFile2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BaseSelector getRef() {
/* 118 */     return (BaseSelector)getCheckedRef(BaseSelector.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/BaseSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */