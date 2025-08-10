/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.imp.exception.ExpiredCredentialException;
/*     */ import com.github.signer4j.imp.exception.InvalidPinException;
/*     */ import com.github.signer4j.imp.exception.LoginCanceledException;
/*     */ import com.github.signer4j.imp.exception.ModuleException;
/*     */ import com.github.signer4j.imp.exception.MscapiException;
/*     */ import com.github.signer4j.imp.exception.MscapiKeysetException;
/*     */ import com.github.signer4j.imp.exception.MscapiUnsupportedFeatureException;
/*     */ import com.github.signer4j.imp.exception.NoTokenPresentException;
/*     */ import com.github.signer4j.imp.exception.NoTokenPresentExceptionFail;
/*     */ import com.github.signer4j.imp.exception.OutOfMemoryException;
/*     */ import com.github.signer4j.imp.exception.PrivateKeyNotFound;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.signer4j.imp.exception.TokenLockedException;
/*     */ import com.github.utils4j.imp.InvokeHandler;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.Consumer;
/*     */ import javax.security.auth.login.AccountLockedException;
/*     */ import javax.security.auth.login.CredentialExpiredException;
/*     */ import javax.security.auth.login.FailedLoginException;
/*     */ import javax.security.auth.login.LoginException;
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
/*     */ public class Signer4JInvoker
/*     */   extends InvokeHandler<Signer4JException>
/*     */ {
/*  64 */   public static final Signer4JInvoker SIGNER4J = new Signer4JInvoker();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T invoke(IProvider<T> tryBlock, Consumer<Throwable> catchBlock, Runnable finallyBlock) throws Signer4JException {
/*     */     try {
/*  71 */       return (T)tryBlock.get();
/*  72 */     } catch (CanceledOperationException e) {
/*  73 */       catchBlock.accept(e);
/*  74 */       throw new LoginCanceledException(e);
/*  75 */     } catch (PrivateKeyNotFound e) {
/*  76 */       catchBlock.accept(e);
/*  77 */       throw new NoTokenPresentException(e);
/*  78 */     } catch (AccountLockedException e) {
/*  79 */       catchBlock.accept(e);
/*  80 */       throw new TokenLockedException(e);
/*  81 */     } catch (CredentialExpiredException|javax.security.auth.login.AccountExpiredException e) {
/*  82 */       catchBlock.accept(e);
/*  83 */       throw new ExpiredCredentialException(e);
/*  84 */     } catch (FailedLoginException e) {
/*  85 */       catchBlock.accept(e);
/*  86 */       throw new InvalidPinException(e);
/*  87 */     } catch (LoginException e) {
/*  88 */       catchBlock.accept(e);
/*     */       
/*  90 */       if (isLoginCanceled(e)) {
/*  91 */         throw new LoginCanceledException(e);
/*     */       }
/*  93 */       if (isTokenLocked(e)) {
/*  94 */         throw new TokenLockedException(e);
/*     */       }
/*  96 */       if (isNoTokenPresent(e)) {
/*  97 */         throw new NoTokenPresentException(e);
/*     */       }
/*  99 */       throw new InvalidPinException(e);
/* 100 */     } catch (Signer4JException|com.github.signer4j.imp.exception.InterruptedOperationException|com.github.signer4j.imp.exception.CertificateAliasNotFoundException e) {
/* 101 */       catchBlock.accept(e);
/* 102 */       throw e;
/* 103 */     } catch (Exception e) {
/* 104 */       catchBlock.accept(e);
/*     */       
/* 106 */       if (isLoginCanceled(e)) {
/* 107 */         throw new LoginCanceledException(e);
/*     */       }
/* 109 */       if (isTokenLocked(e)) {
/* 110 */         throw new TokenLockedException(e);
/*     */       }
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
/* 122 */       if (e instanceof NullPointerException) {
/* 123 */         throw new NoTokenPresentExceptionFail(e);
/*     */       }
/* 125 */       if (isNoTokenPresent(e)) {
/* 126 */         throw new NoTokenPresentException(e);
/*     */       }
/* 128 */       if (isPasswordIncorrect(e)) {
/* 129 */         throw new InvalidPinException(e);
/*     */       }
/* 131 */       if (isMscapiFail(e)) {
/*     */         
/* 133 */         if (isKeysetFail(e)) {
/* 134 */           throw new MscapiKeysetException(e);
/*     */         }
/* 136 */         if (isUnsupportedFeature(e)) {
/* 137 */           throw new MscapiUnsupportedFeatureException(e);
/*     */         }
/* 139 */         if (isMscapiCloudFail(e)) {
/* 140 */           throw new LoginCanceledException("Possível certificado em núvem com aplicativo fechado ou tela cancelada", e);
/*     */         }
/* 142 */         throw new MscapiException(e);
/*     */       } 
/*     */       
/* 145 */       throw new ModuleException(e);
/* 146 */     } catch (OutOfMemoryError e) {
/* 147 */       catchBlock.accept(e);
/* 148 */       throw new OutOfMemoryException(e);
/*     */     } finally {
/* 150 */       finallyBlock.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMscapiCloudFail(Throwable e) {
/* 155 */     return Throwables.traceStream(e)
/* 156 */       .map(t -> Strings.trim(t.getMessage()).toLowerCase())
/* 157 */       .anyMatch(m -> m.contains("a função falhou durante a execução."));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPasswordIncorrect(Throwable e) {
/* 163 */     return (Throwables.traceStream(e)
/* 164 */       .map(t -> Strings.trim(t.getMessage()).toLowerCase())
/* 165 */       .anyMatch(m -> 
/* 166 */         (m.contains("keystore password was incorrect") || m.contains("ckr_pin_len_range"))) || 
/*     */ 
/*     */       
/* 169 */       Throwables.hasCause(e, FailedLoginException.class) || 
/*     */       
/* 171 */       Throwables.hasCause(e, UnrecoverableKeyException.class));
/*     */   }
/*     */   
/*     */   private static boolean isMscapiFail(Throwable ex) {
/* 175 */     return Arrays.<StackTraceElement>stream(ex.getStackTrace())
/* 176 */       .filter(e -> (e != null))
/* 177 */       .map(e -> Strings.trim(e.getClassName()).toLowerCase())
/* 178 */       .anyMatch(m -> m.contains(".mscapi."));
/*     */   }
/*     */   
/*     */   private static boolean isKeysetFail(Throwable e) {
/* 182 */     return Throwables.traceStream(e)
/* 183 */       .map(t -> Strings.trim(t.getMessage()).toLowerCase())
/* 184 */       .anyMatch(m -> 
/* 185 */         (m.contains("o conjunto de chaves não existe") || m.contains("keyset does not exist")));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isUnsupportedFeature(Throwable e) {
/* 191 */     return Throwables.traceStream(e)
/* 192 */       .map(t -> Strings.trim(t.getMessage()).toLowerCase())
/* 193 */       .anyMatch(m -> 
/* 194 */         (m.contains("não dá suporte para o recurso solicitado") || m.contains("does not support the requested feature")));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isLoginCanceled(Throwable e) {
/* 200 */     return (Throwables.hasCause(e, CanceledOperationException.class) || Throwables.traceStream(e)
/* 201 */       .map(t -> Strings.trim(t.getMessage()).toLowerCase())
/* 202 */       .anyMatch(m -> 
/* 203 */         (m.contains("unable to perform password callback") || m.contains("a operação foi cancelada pelo usuário") || m.contains("ação cancelada pelo usuário"))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isNoTokenPresent(Throwable e) {
/* 210 */     return Throwables.traceStream(e)
/* 211 */       .map(t -> Strings.trim(t.getMessage()).toLowerCase())
/* 212 */       .anyMatch(m -> 
/* 213 */         (m.contains("token has been removed") || m.contains("no token present") || m.contains("token is not present") || m.contains("ckr_token_not_present") || m.contains("exception obtaining signature") || m.contains("acesso negado") || m.contains("o cartão inteligente foi removido") || m.contains("não é possível a comunicação do leitor") || m.contains("mas não existe um cartão inteligente atualmente no dispositivo") || m.contains("erro interno") || m.contains("ckr_function_failed")));
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
/*     */   private static boolean isTokenLocked(Throwable e) {
/* 229 */     return Throwables.traceStream(e)
/* 230 */       .map(t -> Strings.trim(t.getMessage()).toLowerCase())
/* 231 */       .anyMatch(m -> 
/* 232 */         (m.contains("ckr_pin_locked") || m.contains("número máximo de tentativas para digitar o pin")));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/Signer4JInvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */