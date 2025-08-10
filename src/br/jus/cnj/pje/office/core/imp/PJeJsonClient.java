/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeJsonCodec;
/*     */ import br.jus.cnj.pje.office.task.IOutputDocument;
/*     */ import br.jus.cnj.pje.office.task.IPjeEndpoint;
/*     */ import br.jus.cnj.pje.office.task.ISignableURLDocument;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.utils4j.ICanceller;
/*     */ import com.github.utils4j.IContentType;
/*     */ import com.github.utils4j.IResultChecker;
/*     */ import com.github.utils4j.imp.Objects;
/*     */ import com.github.utils4j.imp.Pair;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PJeJsonClient
/*     */   extends PjeClient<JSONObject>
/*     */ {
/*     */   public PJeJsonClient(Supplier<IPjeJsonCodec> postCodec, Supplier<ICanceller> canceller) {
/*  52 */     super((Supplier)postCodec, canceller);
/*     */   }
/*     */ 
/*     */   
/*     */   protected IResultChecker getIfError() {
/*  57 */     return IResultChecker.NOTHING;
/*     */   }
/*     */ 
/*     */   
/*     */   protected IResultChecker getIfNotSuccess() {
/*  62 */     return IResultChecker.NOTHING;
/*     */   }
/*     */   
/*     */   protected <R extends JSONObject> R createOutput(R request, IPjeEndpoint target) {
/*  66 */     request.put("Cookie", target.getSession());
/*  67 */     request.put("versao", PjeVersion.CURRENT.toString());
/*  68 */     return request;
/*     */   }
/*     */   
/*     */   private JSONObject createOutput(IPjeEndpoint target) {
/*  72 */     JSONObject out = createOutput(new JSONObject(), target);
/*  73 */     out.put("endPoint", target.getPath());
/*  74 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JSONObject createOutput(IPjeEndpoint target, ISignedData signedData) throws Exception {
/*  79 */     JSONObject out = createOutput(target);
/*  80 */     out.put("assinatura", signedData.getSignature64());
/*  81 */     out.put("cadeiaCertificado", signedData.getCertificateChain64());
/*  82 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JSONObject createOutput(IPjeEndpoint target, ISignedData signedData, IOutputDocument document) throws Exception {
/*  87 */     JSONObject out = createOutput(target);
/*  88 */     out.put("assinatura", signedData.getSignature64());
/*  89 */     out.put("cadeiaCertificado", signedData.getCertificateChain64());
/*  90 */     giveBack(document).forEach(nv -> out.put((String)nv.getKey(), nv.getValue()));
/*  91 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JSONObject createOutput(IPjeEndpoint target, ISignableURLDocument document, IContentType contentType) throws Exception {
/*  96 */     byte[] signature = ((ISignedData)document.getSignedData().orElseThrow(br.jus.cnj.pje.office.task.imp.FileNotSignedException::new)).getSignature();
/*  97 */     JSONObject body = new JSONObject();
/*  98 */     body.put("mimeType", contentType.getMineType());
/*  99 */     body.put("charset", contentType.getCharset());
/* 100 */     body.put("fileName", (String)document.getNome().orElse("arquivo") + contentType.getExtension());
/* 101 */     body.put("signature", signature);
/* 102 */     JSONObject out = createOutput(target);
/* 103 */     out.put(document.getSignatureFieldName(), body);
/* 104 */     giveBack((IOutputDocument)document).forEach(nv -> out.put((String)nv.getKey(), nv.getValue()));
/* 105 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JSONObject createOutput(IPjeEndpoint target, String certificateChain64) throws Exception {
/* 110 */     JSONObject out = createOutput(target);
/* 111 */     out.put("cadeiaDeCertificadosBase64", certificateChain64);
/* 112 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JSONObject createOutput(IPjeEndpoint target, Object pojo) throws Exception {
/* 117 */     JSONObject out = createOutput(target);
/* 118 */     out.put("contentType", ContentType.APPLICATION_JSON.getMimeType());
/* 119 */     out.put("pojo", Objects.toJson(pojo));
/* 120 */     return out;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PJeJsonClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */