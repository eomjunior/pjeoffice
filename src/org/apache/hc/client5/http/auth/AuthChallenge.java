/*    */ package org.apache.hc.client5.http.auth;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.NameValuePair;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public final class AuthChallenge
/*    */ {
/*    */   private final ChallengeType challengeType;
/*    */   private final String schemeName;
/*    */   private final String value;
/*    */   private final List<NameValuePair> params;
/*    */   
/*    */   public AuthChallenge(ChallengeType challengeType, String schemeName, String value, List<? extends NameValuePair> params) {
/* 55 */     this.challengeType = (ChallengeType)Args.notNull(challengeType, "Challenge type");
/* 56 */     this.schemeName = (String)Args.notNull(schemeName, "schemeName");
/* 57 */     this.value = value;
/* 58 */     this.params = (params != null) ? Collections.<NameValuePair>unmodifiableList(new ArrayList<>(params)) : null;
/*    */   }
/*    */   
/*    */   public AuthChallenge(ChallengeType challengeType, String schemeName, NameValuePair... params) {
/* 62 */     this(challengeType, schemeName, null, Arrays.asList(params));
/*    */   }
/*    */   
/*    */   public ChallengeType getChallengeType() {
/* 66 */     return this.challengeType;
/*    */   }
/*    */   
/*    */   public String getSchemeName() {
/* 70 */     return this.schemeName;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 74 */     return this.value;
/*    */   }
/*    */   
/*    */   public List<NameValuePair> getParams() {
/* 78 */     return this.params;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     StringBuilder buffer = new StringBuilder();
/* 84 */     buffer.append(this.schemeName).append(" ");
/* 85 */     if (this.value != null) {
/* 86 */       buffer.append(this.value);
/* 87 */     } else if (this.params != null) {
/* 88 */       buffer.append(this.params);
/*    */     } 
/* 90 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/AuthChallenge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */