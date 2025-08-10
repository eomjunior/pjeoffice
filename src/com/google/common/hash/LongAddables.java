/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Supplier;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ final class LongAddables
/*    */ {
/*    */   private static final Supplier<LongAddable> SUPPLIER;
/*    */   
/*    */   static {
/*    */     Supplier<LongAddable> supplier;
/*    */     try {
/* 33 */       LongAdder unused = new LongAdder();
/* 34 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get()
/*    */           {
/* 38 */             return new LongAdder();
/*    */           }
/*    */         };
/* 41 */     } catch (Throwable t) {
/* 42 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get()
/*    */           {
/* 46 */             return new LongAddables.PureJavaLongAddable();
/*    */           }
/*    */         };
/*    */     } 
/* 50 */     SUPPLIER = supplier;
/*    */   }
/*    */   
/*    */   public static LongAddable create() {
/* 54 */     return (LongAddable)SUPPLIER.get();
/*    */   }
/*    */   
/*    */   private static final class PureJavaLongAddable
/*    */     extends AtomicLong implements LongAddable {
/*    */     public void increment() {
/* 60 */       getAndIncrement();
/*    */     }
/*    */     private PureJavaLongAddable() {}
/*    */     
/*    */     public void add(long x) {
/* 65 */       getAndAdd(x);
/*    */     }
/*    */ 
/*    */     
/*    */     public long sum() {
/* 70 */       return get();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/LongAddables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */