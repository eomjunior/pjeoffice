/*    */ package org.apache.hc.core5.http;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Message<H extends MessageHeaders, B>
/*    */ {
/*    */   private final H head;
/*    */   private final B body;
/*    */   
/*    */   public Message(H head, B body) {
/* 46 */     this.head = (H)Args.notNull(head, "Message head");
/* 47 */     this.body = body;
/*    */   }
/*    */   
/*    */   public H getHead() {
/* 51 */     return this.head;
/*    */   }
/*    */   
/*    */   public B getBody() {
/* 55 */     return this.body;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "[head=" + this.head + ", body=" + this.body + ']';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */