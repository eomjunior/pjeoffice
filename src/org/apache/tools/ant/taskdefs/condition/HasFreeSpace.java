/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ import org.apache.tools.ant.util.ReflectWrapper;
/*     */ import org.apache.tools.ant.util.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HasFreeSpace
/*     */   implements Condition
/*     */ {
/*     */   private String partition;
/*     */   private String needed;
/*     */   
/*     */   public boolean eval() throws BuildException {
/*  46 */     validate();
/*     */     try {
/*  48 */       if (JavaEnvUtils.isAtLeastJavaVersion("1.6")) {
/*     */         
/*  50 */         File fs = new File(this.partition);
/*  51 */         ReflectWrapper w = new ReflectWrapper(fs);
/*  52 */         long free = ((Long)w.invoke("getFreeSpace")).longValue();
/*  53 */         return (free >= StringUtils.parseHumanSizes(this.needed));
/*     */       } 
/*  55 */       throw new BuildException("HasFreeSpace condition not supported on Java5 or less.");
/*     */     }
/*  57 */     catch (Exception e) {
/*  58 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validate() throws BuildException {
/*  63 */     if (null == this.partition) {
/*  64 */       throw new BuildException("Please set the partition attribute.");
/*     */     }
/*  66 */     if (null == this.needed) {
/*  67 */       throw new BuildException("Please set the needed attribute.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPartition() {
/*  76 */     return this.partition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPartition(String partition) {
/*  84 */     this.partition = partition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNeeded() {
/*  92 */     return this.needed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNeeded(String needed) {
/* 100 */     this.needed = needed;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/HasFreeSpace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */