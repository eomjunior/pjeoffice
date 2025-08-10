/*    */ package com.github.signer4j;
/*    */ 
/*    */ import com.github.utils4j.IStreamProvider;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public interface ICertificates
/*    */   extends Iterable<ICertificate>, IStreamProvider<ICertificate>
/*    */ {
/*    */   IToken getToken();
/*    */   
/*    */   int size();
/*    */   
/*    */   List<ICertificate> toList();
/*    */   
/*    */   default Stream<ICertificate> stream() {
/* 45 */     return toList().stream();
/*    */   }
/*    */   
/*    */   default boolean isEmpty() {
/* 49 */     return (size() == 0);
/*    */   }
/*    */   
/*    */   default Iterator<ICertificate> iterator() {
/* 53 */     return toList().iterator();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ICertificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */