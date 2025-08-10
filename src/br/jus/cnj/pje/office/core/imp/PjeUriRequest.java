/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.net.URI;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import org.apache.hc.core5.http.NameValuePair;
/*    */ import org.apache.hc.core5.net.URIBuilder;
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
/*    */ abstract class PjeUriRequest
/*    */   implements IPjeRequest
/*    */ {
/*    */   private final URI uri;
/*    */   private String id;
/*    */   private List<NameValuePair> queryParams;
/*    */   
/*    */   protected PjeUriRequest(URI input) {
/* 51 */     this.uri = input;
/*    */   }
/*    */   
/*    */   private List<NameValuePair> queryParams() {
/* 55 */     return (this.queryParams != null) ? this.queryParams : (this.queryParams = (new URIBuilder(this.uri)).getQueryParams());
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getParameterR() {
/* 60 */     return getParameter("r");
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getParameterU() {
/* 65 */     return getParameter("u");
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<String> getOrigin() {
/* 70 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPreflightable() {
/* 75 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInternal() {
/* 80 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getId() {
/* 85 */     return (this.id != null) ? this.id : (this.id = this.uri.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 90 */     return getId();
/*    */   }
/*    */   
/*    */   protected Optional<String> getParameter(String key) {
/* 94 */     return queryParams().stream().filter(n -> n.getName().equalsIgnoreCase(key)).map(NameValuePair::getValue).filter(Strings::hasText).findFirst();
/*    */   }
/*    */   
/*    */   final void forEachParams(BiConsumer<String, String> consumer) {
/* 98 */     queryParams().forEach(nv -> consumer.accept(nv.getName(), nv.getValue()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeUriRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */