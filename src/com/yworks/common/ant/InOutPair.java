/*    */ package com.yworks.common.ant;
/*    */ 
/*    */ import com.yworks.common.ResourcePolicy;
/*    */ import com.yworks.common.ShrinkBag;
/*    */ import java.io.File;
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
/*    */ public class InOutPair
/*    */   implements ShrinkBag
/*    */ {
/*    */   private File inFile;
/*    */   private File outFile;
/* 21 */   ResourcePolicy resources = ResourcePolicy.COPY;
/*    */   
/*    */   public void setIn(File file) {
/* 24 */     this.inFile = file;
/*    */   }
/*    */   
/*    */   public void setOut(File file) {
/* 28 */     this.outFile = file;
/*    */   }
/*    */   
/*    */   public File getIn() {
/* 32 */     return this.inFile;
/*    */   }
/*    */   
/*    */   public File getOut() {
/* 36 */     return this.outFile;
/*    */   }
/*    */   
/*    */   public boolean isEntryPointJar() {
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setResources(String resourcesStr) {
/*    */     try {
/* 46 */       this.resources = ResourcePolicy.valueOf(resourcesStr.trim().toUpperCase());
/* 47 */     } catch (IllegalArgumentException e) {
/* 48 */       throw new BuildException("Invalid resource policy: " + resourcesStr);
/*    */     } 
/*    */   }
/*    */   
/*    */   public ResourcePolicy getResources() {
/* 53 */     return this.resources;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 57 */     return "in: " + this.inFile + "; out: " + this.outFile;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/InOutPair.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */