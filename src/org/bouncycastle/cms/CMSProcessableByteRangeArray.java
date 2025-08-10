/*    */ package org.bouncycastle.cms;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
/*    */ import org.bouncycastle.util.Arrays;
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
/*    */ public class CMSProcessableByteRangeArray
/*    */   implements CMSTypedData, CMSReadable
/*    */ {
/*    */   private final ASN1ObjectIdentifier type;
/*    */   private final byte[] bytes;
/*    */   private final int offset;
/*    */   private final int length;
/*    */   
/*    */   public CMSProcessableByteRangeArray(byte[] bytes) {
/* 47 */     this(CMSObjectIdentifiers.data, bytes, 0, bytes.length);
/*    */   }
/*    */   
/*    */   public CMSProcessableByteRangeArray(byte[] bytes, int offset, int length) {
/* 51 */     this(CMSObjectIdentifiers.data, bytes, offset, length);
/*    */   }
/*    */   
/*    */   public CMSProcessableByteRangeArray(ASN1ObjectIdentifier type, byte[] bytes, int offset, int length) {
/* 55 */     this.type = type;
/* 56 */     this.bytes = bytes;
/* 57 */     this.offset = offset;
/* 58 */     this.length = length;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getInputStream() {
/* 63 */     return new ByteArrayInputStream(this.bytes, this.offset, this.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(OutputStream zOut) throws IOException, CMSException {
/* 68 */     zOut.write(this.bytes, this.offset, this.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getContent() {
/* 73 */     return Arrays.copyOfRange(this.bytes, this.offset, this.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1ObjectIdentifier getContentType() {
/* 78 */     return this.type;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/CMSProcessableByteRangeArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */