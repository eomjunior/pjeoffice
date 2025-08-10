/*    */ package com.github.filehandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IFileHandler;
/*    */ import com.github.filehandler4j.IFileInfoEvent;
/*    */ import com.github.filehandler4j.IInputDescriptor;
/*    */ import com.github.filehandler4j.IInputFile;
/*    */ import com.github.filehandler4j.IOutputResolver;
/*    */ import io.reactivex.Emitter;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableEmitter;
/*    */ import java.io.File;
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
/*    */ public abstract class AbstractFileHandler<T extends IFileInfoEvent>
/*    */   implements IFileHandler<T>
/*    */ {
/*    */   private IOutputResolver resolver;
/*    */   
/*    */   protected void checkInterrupted() throws InterruptedException {
/* 49 */     if (Thread.currentThread().isInterrupted()) {
/* 50 */       throw new InterruptedException();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final Observable<T> apply(IInputDescriptor desc) {
/* 56 */     this.resolver = (IOutputResolver)desc;
/* 57 */     return Observable.create(emitter -> {
/*    */           try {
/*    */             beforeHandle((Emitter<T>)emitter);
/*    */             for (IInputFile file : desc.getInputFiles()) {
/*    */               checkInterrupted();
/*    */               handle(file, (Emitter<T>)emitter);
/*    */             } 
/*    */             afterHandle((Emitter<T>)emitter);
/*    */             emitter.onComplete();
/* 66 */           } catch (Throwable e) {
/*    */             handleError(e);
/*    */             emitter.onError(e);
/*    */           } finally {
/*    */             reset();
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 77 */     this.resolver = null;
/*    */   }
/*    */   
/*    */   protected final File resolveOutput(String fileName) {
/* 81 */     return this.resolver.resolveOutput(fileName);
/*    */   }
/*    */   
/*    */   protected void interruptAndWait(Thread reader) throws InterruptedException {
/* 85 */     reader.interrupt();
/* 86 */     reader.join(3000L);
/*    */   }
/*    */   
/*    */   protected void beforeHandle(Emitter<T> emitter) throws Exception {}
/*    */   
/*    */   protected void afterHandle(Emitter<T> emitter) throws Exception {}
/*    */   
/*    */   protected void handleError(Throwable e) {}
/*    */   
/*    */   protected abstract void handle(IInputFile paramIInputFile, Emitter<T> paramEmitter) throws Exception;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/imp/AbstractFileHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */