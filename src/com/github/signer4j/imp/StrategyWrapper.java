/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverLookupStrategy;
/*    */ import com.github.signer4j.IDriverVisitor;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ class StrategyWrapper
/*    */   implements IDriverLookupStrategy
/*    */ {
/*    */   private final IDriverLookupStrategy strategy;
/*    */   
/*    */   public StrategyWrapper(IDriverLookupStrategy strategy) {
/* 39 */     this.strategy = (IDriverLookupStrategy)Args.requireNonNull(strategy, "strategy can't be null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final void lookup(IDriverVisitor visitor) {
/* 44 */     this.strategy.lookup(visitor);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/StrategyWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */