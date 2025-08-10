/*    */ package org.apache.hc.client5.http.psl;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ @Contract(threading = ThreadingBehavior.SAFE)
/*    */ public final class PublicSuffixMatcherLoader
/*    */ {
/* 53 */   private static final Logger LOG = LoggerFactory.getLogger(PublicSuffixMatcherLoader.class);
/*    */   
/*    */   private static PublicSuffixMatcher load(InputStream in) throws IOException {
/* 56 */     List<PublicSuffixList> lists = PublicSuffixListParser.INSTANCE.parseByType(new InputStreamReader(in, StandardCharsets.UTF_8));
/*    */     
/* 58 */     return new PublicSuffixMatcher(lists);
/*    */   }
/*    */   private static volatile PublicSuffixMatcher DEFAULT_INSTANCE;
/*    */   public static PublicSuffixMatcher load(URL url) throws IOException {
/* 62 */     Args.notNull(url, "URL");
/* 63 */     try (InputStream in = url.openStream()) {
/* 64 */       return load(in);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static PublicSuffixMatcher load(File file) throws IOException {
/* 69 */     Args.notNull(file, "File");
/* 70 */     try (InputStream in = new FileInputStream(file)) {
/* 71 */       return load(in);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static PublicSuffixMatcher getDefault() {
/* 78 */     if (DEFAULT_INSTANCE == null) {
/* 79 */       synchronized (PublicSuffixMatcherLoader.class) {
/* 80 */         if (DEFAULT_INSTANCE == null) {
/* 81 */           URL url = PublicSuffixMatcherLoader.class.getResource("/mozilla/public-suffix-list.txt");
/*    */           
/* 83 */           if (url != null) {
/*    */             try {
/* 85 */               DEFAULT_INSTANCE = load(url);
/* 86 */             } catch (IOException ex) {
/*    */               
/* 88 */               LOG.warn("Failure loading public suffix list from default resource", ex);
/*    */             } 
/*    */           } else {
/* 91 */             DEFAULT_INSTANCE = new PublicSuffixMatcher(DomainType.ICANN, Collections.singletonList("com"), null);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     }
/* 96 */     return DEFAULT_INSTANCE;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/psl/PublicSuffixMatcherLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */