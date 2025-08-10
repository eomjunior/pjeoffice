/*    */ package com.github.filehandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IFileSlice;
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
/*    */ public class DefaultFileSlice
/*    */   implements IFileSlice
/*    */ {
/*    */   private final long start;
/*    */   private final long end;
/*    */   
/*    */   public DefaultFileSlice() {
/* 39 */     this(1L, 2147483647L);
/*    */   }
/*    */   
/*    */   public DefaultFileSlice(long start, long end) {
/* 43 */     Args.requireTrue((start >= 0L && start <= end), "invalid arguments");
/* 44 */     this.start = start;
/* 45 */     this.end = end;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long start() {
/* 50 */     return this.start;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long end() {
/* 55 */     return this.end;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/imp/DefaultFileSlice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */