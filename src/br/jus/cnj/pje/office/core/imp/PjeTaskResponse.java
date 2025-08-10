/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.taskresolver4j.ITaskResponse;
/*    */ import java.io.IOException;
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
/*    */ public abstract class PjeTaskResponse
/*    */   implements ITaskResponse<IPjeResponse>
/*    */ {
/*    */   private final boolean success;
/*    */   
/*    */   protected PjeTaskResponse(boolean success) {
/* 41 */     this.success = success;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isSuccess() {
/* 46 */     return this.success;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final void processResponse(IPjeResponse response) throws IOException {
/* 52 */     Thread.interrupted();
/* 53 */     doProcessResponse(response);
/*    */   }
/*    */   
/*    */   protected void doProcessResponse(IPjeResponse response) throws IOException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeTaskResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */