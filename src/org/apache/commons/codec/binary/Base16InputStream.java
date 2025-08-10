/*    */ package org.apache.commons.codec.binary;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import org.apache.commons.codec.CodecPolicy;
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
/*    */ public class Base16InputStream
/*    */   extends BaseNCodecInputStream
/*    */ {
/*    */   public Base16InputStream(InputStream in) {
/* 41 */     this(in, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Base16InputStream(InputStream in, boolean doEncode) {
/* 52 */     this(in, doEncode, false);
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
/*    */   public Base16InputStream(InputStream in, boolean doEncode, boolean lowerCase) {
/* 65 */     this(in, doEncode, lowerCase, CodecPolicy.LENIENT);
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
/*    */   
/*    */   public Base16InputStream(InputStream in, boolean doEncode, boolean lowerCase, CodecPolicy decodingPolicy) {
/* 79 */     super(in, new Base16(lowerCase, decodingPolicy), doEncode);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/Base16InputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */