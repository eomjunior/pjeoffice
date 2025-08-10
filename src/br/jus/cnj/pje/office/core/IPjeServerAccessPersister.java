/*    */ package br.jus.cnj.pje.office.core;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.imp.PjePermissionDeniedException;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeTokenPersisterException;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
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
/*    */ public interface IPjeServerAccessPersister
/*    */ {
/*    */   IPjeServerAccessPersister reload();
/*    */   
/*    */   Optional<IPjeServerAccess> hasPermission(String paramString);
/*    */   
/*    */   List<IPjeServerAccess> getServers();
/*    */   
/*    */   void save(IPjeServerAccess paramIPjeServerAccess) throws PjePermissionDeniedException;
/*    */   
/*    */   void remove(IPjeServerAccess paramIPjeServerAccess) throws PjeTokenPersisterException;
/*    */   
/*    */   default void allow(IPjeServerAccess access) throws PjePermissionDeniedException {
/* 49 */     save(access.clone(true));
/*    */   }
/*    */   
/*    */   default void disallow(IPjeServerAccess access) throws PjePermissionDeniedException {
/* 53 */     save(access.clone(false));
/*    */   }
/*    */   
/*    */   void checkAccessPermission(IPjeServerAccess paramIPjeServerAccess) throws PjePermissionDeniedException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeServerAccessPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */