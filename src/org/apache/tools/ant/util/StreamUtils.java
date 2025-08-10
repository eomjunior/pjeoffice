/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Iterator;
/*    */ import java.util.Spliterators;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Stream;
/*    */ import java.util.stream.StreamSupport;
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
/*    */ public class StreamUtils
/*    */ {
/*    */   public static <T> Stream<T> enumerationAsStream(final Enumeration<T> e) {
/* 37 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 16)
/*    */         {
/*    */           public boolean tryAdvance(Consumer<? super T> action) {
/* 40 */             if (e.hasMoreElements()) {
/* 41 */               action.accept(e.nextElement());
/* 42 */               return true;
/*    */             } 
/* 44 */             return false;
/*    */           }
/*    */           public void forEachRemaining(Consumer<? super T> action) {
/* 47 */             while (e.hasMoreElements()) {
/* 48 */               action.accept(e.nextElement());
/*    */             }
/*    */           }
/*    */         }false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Stream<T> iteratorAsStream(Iterator<T> i) {
/* 62 */     return StreamSupport.stream(
/* 63 */         Spliterators.spliteratorUnknownSize(i, 16), false);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/StreamUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */