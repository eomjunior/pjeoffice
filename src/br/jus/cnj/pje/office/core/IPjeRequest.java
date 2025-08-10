/*    */ package br.jus.cnj.pje.office.core;
/*    */ 
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
/*    */ 
/*    */ public interface IPjeRequest
/*    */ {
/*    */   public static final String PJE_REQUEST_PARAMETER_NAME = "r";
/*    */   public static final String PJE_REQUEST_PARAMETER_CACHE = "u";
/* 38 */   public static final String PJE_REQUEST_INSTANCE = IPjeRequest.class.getSimpleName() + ".request";
/*    */   
/*    */   String getId();
/*    */   
/*    */   Optional<String> getParameterR();
/*    */   
/*    */   Optional<String> getParameterU();
/*    */   
/*    */   Optional<String> getOrigin();
/*    */   
/*    */   boolean isPreflightable();
/*    */   
/*    */   boolean isInternal();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */