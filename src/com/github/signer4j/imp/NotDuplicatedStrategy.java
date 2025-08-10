/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverLookupStrategy;
/*    */ import com.github.signer4j.IDriverSetup;
/*    */ import com.github.signer4j.IDriverVisitor;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
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
/*    */ public class NotDuplicatedStrategy
/*    */   implements IDriverLookupStrategy
/*    */ {
/* 44 */   private final Set<IDriverSetup> setups = new HashSet<>();
/*    */   
/*    */   private final List<IDriverLookupStrategy> strategies;
/*    */   
/*    */   public NotDuplicatedStrategy(IDriverLookupStrategy... strategies) {
/* 49 */     Args.requireNonNull(strategies, "Unabled to create compounded strategy with null params");
/* 50 */     this.strategies = (List<IDriverLookupStrategy>)Stream.<IDriverLookupStrategy>of(strategies).filter(f -> (f != null)).collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */   
/*    */   public void lookup(IDriverVisitor visitor) {
/* 55 */     this.setups.clear();
/* 56 */     this.strategies.stream().forEach(s -> s.lookup(this.setups::add));
/* 57 */     this.setups.stream().forEach(visitor::visit);
/*    */   }
/*    */   
/*    */   public NotDuplicatedStrategy more(IDriverLookupStrategy strategy) {
/* 61 */     if (this.strategies != null) {
/* 62 */       this.strategies.add(strategy);
/*    */     }
/* 64 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/NotDuplicatedStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */