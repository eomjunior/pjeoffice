/*    */ package org.apache.hc.client5.http.entity;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.NameValuePair;
/*    */ import org.apache.hc.core5.http.io.entity.StringEntity;
/*    */ import org.apache.hc.core5.net.WWWFormCodec;
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
/*    */ 
/*    */ 
/*    */ public class UrlEncodedFormEntity
/*    */   extends StringEntity
/*    */ {
/*    */   public UrlEncodedFormEntity(Iterable<? extends NameValuePair> parameters, Charset charset) {
/* 57 */     super(WWWFormCodec.format(parameters, (charset != null) ? charset : ContentType.APPLICATION_FORM_URLENCODED
/*    */           
/* 59 */           .getCharset()), (charset != null) ? ContentType.APPLICATION_FORM_URLENCODED
/* 60 */         .withCharset(charset) : ContentType.APPLICATION_FORM_URLENCODED);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UrlEncodedFormEntity(List<? extends NameValuePair> parameters) {
/* 70 */     this(parameters, null);
/*    */   }
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
/*    */   public UrlEncodedFormEntity(Iterable<? extends NameValuePair> parameters) {
/* 83 */     this(parameters, null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/UrlEncodedFormEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */