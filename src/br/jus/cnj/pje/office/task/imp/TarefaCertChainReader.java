/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaCertChain;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.taskresolver4j.imp.RequestReader;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.IOException;
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
/*    */ final class TarefaCertChainReader
/*    */   extends RequestReader<Params, ITarefaCertChain>
/*    */ {
/* 43 */   static final TarefaCertChainReader INSTANCE = new TarefaCertChainReader();
/*    */ 
/*    */   
/*    */   static class TarefaCertChain
/*    */     implements ITarefaCertChain
/*    */   {
/*    */     private String uploadUrl;
/*    */     private boolean deslogarKeyStore = false;
/*    */     
/*    */     public Optional<String> getUploadUrl() {
/* 53 */       return Strings.optional(this.uploadUrl);
/*    */     }
/*    */     
/*    */     public boolean isDeslogarKeyStore() {
/* 57 */       return this.deslogarKeyStore;
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaCertChainReader() {
/* 62 */     super(TarefaCertChain.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params params, ITarefaCertChain pojo) throws IOException {
/* 67 */     return (ITask<?>)new PjeCertificateChainReaderTask(params, pojo);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaCertChainReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */