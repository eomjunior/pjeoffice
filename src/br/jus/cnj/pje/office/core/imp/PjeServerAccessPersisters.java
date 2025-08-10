/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*    */ import br.jus.cnj.pje.office.core.IPjeServerAccessPersister;
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
/*    */ public enum PjeServerAccessPersisters
/*    */   implements IPjeServerAccessPersister
/*    */ {
/* 37 */   DEVMODE(new PjeServerAccessPersister(PjePermissionChecker.DEVMODE)),
/*    */   
/* 39 */   PRODUCTION(new PjeServerAccessPersister(PjePermissionChecker.PRODUCTION));
/*    */   
/*    */   private final IPjeServerAccessPersister persister;
/*    */   
/*    */   PjeServerAccessPersisters(IPjeServerAccessPersister persister) {
/* 44 */     this.persister = persister;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<IPjeServerAccess> hasPermission(String id) {
/* 49 */     return this.persister.hasPermission(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void save(IPjeServerAccess access) throws PjePermissionDeniedException {
/* 54 */     this.persister.save(access);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(IPjeServerAccess access) throws PjeTokenPersisterException {
/* 59 */     this.persister.remove(access);
/*    */   }
/*    */ 
/*    */   
/*    */   public IPjeServerAccessPersister reload() {
/* 64 */     this.persister.reload();
/* 65 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkAccessPermission(IPjeServerAccess serverRequest) throws PjePermissionDeniedException {
/* 70 */     this.persister.checkAccessPermission(serverRequest);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<IPjeServerAccess> getServers() {
/* 75 */     return this.persister.getServers();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeServerAccessPersisters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */