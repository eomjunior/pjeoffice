/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import org.bouncycastle.asn1.ASN1GeneralizedTime;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DERGeneralizedTime
/*     */   extends ASN1GeneralizedTime
/*     */ {
/*     */   public DERGeneralizedTime(byte[] paramArrayOfbyte) {
/*  30 */     super(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public DERGeneralizedTime(Date paramDate) {
/*  35 */     super(paramDate);
/*     */   }
/*     */ 
/*     */   
/*     */   public DERGeneralizedTime(String paramString) {
/*  40 */     super(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] getDERTime() {
/*  45 */     if (this.time[this.time.length - 1] == 90) {
/*     */       
/*  47 */       if (!hasMinutes()) {
/*     */         
/*  49 */         byte[] arrayOfByte = new byte[this.time.length + 4];
/*     */         
/*  51 */         System.arraycopy(this.time, 0, arrayOfByte, 0, this.time.length - 1);
/*  52 */         System.arraycopy(Strings.toByteArray("0000Z"), 0, arrayOfByte, this.time.length - 1, 5);
/*     */         
/*  54 */         return arrayOfByte;
/*     */       } 
/*  56 */       if (!hasSeconds()) {
/*     */         
/*  58 */         byte[] arrayOfByte = new byte[this.time.length + 2];
/*     */         
/*  60 */         System.arraycopy(this.time, 0, arrayOfByte, 0, this.time.length - 1);
/*  61 */         System.arraycopy(Strings.toByteArray("00Z"), 0, arrayOfByte, this.time.length - 1, 3);
/*     */         
/*  63 */         return arrayOfByte;
/*     */       } 
/*  65 */       if (hasFractionalSeconds()) {
/*     */         
/*  67 */         int i = this.time.length - 2;
/*  68 */         while (i > 0 && this.time[i] == 48)
/*     */         {
/*  70 */           i--;
/*     */         }
/*     */         
/*  73 */         if (this.time[i] == 46) {
/*     */           
/*  75 */           byte[] arrayOfByte1 = new byte[i + 1];
/*     */           
/*  77 */           System.arraycopy(this.time, 0, arrayOfByte1, 0, i);
/*  78 */           arrayOfByte1[i] = 90;
/*     */           
/*  80 */           return arrayOfByte1;
/*     */         } 
/*     */ 
/*     */         
/*  84 */         byte[] arrayOfByte = new byte[i + 2];
/*     */         
/*  86 */         System.arraycopy(this.time, 0, arrayOfByte, 0, i + 1);
/*  87 */         arrayOfByte[i + 1] = 90;
/*     */         
/*  89 */         return arrayOfByte;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  94 */       return this.time;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  99 */     return this.time;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 105 */     int i = (getDERTime()).length;
/*     */     
/* 107 */     return 1 + StreamUtil.calculateBodyLength(i) + i;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 112 */     paramASN1OutputStream.writeEncoded(paramBoolean, 24, getDERTime());
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 117 */     return (ASN1Primitive)this;
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 122 */     return (ASN1Primitive)this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERGeneralizedTime.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */