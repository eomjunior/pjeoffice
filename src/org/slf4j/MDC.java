/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.util.Deque;
/*     */ import java.util.Map;
/*     */ import org.slf4j.helpers.NOPMDCAdapter;
/*     */ import org.slf4j.helpers.Reporter;
/*     */ import org.slf4j.spi.MDCAdapter;
/*     */ import org.slf4j.spi.SLF4JServiceProvider;
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
/*     */ public class MDC
/*     */ {
/*     */   static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
/*     */   private static final String MDC_APAPTER_CANNOT_BE_NULL_MESSAGE = "MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA";
/*     */   static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
/*     */   static MDCAdapter mdcAdapter;
/*     */   
/*     */   public static class MDCCloseable
/*     */     implements Closeable
/*     */   {
/*     */     private final String key;
/*     */     
/*     */     private MDCCloseable(String key) {
/*  80 */       this.key = key;
/*     */     }
/*     */     
/*     */     public void close() {
/*  84 */       MDC.remove(this.key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  92 */     SLF4JServiceProvider provider = LoggerFactory.getProvider();
/*  93 */     if (provider != null) {
/*     */ 
/*     */ 
/*     */       
/*  97 */       mdcAdapter = provider.getMDCAdapter();
/*     */     } else {
/*  99 */       Reporter.error("Failed to find provider.");
/* 100 */       Reporter.error("Defaulting to no-operation MDCAdapter implementation.");
/* 101 */       mdcAdapter = (MDCAdapter)new NOPMDCAdapter();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void put(String key, String val) throws IllegalArgumentException {
/* 121 */     if (key == null) {
/* 122 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/* 124 */     if (mdcAdapter == null) {
/* 125 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 127 */     mdcAdapter.put(key, val);
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
/*     */   public static MDCCloseable putCloseable(String key, String val) throws IllegalArgumentException {
/* 159 */     put(key, val);
/* 160 */     return new MDCCloseable(key);
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
/*     */   public static String get(String key) throws IllegalArgumentException {
/* 176 */     if (key == null) {
/* 177 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/*     */     
/* 180 */     if (mdcAdapter == null) {
/* 181 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 183 */     return mdcAdapter.get(key);
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
/*     */   public static void remove(String key) throws IllegalArgumentException {
/* 197 */     if (key == null) {
/* 198 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/*     */     
/* 201 */     if (mdcAdapter == null) {
/* 202 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 204 */     mdcAdapter.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clear() {
/* 211 */     if (mdcAdapter == null) {
/* 212 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 214 */     mdcAdapter.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getCopyOfContextMap() {
/* 225 */     if (mdcAdapter == null) {
/* 226 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 228 */     return mdcAdapter.getCopyOfContextMap();
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
/*     */   public static void setContextMap(Map<String, String> contextMap) {
/* 243 */     if (mdcAdapter == null) {
/* 244 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 246 */     mdcAdapter.setContextMap(contextMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MDCAdapter getMDCAdapter() {
/* 256 */     return mdcAdapter;
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
/*     */   public static void pushByKey(String key, String value) {
/* 269 */     if (mdcAdapter == null) {
/* 270 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 272 */     mdcAdapter.pushByKey(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String popByKey(String key) {
/* 283 */     if (mdcAdapter == null) {
/* 284 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 286 */     return mdcAdapter.popByKey(key);
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
/*     */   public Deque<String> getCopyOfDequeByKey(String key) {
/* 298 */     if (mdcAdapter == null) {
/* 299 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 301 */     return mdcAdapter.getCopyOfDequeByKey(key);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/MDC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */