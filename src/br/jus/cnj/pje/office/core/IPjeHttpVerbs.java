/*    */ package br.jus.cnj.pje.office.core;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.imp.security.bypass.PjeRequestPathBypassers;
/*    */ import com.github.utils4j.imp.IHttpVerbs;
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
/*    */ public interface IPjeHttpVerbs
/*    */   extends IHttpVerbs
/*    */ {
/*    */   public static final String PREFLIGHTABLE = "PUT";
/*    */   
/*    */   static String complianceList() {
/* 24 */     return "PUT, GET, OPTIONS";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static boolean isComplianceable(String method) {
/* 31 */     return ("PUT".equals(method) || "GET".equals(method) || "OPTIONS".equals(method));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static boolean isComplianceable(String method, String requestPath) {
/* 40 */     return (isComplianceable(method) || ("POST".equals(method) && PjeRequestPathBypassers.BACKEND_EMULATOR_API.isBypassable(requestPath)));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeHttpVerbs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */