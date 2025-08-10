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
/*    */ public class EntryPointJar
/*    */   implements ShrinkBag
/*    */ {
/*    */   private File inFile;
/*    */   
/*    */   public void setIn(File file) {
/* 19 */     this.inFile = file;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(File fileName) {
/* 28 */     this.inFile = fileName;
/*    */   }
/*    */   
/*    */   public void setOut(File file) {
/* 32 */     throw new BuildException("You can't set an outfile on an EntryPointJar.");
/*    */   }
/*    */   
/*    */   public File getIn() {
/* 36 */     return this.inFile;
/*    */   }
/*    */   
/*    */   public File getOut() {
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isEntryPointJar() {
/* 44 */     return true;
/*    */   }
/*    */   
/*    */   public void setResources(String resourcesStr) {
/* 48 */     throw new BuildException("You can't set resources on an EntryPointJar.");
/*    */   }
/*    */   
/*    */   public ResourcePolicy getResources() {
/* 52 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/EntryPointJar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */