/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeClient;
/*    */ import br.jus.cnj.pje.office.task.IOutputDocument;
/*    */ import br.jus.cnj.pje.office.task.IPjeEndpoint;
/*    */ import br.jus.cnj.pje.office.task.ISSOPayload;
/*    */ import br.jus.cnj.pje.office.task.ISignableURLDocument;
/*    */ import com.github.signer4j.ISignedData;
/*    */ import com.github.utils4j.IContentType;
/*    */ import com.github.utils4j.IDownloadStatus;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ class PjeClientWrapper
/*    */   implements IPjeClient
/*    */ {
/*    */   private final IPjeClient client;
/*    */   
/*    */   protected PjeClientWrapper(IPjeClient client) {
/* 46 */     this.client = (IPjeClient)Args.requireNonNull(client, "client is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void download(IPjeEndpoint target, IDownloadStatus status) throws PjeClientException, InterruptedException {
/* 51 */     this.client.download(target, status);
/*    */   }
/*    */ 
/*    */   
/*    */   public PjeTaskResponse send(IPjeEndpoint target, ISignedData signedData) throws PjeClientException, InterruptedException {
/* 56 */     return this.client.send(target, signedData);
/*    */   }
/*    */ 
/*    */   
/*    */   public PjeTaskResponse send(IPjeEndpoint target, ISignedData signedData, IOutputDocument document) throws PjeClientException, InterruptedException {
/* 61 */     return this.client.send(target, signedData, document);
/*    */   }
/*    */ 
/*    */   
/*    */   public PjeTaskResponse send(IPjeEndpoint target, ISignableURLDocument file, IContentType contentType) throws PjeClientException, InterruptedException {
/* 66 */     return this.client.send(target, file, contentType);
/*    */   }
/*    */ 
/*    */   
/*    */   public PjeTaskResponse send(IPjeEndpoint target, Object pojo) throws PjeClientException, InterruptedException {
/* 71 */     return this.client.send(target, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   public PjeTaskResponse send(IPjeEndpoint target, String certificateChain64) throws PjeClientException, InterruptedException {
/* 76 */     return this.client.send(target, certificateChain64);
/*    */   }
/*    */ 
/*    */   
/*    */   public PjeTaskResponse send(IPjeEndpoint target, ISSOPayload payload) throws PjeClientException, InterruptedException {
/* 81 */     return this.client.send(target, payload);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClientWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */