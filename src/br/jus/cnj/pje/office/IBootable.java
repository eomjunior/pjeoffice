/*    */ package br.jus.cnj.pje.office;
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
/*    */ public interface IBootable
/*    */ {
/*    */   String model();
/*    */   
/*    */   void boot();
/*    */   
/*    */   void logout();
/*    */   
/*    */   void exit(long paramLong);
/*    */   
/*    */   void stateChanging(boolean paramBoolean);
/*    */   
/*    */   default void exit() {
/* 43 */     exit(0L);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/IBootable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */