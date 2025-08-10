/*    */ package com.yworks.yshrink;
/*    */ 
/*    */ import com.yworks.common.ShrinkBag;
/*    */ import com.yworks.yshrink.ant.ResourceCpResolver;
/*    */ import com.yworks.yshrink.core.Analyzer;
/*    */ import com.yworks.yshrink.core.ClassResolver;
/*    */ import com.yworks.yshrink.model.Model;
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.types.Path;
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
/*    */ public class YShrinkModelImpl
/*    */   implements YShrinkModel
/*    */ {
/* 32 */   Model model = new Model();
/*    */ 
/*    */ 
/*    */   
/*    */   public void createSimpleModel(List<ShrinkBag> bags) throws IOException {
/* 37 */     Analyzer analyzer = new Analyzer();
/* 38 */     analyzer.initModel(this.model, bags);
/* 39 */     analyzer.createInheritanceEdges(this.model);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getAllAncestorClasses(String className) {
/* 44 */     Set<String> parents = new HashSet<>(3);
/* 45 */     this.model.getAllAncestorClasses(className, parents);
/* 46 */     return parents;
/*    */   }
/*    */   
/*    */   public Set<String> getAllImplementedInterfaces(String className) {
/* 50 */     Set<String> interfaces = new HashSet<>(3);
/* 51 */     this.model.getAllImplementedInterfaces(className, interfaces);
/* 52 */     return interfaces;
/*    */   }
/*    */   
/*    */   public Collection<String> getAllClassNames() {
/* 56 */     return this.model.getAllClassNames();
/*    */   }
/*    */   
/*    */   public void setResourceClassPath(Path resourceClassPath, Task target) {
/* 60 */     ResourceCpResolver resourceCpResolver = new ResourceCpResolver(resourceClassPath, target);
/* 61 */     this.model.setClassResolver((ClassResolver)resourceCpResolver);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/YShrinkModelImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */