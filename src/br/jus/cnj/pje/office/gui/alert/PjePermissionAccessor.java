/*    */ package br.jus.cnj.pje.office.gui.alert;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjePermissionAccessor;
/*    */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeAccessTime;
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
/*    */ public class PjePermissionAccessor
/*    */   implements IPjePermissionAccessor
/*    */ {
/*    */   public PjeAccessTime tryAccess(IPjeServerAccess access) {
/* 38 */     return PjeServerPermissionOptions.choose(access);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/alert/PjePermissionAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */