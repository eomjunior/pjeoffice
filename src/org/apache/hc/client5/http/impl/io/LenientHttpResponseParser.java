/*    */ package org.apache.hc.client5.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.config.Http1Config;
/*    */ import org.apache.hc.core5.http.impl.io.DefaultHttpResponseParser;
/*    */ import org.apache.hc.core5.http.message.LineParser;
/*    */ import org.apache.hc.core5.util.CharArrayBuffer;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class LenientHttpResponseParser
/*    */   extends DefaultHttpResponseParser
/*    */ {
/* 50 */   private static final Logger LOG = LoggerFactory.getLogger(LenientHttpResponseParser.class);
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
/*    */   public LenientHttpResponseParser(LineParser lineParser, HttpResponseFactory<ClassicHttpResponse> responseFactory, Http1Config h1Config) {
/* 68 */     super(lineParser, responseFactory, h1Config);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LenientHttpResponseParser(Http1Config h1Config) {
/* 79 */     this(null, null, h1Config);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ClassicHttpResponse createMessage(CharArrayBuffer buffer) throws IOException {
/*    */     try {
/* 85 */       return super.createMessage(buffer);
/* 86 */     } catch (HttpException ex) {
/* 87 */       if (LOG.isDebugEnabled()) {
/* 88 */         LOG.debug("Garbage in response: {}", buffer);
/*    */       }
/* 90 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/LenientHttpResponseParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */