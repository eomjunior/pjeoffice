/*    */ package com.sun.jna;
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
/*    */ public class NativeLong
/*    */   extends IntegerType
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 35 */   public static final int SIZE = Native.LONG_SIZE;
/*    */ 
/*    */   
/*    */   public NativeLong() {
/* 39 */     this(0L);
/*    */   }
/*    */ 
/*    */   
/*    */   public NativeLong(long value) {
/* 44 */     this(value, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public NativeLong(long value, boolean unsigned) {
/* 49 */     super(SIZE, value, unsigned);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/NativeLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */