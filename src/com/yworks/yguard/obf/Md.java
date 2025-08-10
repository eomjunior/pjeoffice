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
/*    */ public class Md
/*    */   extends MdFd
/*    */ {
/*    */   public Md(TreeItem parent, boolean isSynthetic, String name, String descriptor, int access, ObfuscationConfig obfuscationConfig) {
/* 41 */     super(parent, isSynthetic, name, descriptor, access, obfuscationConfig);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getDescriptorName() {
/* 47 */     String[] types = parseTypes();
/* 48 */     StringBuffer sb = new StringBuffer();
/* 49 */     sb.append("(");
/* 50 */     if (types.length > 0)
/*    */     {
/* 52 */       for (int i = 0; i < types.length - 1; i++) {
/*    */         
/* 54 */         sb.append(types[i]);
/* 55 */         if (i < types.length - 2)
/*    */         {
/* 57 */           sb.append(", ");
/*    */         }
/*    */       } 
/*    */     }
/* 61 */     sb.append(");");
/* 62 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isWildcardMatch(String namePattern, String descPattern) {
/* 73 */     return (
/* 74 */       isMatch(namePattern, getFullInName()) && 
/* 75 */       isMatch(descPattern, getDescriptor()));
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
/*    */   public boolean isNRWildcardMatch(String namePattern, String descPattern) {
/* 87 */     return (
/* 88 */       isNRMatch(namePattern, getFullInName()) && 
/* 89 */       isMatch(descPattern, getDescriptor()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/Md.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */