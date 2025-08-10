/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccessPermissionChecker;
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccessPersister;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ class PjeServerAccessPersister
/*     */   implements IPjeServerAccessPersister
/*     */ {
/*  47 */   private static final Logger LOGGER = LoggerFactory.getLogger(PjeServerAccessPersister.class);
/*     */   
/*     */   private boolean loaded = false;
/*     */   
/*     */   private final IPjeServerAccessPermissionChecker checker;
/*     */   
/*  53 */   private final Map<String, IPjeServerAccess> pool = new HashMap<>();
/*     */   
/*     */   protected PjeServerAccessPersister(IPjeServerAccessPermissionChecker checker) {
/*  56 */     this.checker = (IPjeServerAccessPermissionChecker)Args.requireNonNull(checker, "checker is null");
/*     */   }
/*     */   
/*     */   protected boolean isLoaded() {
/*  60 */     return this.loaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkLoaded() {
/*  69 */     if (!isLoaded()) {
/*  70 */       load();
/*  71 */       this.loaded = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IPjeServerAccessPersister reload() {
/*  77 */     this.loaded = false;
/*  78 */     this.pool.clear();
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IPjeServerAccess> hasPermission(String id) {
/*  84 */     Args.requireNonNull(id, "id is null");
/*  85 */     checkLoaded();
/*  86 */     return Optional.ofNullable(this.pool.get(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkAccessPermission(IPjeServerAccess access) throws PjePermissionDeniedException {
/*  91 */     this.checker.checkAccessPermission(access);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void save(IPjeServerAccess access) throws PjePermissionDeniedException {
/*  96 */     Args.requireNonNull(access, "access is null");
/*  97 */     checkLoaded();
/*  98 */     checkAccessPermission(access);
/*  99 */     access = access.newInstance();
/* 100 */     persist(new IPjeServerAccess[] { access });
/* 101 */     this.pool.put(access.getId(), access);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void remove(IPjeServerAccess access) throws PjeTokenPersisterException {
/* 106 */     Args.requireNonNull(access, "access is null");
/* 107 */     checkLoaded();
/* 108 */     unpersist(access);
/* 109 */     this.pool.remove(access.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IPjeServerAccess> getServers() {
/* 114 */     checkLoaded();
/* 115 */     return new ArrayList<>(this.pool.values());
/*     */   }
/*     */   
/*     */   protected void add(IPjeServerAccess access) {
/* 119 */     if (access != null) {
/*     */       try {
/* 121 */         checkAccessPermission(access);
/* 122 */         this.pool.put(access.getId(), access);
/* 123 */       } catch (PjePermissionDeniedException e) {
/* 124 */         unpersist(access);
/* 125 */         LOGGER.warn("Servidor de acesso não autorizado nas configurações (ignorado). ServerAccess: " + access.toString(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void load() {
/* 131 */     PjeConfig.loadServerAccess(this::add);
/*     */   }
/*     */   
/*     */   protected void persist(IPjeServerAccess... access) {
/* 135 */     PjeConfig.save(access);
/*     */   }
/*     */   
/*     */   protected void unpersist(IPjeServerAccess access) {
/* 139 */     PjeConfig.delete(access);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface IPool {
/*     */     void add(IPjeServerAccess param1IPjeServerAccess);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeServerAccessPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */