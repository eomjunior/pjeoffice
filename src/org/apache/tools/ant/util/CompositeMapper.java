/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ public class CompositeMapper
/*    */   extends ContainerMapper
/*    */ {
/*    */   public String[] mapFileName(String sourceFileName) {
/* 33 */     String[] result = (String[])getMappers().stream().filter(Objects::nonNull).map(m -> m.mapFileName(sourceFileName)).filter(Objects::nonNull).flatMap(Stream::of).toArray(x$0 -> new String[x$0]);
/* 34 */     return (result.length == 0) ? null : result;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/CompositeMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */