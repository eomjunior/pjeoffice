/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.progress4j.imp.ProgressPosition;
/*    */ import com.github.signer4j.IRepository;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ 
/*    */ public enum Repository
/*    */   implements IRepository
/*    */ {
/* 10 */   NATIVE("NATIVE")
/*    */   {
/*    */     public Repository adaptProgress() {
/* 13 */       ProgressPosition.DEFAULT_WINDOW_DELTA_Y_POSITION = 0;
/* 14 */       return this;
/*    */     }
/*    */   },
/*    */   
/* 18 */   MSCAPI("MSCAPI")
/*    */   {
/*    */     public Repository adaptProgress() {
/* 21 */       ProgressPosition.DEFAULT_WINDOW_DELTA_Y_POSITION = -220;
/* 22 */       return this;
/*    */     }
/*    */   };
/*    */   
/*    */   private final String name;
/*    */   
/*    */   public static Repository from(String name) {
/* 29 */     return (name == null || NATIVE.name.equalsIgnoreCase(name = Strings.trim(name))) ? NATIVE : (MSCAPI.name.equalsIgnoreCase(name) ? MSCAPI : NATIVE);
/*    */   }
/*    */   
/*    */   Repository(String name) {
/* 33 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isMSCAPI() {
/* 42 */     return (this == MSCAPI);
/*    */   }
/*    */   
/*    */   public boolean isNative() {
/* 46 */     return (this == NATIVE);
/*    */   }
/*    */   
/*    */   public abstract Repository adaptProgress();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/Repository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */