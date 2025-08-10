/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.SimpleTimeZone;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DateUtil;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.Arrays;
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
/*     */ public class ASN1UTCTime
/*     */   extends ASN1Primitive
/*     */ {
/*     */   private byte[] time;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1UTCTime getInstance(Object paramObject) {
/*  49 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1UTCTime)
/*     */     {
/*  51 */       return (org.bouncycastle.asn1.ASN1UTCTime)paramObject;
/*     */     }
/*     */     
/*  54 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  58 */         return (org.bouncycastle.asn1.ASN1UTCTime)fromByteArray((byte[])paramObject);
/*     */       }
/*  60 */       catch (Exception exception) {
/*     */         
/*  62 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  66 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
/*     */   }
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
/*     */   public static org.bouncycastle.asn1.ASN1UTCTime getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  83 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  85 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.ASN1UTCTime)
/*     */     {
/*  87 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  91 */     return new org.bouncycastle.asn1.ASN1UTCTime(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
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
/*     */   public ASN1UTCTime(String paramString) {
/* 108 */     this.time = Strings.toByteArray(paramString);
/*     */     
/*     */     try {
/* 111 */       getDate();
/*     */     }
/* 113 */     catch (ParseException parseException) {
/*     */       
/* 115 */       throw new IllegalArgumentException("invalid date string: " + parseException.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1UTCTime(Date paramDate) {
/* 126 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'", DateUtil.EN_Locale);
/*     */     
/* 128 */     simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
/*     */     
/* 130 */     this.time = Strings.toByteArray(simpleDateFormat.format(paramDate));
/*     */   }
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
/*     */   public ASN1UTCTime(Date paramDate, Locale paramLocale) {
/* 144 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'", paramLocale);
/*     */     
/* 146 */     simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
/*     */     
/* 148 */     this.time = Strings.toByteArray(simpleDateFormat.format(paramDate));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1UTCTime(byte[] paramArrayOfbyte) {
/* 154 */     if (paramArrayOfbyte.length < 2)
/*     */     {
/* 156 */       throw new IllegalArgumentException("UTCTime string too short");
/*     */     }
/* 158 */     this.time = paramArrayOfbyte;
/* 159 */     if (!isDigit(0) || !isDigit(1))
/*     */     {
/* 161 */       throw new IllegalArgumentException("illegal characters in UTCTime string");
/*     */     }
/*     */   }
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
/*     */   public Date getDate() throws ParseException {
/* 175 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmssz");
/*     */     
/* 177 */     return DateUtil.epochAdjust(simpleDateFormat.parse(getTime()));
/*     */   }
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
/*     */   public Date getAdjustedDate() throws ParseException {
/* 190 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
/*     */     
/* 192 */     simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
/*     */     
/* 194 */     return DateUtil.epochAdjust(simpleDateFormat.parse(getAdjustedTime()));
/*     */   }
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
/*     */ 
/*     */   
/*     */   public String getTime() {
/* 215 */     String str1 = Strings.fromByteArray(this.time);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     if (str1.indexOf('-') < 0 && str1.indexOf('+') < 0) {
/*     */       
/* 222 */       if (str1.length() == 11)
/*     */       {
/* 224 */         return str1.substring(0, 10) + "00GMT+00:00";
/*     */       }
/*     */ 
/*     */       
/* 228 */       return str1.substring(0, 12) + "GMT+00:00";
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 233 */     int i = str1.indexOf('-');
/* 234 */     if (i < 0)
/*     */     {
/* 236 */       i = str1.indexOf('+');
/*     */     }
/* 238 */     String str2 = str1;
/*     */     
/* 240 */     if (i == str1.length() - 3)
/*     */     {
/* 242 */       str2 = str2 + "00";
/*     */     }
/*     */     
/* 245 */     if (i == 10)
/*     */     {
/* 247 */       return str2.substring(0, 10) + "00GMT" + str2.substring(0, 10) + ":" + str2.substring(10, 13);
/*     */     }
/*     */ 
/*     */     
/* 251 */     return str2.substring(0, 12) + "GMT" + str2.substring(0, 12) + ":" + str2.substring(12, 15);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAdjustedTime() {
/* 262 */     String str = getTime();
/*     */     
/* 264 */     return (str.charAt(0) < '5') ? ("20" + 
/*     */       
/* 266 */       str) : ("19" + 
/*     */ 
/*     */ 
/*     */       
/* 270 */       str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDigit(int paramInt) {
/* 276 */     return (this.time.length > paramInt && this.time[paramInt] >= 48 && this.time[paramInt] <= 57);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 281 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 286 */     int i = this.time.length;
/*     */     
/* 288 */     return 1 + StreamUtil.calculateBodyLength(i) + i;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 293 */     paramASN1OutputStream.writeEncoded(paramBoolean, 23, this.time);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 299 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1UTCTime))
/*     */     {
/* 301 */       return false;
/*     */     }
/*     */     
/* 304 */     return Arrays.areEqual(this.time, ((org.bouncycastle.asn1.ASN1UTCTime)paramASN1Primitive).time);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 309 */     return Arrays.hashCode(this.time);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 314 */     return Strings.fromByteArray(this.time);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1UTCTime.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */