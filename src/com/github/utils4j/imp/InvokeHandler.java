/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.function.IProvider;
/*    */ import java.util.function.Consumer;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public abstract class InvokeHandler<E extends Throwable>
/*    */ {
/* 39 */   private static final Logger LOGGER = LoggerFactory.getLogger(InvokeHandler.class); private static final Runnable DEFAULT_FINALLY_BLOCK = () -> {
/*    */     
/*    */     };
/*    */   static {
/* 43 */     DEFAULT_CATCH_BLOCK = (e -> LOGGER.warn("InvokeHandler (catchBlock)", e));
/*    */   }
/*    */   private static final Consumer<Throwable> DEFAULT_CATCH_BLOCK;
/*    */   
/*    */   public final <T> T invoke(IProvider<T> tryBlock) throws E {
/* 48 */     return invoke(tryBlock, DEFAULT_CATCH_BLOCK);
/*    */   }
/*    */   
/*    */   public final <T> T invoke(IProvider<T> tryBlock, Runnable finallyBlock) throws E {
/* 52 */     return invoke(tryBlock, DEFAULT_CATCH_BLOCK, finallyBlock);
/*    */   }
/*    */   
/*    */   public final <T> T invoke(IProvider<T> tryBlock, Consumer<Throwable> catchBlock) throws E {
/* 56 */     return invoke(tryBlock, catchBlock, DEFAULT_FINALLY_BLOCK);
/*    */   }
/*    */   
/*    */   public abstract <T> T invoke(IProvider<T> paramIProvider, Consumer<Throwable> paramConsumer, Runnable paramRunnable) throws E;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/InvokeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */