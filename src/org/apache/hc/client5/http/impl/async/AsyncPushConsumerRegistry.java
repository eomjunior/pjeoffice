/*    */ package org.apache.hc.client5.http.impl.async;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.apache.hc.core5.function.Supplier;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*    */ import org.apache.hc.core5.http.protocol.UriPatternMatcher;
/*    */ import org.apache.hc.core5.net.URIAuthority;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ class AsyncPushConsumerRegistry
/*    */ {
/* 47 */   private final UriPatternMatcher<Supplier<AsyncPushConsumer>> primary = new UriPatternMatcher();
/* 48 */   private final ConcurrentMap<String, UriPatternMatcher<Supplier<AsyncPushConsumer>>> hostMap = new ConcurrentHashMap<>();
/*    */ 
/*    */   
/*    */   private UriPatternMatcher<Supplier<AsyncPushConsumer>> getPatternMatcher(String hostname) {
/* 52 */     if (hostname == null) {
/* 53 */       return this.primary;
/*    */     }
/* 55 */     UriPatternMatcher<Supplier<AsyncPushConsumer>> hostMatcher = this.hostMap.get(hostname);
/* 56 */     if (hostMatcher != null) {
/* 57 */       return hostMatcher;
/*    */     }
/* 59 */     return this.primary;
/*    */   }
/*    */   
/*    */   public AsyncPushConsumer get(HttpRequest request) {
/* 63 */     Args.notNull(request, "Request");
/* 64 */     URIAuthority authority = request.getAuthority();
/* 65 */     String key = (authority != null) ? authority.getHostName().toLowerCase(Locale.ROOT) : null;
/* 66 */     UriPatternMatcher<Supplier<AsyncPushConsumer>> patternMatcher = getPatternMatcher(key);
/* 67 */     if (patternMatcher == null) {
/* 68 */       return null;
/*    */     }
/* 70 */     String path = request.getPath();
/* 71 */     int i = path.indexOf('?');
/* 72 */     if (i != -1) {
/* 73 */       path = path.substring(0, i);
/*    */     }
/* 75 */     Supplier<AsyncPushConsumer> supplier = (Supplier<AsyncPushConsumer>)patternMatcher.lookup(path);
/* 76 */     return (supplier != null) ? (AsyncPushConsumer)supplier.get() : null;
/*    */   }
/*    */   
/*    */   public void register(String hostname, String uriPattern, Supplier<AsyncPushConsumer> supplier) {
/* 80 */     Args.notBlank(uriPattern, "URI pattern");
/* 81 */     Args.notNull(supplier, "Supplier");
/* 82 */     if (hostname == null) {
/* 83 */       this.primary.register(uriPattern, supplier);
/*    */     } else {
/* 85 */       String key = hostname.toLowerCase(Locale.ROOT);
/* 86 */       UriPatternMatcher<Supplier<AsyncPushConsumer>> matcher = this.hostMap.get(key);
/* 87 */       if (matcher == null) {
/* 88 */         UriPatternMatcher<Supplier<AsyncPushConsumer>> newMatcher = new UriPatternMatcher();
/* 89 */         matcher = this.hostMap.putIfAbsent(key, newMatcher);
/* 90 */         if (matcher == null) {
/* 91 */           matcher = newMatcher;
/*    */         }
/*    */       } 
/* 94 */       matcher.register(uriPattern, supplier);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/AsyncPushConsumerRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */