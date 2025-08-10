/*    */ package org.apache.hc.core5.net;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.http.NameValuePair;
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
/*    */ public class WWWFormCodec
/*    */ {
/*    */   private static final char QP_SEP_A = '&';
/*    */   
/*    */   public static List<NameValuePair> parse(CharSequence s, Charset charset) {
/* 53 */     return URIBuilder.parseQuery(s, charset, true);
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
/*    */   public static void format(StringBuilder buf, Iterable<? extends NameValuePair> params, Charset charset) {
/* 66 */     URIBuilder.formatQuery(buf, params, charset, true);
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
/*    */   public static String format(Iterable<? extends NameValuePair> params, Charset charset) {
/* 78 */     StringBuilder buf = new StringBuilder();
/* 79 */     URIBuilder.formatQuery(buf, params, charset, true);
/* 80 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/net/WWWFormCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */