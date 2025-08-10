/*     */ package org.apache.tools.ant.types.selectors.modifiedselector;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildEvent;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.BuildListener;
/*     */ import org.apache.tools.ant.IntrospectionHelper;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*     */ import org.apache.tools.ant.types.selectors.BaseExtendSelector;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.ResourceUtils;
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
/*     */ public class ModifiedSelector
/*     */   extends BaseExtendSelector
/*     */   implements BuildListener, ResourceSelector
/*     */ {
/*     */   private static final String CACHE_PREFIX = "cache.";
/*     */   private static final String ALGORITHM_PREFIX = "algorithm.";
/*     */   private static final String COMPARATOR_PREFIX = "comparator.";
/* 158 */   private CacheName cacheName = null;
/*     */ 
/*     */   
/*     */   private String cacheClass;
/*     */ 
/*     */   
/* 164 */   private AlgorithmName algoName = null;
/*     */ 
/*     */   
/*     */   private String algorithmClass;
/*     */ 
/*     */   
/* 170 */   private ComparatorName compName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String comparatorClass;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean update = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean selectDirectories = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean selectResourcesWithoutInputStream = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean delayUpdate = true;
/*     */ 
/*     */ 
/*     */   
/* 195 */   private Comparator<? super String> comparator = null;
/*     */ 
/*     */   
/* 198 */   private Algorithm algorithm = null;
/*     */ 
/*     */   
/* 201 */   private Cache cache = null;
/*     */ 
/*     */   
/* 204 */   private int modified = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isConfigured = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 214 */   private List<Parameter> configParameter = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   private List<Parameter> specialParameter = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */   
/* 226 */   private ClassLoader myClassLoader = null;
/*     */ 
/*     */   
/* 229 */   private Path classpath = null;
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
/*     */   public void verifySettings() {
/* 246 */     configure();
/* 247 */     if (this.cache == null) {
/* 248 */       setError("Cache must be set.");
/* 249 */     } else if (this.algorithm == null) {
/* 250 */       setError("Algorithm must be set.");
/* 251 */     } else if (!this.cache.isValid()) {
/* 252 */       setError("Cache must be proper configured.");
/* 253 */     } else if (!this.algorithm.isValid()) {
/* 254 */       setError("Algorithm must be proper configured.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure() {
/*     */     File cachefile;
/* 280 */     if (this.isConfigured) {
/*     */       return;
/*     */     }
/* 283 */     this.isConfigured = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     Project p = getProject();
/* 289 */     String filename = "cache.properties";
/*     */     
/* 291 */     if (p != null) {
/*     */       
/* 293 */       cachefile = new File(p.getBaseDir(), filename);
/*     */ 
/*     */       
/* 296 */       getProject().addBuildListener(this);
/*     */     } else {
/*     */       
/* 299 */       cachefile = new File(filename);
/* 300 */       setDelayUpdate(false);
/*     */     } 
/* 302 */     Cache defaultCache = new PropertiesfileCache(cachefile);
/* 303 */     Algorithm defaultAlgorithm = new DigestAlgorithm();
/* 304 */     Comparator<? super String> defaultComparator = new EqualComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     for (Parameter parameter : this.configParameter) {
/* 310 */       if (parameter.getName().indexOf('.') > 0) {
/*     */         
/* 312 */         this.specialParameter.add(parameter); continue;
/*     */       } 
/* 314 */       useParameter(parameter);
/*     */     } 
/*     */     
/* 317 */     this.configParameter.clear();
/*     */ 
/*     */     
/* 320 */     if (this.algoName != null) {
/*     */       
/* 322 */       if ("hashvalue".equals(this.algoName.getValue())) {
/* 323 */         this.algorithm = new HashvalueAlgorithm();
/* 324 */       } else if ("digest".equals(this.algoName.getValue())) {
/* 325 */         this.algorithm = new DigestAlgorithm();
/* 326 */       } else if ("checksum".equals(this.algoName.getValue())) {
/* 327 */         this.algorithm = new ChecksumAlgorithm();
/* 328 */       } else if ("lastmodified".equals(this.algoName.getValue())) {
/* 329 */         this.algorithm = new LastModifiedAlgorithm();
/*     */       } 
/* 331 */     } else if (this.algorithmClass != null) {
/*     */       
/* 333 */       this.algorithm = loadClass(this.algorithmClass, "is not an Algorithm.", Algorithm.class);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 339 */       this.algorithm = defaultAlgorithm;
/*     */     } 
/*     */ 
/*     */     
/* 343 */     if (this.cacheName != null) {
/*     */       
/* 345 */       if ("propertyfile".equals(this.cacheName.getValue())) {
/* 346 */         this.cache = new PropertiesfileCache();
/*     */       }
/* 348 */     } else if (this.cacheClass != null) {
/*     */       
/* 350 */       this.cache = loadClass(this.cacheClass, "is not a Cache.", Cache.class);
/*     */     } else {
/*     */       
/* 353 */       this.cache = defaultCache;
/*     */     } 
/*     */ 
/*     */     
/* 357 */     if (this.compName != null) {
/*     */       
/* 359 */       if ("equal".equals(this.compName.getValue())) {
/* 360 */         this.comparator = new EqualComparator();
/* 361 */       } else if ("rule".equals(this.compName.getValue())) {
/*     */ 
/*     */ 
/*     */         
/* 365 */         throw new BuildException("RuleBasedCollator not yet supported.");
/*     */       }
/*     */     
/*     */     }
/* 369 */     else if (this.comparatorClass != null) {
/*     */ 
/*     */       
/* 372 */       Comparator<? super String> localComparator = loadClass(this.comparatorClass, "is not a Comparator.", (Class)Comparator.class);
/*     */       
/* 374 */       this.comparator = localComparator;
/*     */     } else {
/*     */       
/* 377 */       this.comparator = defaultComparator;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     this.specialParameter.forEach(this::useParameter);
/* 384 */     this.specialParameter.clear();
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
/*     */   protected <T> T loadClass(String classname, String msg, Class<? extends T> type) {
/*     */     try {
/*     */       Class<?> clazz;
/* 402 */       ClassLoader cl = getClassLoader();
/*     */       
/* 404 */       if (cl != null) {
/* 405 */         clazz = cl.loadClass(classname);
/*     */       } else {
/* 407 */         clazz = Class.forName(classname);
/*     */       } 
/*     */       
/* 410 */       Object rv = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*     */       
/* 412 */       if (!type.isInstance(rv)) {
/* 413 */         throw new BuildException("Specified class (%s) %s", new Object[] { classname, msg });
/*     */       }
/* 415 */       return (T)rv;
/* 416 */     } catch (ClassNotFoundException e) {
/* 417 */       throw new BuildException("Specified class (%s) not found.", new Object[] { classname });
/* 418 */     } catch (Exception e) {
/* 419 */       throw new BuildException(e);
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
/*     */   
/*     */   public boolean isSelected(Resource resource) {
/* 436 */     if (resource.isFilesystemOnly()) {
/*     */ 
/*     */       
/* 439 */       FileResource fileResource = (FileResource)resource;
/* 440 */       File file = fileResource.getFile();
/* 441 */       String filename = fileResource.getName();
/* 442 */       File basedir = fileResource.getBaseDir();
/* 443 */       return isSelected(basedir, filename, file);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 448 */       FileUtils fu = FileUtils.getFileUtils();
/* 449 */       File tmpFile = fu.createTempFile(getProject(), "modified-", ".tmp", null, true, false);
/* 450 */       FileResource fileResource = new FileResource(tmpFile);
/* 451 */       ResourceUtils.copyResource(resource, (Resource)fileResource);
/* 452 */       boolean isSelected = isSelected(tmpFile.getParentFile(), tmpFile
/* 453 */           .getName(), resource
/* 454 */           .toLongString());
/* 455 */       tmpFile.delete();
/* 456 */       return isSelected;
/* 457 */     } catch (UnsupportedOperationException uoe) {
/* 458 */       log("The resource '" + resource
/* 459 */           .getName() + "' does not provide an InputStream, so it is not checked. According to 'selres' attribute value it is " + (
/*     */ 
/*     */           
/* 462 */           this.selectResourcesWithoutInputStream ? "" : " not") + "selected.", 2);
/*     */       
/* 464 */       return this.selectResourcesWithoutInputStream;
/* 465 */     } catch (Exception e) {
/* 466 */       throw new BuildException(e);
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
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 481 */     return isSelected(basedir, filename, file.getAbsolutePath());
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
/*     */   private boolean isSelected(File basedir, String filename, String cacheKey) {
/* 494 */     validate();
/* 495 */     File f = new File(basedir, filename);
/*     */ 
/*     */     
/* 498 */     if (f.isDirectory()) {
/* 499 */       return this.selectDirectories;
/*     */     }
/*     */ 
/*     */     
/* 503 */     String cachedValue = String.valueOf(this.cache.get(f.getAbsolutePath()));
/* 504 */     String newValue = this.algorithm.getValue(f);
/*     */     
/* 506 */     boolean rv = (this.comparator.compare(cachedValue, newValue) != 0);
/*     */ 
/*     */     
/* 509 */     if (this.update && rv) {
/* 510 */       this.cache.put(f.getAbsolutePath(), newValue);
/* 511 */       setModified(getModified() + 1);
/* 512 */       if (!getDelayUpdate()) {
/* 513 */         saveCache();
/*     */       }
/*     */     } 
/* 516 */     return rv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveCache() {
/* 524 */     if (getModified() > 0) {
/* 525 */       this.cache.save();
/* 526 */       setModified(0);
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
/*     */   public void setAlgorithmClass(String classname) {
/* 539 */     this.algorithmClass = classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComparatorClass(String classname) {
/* 548 */     this.comparatorClass = classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheClass(String classname) {
/* 557 */     this.cacheClass = classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdate(boolean update) {
/* 566 */     this.update = update;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeldirs(boolean seldirs) {
/* 575 */     this.selectDirectories = seldirs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelres(boolean newValue) {
/* 584 */     this.selectResourcesWithoutInputStream = newValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getModified() {
/* 593 */     return this.modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModified(int modified) {
/* 602 */     this.modified = modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getDelayUpdate() {
/* 611 */     return this.delayUpdate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelayUpdate(boolean delayUpdate) {
/* 620 */     this.delayUpdate = delayUpdate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClasspath(Path path) {
/* 629 */     if (this.classpath != null) {
/* 630 */       throw new BuildException("<classpath> can be set only once.");
/*     */     }
/* 632 */     this.classpath = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 641 */     if (this.myClassLoader == null) {
/* 642 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 647 */         .myClassLoader = (this.classpath == null) ? getClass().getClassLoader() : (ClassLoader)getProject().createClassLoader(this.classpath);
/*     */     }
/* 649 */     return this.myClassLoader;
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
/*     */   public void setClassLoader(ClassLoader loader) {
/* 662 */     this.myClassLoader = loader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(String key, Object value) {
/* 672 */     Parameter par = new Parameter();
/* 673 */     par.setName(key);
/* 674 */     par.setValue(String.valueOf(value));
/* 675 */     this.configParameter.add(par);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(Parameter parameter) {
/* 684 */     this.configParameter.add(parameter);
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
/*     */   public void setParameters(Parameter... parameters) {
/* 697 */     if (parameters != null) {
/* 698 */       Collections.addAll(this.configParameter, parameters);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void useParameter(Parameter parameter) {
/* 717 */     String key = parameter.getName();
/* 718 */     String value = parameter.getValue();
/* 719 */     if ("cache".equals(key)) {
/* 720 */       CacheName cn = new CacheName();
/* 721 */       cn.setValue(value);
/* 722 */       setCache(cn);
/* 723 */     } else if ("algorithm".equals(key)) {
/* 724 */       AlgorithmName an = new AlgorithmName();
/* 725 */       an.setValue(value);
/* 726 */       setAlgorithm(an);
/* 727 */     } else if ("comparator".equals(key)) {
/* 728 */       ComparatorName cn = new ComparatorName();
/* 729 */       cn.setValue(value);
/* 730 */       setComparator(cn);
/* 731 */     } else if ("update".equals(key)) {
/* 732 */       setUpdate("true".equalsIgnoreCase(value));
/* 733 */     } else if ("delayupdate".equals(key)) {
/* 734 */       setDelayUpdate("true".equalsIgnoreCase(value));
/* 735 */     } else if ("seldirs".equals(key)) {
/* 736 */       setSeldirs("true".equalsIgnoreCase(value));
/* 737 */     } else if (key.startsWith("cache.")) {
/* 738 */       String name = key.substring("cache.".length());
/* 739 */       tryToSetAParameter(this.cache, name, value);
/* 740 */     } else if (key.startsWith("algorithm.")) {
/* 741 */       String name = key.substring("algorithm.".length());
/* 742 */       tryToSetAParameter(this.algorithm, name, value);
/* 743 */     } else if (key.startsWith("comparator.")) {
/* 744 */       String name = key.substring("comparator.".length());
/* 745 */       tryToSetAParameter(this.comparator, name, value);
/*     */     } else {
/* 747 */       setError("Invalid parameter " + key);
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
/*     */   protected void tryToSetAParameter(Object obj, String name, String value) {
/* 760 */     Project prj = (getProject() != null) ? getProject() : new Project();
/*     */     
/* 762 */     IntrospectionHelper iHelper = IntrospectionHelper.getHelper(prj, obj.getClass());
/*     */     try {
/* 764 */       iHelper.setAttribute(prj, obj, name, value);
/* 765 */     } catch (BuildException buildException) {}
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
/*     */   public String toString() {
/* 780 */     return String.format("{modifiedselector update=%s seldirs=%s cache=%s algorithm=%s comparator=%s}", new Object[] {
/* 781 */           Boolean.valueOf(this.update), Boolean.valueOf(this.selectDirectories), this.cache, this.algorithm, this.comparator
/*     */         });
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
/*     */   public void buildFinished(BuildEvent event) {
/* 794 */     if (getDelayUpdate()) {
/* 795 */       saveCache();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void targetFinished(BuildEvent event) {
/* 806 */     if (getDelayUpdate()) {
/* 807 */       saveCache();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskFinished(BuildEvent event) {
/* 818 */     if (getDelayUpdate()) {
/* 819 */       saveCache();
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
/*     */   public void buildStarted(BuildEvent event) {}
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
/*     */   public void targetStarted(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskStarted(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageLogged(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cache getCache() {
/* 874 */     return this.cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCache(CacheName name) {
/* 882 */     this.cacheName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CacheName
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 896 */       return new String[] { "propertyfile" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Algorithm getAlgorithm() {
/* 905 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlgorithm(AlgorithmName name) {
/* 913 */     this.algoName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class AlgorithmName
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 927 */       return new String[] { "hashvalue", "digest", "checksum", "lastmodified" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super String> getComparator() {
/* 936 */     return this.comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComparator(ComparatorName name) {
/* 944 */     this.compName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ComparatorName
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 958 */       return new String[] { "equal", "rule" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/modifiedselector/ModifiedSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */