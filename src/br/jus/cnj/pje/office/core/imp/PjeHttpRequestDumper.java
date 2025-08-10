/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpEntity;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.NameValuePair;
/*    */ import org.apache.hc.core5.http.io.entity.EmptyInputStream;
/*    */ import org.apache.hc.core5.net.URIBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PjeHttpRequestDumper
/*    */   extends PjeAbstractRequestDumper
/*    */ {
/*    */   private final HttpRequest request;
/*    */   private final EntityDetails entity;
/*    */   
/*    */   public PjeHttpRequestDumper(HttpRequest request, EntityDetails entity) {
/* 29 */     this.request = (HttpRequest)Args.requireNonNull(request, "request is null");
/* 30 */     this.entity = entity;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final String getMethod() {
/* 35 */     return this.request.getMethod();
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void forEachHeaders(BiConsumer<String, List<String>> biconsumer) {
/* 40 */     Arrays.<Header>stream(this.request.getHeaders()).forEach(h -> biconsumer.accept(h.getName(), Arrays.asList(new String[] { h.getValue() })));
/*    */   }
/*    */ 
/*    */   
/*    */   protected final InputStream getBody() throws IOException {
/* 45 */     if (this.entity instanceof HttpEntity)
/* 46 */       return ((HttpEntity)this.entity).getContent(); 
/* 47 */     return (InputStream)EmptyInputStream.INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final String getURI() {
/*    */     try {
/* 53 */       return this.request.getUri().toString();
/* 54 */     } catch (URISyntaxException e) {
/* 55 */       LOGGER.warn("Não foi possível identificar a URI da requisição", e);
/* 56 */       return Strings.empty();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void forEachParams(BiConsumer<String, String> biconsumer) {
/*    */     try {
/* 63 */       (new URIBuilder(getURI())).getQueryParams().stream().forEach(nv -> biconsumer.accept(nv.getName(), nv.getValue()));
/* 64 */     } catch (URISyntaxException e) {
/* 65 */       LOGGER.warn("Não foi possível iterar sobre os parâmetros da requisição", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeHttpRequestDumper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */