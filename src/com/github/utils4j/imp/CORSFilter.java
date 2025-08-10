/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.ICorsHeadersProvider;
/*    */ import com.github.utils4j.IRequestRejectNotifier;
/*    */ import com.sun.net.httpserver.Headers;
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
/*    */ public abstract class CORSFilter
/*    */   extends RejectableFilter
/*    */ {
/*    */   private final ICorsHeadersProvider cors;
/*    */   
/*    */   protected CORSFilter(IRequestRejectNotifier rejector, ICorsHeadersProvider cors) {
/* 22 */     super(rejector);
/* 23 */     this.cors = Args.<ICorsHeadersProvider>requireNonNull(cors, "cors is null");
/*    */   }
/*    */   
/*    */   private void noCache(Headers response) {
/* 27 */     response.set("Cache-Control", "no-store");
/* 28 */     response.set("Vary", "Origin");
/*    */   }
/*    */   
/*    */   protected final void reject(Headers response) {
/* 32 */     response.clear();
/* 33 */     noCache(response);
/*    */   }
/*    */   
/*    */   protected void accept(Headers response, String origin) {
/* 37 */     response.set("Access-Control-Allow-Origin", origin);
/* 38 */     noCache(response);
/*    */   }
/*    */   
/*    */   protected void accept(Headers response, String origin, boolean isOptionsVerb) {
/* 42 */     accept(response, origin);
/* 43 */     if (isOptionsVerb) {
/* 44 */       this.cors.getAccessControlAllowMethods().ifPresent(h -> response.set("Access-Control-Allow-Methods", h));
/* 45 */       this.cors.getAccessControlAllowHeaders().ifPresent(h -> response.set("Access-Control-Allow-Headers", h));
/* 46 */       this.cors.getAccessControlAllowCredentials().ifPresent(h -> response.set("Access-Control-Allow-Credentials", h));
/* 47 */       this.cors.getAccessControlAllowPrivateNetwork().ifPresent(h -> response.set("Access-Control-Allow-Private-Network", h));
/* 48 */       this.cors.getAccessControlMaxAgeHeader().ifPresent(h -> response.set("Access-Control-Max-Age", h));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/CORSFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */