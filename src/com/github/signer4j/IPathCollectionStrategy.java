/*    */ package com.github.signer4j;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface IPathCollectionStrategy
/*    */   extends IDriverLookupStrategy {
/*  8 */   public static final IPathCollectionStrategy NOTHING = new IPathCollectionStrategy()
/*    */     {
/*    */       public List<String> queriedPaths() {
/* 11 */         return Collections.emptyList();
/*    */       }
/*    */       
/*    */       public void lookup(IDriverVisitor visitor) {}
/*    */     };
/*    */   
/*    */   List<String> queriedPaths();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IPathCollectionStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */