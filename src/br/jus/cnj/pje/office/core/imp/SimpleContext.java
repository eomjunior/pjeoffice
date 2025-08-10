/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeContext;
/*    */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Pair;
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
/*    */ public final class SimpleContext
/*    */   implements IPjeContext
/*    */ {
/*    */   private final Pair<IPjeRequest, IPjeResponse> pair;
/*    */   
/*    */   public static SimpleContext of(IPjeRequest request, IPjeResponse response) {
/* 42 */     Args.requireNonNull(request, "request is null");
/* 43 */     Args.requireNonNull(response, "response is null");
/* 44 */     return new SimpleContext(request, response);
/*    */   }
/*    */   
/*    */   private SimpleContext(IPjeRequest request, IPjeResponse response) {
/* 48 */     this.pair = Pair.of(request, response);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getId() {
/* 53 */     return getRequest().getId();
/*    */   }
/*    */ 
/*    */   
/*    */   public final IPjeRequest getRequest() {
/* 58 */     return (IPjeRequest)this.pair.getKey();
/*    */   }
/*    */ 
/*    */   
/*    */   public final IPjeResponse getResponse() {
/* 63 */     return (IPjeResponse)this.pair.getValue();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/SimpleContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */