/*    */ package org.apache.hc.core5.http;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnsupportedHttpVersionException
/*    */   extends ProtocolException
/*    */ {
/*    */   private static final long serialVersionUID = -1348448090193107031L;
/*    */   
/*    */   public UnsupportedHttpVersionException() {}
/*    */   
/*    */   public UnsupportedHttpVersionException(ProtocolVersion protocolVersion) {
/* 52 */     super("Unsupported version: " + protocolVersion);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnsupportedHttpVersionException(String message) {
/* 61 */     super(message);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/UnsupportedHttpVersionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */