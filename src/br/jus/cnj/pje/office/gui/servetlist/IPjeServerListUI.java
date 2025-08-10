/*    */ package br.jus.cnj.pje.office.gui.servetlist;
/*    */ 
/*    */ import java.util.List;
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
/*    */ interface IPjeServerListUI
/*    */ {
/*    */   List<IServerEntry> show(List<IServerEntry> paramList);
/*    */   
/*    */   public enum Authorization
/*    */   {
/* 35 */     SIM, NÃO;
/*    */     
/*    */     static Authorization from(boolean autorized) {
/* 38 */       return autorized ? SIM : NÃO;
/*    */     }
/*    */   }
/*    */   
/*    */   public enum Action {
/* 43 */     REMOVER;
/*    */     
/*    */     public String toString() {
/* 46 */       return "Remover";
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface IServerEntry {
/*    */     String getApp();
/*    */     
/*    */     String getServer();
/*    */     
/*    */     IPjeServerListUI.Authorization getAuthorization();
/*    */     
/*    */     String getCode();
/*    */     
/*    */     IPjeServerListUI.Action getAction();
/*    */     
/*    */     IServerEntry clone();
/*    */     
/*    */     void setAuthorization(IPjeServerListUI.Authorization param1Authorization);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/servetlist/IPjeServerListUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */