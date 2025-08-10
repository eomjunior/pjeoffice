/*    */ package br.jus.cnj.pje.office.core.imp.security;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeCustomHeaders;
/*    */ import br.jus.cnj.pje.office.core.IPjeHttpVerbs;
/*    */ import com.github.utils4j.ICorsHeadersProvider;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.util.Optional;
/*    */ 
/*    */ enum PjeHeaders
/*    */   implements ICorsHeadersProvider, IPjeCustomHeaders
/*    */ {
/* 12 */   RESPONSE; private static final Optional<String> DEFAULT_ACCESS_CONTROL_ALLOW_PRIVATE_NETWORK; private static final Optional<String> DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS;
/*    */   static {
/* 14 */     DEFAULT_ACCESS_CONTROL_MAX_AGE = Optional.of("0");
/*    */     
/* 16 */     DEFAULT_ACCESS_CONTROL_ALLOW_METHODS = Optional.of(
/* 17 */         IPjeHttpVerbs.complianceList());
/*    */ 
/*    */     
/* 20 */     DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS = Optional.of("Origin, X-Requested-With, Content-Type, Accept, Force-Preflight");
/*    */ 
/*    */ 
/*    */     
/* 24 */     DEFAULT_ACCESS_CONTROL_ALLOW_PRIVATE_NETWORK = Strings.trueOptional();
/*    */   }
/*    */   private static final Optional<String> DEFAULT_ACCESS_CONTROL_ALLOW_METHODS; private static final Optional<String> DEFAULT_ACCESS_CONTROL_MAX_AGE;
/*    */   public final Optional<String> getAccessControlAllowMethods() {
/* 28 */     return DEFAULT_ACCESS_CONTROL_ALLOW_METHODS;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getAccessControlAllowHeaders() {
/* 33 */     return DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getAccessControlAllowPrivateNetwork() {
/* 38 */     return DEFAULT_ACCESS_CONTROL_ALLOW_PRIVATE_NETWORK;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getAccessControlMaxAgeHeader() {
/* 43 */     return DEFAULT_ACCESS_CONTROL_MAX_AGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getAccessControlAllowCredentials() {
/* 48 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/security/PjeHeaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */