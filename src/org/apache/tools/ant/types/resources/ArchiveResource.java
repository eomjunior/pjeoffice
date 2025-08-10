/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Reference;
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
/*     */ public abstract class ArchiveResource
/*     */   extends Resource
/*     */ {
/*  35 */   private static final int NULL_ARCHIVE = Resource.getMagicNumber("null archive".getBytes());
/*     */   
/*     */   private Resource archive;
/*     */   private boolean haveEntry = false;
/*     */   private boolean modeSet = false;
/*  40 */   private int mode = 0;
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
/*     */   protected ArchiveResource(File a) {
/*  54 */     this(a, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArchiveResource(File a, boolean withEntry) {
/*  64 */     setArchive(a);
/*  65 */     this.haveEntry = withEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArchiveResource(Resource a, boolean withEntry) {
/*  75 */     addConfigured((ResourceCollection)a);
/*  76 */     this.haveEntry = withEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArchive(File a) {
/*  84 */     checkAttributesAllowed();
/*  85 */     this.archive = new FileResource(a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(int mode) {
/*  93 */     checkAttributesAllowed();
/*  94 */     this.mode = mode;
/*  95 */     this.modeSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection a) {
/* 104 */     checkChildrenAllowed();
/* 105 */     if (this.archive != null) {
/* 106 */       throw new BuildException("you must not specify more than one archive");
/*     */     }
/*     */     
/* 109 */     if (a.size() != 1) {
/* 110 */       throw new BuildException("only single argument resource collections are supported as archives");
/*     */     }
/*     */     
/* 113 */     this.archive = a.iterator().next();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getArchive() {
/* 121 */     return isReference() ? getRef().getArchive() : this.archive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 130 */     if (isReference()) {
/* 131 */       return getRef().getLastModified();
/*     */     }
/* 133 */     checkEntry();
/* 134 */     return super.getLastModified();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 143 */     if (isReference()) {
/* 144 */       return getRef().getSize();
/*     */     }
/* 146 */     checkEntry();
/* 147 */     return super.getSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 156 */     if (isReference()) {
/* 157 */       return getRef().isDirectory();
/*     */     }
/* 159 */     checkEntry();
/* 160 */     return super.isDirectory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/* 169 */     if (isReference()) {
/* 170 */       return getRef().isExists();
/*     */     }
/* 172 */     checkEntry();
/* 173 */     return super.isExists();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMode() {
/* 181 */     if (isReference()) {
/* 182 */       return getRef().getMode();
/*     */     }
/* 184 */     checkEntry();
/* 185 */     return this.mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 194 */     if (this.archive != null || this.modeSet) {
/* 195 */       throw tooManyAttributes();
/*     */     }
/* 197 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Resource another) {
/* 208 */     return equals(another) ? 0 : super.compareTo(another);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object another) {
/* 219 */     if (this == another) {
/* 220 */       return true;
/*     */     }
/* 222 */     if (isReference()) {
/* 223 */       return getRef().equals(another);
/*     */     }
/* 225 */     if (another == null || !another.getClass().equals(getClass())) {
/* 226 */       return false;
/*     */     }
/* 228 */     ArchiveResource r = (ArchiveResource)another;
/* 229 */     return (getArchive().equals(r.getArchive()) && 
/* 230 */       getName().equals(r.getName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 239 */     return super.hashCode() * (
/* 240 */       (getArchive() == null) ? NULL_ARCHIVE : getArchive().hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 249 */     return isReference() ? getRef().toString() : (
/* 250 */       getArchive().toString() + ':' + getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void checkEntry() throws BuildException {
/* 258 */     dieOnCircularReference();
/* 259 */     if (this.haveEntry) {
/*     */       return;
/*     */     }
/* 262 */     String name = getName();
/* 263 */     if (name == null) {
/* 264 */       throw new BuildException("entry name not set");
/*     */     }
/* 266 */     Resource r = getArchive();
/* 267 */     if (r == null) {
/* 268 */       throw new BuildException("archive attribute not set");
/*     */     }
/* 270 */     if (!r.isExists()) {
/* 271 */       throw new BuildException("%s does not exist.", new Object[] { r });
/*     */     }
/* 273 */     if (r.isDirectory()) {
/* 274 */       throw new BuildException("%s denotes a directory.", new Object[] { r });
/*     */     }
/* 276 */     fetchEntry();
/* 277 */     this.haveEntry = true;
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
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
/* 290 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 293 */     if (isReference()) {
/* 294 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 296 */       if (this.archive != null) {
/* 297 */         pushAndInvokeCircularReferenceCheck((DataType)this.archive, stk, p);
/*     */       }
/* 299 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ArchiveResource getRef() {
/* 305 */     return (ArchiveResource)getCheckedRef(ArchiveResource.class);
/*     */   }
/*     */   
/*     */   protected ArchiveResource() {}
/*     */   
/*     */   protected abstract void fetchEntry();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/ArchiveResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */