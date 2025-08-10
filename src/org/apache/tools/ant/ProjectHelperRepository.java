/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.helper.ProjectHelper2;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.util.LoaderUtils;
/*     */ import org.apache.tools.ant.util.StreamUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProjectHelperRepository
/*     */ {
/*     */   private static final String DEBUG_PROJECT_HELPER_REPOSITORY = "ant.project-helper-repo.debug";
/*  53 */   private static final boolean DEBUG = "true"
/*  54 */     .equals(System.getProperty("ant.project-helper-repo.debug"));
/*     */   
/*  56 */   private static ProjectHelperRepository instance = new ProjectHelperRepository();
/*     */ 
/*     */   
/*  59 */   private List<Constructor<? extends ProjectHelper>> helpers = new ArrayList<>();
/*     */   
/*     */   private static Constructor<ProjectHelper2> PROJECTHELPER2_CONSTRUCTOR;
/*     */   
/*     */   static {
/*     */     try {
/*  65 */       PROJECTHELPER2_CONSTRUCTOR = ProjectHelper2.class.getConstructor(new Class[0]);
/*  66 */     } catch (Exception e) {
/*     */       
/*  68 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static ProjectHelperRepository getInstance() {
/*  73 */     return instance;
/*     */   }
/*     */   
/*     */   private ProjectHelperRepository() {
/*  77 */     collectProjectHelpers();
/*     */   }
/*     */ 
/*     */   
/*     */   private void collectProjectHelpers() {
/*  82 */     registerProjectHelper(getProjectHelperBySystemProperty());
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  87 */       ClassLoader classLoader = LoaderUtils.getContextClassLoader();
/*  88 */       if (classLoader != null) {
/*  89 */         for (URL resource : Collections.<URL>list(classLoader.getResources("META-INF/services/org.apache.tools.ant.ProjectHelper"))) {
/*  90 */           URLConnection conn = resource.openConnection();
/*  91 */           conn.setUseCaches(false);
/*  92 */           registerProjectHelper(getProjectHelperByService(conn.getInputStream()));
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*  97 */       InputStream systemResource = ClassLoader.getSystemResourceAsStream("META-INF/services/org.apache.tools.ant.ProjectHelper");
/*  98 */       if (systemResource != null) {
/*  99 */         registerProjectHelper(getProjectHelperByService(systemResource));
/*     */       }
/* 101 */     } catch (Exception e) {
/* 102 */       System.err.println("Unable to load ProjectHelper from service META-INF/services/org.apache.tools.ant.ProjectHelper (" + e
/*     */           
/* 104 */           .getClass().getName() + ": " + e
/* 105 */           .getMessage() + ")");
/* 106 */       if (DEBUG) {
/* 107 */         e.printStackTrace(System.err);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerProjectHelper(String helperClassName) throws BuildException {
/* 127 */     registerProjectHelper(getHelperConstructor(helperClassName));
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
/*     */   public void registerProjectHelper(Class<? extends ProjectHelper> helperClass) throws BuildException {
/*     */     try {
/* 144 */       registerProjectHelper(helperClass.getConstructor(new Class[0]));
/* 145 */     } catch (NoSuchMethodException e) {
/* 146 */       throw new BuildException("Couldn't find no-arg constructor in " + helperClass
/* 147 */           .getName());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void registerProjectHelper(Constructor<? extends ProjectHelper> helperConstructor) {
/* 152 */     if (helperConstructor == null) {
/*     */       return;
/*     */     }
/* 155 */     if (DEBUG) {
/* 156 */       System.out.println("ProjectHelper " + helperConstructor
/* 157 */           .getClass().getName() + " registered.");
/*     */     }
/* 159 */     this.helpers.add(helperConstructor);
/*     */   }
/*     */   
/*     */   private Constructor<? extends ProjectHelper> getProjectHelperBySystemProperty() {
/* 163 */     String helperClass = System.getProperty("org.apache.tools.ant.ProjectHelper");
/*     */     try {
/* 165 */       if (helperClass != null) {
/* 166 */         return getHelperConstructor(helperClass);
/*     */       }
/* 168 */     } catch (SecurityException e) {
/* 169 */       System.err.println("Unable to load ProjectHelper class \"" + helperClass + " specified in system property " + "org.apache.tools.ant.ProjectHelper" + " (" + e
/*     */ 
/*     */           
/* 172 */           .getMessage() + ")");
/* 173 */       if (DEBUG) {
/* 174 */         e.printStackTrace(System.err);
/*     */       }
/*     */     } 
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Constructor<? extends ProjectHelper> getProjectHelperByService(InputStream is) {
/*     */     try {
/* 184 */       BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
/*     */       
/* 186 */       String helperClassName = rd.readLine();
/* 187 */       rd.close();
/*     */       
/* 189 */       if (helperClassName != null && !helperClassName.isEmpty()) {
/* 190 */         return getHelperConstructor(helperClassName);
/*     */       }
/* 192 */     } catch (Exception e) {
/* 193 */       System.out.println("Unable to load ProjectHelper from service META-INF/services/org.apache.tools.ant.ProjectHelper (" + e
/* 194 */           .getMessage() + ")");
/* 195 */       if (DEBUG) {
/* 196 */         e.printStackTrace(System.err);
/*     */       }
/*     */     } 
/* 199 */     return null;
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
/*     */   private Constructor<? extends ProjectHelper> getHelperConstructor(String helperClass) throws BuildException {
/* 218 */     ClassLoader classLoader = LoaderUtils.getContextClassLoader();
/*     */     try {
/* 220 */       Class<?> clazz = null;
/* 221 */       if (classLoader != null) {
/*     */         try {
/* 223 */           clazz = classLoader.loadClass(helperClass);
/* 224 */         } catch (ClassNotFoundException classNotFoundException) {}
/*     */       }
/*     */ 
/*     */       
/* 228 */       if (clazz == null) {
/* 229 */         clazz = Class.forName(helperClass);
/*     */       }
/* 231 */       return clazz.<ProjectHelper>asSubclass(ProjectHelper.class).getConstructor(new Class[0]);
/* 232 */     } catch (Exception e) {
/* 233 */       throw new BuildException(e);
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
/*     */   public ProjectHelper getProjectHelperForBuildFile(Resource buildFile) throws BuildException {
/* 247 */     ProjectHelper ph = StreamUtils.iteratorAsStream(getHelpers()).filter(helper -> helper.canParseBuildFile(buildFile)).findFirst().orElse(null);
/*     */     
/* 249 */     if (ph == null) {
/* 250 */       throw new BuildException("BUG: at least the ProjectHelper2 should have supported the file " + buildFile);
/*     */     }
/*     */     
/* 253 */     if (DEBUG) {
/* 254 */       System.out.println("ProjectHelper " + ph.getClass().getName() + " selected for the build file " + buildFile);
/*     */     }
/*     */     
/* 257 */     return ph;
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
/*     */   public ProjectHelper getProjectHelperForAntlib(Resource antlib) throws BuildException {
/* 270 */     ProjectHelper ph = StreamUtils.iteratorAsStream(getHelpers()).filter(helper -> helper.canParseAntlibDescriptor(antlib)).findFirst().orElse(null);
/*     */     
/* 272 */     if (ph == null) {
/* 273 */       throw new BuildException("BUG: at least the ProjectHelper2 should have supported the file " + antlib);
/*     */     }
/*     */     
/* 276 */     if (DEBUG) {
/* 277 */       System.out.println("ProjectHelper " + ph.getClass().getName() + " selected for the antlib " + antlib);
/*     */     }
/*     */     
/* 280 */     return ph;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<ProjectHelper> getHelpers() {
/* 291 */     Stream.Builder<Constructor<? extends ProjectHelper>> b = Stream.builder();
/* 292 */     Objects.requireNonNull(b); this.helpers.forEach(b::add);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     Objects.requireNonNull(ProjectHelper.class); return b.add(PROJECTHELPER2_CONSTRUCTOR).build().map(c -> { try { return c.newInstance(new Object[0]); } catch (Exception e) { throw new BuildException("Failed to invoke no-arg constructor on " + c.getName()); }  }).map(ProjectHelper.class::cast).iterator();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/ProjectHelperRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */