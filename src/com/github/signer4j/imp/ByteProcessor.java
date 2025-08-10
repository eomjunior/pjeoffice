/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IByteProcessor;
/*    */ import com.github.signer4j.ICertificateChooser;
/*    */ import com.github.signer4j.IChoice;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.function.IProvider;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.locks.ReentrantLock;
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
/*    */ public abstract class ByteProcessor
/*    */   implements IByteProcessor
/*    */ {
/*    */   private final ICertificateChooser chooser;
/*    */   private final Optional<ReentrantLock> lock;
/*    */   protected final Runnable logout;
/*    */   
/*    */   public ByteProcessor(ICertificateChooser chooser, Runnable logout, Optional<ReentrantLock> lock) {
/* 51 */     this.chooser = (ICertificateChooser)Args.requireNonNull(chooser, "chooser is null");
/* 52 */     this.logout = (Runnable)Args.requireNonNull(logout, "logout is null");
/* 53 */     this.lock = (Optional<ReentrantLock>)Args.requireNonNull(lock, "lock is null");
/*    */   }
/*    */   
/*    */   protected final <T> T invoke(IProvider<T> tryBlock) throws Signer4JException {
/* 57 */     return (T)Signer4JInvoker.SIGNER4J.invoke(tryBlock, Signer4jContext.ifNotInterruptedDiscardWith(this.logout));
/*    */   }
/*    */   
/*    */   protected IChoice select() throws Signer4JException, SwitchRepositoryException {
/* 61 */     return this.lock.isPresent() ? choose(this.lock.get()) : choose();
/*    */   }
/*    */   
/*    */   private final IChoice choose(ReentrantLock lock) throws Signer4JException {
/* 65 */     return TokenAbort.HANDLER.<IChoice>handle(this::choose, lock, this.logout);
/*    */   }
/*    */   
/*    */   private final IChoice choose() throws Signer4JException, SwitchRepositoryException {
/* 69 */     IChoice choice = this.chooser.choose();
/*    */     
/* 71 */     if (choice.isCanceled()) {
/* 72 */       Signer4jContext.discardQuietly();
/* 73 */       throw new CanceledOperationException();
/*    */     } 
/*    */     
/* 76 */     return choice;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/ByteProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */