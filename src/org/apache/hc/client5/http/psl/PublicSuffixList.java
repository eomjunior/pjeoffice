/*    */ package org.apache.hc.client5.http.psl;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public final class PublicSuffixList
/*    */ {
/*    */   private final DomainType type;
/*    */   private final List<String> rules;
/*    */   private final List<String> exceptions;
/*    */   
/*    */   public PublicSuffixList(DomainType type, List<String> rules, List<String> exceptions) {
/* 57 */     this.type = (DomainType)Args.notNull(type, "Domain type");
/* 58 */     this.rules = Collections.unmodifiableList((List<? extends String>)Args.notNull(rules, "Domain suffix rules"));
/* 59 */     this.exceptions = Collections.unmodifiableList((exceptions != null) ? exceptions : Collections.<String>emptyList());
/*    */   }
/*    */   
/*    */   public PublicSuffixList(List<String> rules, List<String> exceptions) {
/* 63 */     this(DomainType.UNKNOWN, rules, exceptions);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DomainType getType() {
/* 70 */     return this.type;
/*    */   }
/*    */   
/*    */   public List<String> getRules() {
/* 74 */     return this.rules;
/*    */   }
/*    */   
/*    */   public List<String> getExceptions() {
/* 78 */     return this.exceptions;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/psl/PublicSuffixList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */