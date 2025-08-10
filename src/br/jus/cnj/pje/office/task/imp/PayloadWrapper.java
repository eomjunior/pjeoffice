/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
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
/*    */ public abstract class PayloadWrapper
/*    */   implements IPayload
/*    */ {
/*    */   private final IPayload payload;
/*    */   
/*    */   protected PayloadWrapper(IPayload payload) {
/* 41 */     this.payload = (IPayload)Args.requireNonNull(payload, "payload is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getServidor() {
/* 46 */     return this.payload.getServidor();
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getAplicacao() {
/* 51 */     return this.payload.getAplicacao();
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getSessao() {
/* 56 */     return this.payload.getSessao();
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getCodigoSeguranca() {
/* 61 */     return this.payload.getCodigoSeguranca();
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getTarefaId() {
/* 66 */     return this.payload.getTarefaId();
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getTarefa() {
/* 71 */     return this.payload.getTarefa();
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getOrigin() {
/* 76 */     return this.payload.getOrigin();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PayloadWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */