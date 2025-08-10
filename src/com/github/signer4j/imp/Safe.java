/*    */ package com.github.signer4j.imp;
/*    */ import com.github.signer4j.IToken;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ enum Safe {
/*    */   private final Map<String, char[]> vault;
/*    */   private volatile boolean enabled;
/* 11 */   BOX;
/*    */   Safe() {
/* 13 */     this.enabled = true;
/*    */     
/* 15 */     this.vault = (Map)new HashMap<>();
/*    */   }
/*    */   final void clear() {
/* 18 */     this.vault.clear();
/*    */   }
/*    */   
/*    */   final char[] get(String serial) {
/* 22 */     return this.vault.get(serial);
/*    */   }
/*    */   
/*    */   final void put(String serial, char[] password) {
/* 26 */     this.vault.put(serial, password);
/*    */   }
/*    */   
/*    */   final void remove(String serial) {
/* 30 */     this.vault.remove(serial);
/*    */   }
/*    */   
/*    */   final void open() {
/* 34 */     this.enabled = true;
/*    */   }
/*    */   
/*    */   final void close() {
/* 38 */     this.enabled = false;
/*    */   }
/*    */   
/*    */   private char[] read(String serial) {
/* 42 */     return this.enabled ? this.vault.get(serial) : null;
/*    */   }
/*    */   
/*    */   final void authenticate(IToken token) throws Signer4JException {
/* 46 */     Args.requireNonNull(token, "token is null");
/* 47 */     token.login(read(token.getSerial()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/Safe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */