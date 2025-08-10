/*     */ package br.jus.cnj.pje.office.gui.servetlist;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeConfig;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeServerAccess;
/*     */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum PjeServerList
/*     */   implements IPjeServerListAccessor
/*     */ {
/*  45 */   ACCESSOR;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IPjeServerListUI serverList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PjeServerList() {
/*  92 */     this.serverList = new PjeServerListUI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void show() {
/* 104 */     List<IPjeServerAccess> serverAccess = (List<IPjeServerAccess>)this.serverList.show((List<IPjeServerListUI.IServerEntry>)PjeSecurity.GRANTOR.getServers().stream().map(x$0 -> new ServerEntry(x$0)).collect(Collectors.toList())).stream().map(se -> new PjeServerAccess(se.getApp(), se.getServer(), se.getCode(), IPjeServerListUI.Authorization.SIM.equals(se.getAuthorization()))).collect(Collectors.toList());
/* 105 */     PjeConfig.overwrite(serverAccess.<IPjeServerAccess>toArray(new IPjeServerAccess[serverAccess.size()]));
/* 106 */     PjeSecurity.GRANTOR.refresh();
/*     */   }
/*     */   
/*     */   private static class ServerEntry implements IPjeServerListUI.IServerEntry {
/*     */     private IPjeServerAccess serverAccess;
/*     */     private IPjeServerListUI.Authorization authorization;
/*     */     
/*     */     private ServerEntry(IPjeServerAccess serverAccess) {
/*     */       this.serverAccess = (IPjeServerAccess)Args.requireNonNull(serverAccess, "serverAccess is null");
/*     */       this.authorization = IPjeServerListUI.Authorization.from(serverAccess.isAutorized());
/*     */     }
/*     */     
/*     */     public String getApp() {
/*     */       return this.serverAccess.getApp();
/*     */     }
/*     */     
/*     */     public String getServer() {
/*     */       return this.serverAccess.getServer();
/*     */     }
/*     */     
/*     */     public IPjeServerListUI.Authorization getAuthorization() {
/*     */       return this.authorization;
/*     */     }
/*     */     
/*     */     public IPjeServerListUI.Action getAction() {
/*     */       return IPjeServerListUI.Action.REMOVER;
/*     */     }
/*     */     
/*     */     public String getCode() {
/*     */       return this.serverAccess.getCode();
/*     */     }
/*     */     
/*     */     public IPjeServerListUI.IServerEntry clone() {
/*     */       return new ServerEntry(this.serverAccess.newInstance());
/*     */     }
/*     */     
/*     */     public void setAuthorization(IPjeServerListUI.Authorization authorization) {
/*     */       this.authorization = authorization;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/servetlist/PjeServerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */