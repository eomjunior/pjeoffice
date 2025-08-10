/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ public class FirstMatchMapper
/*    */   extends ContainerMapper
/*    */ {
/*    */   public String[] mapFileName(String sourceFileName) {
/* 33 */     return getMappers().stream().filter(Objects::nonNull)
/* 34 */       .map(m -> m.mapFileName(sourceFileName)).filter(Objects::nonNull)
/* 35 */       .findFirst().orElse(null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/FirstMatchMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */