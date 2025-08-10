/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*    */ import br.jus.cnj.pje.office.task.IPayload;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class PayloadRequest
/*    */   extends PayloadWrapper
/*    */ {
/*    */   private final IPjeRequest request;
/*    */   
/*    */   public PayloadRequest(IPayload payload, IPjeRequest request) {
/* 42 */     super(payload);
/* 43 */     this.request = (IPjeRequest)Args.requireNonNull(request, "request is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getOrigin() {
/* 48 */     return this.request.getOrigin();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isFromPreflightableRequest() {
/* 53 */     return this.request.isPreflightable();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PayloadRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */