/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IRequestRejectNotifier;
/*    */ import com.sun.net.httpserver.Filter;
/*    */ 
/*    */ public abstract class RejectableFilter
/*    */   extends Filter
/*    */ {
/*    */   protected final IRequestRejectNotifier notifier;
/*    */   
/*    */   public RejectableFilter(IRequestRejectNotifier notifier) {
/* 12 */     this.notifier = Args.<IRequestRejectNotifier>requireNonNull(notifier, "notifier is null");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/RejectableFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */