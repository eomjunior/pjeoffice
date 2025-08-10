/*     */ package com.github.taskresolver4j.imp;
/*     */ 
/*     */ import com.github.taskresolver4j.IExceptionContext;
/*     */ import com.github.taskresolver4j.IFailureAlerter;
/*     */ import com.github.taskresolver4j.exception.ExceptionContext;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.BooleanTimeout;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
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
/*     */ class AsyncFailureAlerter
/*     */   implements IFailureAlerter
/*     */ {
/*     */   private final Supplier<Boolean> isToPostpone;
/*     */   private final Consumer<IExceptionContext> display;
/*  47 */   private final List<IExceptionContext> stack = new LinkedList<>();
/*     */   
/*  49 */   private final BooleanTimeout debt = new BooleanTimeout("async-fail", 2100L, this::checkDebt);
/*     */   
/*     */   public AsyncFailureAlerter(Consumer<IExceptionContext> display, Supplier<Boolean> isToPostpone) {
/*  52 */     this.display = (Consumer<IExceptionContext>)Args.requireNonNull(display, "display is null");
/*  53 */     this.isToPostpone = (Supplier<Boolean>)Args.requireNonNull(isToPostpone, "isToPostpone is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public final void alert(IExceptionContext context) {
/*  58 */     if (context != null) {
/*  59 */       synchronized (this.stack) {
/*  60 */         this.stack.add(context);
/*     */       } 
/*  62 */       this.debt.setTrue();
/*     */     } 
/*     */   }
/*     */   private void checkDebt() {
/*     */     List<IExceptionContext> failList;
/*  67 */     if (((Boolean)this.isToPostpone.get()).booleanValue()) {
/*  68 */       this.debt.setTrue();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  73 */     synchronized (this.stack) {
/*  74 */       failList = new ArrayList<>(this.stack);
/*  75 */       this.stack.clear();
/*     */     } 
/*     */     
/*  78 */     int count = failList.size();
/*  79 */     if (count == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     String title = null;
/*  84 */     String detail = null;
/*  85 */     Throwable rootCause = null;
/*     */     
/*  87 */     StringBuilder details = new StringBuilder();
/*     */     
/*  89 */     for (IExceptionContext context : failList) {
/*  90 */       Throwable contextCause = context.getCause();
/*  91 */       String contextMessage = context.getMessage();
/*  92 */       String contextDetail = context.getDetail();
/*     */       
/*  94 */       if (rootCause == null) {
/*  95 */         rootCause = contextCause;
/*  96 */       } else if (contextCause != null) {
/*  97 */         rootCause.addSuppressed(contextCause);
/*     */       } 
/*  99 */       int s = details.length();
/* 100 */       if (title == null || !title.startsWith(contextMessage)) {
/* 101 */         details.append(title = contextMessage);
/*     */       }
/* 103 */       if (detail == null || !detail.startsWith(contextDetail)) {
/* 104 */         details.append(detail = contextDetail);
/*     */       }
/* 106 */       if (s != detail.length()) {
/* 107 */         details.append('\n');
/*     */       }
/*     */     } 
/*     */     
/* 111 */     String message = count + ((count > 1) ? " operações falharam" : " operação falhou") + ". Tente novamente!";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     this.display.accept(new ExceptionContext(message, details.toString(), rootCause));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/imp/AsyncFailureAlerter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */