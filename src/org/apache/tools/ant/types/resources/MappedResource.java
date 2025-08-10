/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappedResource
/*     */   extends ResourceDecorator
/*     */ {
/*     */   private final FileNameMapper mapper;
/*     */   
/*     */   public MappedResource(Resource r, FileNameMapper m) {
/*  42 */     super((ResourceCollection)r);
/*  43 */     this.mapper = m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  51 */     String name = getResource().getName();
/*  52 */     if (isReference()) {
/*  53 */       return name;
/*     */     }
/*  55 */     String[] mapped = this.mapper.mapFileName(name);
/*  56 */     return (mapped != null && mapped.length > 0) ? mapped[0] : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/*  65 */     if (this.mapper != null) {
/*  66 */       throw noChildrenAllowed();
/*     */     }
/*  68 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T as(Class<T> clazz) {
/*  77 */     return FileProvider.class.isAssignableFrom(clazz) ? 
/*  78 */       null : (T)getResource().as(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  87 */     String n = getName();
/*  88 */     return (n == null) ? super.hashCode() : n.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  98 */     if (other == null || other.getClass() != getClass()) {
/*  99 */       return false;
/*     */     }
/* 101 */     MappedResource m = (MappedResource)other;
/* 102 */     String myName = getName();
/* 103 */     String otherName = m.getName();
/* 104 */     return (((myName == null) ? (otherName == null) : myName.equals(otherName)) && 
/* 105 */       getResource().equals(m.getResource()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     if (isReference()) {
/* 111 */       return getRef().toString();
/*     */     }
/* 113 */     return getName();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/MappedResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */