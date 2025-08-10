/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeWebCodec;
/*     */ import br.jus.cnj.pje.office.task.IOutputDocument;
/*     */ import br.jus.cnj.pje.office.task.IPjeEndpoint;
/*     */ import br.jus.cnj.pje.office.task.ISignableURLDocument;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.utils4j.ICanceller;
/*     */ import com.github.utils4j.IContentType;
/*     */ import com.github.utils4j.IResultChecker;
/*     */ import com.github.utils4j.imp.Objects;
/*     */ import com.github.utils4j.imp.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpPost;
/*     */ import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
/*     */ import org.apache.hc.client5.http.entity.mime.ByteArrayBody;
/*     */ import org.apache.hc.client5.http.entity.mime.ContentBody;
/*     */ import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
/*     */ import org.apache.hc.client5.http.entity.mime.StringBody;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.io.entity.StringEntity;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
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
/*     */ class PjeWebClient
/*     */   extends PjeClient<HttpPost>
/*     */ {
/*     */   PjeWebClient(Supplier<IPjeWebCodec> codec, Supplier<ICanceller> canceller) {
/*  61 */     super((Supplier)codec, canceller);
/*     */   }
/*     */   
/*     */   private HttpPost createPost(IPjeEndpoint endpoint) throws InterruptedException {
/*  65 */     return (HttpPost)createOutput(new HttpPost(endpoint.getPath()), endpoint);
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpPost createOutput(IPjeEndpoint endpoint, ISignedData signedData) throws Exception {
/*  70 */     HttpPost postRequest = createPost(endpoint);
/*  71 */     postRequest.setEntity((HttpEntity)new UrlEncodedFormEntity(Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("assinatura", signedData
/*  72 */                 .getSignature64()), new BasicNameValuePair("cadeiaCertificado", signedData
/*  73 */                 .getCertificateChain64()) })));
/*     */     
/*  75 */     return postRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpPost createOutput(IPjeEndpoint endpoint, ISignedData signedData, IOutputDocument document) throws Exception {
/*  80 */     HttpPost postRequest = createPost(endpoint);
/*  81 */     List<NameValuePair> parameters = new ArrayList<>();
/*  82 */     parameters.add(new BasicNameValuePair("assinatura", signedData.getSignature64()));
/*  83 */     parameters.add(new BasicNameValuePair("cadeiaCertificado", signedData.getCertificateChain64()));
/*  84 */     giveBack(document).forEach(nv -> parameters.add(new BasicNameValuePair((String)nv.getKey(), (String)nv.getValue())));
/*  85 */     postRequest.setEntity((HttpEntity)new UrlEncodedFormEntity(parameters));
/*  86 */     return postRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpPost createOutput(IPjeEndpoint endpoint, ISignableURLDocument file, IContentType contentType) throws Exception {
/*  91 */     byte[] signature = ((ISignedData)file.getSignedData().orElseThrow(br.jus.cnj.pje.office.task.imp.FileNotSignedException::new)).getSignature();
/*  92 */     MultipartEntityBuilder builder = MultipartEntityBuilder.create();
/*  93 */     builder.addPart(file.getSignatureFieldName(), (ContentBody)new ByteArrayBody(signature, 
/*     */           
/*  95 */           ContentType.create(contentType.getMineType(), contentType.getCharset()), (String)file
/*  96 */           .getNome().orElse("arquivo") + contentType.getExtension()));
/*     */     
/*  98 */     giveBack((IOutputDocument)file).forEach(nv -> builder.addPart((String)nv.getKey(), (ContentBody)new StringBody((String)nv.getValue(), ContentType.TEXT_PLAIN)));
/*  99 */     HttpPost postRequest = createPost(endpoint);
/* 100 */     postRequest.setEntity(builder.build());
/* 101 */     return postRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpPost createOutput(IPjeEndpoint endpoint, String certificateChain64) throws Exception {
/* 106 */     HttpPost postRequest = createPost(endpoint);
/* 107 */     postRequest.setEntity((HttpEntity)new UrlEncodedFormEntity(Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("cadeiaDeCertificadosBase64", certificateChain64) })));
/*     */ 
/*     */     
/* 110 */     return postRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpPost createOutput(IPjeEndpoint endpoint, Object pojo) throws Exception {
/* 115 */     HttpPost postRequest = createPost(endpoint);
/* 116 */     postRequest.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
/* 117 */     postRequest.setEntity((HttpEntity)new StringEntity(Objects.toJson(pojo), ContentType.APPLICATION_JSON));
/* 118 */     return postRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   protected IResultChecker getIfError() {
/* 123 */     return Checker.THROW_IF_ERROR;
/*     */   }
/*     */ 
/*     */   
/*     */   protected IResultChecker getIfNotSuccess() {
/* 128 */     return Checker.THROW_IF_NOT_SUCCESS;
/*     */   }
/*     */   
/*     */   protected enum Checker
/*     */     implements IResultChecker {
/* 133 */     THROW_IF_ERROR
/*     */     {
/*     */       public void handle(String response, int code) throws Exception {
/* 136 */         int length = response.length();
/* 137 */         if (response.startsWith("Erro:"))
/*     */         {
/* 139 */           String message = (length > "Erro:".length()) ? response.substring("Erro:".length()) : "Desconhecido";
/*     */           
/* 141 */           throw new PjeClientException("Servidor retornando - HTTP Code: " + code + ".\n" + PjeServerTracker.RESPONSE.mark(message));
/*     */         }
/*     */       
/*     */       }
/*     */     },
/* 146 */     THROW_IF_NOT_SUCCESS
/*     */     {
/*     */       public void handle(String response, int code) throws Exception {
/* 149 */         if (response.startsWith("Sucesso"))
/*     */           return; 
/* 151 */         THROW_IF_ERROR.handle(response, code);
/* 152 */         throw new PjeClientException("Servidor retornando - HTTP Code: " + code + ".\nServidor informa NÃO ter recebido os dados enviados vez que a string 'Sucesso' está ausente na resposta entregue ao assinador");
/*     */       }
/*     */     };
/*     */     
/*     */     private static final String SERVER_SUCCESS_RESPONSE = "Sucesso";
/*     */     private static final String SERVER_FAIL_RESPONSE = "Erro:";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeWebClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */