/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import br.jus.cnj.pje.office.task.IPayload;
/*    */ import br.jus.cnj.pje.office.task.imp.PayloadRequest;
/*    */ import br.jus.cnj.pje.office.task.imp.PayloadRequestReader;
/*    */ import com.github.taskresolver4j.IRequestResolver;
/*    */ import com.github.taskresolver4j.ITaskRequest;
/*    */ import com.github.taskresolver4j.exception.TaskResolverException;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.io.IOException;
/*    */ import java.util.function.Function;
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
/*    */ enum PjeRequestResolver
/*    */   implements IRequestResolver<IPjeRequest, IPjeResponse, PjeTaskRequest>
/*    */ {
/* 44 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public PjeTaskRequest resolve(IPjeRequest request) throws TaskResolverException {
/*    */     PjeTaskRequest tr;
/* 49 */     request.getParameterU().orElseThrow(() -> new TaskResolverException("Parameter 'u' not found! (browser cache issue)"));
/*    */ 
/*    */ 
/*    */     
/* 53 */     String r = (String)request.getParameterR().orElseThrow(() -> new TaskResolverException("Unabled to resolve task with empty request 'r' param (nothing to do)"));
/*    */ 
/*    */ 
/*    */     
/* 57 */     Function<IPayload, IPayload> payloadRequest = m -> new PayloadRequest(m, request);
/*    */ 
/*    */     
/*    */     try {
/* 61 */       tr = (PjeTaskRequest)PayloadRequestReader.PAYLOAD.read(r, (Params)new PjeTaskRequest(request), payloadRequest);
/* 62 */     } catch (IOException e) {
/* 63 */       throw new TaskResolverException("Unabled to read 'r' request parameter: " + r, e);
/*    */     } 
/*    */     
/* 66 */     StringBuilder whyNot = new StringBuilder();
/* 67 */     if (!tr.isValid(whyNot)) {
/* 68 */       throw new TaskResolverException("Unabled to read 'r' request parameter because: " + whyNot);
/*    */     }
/*    */     
/* 71 */     return tr;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeRequestResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */