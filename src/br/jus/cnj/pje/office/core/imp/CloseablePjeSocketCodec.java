/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeSocketCodec;
/*    */ import com.github.utils4j.IDownloadStatus;
/*    */ import com.github.utils4j.IResultChecker;
/*    */ import com.github.utils4j.imp.States;
/*    */ import com.github.utils4j.imp.function.IProvider;
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.classic.methods.HttpGet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class CloseablePjeSocketCodec<T>
/*    */   implements IPjeSocketCodec<T>
/*    */ {
/*    */   private boolean closed;
/*    */   
/*    */   public final boolean isClosed() {
/* 49 */     return this.closed;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void close() throws IOException {
/* 54 */     if (!isClosed()) {
/* 55 */       doClose();
/* 56 */       this.closed = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final PjeTaskResponse post(IProvider<T> provider, IResultChecker checker) throws Exception {
/* 62 */     States.requireFalse(isClosed(), "o codec já foi fechado");
/* 63 */     return doPost(provider, checker);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void get(IProvider<HttpGet> provider, IDownloadStatus status) throws Exception {
/* 68 */     States.requireFalse(isClosed(), "o codec já foi fechado");
/* 69 */     doGet(provider, status);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String get(IProvider<HttpGet> provider) throws Exception {
/* 74 */     States.requireFalse(isClosed(), "o codec já foi fechado");
/* 75 */     return doGet(provider);
/*    */   }
/*    */   
/*    */   protected void doClose() {}
/*    */   
/*    */   protected abstract PjeTaskResponse doPost(IProvider<T> paramIProvider, IResultChecker paramIResultChecker) throws Exception;
/*    */   
/*    */   protected abstract void doGet(IProvider<HttpGet> paramIProvider, IDownloadStatus paramIDownloadStatus) throws Exception;
/*    */   
/*    */   protected abstract String doGet(IProvider<HttpGet> paramIProvider) throws Exception;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/CloseablePjeSocketCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */