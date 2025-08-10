/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.SimpleTimeZone;
/*     */ import java.util.TimeZone;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DERGeneralizedTime;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ASN1GeneralizedTime
/*     */   extends ASN1Primitive
/*     */ {
/*     */   protected byte[] time;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1GeneralizedTime getInstance(Object paramObject) {
/*  59 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1GeneralizedTime)
/*     */     {
/*  61 */       return (org.bouncycastle.asn1.ASN1GeneralizedTime)paramObject;
/*     */     }
/*     */     
/*  64 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  68 */         return (org.bouncycastle.asn1.ASN1GeneralizedTime)fromByteArray((byte[])paramObject);
/*     */       }
/*  70 */       catch (Exception exception) {
/*     */         
/*  72 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  76 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.ASN1GeneralizedTime getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  93 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  95 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.ASN1GeneralizedTime)
/*     */     {
/*  97 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/* 101 */     return new org.bouncycastle.asn1.ASN1GeneralizedTime(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
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
/*     */   public ASN1GeneralizedTime(String paramString) {
/* 117 */     this.time = Strings.toByteArray(paramString);
/*     */     
/*     */     try {
/* 120 */       getDate();
/*     */     }
/* 122 */     catch (ParseException parseException) {
/*     */       
/* 124 */       throw new IllegalArgumentException("invalid date string: " + parseException.getMessage());
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
/*     */   public ASN1GeneralizedTime(Date paramDate) {
/* 136 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'", DateUtil.EN_Locale);
/*     */     
/* 138 */     simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
/*     */     
/* 140 */     this.time = Strings.toByteArray(simpleDateFormat.format(paramDate));
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
/*     */   public ASN1GeneralizedTime(Date paramDate, Locale paramLocale) {
/* 154 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'", paramLocale);
/*     */     
/* 156 */     simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
/*     */     
/* 158 */     this.time = Strings.toByteArray(simpleDateFormat.format(paramDate));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1GeneralizedTime(byte[] paramArrayOfbyte) {
/* 164 */     if (paramArrayOfbyte.length < 4)
/*     */     {
/* 166 */       throw new IllegalArgumentException("GeneralizedTime string too short");
/*     */     }
/* 168 */     this.time = paramArrayOfbyte;
/*     */     
/* 170 */     if (!isDigit(0) || !isDigit(1) || !isDigit(2) || !isDigit(3))
/*     */     {
/* 172 */       throw new IllegalArgumentException("illegal characters in GeneralizedTime string");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTimeString() {
/* 183 */     return Strings.fromByteArray(this.time);
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
/*     */   public String getTime() {
/* 201 */     String str = Strings.fromByteArray(this.time);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     if (str.charAt(str.length() - 1) == 'Z')
/*     */     {
/* 208 */       return str.substring(0, str.length() - 1) + "GMT+00:00";
/*     */     }
/*     */ 
/*     */     
/* 212 */     int i = str.length() - 6;
/* 213 */     char c = str.charAt(i);
/* 214 */     if ((c == '-' || c == '+') && str.indexOf("GMT") == i - 3)
/*     */     {
/*     */       
/* 217 */       return str;
/*     */     }
/*     */     
/* 220 */     i = str.length() - 5;
/* 221 */     c = str.charAt(i);
/* 222 */     if (c == '-' || c == '+')
/*     */     {
/* 224 */       return str.substring(0, i) + "GMT" + str.substring(0, i) + ":" + str
/*     */         
/* 226 */         .substring(i, i + 3);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 231 */     i = str.length() - 3;
/* 232 */     c = str.charAt(i);
/* 233 */     if (c == '-' || c == '+')
/*     */     {
/* 235 */       return str.substring(0, i) + "GMT" + str.substring(0, i) + ":00";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     return str + str;
/*     */   }
/*     */ 
/*     */   
/*     */   private String calculateGMTOffset(String paramString) {
/* 246 */     String str = "+";
/* 247 */     TimeZone timeZone = TimeZone.getDefault();
/* 248 */     int i = timeZone.getRawOffset();
/* 249 */     if (i < 0) {
/*     */       
/* 251 */       str = "-";
/* 252 */       i = -i;
/*     */     } 
/* 254 */     int j = i / 3600000;
/* 255 */     int k = (i - j * 60 * 60 * 1000) / 60000;
/*     */ 
/*     */     
/*     */     try {
/* 259 */       if (timeZone.useDaylightTime())
/*     */       {
/* 261 */         if (hasFractionalSeconds())
/*     */         {
/* 263 */           paramString = pruneFractionalSeconds(paramString);
/*     */         }
/* 265 */         SimpleDateFormat simpleDateFormat = calculateGMTDateFormat();
/* 266 */         if (timeZone.inDaylightTime(simpleDateFormat
/* 267 */             .parse(paramString + "GMT" + paramString + str + ":" + convert(j))))
/*     */         {
/* 269 */           j += str.equals("+") ? 1 : -1;
/*     */         }
/*     */       }
/*     */     
/* 273 */     } catch (ParseException parseException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     return "GMT" + str + convert(j) + ":" + convert(k);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private SimpleDateFormat calculateGMTDateFormat() {
/*     */     SimpleDateFormat simpleDateFormat;
/* 285 */     if (hasFractionalSeconds()) {
/*     */       
/* 287 */       simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSSz");
/*     */     }
/* 289 */     else if (hasSeconds()) {
/*     */       
/* 291 */       simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
/*     */     }
/* 293 */     else if (hasMinutes()) {
/*     */       
/* 295 */       simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmz");
/*     */     }
/*     */     else {
/*     */       
/* 299 */       simpleDateFormat = new SimpleDateFormat("yyyyMMddHHz");
/*     */     } 
/*     */     
/* 302 */     simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
/* 303 */     return simpleDateFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String pruneFractionalSeconds(String paramString) {
/* 309 */     String str = paramString.substring(14);
/*     */     byte b;
/* 311 */     for (b = 1; b < str.length(); b++) {
/*     */       
/* 313 */       char c = str.charAt(b);
/* 314 */       if ('0' > c || c > '9') {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 320 */     if (b - 1 > 3) {
/*     */       
/* 322 */       str = str.substring(0, 4) + str.substring(0, 4);
/* 323 */       paramString = paramString.substring(0, 14) + paramString.substring(0, 14);
/*     */     }
/* 325 */     else if (b - 1 == 1) {
/*     */       
/* 327 */       str = str.substring(0, b) + "00" + str.substring(0, b);
/* 328 */       paramString = paramString.substring(0, 14) + paramString.substring(0, 14);
/*     */     }
/* 330 */     else if (b - 1 == 2) {
/*     */       
/* 332 */       str = str.substring(0, b) + "0" + str.substring(0, b);
/* 333 */       paramString = paramString.substring(0, 14) + paramString.substring(0, 14);
/*     */     } 
/*     */     
/* 336 */     return paramString;
/*     */   }
/*     */ 
/*     */   
/*     */   private String convert(int paramInt) {
/* 341 */     if (paramInt < 10)
/*     */     {
/* 343 */       return "0" + paramInt;
/*     */     }
/*     */     
/* 346 */     return Integer.toString(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDate() throws ParseException {
/*     */     SimpleDateFormat simpleDateFormat;
/* 353 */     String str1 = Strings.fromByteArray(this.time);
/* 354 */     String str2 = str1;
/*     */     
/* 356 */     if (str1.endsWith("Z")) {
/*     */       
/* 358 */       if (hasFractionalSeconds()) {
/*     */         
/* 360 */         simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
/*     */       }
/* 362 */       else if (hasSeconds()) {
/*     */         
/* 364 */         simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
/*     */       }
/* 366 */       else if (hasMinutes()) {
/*     */         
/* 368 */         simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm'Z'");
/*     */       }
/*     */       else {
/*     */         
/* 372 */         simpleDateFormat = new SimpleDateFormat("yyyyMMddHH'Z'");
/*     */       } 
/*     */       
/* 375 */       simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
/*     */     }
/* 377 */     else if (str1.indexOf('-') > 0 || str1.indexOf('+') > 0) {
/*     */       
/* 379 */       str2 = getTime();
/* 380 */       simpleDateFormat = calculateGMTDateFormat();
/*     */     }
/*     */     else {
/*     */       
/* 384 */       if (hasFractionalSeconds()) {
/*     */         
/* 386 */         simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
/*     */       }
/* 388 */       else if (hasSeconds()) {
/*     */         
/* 390 */         simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
/*     */       }
/* 392 */       else if (hasMinutes()) {
/*     */         
/* 394 */         simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
/*     */       }
/*     */       else {
/*     */         
/* 398 */         simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
/*     */       } 
/*     */       
/* 401 */       simpleDateFormat.setTimeZone(new SimpleTimeZone(0, TimeZone.getDefault().getID()));
/*     */     } 
/*     */     
/* 404 */     if (hasFractionalSeconds())
/*     */     {
/* 406 */       str2 = pruneFractionalSeconds(str2);
/*     */     }
/*     */     
/* 409 */     return DateUtil.epochAdjust(simpleDateFormat.parse(str2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasFractionalSeconds() {
/* 414 */     for (byte b = 0; b != this.time.length; b++) {
/*     */       
/* 416 */       if (this.time[b] == 46)
/*     */       {
/* 418 */         if (b == 14)
/*     */         {
/* 420 */           return true;
/*     */         }
/*     */       }
/*     */     } 
/* 424 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasSeconds() {
/* 429 */     return (isDigit(12) && isDigit(13));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasMinutes() {
/* 434 */     return (isDigit(10) && isDigit(11));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isDigit(int paramInt) {
/* 439 */     return (this.time.length > paramInt && this.time[paramInt] >= 48 && this.time[paramInt] <= 57);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 444 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 449 */     int i = this.time.length;
/*     */     
/* 451 */     return 1 + StreamUtil.calculateBodyLength(i) + i;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 456 */     paramASN1OutputStream.writeEncoded(paramBoolean, 24, this.time);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 461 */     return (ASN1Primitive)new DERGeneralizedTime(this.time);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 466 */     return (ASN1Primitive)new DERGeneralizedTime(this.time);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 472 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1GeneralizedTime))
/*     */     {
/* 474 */       return false;
/*     */     }
/*     */     
/* 477 */     return Arrays.areEqual(this.time, ((org.bouncycastle.asn1.ASN1GeneralizedTime)paramASN1Primitive).time);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 482 */     return Arrays.hashCode(this.time);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1GeneralizedTime.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */