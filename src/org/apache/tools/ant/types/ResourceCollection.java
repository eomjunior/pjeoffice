/*    */ package org.apache.tools.ant.types;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ResourceCollection
/*    */   extends Iterable<Resource>
/*    */ {
/*    */   int size();
/*    */   
/*    */   boolean isFilesystemOnly();
/*    */   
/*    */   default Stream<? extends Resource> stream() {
/* 49 */     Stream.Builder<Resource> b = Stream.builder();
/* 50 */     forEach(b);
/* 51 */     return b.build();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean isEmpty() {
/* 59 */     return (size() == 0);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/ResourceCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */