/*    */ package br.jus.cnj.pje.office.core.imp.security.bypass;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
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
/*    */ public enum PjeRequestPathBypassers
/*    */   implements IPjeRequestPathBypasser
/*    */ {
/* 17 */   BACKEND_EMULATOR_API
/*    */   {
/*    */     private boolean isBackendEmulatorRequest(String requestPath) {
/* 20 */       return "/pjeOffice/fakeBackend".equals(requestPath);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public final boolean isBypassable(String requestPath) {
/* 26 */       return (PjeSecurity.GRANTOR.isDevMode() && isBackendEmulatorRequest(requestPath));
/*    */     }
/*    */   },
/* 29 */   PUBLIC_API
/*    */   {
/*    */     private boolean isShutdownRequest(String requestPath) {
/* 32 */       return "/pjeOffice/shutdown".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean isFaviconRequest(String requestPath) {
/* 37 */       return "/favicon.ico".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean isPingRequest(String requestPath) {
/* 42 */       return "/pjeOffice/".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean isLoggerRequest(String requestPath) {
/* 47 */       return "/pjeOffice/logger/".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean isAboutRequest(String requestPath) {
/* 52 */       return "/pjeOffice/versao/".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean isMinimumRequest(String requestPath) {
/* 57 */       return "/pjeOffice/fakeBackend/minimum".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean isXmlRequest(String requestPath) {
/* 62 */       return "/pjeOffice/fakeBackend/xml".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean isWelcomeRequest(String requestPath) {
/* 67 */       return "/pjeOffice/welcome".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     public final boolean isBypassable(String requestPath) {
/* 72 */       return (isFaviconRequest(requestPath) || isPingRequest(requestPath) || isLoggerRequest(requestPath) || isAboutRequest(requestPath) || isXmlRequest(requestPath) || isMinimumRequest(requestPath) || isShutdownRequest(requestPath) || isWelcomeRequest(requestPath));
/*    */     }
/*    */   },
/* 75 */   PRIVATE_API
/*    */   {
/*    */     private boolean isLogoutRequest(String requestPath) {
/* 78 */       return "/pjeOffice/logout".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean isTaskRequest(String requestPath) {
/* 83 */       return "/pjeOffice/requisicao/".equals(requestPath);
/*    */     }
/*    */ 
/*    */     
/*    */     public final boolean isBypassable(String requestPath) {
/* 88 */       return (!isLogoutRequest(requestPath) && !isTaskRequest(requestPath));
/*    */     }
/*    */   },
/* 91 */   DEFAULT
/*    */   {
/*    */     public final boolean isBypassable(String requestPath) {
/* 94 */       return (PRIVATE_API.isBypassable(requestPath) && (PUBLIC_API.isBypassable(requestPath) || BACKEND_EMULATOR_API.isBypassable(requestPath)));
/*    */     }
/*    */   };
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/security/bypass/PjeRequestPathBypassers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */