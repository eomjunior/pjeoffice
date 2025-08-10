/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.taskresolver4j.imp.DefaultTaskRequest;
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
/*    */ public class PjeTaskRequest
/*    */   extends DefaultTaskRequest<IPjeResponse>
/*    */ {
/*    */   public PjeTaskRequest(IPjeRequest request) {
/* 38 */     of(IPjeRequest.PJE_REQUEST_INSTANCE, request);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeTaskRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */