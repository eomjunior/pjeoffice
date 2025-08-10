/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.io.HttpMessageWriter;
/*    */ import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
/*    */ import org.apache.hc.core5.http.message.BasicLineFormatter;
/*    */ import org.apache.hc.core5.http.message.LineFormatter;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ public class DefaultHttpRequestWriterFactory
/*    */   implements HttpMessageWriterFactory<ClassicHttpRequest>
/*    */ {
/* 46 */   public static final DefaultHttpRequestWriterFactory INSTANCE = new DefaultHttpRequestWriterFactory();
/*    */   
/*    */   private final LineFormatter lineFormatter;
/*    */ 
/*    */   
/*    */   public DefaultHttpRequestWriterFactory(LineFormatter lineFormatter) {
/* 52 */     this.lineFormatter = (lineFormatter != null) ? lineFormatter : (LineFormatter)BasicLineFormatter.INSTANCE;
/*    */   }
/*    */   
/*    */   public DefaultHttpRequestWriterFactory() {
/* 56 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpMessageWriter<ClassicHttpRequest> create() {
/* 61 */     return new DefaultHttpRequestWriter(this.lineFormatter);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultHttpRequestWriterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */