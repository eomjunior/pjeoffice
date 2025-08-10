/*    */ package br.jus.cnj.pje.office.task;
/*    */ 
/*    */ import java.beans.Transient;
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
/*    */ public interface IPayload
/*    */ {
/* 35 */   public static final String PJE_PAYLOAD_REQUEST_PARAM = IPayload.class.getSimpleName() + ".instance";
/*    */   
/*    */   Optional<String> getServidor();
/*    */   
/*    */   Optional<String> getAplicacao();
/*    */   
/*    */   Optional<String> getSessao();
/*    */   
/*    */   Optional<String> getCodigoSeguranca();
/*    */   
/*    */   Optional<String> getTarefaId();
/*    */   
/*    */   Optional<String> getTarefa();
/*    */   
/*    */   @Transient
/*    */   Optional<String> getOrigin();
/*    */   
/*    */   @Transient
/*    */   boolean isFromPreflightableRequest();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/IPayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */