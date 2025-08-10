/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ 
/*    */ class DateUtil
/*    */ {
/* 12 */   private static Long ZERO = longValueOf(0L);
/*    */   
/* 14 */   private static final Map localeCache = new HashMap<>();
/*    */   
/* 16 */   static Locale EN_Locale = forEN();
/*    */ 
/*    */   
/*    */   private static Locale forEN() {
/* 20 */     if ("en".equalsIgnoreCase(Locale.getDefault().getLanguage()))
/*    */     {
/* 22 */       return Locale.getDefault();
/*    */     }
/*    */     
/* 25 */     Locale[] arrayOfLocale = Locale.getAvailableLocales();
/* 26 */     for (byte b = 0; b != arrayOfLocale.length; b++) {
/*    */       
/* 28 */       if ("en".equalsIgnoreCase(arrayOfLocale[b].getLanguage()))
/*    */       {
/* 30 */         return arrayOfLocale[b];
/*    */       }
/*    */     } 
/*    */     
/* 34 */     return Locale.getDefault();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static Date epochAdjust(Date paramDate) throws ParseException {
/* 40 */     Locale locale = Locale.getDefault();
/* 41 */     if (locale == null)
/*    */     {
/* 43 */       return paramDate;
/*    */     }
/*    */     
/* 46 */     synchronized (localeCache) {
/*    */       
/* 48 */       Long long_ = (Long)localeCache.get(locale);
/*    */       
/* 50 */       if (long_ == null) {
/*    */         
/* 52 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
/* 53 */         long l = simpleDateFormat.parse("19700101000000GMT+00:00").getTime();
/*    */         
/* 55 */         if (l == 0L) {
/*    */           
/* 57 */           long_ = ZERO;
/*    */         }
/*    */         else {
/*    */           
/* 61 */           long_ = longValueOf(l);
/*    */         } 
/*    */         
/* 64 */         localeCache.put(locale, long_);
/*    */       } 
/*    */       
/* 67 */       if (long_ != ZERO)
/*    */       {
/* 69 */         return new Date(paramDate.getTime() - long_.longValue());
/*    */       }
/*    */       
/* 72 */       return paramDate;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static Long longValueOf(long paramLong) {
/* 78 */     return Long.valueOf(paramLong);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DateUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */