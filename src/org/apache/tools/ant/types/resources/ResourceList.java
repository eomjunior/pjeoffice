/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.filters.util.ChainReaderHelper;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.FilterChain;
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
/*     */ public class ResourceList
/*     */   extends DataType
/*     */   implements ResourceCollection
/*     */ {
/*  48 */   private final Vector<FilterChain> filterChains = new Vector<>();
/*  49 */   private final ArrayList<ResourceCollection> textDocuments = new ArrayList<>();
/*  50 */   private AppendableResourceCollection cachedResources = null;
/*  51 */   private String encoding = null;
/*     */ 
/*     */   
/*     */   private File baseDir;
/*     */ 
/*     */   
/*     */   private boolean preserveDuplicates = false;
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/*  61 */     if (isReference()) {
/*  62 */       throw noChildrenAllowed();
/*     */     }
/*  64 */     this.textDocuments.add(rc);
/*  65 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addFilterChain(FilterChain filter) {
/*  74 */     if (isReference()) {
/*  75 */       throw noChildrenAllowed();
/*     */     }
/*  77 */     this.filterChains.add(filter);
/*  78 */     setChecked(false);
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
/*     */ 
/*     */   
/*     */   public final void setEncoding(String encoding) {
/*  94 */     if (isReference()) {
/*  95 */       throw tooManyAttributes();
/*     */     }
/*  97 */     this.encoding = encoding;
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
/*     */   public final void setBasedir(File baseDir) {
/* 109 */     if (isReference()) {
/* 110 */       throw tooManyAttributes();
/*     */     }
/* 112 */     this.baseDir = baseDir;
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
/*     */   public final void setPreserveDuplicates(boolean preserveDuplicates) {
/* 125 */     if (isReference()) {
/* 126 */       throw tooManyAttributes();
/*     */     }
/* 128 */     this.preserveDuplicates = preserveDuplicates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) throws BuildException {
/* 139 */     if (this.encoding != null) {
/* 140 */       throw tooManyAttributes();
/*     */     }
/* 142 */     if (!this.filterChains.isEmpty() || !this.textDocuments.isEmpty()) {
/* 143 */       throw noChildrenAllowed();
/*     */     }
/* 145 */     super.setRefid(r);
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
/*     */   public final synchronized Iterator<Resource> iterator() {
/* 157 */     if (isReference()) {
/* 158 */       return getRef().iterator();
/*     */     }
/* 160 */     return cache().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 170 */     if (isReference()) {
/* 171 */       return getRef().size();
/*     */     }
/* 173 */     return cache().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isFilesystemOnly() {
/* 183 */     if (isReference()) {
/* 184 */       return getRef().isFilesystemOnly();
/*     */     }
/* 186 */     return cache().isFilesystemOnly();
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
/* 200 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 203 */     if (isReference()) {
/* 204 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 206 */       for (ResourceCollection resourceCollection : this.textDocuments) {
/* 207 */         if (resourceCollection instanceof DataType) {
/* 208 */           pushAndInvokeCircularReferenceCheck((DataType)resourceCollection, stk, p);
/*     */         }
/*     */       } 
/* 211 */       for (FilterChain filterChain : this.filterChains) {
/* 212 */         pushAndInvokeCircularReferenceCheck((DataType)filterChain, stk, p);
/*     */       }
/* 214 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ResourceList getRef() {
/* 219 */     return (ResourceList)getCheckedRef(ResourceList.class);
/*     */   }
/*     */   
/*     */   private AppendableResourceCollection newResourceCollection() {
/* 223 */     if (this.preserveDuplicates) {
/* 224 */       Resources resources = new Resources();
/* 225 */       resources.setCache(true);
/* 226 */       return resources;
/*     */     } 
/* 228 */     Union union = new Union();
/* 229 */     union.setCache(true);
/* 230 */     return union;
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized ResourceCollection cache() {
/* 235 */     if (this.cachedResources == null) {
/* 236 */       dieOnCircularReference();
/* 237 */       this.cachedResources = newResourceCollection();
/*     */       
/* 239 */       Objects.requireNonNull(this.cachedResources); this.textDocuments.stream().flatMap(ResourceCollection::stream).map(this::read).forEach(this.cachedResources::add);
/*     */     } 
/* 241 */     return this.cachedResources;
/*     */   }
/*     */   private ResourceCollection read(Resource r) {
/*     */     
/* 245 */     try { BufferedReader reader = new BufferedReader(open(r)); 
/* 246 */       try { AppendableResourceCollection streamResources = newResourceCollection();
/* 247 */         Objects.requireNonNull(streamResources); reader.lines().map(this::parse).forEach(streamResources::add);
/* 248 */         AppendableResourceCollection appendableResourceCollection1 = streamResources;
/* 249 */         reader.close(); return appendableResourceCollection1; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 250 */     { throw new BuildException("Unable to read resource " + r.getName() + ": " + ioe, ioe, 
/* 251 */           getLocation()); }
/*     */   
/*     */   }
/*     */   
/*     */   private Reader open(Resource r) throws IOException {
/* 256 */     ChainReaderHelper crh = new ChainReaderHelper();
/* 257 */     crh.setPrimaryReader(new InputStreamReader(new BufferedInputStream(r
/* 258 */             .getInputStream()), (this.encoding == null) ? 
/* 259 */           Charset.defaultCharset() : Charset.forName(this.encoding)));
/* 260 */     crh.setFilterChains(this.filterChains);
/* 261 */     crh.setProject(getProject());
/* 262 */     return (Reader)crh.getAssembledReader();
/*     */   }
/*     */ 
/*     */   
/*     */   private Resource parse(String line) {
/* 267 */     PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(getProject());
/* 268 */     Object expanded = propertyHelper.parseProperties(line);
/* 269 */     if (expanded instanceof Resource) {
/* 270 */       return (Resource)expanded;
/*     */     }
/* 272 */     String expandedLine = expanded.toString();
/* 273 */     if (expandedLine.contains(":")) {
/*     */       
/*     */       try {
/* 276 */         return new URLResource(expandedLine);
/* 277 */       } catch (BuildException buildException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     if (this.baseDir != null) {
/* 285 */       FileResource fr = new FileResource(this.baseDir, expandedLine);
/* 286 */       fr.setProject(getProject());
/* 287 */       return fr;
/*     */     } 
/* 289 */     return new FileResource(getProject(), expandedLine);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/ResourceList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */