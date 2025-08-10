/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ @GwtCompatible
/*    */ final class Count
/*    */   implements Serializable
/*    */ {
/*    */   private int value;
/*    */   
/*    */   Count(int value) {
/* 32 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int get() {
/* 36 */     return this.value;
/*    */   }
/*    */   
/*    */   public void add(int delta) {
/* 40 */     this.value += delta;
/*    */   }
/*    */   
/*    */   public int addAndGet(int delta) {
/* 44 */     return this.value += delta;
/*    */   }
/*    */   
/*    */   public void set(int newValue) {
/* 48 */     this.value = newValue;
/*    */   }
/*    */   
/*    */   public int getAndSet(int newValue) {
/* 52 */     int result = this.value;
/* 53 */     this.value = newValue;
/* 54 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 59 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object obj) {
/* 64 */     return (obj instanceof Count && ((Count)obj).value == this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return Integer.toString(this.value);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Count.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */