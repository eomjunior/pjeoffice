/*     */ package org.apache.hc.client5.http.psl;
/*     */ 
/*     */ import java.net.IDN;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.hc.client5.http.utils.DnsUtils;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public final class PublicSuffixMatcher
/*     */ {
/*     */   private final Map<String, DomainType> rules;
/*     */   private final Map<String, DomainType> exceptions;
/*     */   
/*     */   public PublicSuffixMatcher(Collection<String> rules, Collection<String> exceptions) {
/*  58 */     this(DomainType.UNKNOWN, rules, exceptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicSuffixMatcher(DomainType domainType, Collection<String> rules, Collection<String> exceptions) {
/*  66 */     Args.notNull(domainType, "Domain type");
/*  67 */     Args.notNull(rules, "Domain suffix rules");
/*  68 */     this.rules = new ConcurrentHashMap<>(rules.size());
/*  69 */     for (String rule : rules) {
/*  70 */       this.rules.put(rule, domainType);
/*     */     }
/*  72 */     this.exceptions = new ConcurrentHashMap<>();
/*  73 */     if (exceptions != null) {
/*  74 */       for (String exception : exceptions) {
/*  75 */         this.exceptions.put(exception, domainType);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicSuffixMatcher(Collection<PublicSuffixList> lists) {
/*  84 */     Args.notNull(lists, "Domain suffix lists");
/*  85 */     this.rules = new ConcurrentHashMap<>();
/*  86 */     this.exceptions = new ConcurrentHashMap<>();
/*  87 */     for (PublicSuffixList list : lists) {
/*  88 */       DomainType domainType = list.getType();
/*  89 */       List<String> rules = list.getRules();
/*  90 */       for (String rule : rules) {
/*  91 */         this.rules.put(rule, domainType);
/*     */       }
/*  93 */       List<String> exceptions = list.getExceptions();
/*  94 */       if (exceptions != null) {
/*  95 */         for (String exception : exceptions) {
/*  96 */           this.exceptions.put(exception, domainType);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DomainType findEntry(Map<String, DomainType> map, String rule) {
/* 103 */     if (map == null) {
/* 104 */       return null;
/*     */     }
/* 106 */     return map.get(rule);
/*     */   }
/*     */   
/*     */   private static boolean match(DomainType domainType, DomainType expectedType) {
/* 110 */     return (domainType != null && (expectedType == null || domainType.equals(expectedType)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDomainRoot(String domain) {
/* 121 */     return getDomainRoot(domain, null);
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
/*     */   public String getDomainRoot(String domain, DomainType expectedType) {
/* 135 */     if (domain == null) {
/* 136 */       return null;
/*     */     }
/* 138 */     if (domain.startsWith(".")) {
/* 139 */       return null;
/*     */     }
/* 141 */     String segment = DnsUtils.normalize(domain);
/* 142 */     String result = null;
/* 143 */     while (segment != null) {
/*     */       
/* 145 */       String key = IDN.toUnicode(segment);
/* 146 */       DomainType exceptionRule = findEntry(this.exceptions, key);
/* 147 */       if (match(exceptionRule, expectedType)) {
/* 148 */         return segment;
/*     */       }
/* 150 */       DomainType domainRule = findEntry(this.rules, key);
/* 151 */       if (match(domainRule, expectedType)) {
/* 152 */         if (domainRule == DomainType.PRIVATE) {
/* 153 */           return segment;
/*     */         }
/* 155 */         return result;
/*     */       } 
/*     */       
/* 158 */       int nextdot = segment.indexOf('.');
/* 159 */       String nextSegment = (nextdot != -1) ? segment.substring(nextdot + 1) : null;
/*     */       
/* 161 */       if (nextSegment != null) {
/* 162 */         DomainType wildcardDomainRule = findEntry(this.rules, "*." + IDN.toUnicode(nextSegment));
/* 163 */         if (match(wildcardDomainRule, expectedType)) {
/* 164 */           if (wildcardDomainRule == DomainType.PRIVATE) {
/* 165 */             return segment;
/*     */           }
/* 167 */           return result;
/*     */         } 
/*     */       } 
/* 170 */       result = segment;
/* 171 */       segment = nextSegment;
/*     */     } 
/*     */ 
/*     */     
/* 175 */     if (expectedType == null || expectedType == DomainType.UNKNOWN) {
/* 176 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 180 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(String domain) {
/* 187 */     return matches(domain, null);
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
/*     */   public boolean matches(String domain, DomainType expectedType) {
/* 200 */     if (domain == null) {
/* 201 */       return false;
/*     */     }
/* 203 */     String domainRoot = getDomainRoot(
/* 204 */         domain.startsWith(".") ? domain.substring(1) : domain, expectedType);
/* 205 */     return (domainRoot == null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/psl/PublicSuffixMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */