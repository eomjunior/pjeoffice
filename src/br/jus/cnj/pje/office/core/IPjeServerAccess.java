/*    */ package br.jus.cnj.pje.office.core;
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
/*    */ public interface IPjeServerAccess
/*    */ {
/*    */   String getId();
/*    */   
/*    */   String getApp();
/*    */   
/*    */   String getServer();
/*    */   
/*    */   String getCode();
/*    */   
/*    */   boolean isAutorized();
/*    */   
/*    */   IPjeServerAccess clone(boolean paramBoolean);
/*    */   
/*    */   default IPjeServerAccess newInstance() {
/* 45 */     return clone(isAutorized());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeServerAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */