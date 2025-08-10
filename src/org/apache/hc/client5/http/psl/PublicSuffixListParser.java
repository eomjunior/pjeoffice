/*     */ package org.apache.hc.client5.http.psl;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public final class PublicSuffixListParser
/*     */ {
/*  52 */   public static final PublicSuffixListParser INSTANCE = new PublicSuffixListParser();
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
/*     */   public PublicSuffixList parse(Reader reader) throws IOException {
/*  67 */     List<String> rules = new ArrayList<>();
/*  68 */     List<String> exceptions = new ArrayList<>();
/*  69 */     BufferedReader r = new BufferedReader(reader);
/*     */     
/*     */     String line;
/*  72 */     while ((line = r.readLine()) != null) {
/*  73 */       if (line.isEmpty()) {
/*     */         continue;
/*     */       }
/*  76 */       if (line.startsWith("//")) {
/*     */         continue;
/*     */       }
/*  79 */       if (line.startsWith(".")) {
/*  80 */         line = line.substring(1);
/*     */       }
/*     */       
/*  83 */       boolean isException = line.startsWith("!");
/*  84 */       if (isException) {
/*  85 */         line = line.substring(1);
/*     */       }
/*     */       
/*  88 */       if (isException) {
/*  89 */         exceptions.add(line); continue;
/*     */       } 
/*  91 */       rules.add(line);
/*     */     } 
/*     */     
/*  94 */     return new PublicSuffixList(DomainType.UNKNOWN, rules, exceptions);
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
/*     */   public List<PublicSuffixList> parseByType(Reader reader) throws IOException {
/* 109 */     List<PublicSuffixList> result = new ArrayList<>(2);
/*     */     
/* 111 */     BufferedReader r = new BufferedReader(reader);
/*     */     
/* 113 */     DomainType domainType = null;
/* 114 */     List<String> rules = null;
/* 115 */     List<String> exceptions = null;
/*     */     String line;
/* 117 */     while ((line = r.readLine()) != null) {
/* 118 */       if (line.isEmpty()) {
/*     */         continue;
/*     */       }
/* 121 */       if (line.startsWith("//")) {
/*     */         
/* 123 */         if (domainType == null) {
/* 124 */           if (line.contains("===BEGIN ICANN DOMAINS===")) {
/* 125 */             domainType = DomainType.ICANN; continue;
/* 126 */           }  if (line.contains("===BEGIN PRIVATE DOMAINS==="))
/* 127 */             domainType = DomainType.PRIVATE; 
/*     */           continue;
/*     */         } 
/* 130 */         if (line.contains("===END ICANN DOMAINS===") || line.contains("===END PRIVATE DOMAINS===")) {
/* 131 */           if (rules != null) {
/* 132 */             result.add(new PublicSuffixList(domainType, rules, exceptions));
/*     */           }
/* 134 */           domainType = null;
/* 135 */           rules = null;
/* 136 */           exceptions = null;
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 142 */       if (domainType == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 146 */       if (line.startsWith(".")) {
/* 147 */         line = line.substring(1);
/*     */       }
/*     */       
/* 150 */       boolean isException = line.startsWith("!");
/* 151 */       if (isException) {
/* 152 */         line = line.substring(1);
/*     */       }
/*     */       
/* 155 */       if (isException) {
/* 156 */         if (exceptions == null) {
/* 157 */           exceptions = new ArrayList<>();
/*     */         }
/* 159 */         exceptions.add(line); continue;
/*     */       } 
/* 161 */       if (rules == null) {
/* 162 */         rules = new ArrayList<>();
/*     */       }
/* 164 */       rules.add(line);
/*     */     } 
/*     */     
/* 167 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/psl/PublicSuffixListParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */