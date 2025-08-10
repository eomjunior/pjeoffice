/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.InputStream;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ 
/*    */ class PjeHttpExchangeRequestDumper
/*    */   extends PjeAbstractRequestDumper
/*    */ {
/*    */   private final PjeHttpExchangeRequest request;
/*    */   
/*    */   public PjeHttpExchangeRequestDumper(PjeHttpExchangeRequest request, String warning) {
/* 14 */     super(warning);
/* 15 */     this.request = (PjeHttpExchangeRequest)Args.requireNonNull(request, "request is null");
/*    */   }
/*    */ 
/*    */   
/*    */   protected final String getURI() {
/* 20 */     return this.request.toURI().toString();
/*    */   }
/*    */ 
/*    */   
/*    */   protected final String getMethod() {
/* 25 */     return this.request.getMethod();
/*    */   }
/*    */ 
/*    */   
/*    */   protected final InputStream getBody() {
/* 30 */     return this.request.body();
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void forEachParams(BiConsumer<String, String> biconsumer) {
/* 35 */     this.request.forEachParams(biconsumer);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void forEachHeaders(BiConsumer<String, List<String>> biconsumer) {
/* 40 */     this.request.forEachHeaders(biconsumer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeHttpExchangeRequestDumper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */