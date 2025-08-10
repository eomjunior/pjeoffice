/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public enum Console {
/*    */   final BufferedReader console;
/*  9 */   READER;
/*    */   Console() {
/* 11 */     this.console = new BufferedReader(new InputStreamReader(System.in));
/*    */   }
/*    */   public Optional<String> readLine() {
/*    */     try {
/* 15 */       return Strings.optional(this.console.readLine());
/* 16 */     } catch (Exception e) {
/* 17 */       return Optional.empty();
/*    */     } 
/*    */   }
/*    */   
/*    */   public Optional<Integer> readInt() {
/*    */     try {
/* 23 */       return Optional.of(Integer.valueOf(Integer.parseInt(this.console.readLine())));
/* 24 */     } catch (Exception e) {
/* 25 */       return Optional.empty();
/*    */     } 
/*    */   }
/*    */   
/*    */   public Optional<Float> readFloat() {
/*    */     try {
/* 31 */       return Optional.of(Float.valueOf(Float.parseFloat(this.console.readLine())));
/* 32 */     } catch (Exception e) {
/* 33 */       return Optional.empty();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Console.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */