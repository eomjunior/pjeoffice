/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IAuthStrategy;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.TokenType;
/*     */ import com.github.signer4j.gui.alert.TokenUseAlert;
/*     */ import com.github.signer4j.imp.exception.LoginCanceledException;
/*     */ import com.github.signer4j.imp.exception.NoTokenPresentException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum AuthStrategy
/*     */   implements IAuthStrategy
/*     */ {
/*  44 */   ONE_TIME("Solicitar senha uma vez (no login)")
/*     */   {
/*     */     
/*     */     protected void doLogin(IToken token) throws Signer4JException
/*     */     {
/*  49 */       if (!TokenType.A1.equals(token.getType())) {
/*  50 */         super.doLogin(token);
/*     */         
/*     */         return;
/*     */       } 
/*  54 */       Safe.BOX.authenticate(token);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isOneTimeStrategy() {
/*  59 */       return true;
/*     */     }
/*     */   },
/*     */   
/*  63 */   AWAYS("Sempre solicitar senha")
/*     */   {
/*     */     protected final void doLogout(IToken token)
/*     */     {
/*  67 */       token.logout();
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isAwayStrategy() {
/*  72 */       return true;
/*     */     }
/*     */   },
/*     */   
/*  76 */   CONFIRM("Confirmar uso do dispositivo (pós login)")
/*     */   {
/*     */     
/*     */     protected final void preLogin(IToken token, boolean isUsing) throws Signer4JException
/*     */     {
/*  81 */       super.preLogin(token, isUsing);
/*     */       
/*  83 */       boolean isToAsk = (!isUsing && (token.isAuthenticated() || token.isMscapi()));
/*     */       
/*  85 */       if (isToAsk && TokenUseAlert.isNotOk()) {
/*  86 */         Signer4jContext.discardQuietly();
/*  87 */         token.logout();
/*  88 */         throw new LoginCanceledException();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doLogin(IToken token) throws Signer4JException {
/*  94 */       ONE_TIME.doLogin(token);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isConfirmStrategy() {
/*  99 */       return true;
/*     */     }
/*     */   };
/*     */   
/*     */   public static Optional<AuthStrategy> forName(String name) {
/*     */     try {
/* 105 */       return Optional.of(valueOf(name));
/* 106 */     } catch (Exception e) {
/* 107 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private final String label;
/*     */   
/*     */   AuthStrategy(String message) {
/* 114 */     this.label = message;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getStrategyLabel() {
/* 119 */     return this.label;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void logout(IToken token) {
/* 124 */     doLogout(token);
/* 125 */     Safe.BOX.close();
/*     */   }
/*     */   
/*     */   protected void doLogout(IToken token) {}
/*     */   
/*     */   protected void doLogin(IToken token) throws Signer4JException {
/* 131 */     token.login(Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void preLogin(IToken token, boolean isUsing) throws Signer4JException {
/* 136 */     if (Signer4jContext.isDiscarded()) {
/* 137 */       throw new LoginCanceledException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void login(IToken token, boolean isUsing) throws Signer4JException {
/* 143 */     preLogin(token, isUsing);
/*     */     
/* 145 */     if (!token.isAuthenticated())
/*     */       try {
/* 147 */         doLogin(token);
/* 148 */         Safe.BOX.open();
/* 149 */       } catch (NoTokenPresentException|com.github.signer4j.imp.exception.InvalidPinException e) {
/* 150 */         throw e;
/*     */       }
/* 152 */       catch (Signer4JException e) {
/* 153 */         Signer4jContext.discardQuietly();
/* 154 */         throw e;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/AuthStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */