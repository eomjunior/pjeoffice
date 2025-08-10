/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class LongAddables
/*    */ {
/*    */   private static final Supplier<LongAddable> SUPPLIER;
/*    */   
/*    */   static {
/*    */     Supplier<LongAddable> supplier;
/*    */     try {
/* 35 */       LongAdder unused = new LongAdder();
/* 36 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get()
/*    */           {
/* 40 */             return new LongAdder();
/*    */           }
/*    */         };
/* 43 */     } catch (Throwable t) {
/* 44 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get()
/*    */           {
/* 48 */             return new LongAddables.PureJavaLongAddable();
/*    */           }
/*    */         };
/*    */     } 
/* 52 */     SUPPLIER = supplier;
/*    */   }
/*    */   
/*    */   public static LongAddable create() {
/* 56 */     return (LongAddable)SUPPLIER.get();
/*    */   }
/*    */   
/*    */   private static final class PureJavaLongAddable
/*    */     extends AtomicLong implements LongAddable {
/*    */     public void increment() {
/* 62 */       getAndIncrement();
/*    */     }
/*    */     private PureJavaLongAddable() {}
/*    */     
/*    */     public void add(long x) {
/* 67 */       getAndAdd(x);
/*    */     }
/*    */ 
/*    */     
/*    */     public long sum() {
/* 72 */       return get();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/LongAddables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */