/*    */ package com.yworks.util.abstractjar.impl;
/*    */ 
/*    */ import com.yworks.util.abstractjar.Entry;
/*    */ import java.util.jar.JarEntry;
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
/*    */ public class JarEntryWrapper
/*    */   implements Entry
/*    */ {
/*    */   JarEntry jarEntry;
/*    */   
/*    */   public JarEntryWrapper(JarEntry jarEntry) {
/* 22 */     this.jarEntry = jarEntry;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDirectory() {
/* 27 */     return this.jarEntry.isDirectory();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 32 */     return this.jarEntry.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getSize() {
/* 37 */     return this.jarEntry.getSize();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JarEntry getJarEntry() {
/* 46 */     return this.jarEntry;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/impl/JarEntryWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */