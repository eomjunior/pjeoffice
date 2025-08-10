/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.classic.ExecChain;
/*    */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpException;
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
/*    */ class ExecChainElement
/*    */ {
/*    */   private final ExecChainHandler handler;
/*    */   private final ExecChainElement next;
/*    */   
/*    */   ExecChainElement(ExecChainHandler handler, ExecChainElement next) {
/* 44 */     this.handler = handler;
/* 45 */     this.next = next;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassicHttpResponse execute(ClassicHttpRequest request, ExecChain.Scope scope) throws IOException, HttpException {
/* 51 */     return this.handler.execute(request, scope, (this.next != null) ? this.next::execute : null);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "{handler=" + this.handler
/* 57 */       .getClass() + ", next=" + ((this.next != null) ? (String)this.next.handler
/* 58 */       .getClass() : "null") + '}';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/ExecChainElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */