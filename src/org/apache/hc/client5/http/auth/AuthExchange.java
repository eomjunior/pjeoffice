/*     */ package org.apache.hc.client5.http.auth;
/*     */ 
/*     */ import java.util.Queue;
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
/*     */ public class AuthExchange
/*     */ {
/*     */   private State state;
/*     */   private AuthScheme authScheme;
/*     */   private Queue<AuthScheme> authOptions;
/*     */   private String pathPrefix;
/*     */   
/*     */   public enum State
/*     */   {
/*  43 */     UNCHALLENGED, CHALLENGED, HANDSHAKE, FAILURE, SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthExchange() {
/*  54 */     this.state = State.UNCHALLENGED;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  58 */     this.state = State.UNCHALLENGED;
/*  59 */     this.authOptions = null;
/*  60 */     this.authScheme = null;
/*  61 */     this.pathPrefix = null;
/*     */   }
/*     */   
/*     */   public State getState() {
/*  65 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(State state) {
/*  69 */     this.state = (state != null) ? state : State.UNCHALLENGED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScheme getAuthScheme() {
/*  76 */     return this.authScheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/*  83 */     return (this.authScheme != null && this.authScheme.isConnectionBased());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPathPrefix() {
/*  90 */     return this.pathPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathPrefix(String pathPrefix) {
/*  97 */     this.pathPrefix = pathPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void select(AuthScheme authScheme) {
/* 106 */     Args.notNull(authScheme, "Auth scheme");
/* 107 */     this.authScheme = authScheme;
/* 108 */     this.authOptions = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<AuthScheme> getAuthOptions() {
/* 115 */     return this.authOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptions(Queue<AuthScheme> authOptions) {
/* 124 */     Args.notEmpty(authOptions, "Queue of auth options");
/* 125 */     this.authOptions = authOptions;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     StringBuilder buffer = new StringBuilder();
/* 131 */     buffer.append("[").append(this.state);
/* 132 */     if (this.authScheme != null) {
/* 133 */       buffer.append(" ").append(this.authScheme);
/*     */     }
/* 135 */     buffer.append("]");
/* 136 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/AuthExchange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */