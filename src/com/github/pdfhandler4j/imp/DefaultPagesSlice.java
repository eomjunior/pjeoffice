/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.imp.DefaultFileSlice;
/*    */ import com.github.pdfhandler4j.IPagesSlice;
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
/*    */ public final class DefaultPagesSlice
/*    */   extends DefaultFileSlice
/*    */   implements IPagesSlice
/*    */ {
/*    */   public DefaultPagesSlice() {
/* 36 */     this(1L, 2147483647L);
/*    */   }
/*    */   
/*    */   public DefaultPagesSlice(long startPage, long endPage) {
/* 40 */     super(startPage, endPage);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/DefaultPagesSlice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */