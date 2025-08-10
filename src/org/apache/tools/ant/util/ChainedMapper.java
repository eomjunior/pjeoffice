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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChainedMapper
/*    */   extends ContainerMapper
/*    */ {
/*    */   public String[] mapFileName(String sourceFileName) {
/* 40 */     String[] result = (String[])getMappers().stream().filter(Objects::nonNull).reduce(new String[] { sourceFileName }, (i, m) -> {
/*    */           Objects.requireNonNull(m); return (String[])Stream.<String>of(i).map(m::mapFileName).filter(Objects::nonNull).flatMap(Stream::of).toArray(());
/*    */         }(i, o) -> o);
/* 43 */     return (result == null || result.length == 0) ? null : result;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ChainedMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */