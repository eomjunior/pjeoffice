/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccessPermissionChecker;
/*     */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
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
/*     */ enum PjePermissionChecker
/*     */   implements IPjeServerAccessPermissionChecker
/*     */ {
/*  39 */   DEVMODE
/*     */   {
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkAccessPermission(IPjeServerAccess token) throws PjePermissionDeniedException {}
/*     */   },
/*  46 */   DENY_ALL
/*     */   {
/*     */     public void checkAccessPermission(IPjeServerAccess access) throws PjePermissionDeniedException {
/*  49 */       throw new PjePermissionDeniedException("Permissão negada (deny all implementation)");
/*     */     }
/*     */   },
/*     */   
/*  53 */   PRODUCTION
/*     */   {
/*     */     public void checkAccessPermission(IPjeServerAccess access) throws PjePermissionDeniedException {
/*     */       String cleanCode;
/*  57 */       Args.requireNonNull(access, "access is null");
/*     */ 
/*     */       
/*     */       try {
/*  61 */         cleanCode = PjeSecurity.decrypt(access.getCode());
/*  62 */       } catch (Exception e) {
/*  63 */         throw new PjePermissionDeniedException("Não foi possível descriptografar código de segurança. Acesso negado!");
/*     */       } 
/*     */       
/*  66 */       int dots = cleanCode.indexOf(':');
/*  67 */       if (dots < 0 || dots == cleanCode.length() - 1) {
/*  68 */         throw new PjePermissionDeniedException("Parâmetro em formato inválido: " + cleanCode + ". Contactar CNJ ");
/*     */       }
/*     */       
/*  71 */       String appCode = Strings.trim(cleanCode.substring(0, dots));
/*  72 */       if (!access.getApp().equals(appCode)) {
/*  73 */         throw new PjePermissionDeniedException(String.format("Código de segurança inválido. Parâmetro 'aplicacao' não confere: '%s' e '%s'. Acesso negado!", new Object[] { access
/*  74 */                 .getApp(), appCode }));
/*     */       }
/*     */       
/*  77 */       String accessServer = slashServer(access.getServer());
/*     */       
/*  79 */       String urls = Strings.trim(cleanCode.substring(dots + 1));
/*     */       do {
/*     */         String url;
/*  82 */         int comma = urls.indexOf(',');
/*  83 */         if (comma < 0) {
/*  84 */           url = urls;
/*  85 */           urls = "";
/*     */         } else {
/*  87 */           url = Strings.trim(urls.substring(0, comma));
/*  88 */           urls = Strings.trim(urls.substring(comma + 1));
/*     */         } 
/*  90 */         if (accessServer.equalsIgnoreCase(slashServer(url))) {
/*     */           return;
/*     */         }
/*  93 */       } while (!urls.isEmpty());
/*     */       
/*  95 */       throw new PjeServerAccessPermissionException("Acesso ao site: " + access.getServer() + " não autorizado");
/*     */     }
/*     */   };
/*     */   
/*     */   private static String slashServer(String address) {
/* 100 */     return address.endsWith("/") ? address : (address + "/");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjePermissionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */