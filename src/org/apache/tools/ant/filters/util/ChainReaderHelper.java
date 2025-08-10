/*     */ package org.apache.tools.ant.filters.util;
/*     */ 
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Vector;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.filters.BaseFilterReader;
/*     */ import org.apache.tools.ant.filters.ChainableReader;
/*     */ import org.apache.tools.ant.types.AntFilterReader;
/*     */ import org.apache.tools.ant.types.FilterChain;
/*     */ import org.apache.tools.ant.types.Parameterizable;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public final class ChainReaderHelper
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   public Reader primaryReader;
/*     */   
/*     */   public class ChainReader
/*     */     extends FilterReader
/*     */   {
/*     */     private List<AntClassLoader> cleanupLoaders;
/*     */     
/*     */     private ChainReader(Reader in, List<AntClassLoader> cleanupLoaders) {
/*  58 */       super(in);
/*  59 */       this.cleanupLoaders = cleanupLoaders;
/*     */     }
/*     */     
/*     */     public String readFully() throws IOException {
/*  63 */       return ChainReaderHelper.this.readFully(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/*  68 */       ChainReaderHelper.cleanUpClassLoaders(this.cleanupLoaders);
/*  69 */       super.close();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void finalize() throws Throwable {
/*     */       try {
/*  75 */         close();
/*     */       } finally {
/*  77 */         super.finalize();
/*     */       } 
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
/*     */ 
/*     */ 
/*     */   
/*  93 */   public int bufferSize = 8192;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public Vector<FilterChain> filterChains = new Vector<>();
/*     */ 
/*     */   
/* 101 */   private Project project = null;
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
/*     */   public ChainReaderHelper(Project project, Reader primaryReader, Iterable<FilterChain> filterChains) {
/* 119 */     withProject(project).withPrimaryReader(primaryReader)
/* 120 */       .withFilterChains(filterChains);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrimaryReader(Reader rdr) {
/* 128 */     this.primaryReader = rdr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainReaderHelper withPrimaryReader(Reader rdr) {
/* 137 */     setPrimaryReader(rdr);
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/* 146 */     this.project = project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainReaderHelper withProject(Project project) {
/* 155 */     setProject(project);
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/* 165 */     return this.project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferSize(int size) {
/* 174 */     this.bufferSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainReaderHelper withBufferSize(int size) {
/* 183 */     setBufferSize(size);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilterChains(Vector<FilterChain> fchain) {
/* 193 */     this.filterChains = fchain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainReaderHelper withFilterChains(Iterable<FilterChain> filterChains) {
/*     */     Vector<FilterChain> fcs;
/* 203 */     if (filterChains instanceof Vector) {
/* 204 */       fcs = (Vector<FilterChain>)filterChains;
/*     */     } else {
/* 206 */       fcs = new Vector<>();
/* 207 */       Objects.requireNonNull(fcs); filterChains.forEach(fcs::add);
/*     */     } 
/* 209 */     setFilterChains(fcs);
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainReaderHelper with(Consumer<ChainReaderHelper> consumer) {
/* 219 */     consumer.accept(this);
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainReader getAssembledReader() throws BuildException {
/* 229 */     if (this.primaryReader == null) {
/* 230 */       throw new BuildException("primaryReader must not be null.");
/*     */     }
/*     */     
/* 233 */     Reader instream = this.primaryReader;
/* 234 */     List<AntClassLoader> classLoadersToCleanUp = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 238 */     List<Object> finalFilters = (List<Object>)this.filterChains.stream().map(FilterChain::getFilterReaders).flatMap(Collection::stream).collect(Collectors.toList());
/*     */     
/* 240 */     if (!finalFilters.isEmpty()) {
/* 241 */       boolean success = false;
/*     */       try {
/* 243 */         for (Object o : finalFilters) {
/* 244 */           if (o instanceof AntFilterReader) {
/*     */             
/* 246 */             instream = expandReader((AntFilterReader)o, instream, classLoadersToCleanUp); continue;
/*     */           } 
/* 248 */           if (o instanceof ChainableReader) {
/* 249 */             setProjectOnObject(o);
/* 250 */             instream = ((ChainableReader)o).chain(instream);
/* 251 */             setProjectOnObject(instream);
/*     */           } 
/*     */         } 
/* 254 */         success = true;
/*     */       } finally {
/* 256 */         if (!success && !classLoadersToCleanUp.isEmpty()) {
/* 257 */           cleanUpClassLoaders(classLoadersToCleanUp);
/*     */         }
/*     */       } 
/*     */     } 
/* 261 */     return new ChainReader(instream, classLoadersToCleanUp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setProjectOnObject(Object obj) {
/* 270 */     if (this.project == null) {
/*     */       return;
/*     */     }
/* 273 */     if (obj instanceof BaseFilterReader) {
/* 274 */       ((BaseFilterReader)obj).setProject(this.project);
/*     */       return;
/*     */     } 
/* 277 */     this.project.setProjectReference(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void cleanUpClassLoaders(List<AntClassLoader> loaders) {
/* 284 */     loaders.forEach(AntClassLoader::cleanup);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readFully(Reader rdr) throws IOException {
/* 295 */     return FileUtils.readFully(rdr, this.bufferSize);
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
/*     */   private Reader expandReader(AntFilterReader filter, Reader ancestor, List<AntClassLoader> classLoadersToCleanUp) {
/* 307 */     String className = filter.getClassName();
/* 308 */     Path classpath = filter.getClasspath();
/* 309 */     if (className != null) {
/*     */       try {
/*     */         Class<? extends FilterReader> clazz;
/*     */         try {
/* 313 */           if (classpath == null) {
/*     */             
/* 315 */             clazz = Class.forName(className).asSubclass(FilterReader.class);
/*     */           } else {
/*     */             
/* 318 */             AntClassLoader al = filter.getProject().createClassLoader(classpath);
/* 319 */             classLoadersToCleanUp.add(al);
/*     */             
/* 321 */             clazz = Class.forName(className, true, (ClassLoader)al).asSubclass(FilterReader.class);
/*     */           } 
/* 323 */         } catch (ClassCastException ex) {
/* 324 */           throw new BuildException("%s does not extend %s", new Object[] { className, FilterReader.class
/* 325 */                 .getName() });
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 332 */         Optional<Constructor<?>> ctor = Stream.<Constructor<?>>of(clazz.getConstructors()).filter(c -> (c.getParameterCount() == 1 && c.getParameterTypes()[0].isAssignableFrom(Reader.class))).findFirst();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 338 */         Object instream = ((Constructor)ctor.<Throwable>orElseThrow(() -> new BuildException("%s does not define a public constructor that takes in a %s as its single argument.", new Object[] { className, Reader.class.getSimpleName() }))).newInstance(new Object[] { ancestor });
/*     */         
/* 340 */         setProjectOnObject(instream);
/* 341 */         if (Parameterizable.class.isAssignableFrom(clazz)) {
/* 342 */           ((Parameterizable)instream).setParameters(filter.getParams());
/*     */         }
/* 344 */         return (Reader)instream;
/* 345 */       } catch (ClassNotFoundException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
/*     */         Class<? extends FilterReader> clazz;
/* 347 */         throw new BuildException(clazz);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 353 */     return ancestor;
/*     */   }
/*     */   
/*     */   public ChainReaderHelper() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/util/ChainReaderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */