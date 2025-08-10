/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Stack;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.ArchiveFileSet;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.TarFileSet;
/*     */ import org.apache.tools.ant.types.ZipFileSet;
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
/*     */ public class Archives
/*     */   extends DataType
/*     */   implements ResourceCollection, Cloneable
/*     */ {
/*  45 */   private Union zips = new Union();
/*  46 */   private Union tars = new Union();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Union createZips() {
/*  55 */     if (isReference()) {
/*  56 */       throw noChildrenAllowed();
/*     */     }
/*  58 */     setChecked(false);
/*  59 */     return this.zips;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Union createTars() {
/*  69 */     if (isReference()) {
/*  70 */       throw noChildrenAllowed();
/*     */     }
/*  72 */     setChecked(false);
/*  73 */     return this.tars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  83 */     if (isReference()) {
/*  84 */       return getRef().size();
/*     */     }
/*  86 */     dieOnCircularReference();
/*  87 */     return streamArchives().mapToInt(ArchiveFileSet::size).sum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/*  96 */     if (isReference()) {
/*  97 */       return getRef().iterator();
/*     */     }
/*  99 */     dieOnCircularReference();
/*     */     
/* 101 */     Objects.requireNonNull(Resource.class); return streamArchives().flatMap(ResourceCollection::stream).map(Resource.class::cast).iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 108 */     if (isReference()) {
/* 109 */       return getRef().isFilesystemOnly();
/*     */     }
/* 111 */     dieOnCircularReference();
/*     */     
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 123 */     if (!this.zips.getResourceCollections().isEmpty() || !this.tars.getResourceCollections().isEmpty()) {
/* 124 */       throw tooManyAttributes();
/*     */     }
/* 126 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 138 */       Archives a = (Archives)super.clone();
/* 139 */       a.zips = (Union)this.zips.clone();
/* 140 */       a.tars = (Union)this.tars.clone();
/* 141 */       return a;
/* 142 */     } catch (CloneNotSupportedException e) {
/* 143 */       throw new BuildException(e);
/*     */     } 
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
/*     */   protected Iterator<ArchiveFileSet> grabArchives() {
/* 156 */     return streamArchives().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Stream<ArchiveFileSet> streamArchives() {
/* 162 */     List<ArchiveFileSet> l = new LinkedList<>();
/* 163 */     for (Resource r : this.zips) {
/* 164 */       l.add(configureArchive((ArchiveFileSet)new ZipFileSet(), r));
/*     */     }
/* 166 */     for (Resource r : this.tars) {
/* 167 */       l.add(configureArchive((ArchiveFileSet)new TarFileSet(), r));
/*     */     }
/* 169 */     return l.stream();
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
/*     */   
/*     */   protected ArchiveFileSet configureArchive(ArchiveFileSet afs, Resource src) {
/* 182 */     afs.setProject(getProject());
/* 183 */     afs.setSrcResource(src);
/* 184 */     return afs;
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
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 198 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 201 */     if (isReference()) {
/* 202 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 204 */       pushAndInvokeCircularReferenceCheck(this.zips, stk, p);
/* 205 */       pushAndInvokeCircularReferenceCheck(this.tars, stk, p);
/* 206 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Archives getRef() {
/* 211 */     return (Archives)getCheckedRef(Archives.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Archives.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */