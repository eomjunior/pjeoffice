/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeJsonCodec;
/*    */ import com.github.utils4j.ICanceller;
/*    */ import com.github.utils4j.IDownloadStatus;
/*    */ import com.github.utils4j.IGetCodec;
/*    */ import com.github.utils4j.IResultChecker;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.function.IProvider;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.function.Supplier;
/*    */ import org.apache.hc.client5.http.classic.methods.HttpGet;
/*    */ import org.json.JSONObject;
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
/*    */ class PjeClipClient
/*    */   extends PjeClientWrapper
/*    */ {
/*    */   PjeClipClient(Supplier<IPjeJsonCodec> codec, Supplier<ICanceller> canceller) {
/* 48 */     super(new PJeJsonClient(codec, canceller));
/*    */   }
/*    */   
/*    */   static class PjeClipJsonCodec extends CloseablePjeSocketCodec<JSONObject> implements IPjeJsonCodec {
/*    */     private final Supplier<IGetCodec> codec;
/*    */     private final Charset charset;
/*    */     
/*    */     PjeClipJsonCodec(Charset charset, Supplier<IGetCodec> codec) {
/* 56 */       this.charset = (Charset)Args.requireNonNull(charset, "charset is null");
/* 57 */       this.codec = (Supplier<IGetCodec>)Args.requireNonNull(codec, "downloader is null");
/*    */     }
/*    */ 
/*    */     
/*    */     protected PjeTaskResponse doPost(IProvider<JSONObject> provider, IResultChecker checker) throws Exception {
/* 62 */       return new PjeClipTaskResponse(((JSONObject)provider.get()).toString(), this.charset);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void doGet(IProvider<HttpGet> provider, IDownloadStatus status) throws Exception {
/* 67 */       ((IGetCodec)this.codec.get()).get(provider, status);
/*    */     }
/*    */ 
/*    */     
/*    */     protected String doGet(IProvider<HttpGet> provider) throws Exception {
/* 72 */       return ((IGetCodec)this.codec.get()).get(provider);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClipClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */