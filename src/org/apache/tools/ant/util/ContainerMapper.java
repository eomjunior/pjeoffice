/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.tools.ant.types.Mapper;
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
/*    */ public abstract class ContainerMapper
/*    */   implements FileNameMapper
/*    */ {
/* 34 */   private List<FileNameMapper> mappers = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addConfiguredMapper(Mapper mapper) {
/* 41 */     add(mapper.getImplementation());
/*    */   }
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
/*    */   public void addConfigured(FileNameMapper fileNameMapper) {
/* 54 */     add(fileNameMapper);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void add(FileNameMapper fileNameMapper) {
/* 66 */     if (this == fileNameMapper || (fileNameMapper instanceof ContainerMapper && ((ContainerMapper)fileNameMapper)
/*    */       
/* 68 */       .contains(this))) {
/* 69 */       throw new IllegalArgumentException("Circular mapper containment condition detected");
/*    */     }
/*    */     
/* 72 */     this.mappers.add(fileNameMapper);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected synchronized boolean contains(FileNameMapper fileNameMapper) {
/* 82 */     for (FileNameMapper m : this.mappers) {
/* 83 */       if (m == fileNameMapper) {
/* 84 */         return true;
/*    */       }
/* 86 */       if (m instanceof ContainerMapper && ((ContainerMapper)m)
/* 87 */         .contains(fileNameMapper)) {
/* 88 */         return true;
/*    */       }
/*    */     } 
/* 91 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized List<FileNameMapper> getMappers() {
/* 99 */     return Collections.unmodifiableList(this.mappers);
/*    */   }
/*    */   
/*    */   public void setFrom(String ignore) {}
/*    */   
/*    */   public void setTo(String ignore) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ContainerMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */