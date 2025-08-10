/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
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
/*    */ class PjeServerAccessWrapper
/*    */   implements IPjeServerAccess
/*    */ {
/*    */   private IPjeServerAccess access;
/*    */   
/*    */   protected PjeServerAccessWrapper(IPjeServerAccess access) {
/* 37 */     this.access = access;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getId() {
/* 42 */     return this.access.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getApp() {
/* 47 */     return this.access.getApp();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getServer() {
/* 52 */     return this.access.getServer();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 57 */     return this.access.getCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAutorized() {
/* 62 */     return this.access.isAutorized();
/*    */   }
/*    */ 
/*    */   
/*    */   public IPjeServerAccess clone(boolean allowed) {
/* 67 */     return this.access.clone(allowed);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeServerAccessWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */