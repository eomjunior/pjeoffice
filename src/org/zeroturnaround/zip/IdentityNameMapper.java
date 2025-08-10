/*    */ package org.zeroturnaround.zip;
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
/*    */ final class IdentityNameMapper
/*    */   implements NameMapper
/*    */ {
/* 27 */   public static final NameMapper INSTANCE = new IdentityNameMapper();
/*    */ 
/*    */ 
/*    */   
/*    */   public String map(String name) {
/* 32 */     return name;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/IdentityNameMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */