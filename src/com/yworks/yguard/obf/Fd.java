/*    */ package com.yworks.yguard.obf;
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
/*    */ public class Fd
/*    */   extends MdFd
/*    */ {
/*    */   public Fd(TreeItem parent, boolean isSynthetic, String name, String descriptor, int access, ObfuscationConfig obfuscationConfig) {
/* 41 */     super(parent, isSynthetic, name, descriptor, access, obfuscationConfig);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getDescriptorName() {
/* 47 */     return ";";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isWildcardMatch(String namePattern) {
/* 57 */     return isMatch(namePattern, getFullInName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isNRWildcardMatch(String namePattern) {
/* 67 */     return isNRMatch(namePattern, getFullInName());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/Fd.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */