/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
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
/*     */ public class Union
/*     */   extends BaseResourceCollectionContainer
/*     */ {
/*     */   public static Union getInstance(ResourceCollection rc) {
/*  43 */     return (rc instanceof Union) ? (Union)rc : new Union(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Union() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Union(Project project) {
/*  57 */     super(project);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Union(ResourceCollection rc) {
/*  65 */     this(Project.getProject(rc), rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Union(Project project, ResourceCollection rc) {
/*  74 */     super(project);
/*  75 */     add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] list() {
/*  84 */     if (isReference()) {
/*  85 */       return getRef().list();
/*     */     }
/*  87 */     return (String[])streamResources().map(Object::toString).toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource[] listResources() {
/*  95 */     if (isReference()) {
/*  96 */       return getRef().listResources();
/*     */     }
/*  98 */     return streamResources().<Resource>toArray(x$0 -> new Resource[x$0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<Resource> getCollection() {
/* 107 */     return getAllResources();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected <T> Collection<T> getCollection(boolean asString) {
/* 120 */     return asString ? (Collection)getAllToStrings() : 
/* 121 */       (Collection)getAllResources();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<String> getAllToStrings() {
/* 129 */     return (Collection<String>)streamResources(Object::toString)
/* 130 */       .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Resource> getAllResources() {
/* 138 */     return streamResources()
/* 139 */       .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
/*     */   }
/*     */   
/*     */   private Union getRef() {
/* 143 */     return (Union)getCheckedRef(Union.class);
/*     */   }
/*     */   
/*     */   private Stream<? extends Resource> streamResources() {
/* 147 */     return streamResources((Function)Function.identity());
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Stream<? extends T> streamResources(Function<? super Resource, ? extends T> mapper) {
/* 152 */     return getResourceCollections().stream()
/* 153 */       .flatMap(ResourceCollection::stream).<T>map(mapper).distinct();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Union.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */