/*    */ package br.jus.cnj.pje.office.core.imp.security;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
/*    */ import com.github.utils4j.IRequestRejectNotifier;
/*    */ import com.github.utils4j.imp.PreflightFilter;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PjePreflightFilter
/*    */   extends PreflightFilter
/*    */ {
/*    */   public PjePreflightFilter(IRequestRejectNotifier rejector) {
/* 17 */     super(rejector, PjeHeaders.RESPONSE);
/*    */   }
/*    */ 
/*    */   
/*    */   public String description() {
/* 22 */     return super.description() + ". Esta implementação integra o mecanismo de segurança do PjeOffice ao controle de preflight's";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected final boolean isAcceptable(PreflightFilter.PreflightPack pck, StringBuilder whyNot) {
/* 28 */     if (!PjeSecurity.GRANTOR.isPermitted(pck.origin)) {
/*    */       
/* 30 */       whyNot.append("Whitelist fail - tentativa de acesso indevido a partir da origem: " + pck.origin);
/* 31 */       return false;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 36 */     boolean isPreflightableMethod = "PUT".equals(pck.acrmHeader);
/*    */     
/* 38 */     if (isPreflightableMethod) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 45 */       boolean hasForcePreflightHeader = pck.acrhHeaders.stream().map(Strings::trim).map(String::toLowerCase).filter(h -> h.contains("Force-Preflight".toLowerCase())).findAny().isPresent();
/*    */       
/* 47 */       if (!hasForcePreflightHeader)
/*    */       {
/*    */ 
/*    */ 
/*    */         
/* 52 */         whyNot.append("Requisições PUT devem ser acompanhadas do cabeçalho 'Force-Preflight' em ACCESS_CONTROL_ALLOW_HEADERS");
/*    */       }
/*    */ 
/*    */       
/* 56 */       return hasForcePreflightHeader;
/*    */     } 
/*    */ 
/*    */     
/* 60 */     boolean isGetMethod = "GET".equals(pck.acrmHeader);
/*    */     
/* 62 */     if (!isGetMethod)
/*    */     {
/* 64 */       whyNot.append("Método '" + pck.acrmHeader + "' em " + "Access-Control-Request-Method" + " não é aceito em requisições preflight");
/*    */     }
/*    */ 
/*    */     
/* 68 */     return isGetMethod;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/security/PjePreflightFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */