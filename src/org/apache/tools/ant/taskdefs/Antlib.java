/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ComponentHelper;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.ProjectHelperRepository;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.TaskContainer;
/*     */ import org.apache.tools.ant.UnknownElement;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.URLResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Antlib
/*     */   extends Task
/*     */   implements TaskContainer
/*     */ {
/*     */   public static final String TAG = "antlib";
/*     */   private ClassLoader classLoader;
/*     */   
/*     */   public static Antlib createAntlib(Project project, URL antlibUrl, String uri) {
/*     */     try {
/*  67 */       URLConnection conn = antlibUrl.openConnection();
/*  68 */       conn.setUseCaches(false);
/*  69 */       conn.connect();
/*  70 */     } catch (IOException ex) {
/*  71 */       throw new BuildException("Unable to find " + antlibUrl, ex);
/*     */     } 
/*     */ 
/*     */     
/*  75 */     ComponentHelper helper = ComponentHelper.getComponentHelper(project);
/*  76 */     helper.enterAntLib(uri);
/*  77 */     URLResource antlibResource = new URLResource(antlibUrl);
/*     */     
/*     */     try {
/*  80 */       ProjectHelper parser = null;
/*     */       
/*  82 */       Object p = project.getReference("ant.projectHelper");
/*  83 */       if (p instanceof ProjectHelper) {
/*  84 */         parser = (ProjectHelper)p;
/*  85 */         if (!parser.canParseAntlibDescriptor((Resource)antlibResource)) {
/*  86 */           parser = null;
/*     */         }
/*     */       } 
/*  89 */       if (parser == null) {
/*     */         
/*  91 */         ProjectHelperRepository helperRepository = ProjectHelperRepository.getInstance();
/*  92 */         parser = helperRepository.getProjectHelperForAntlib((Resource)antlibResource);
/*     */       } 
/*     */       
/*  95 */       UnknownElement ue = parser.parseAntlibDescriptor(project, (Resource)antlibResource);
/*     */       
/*  97 */       if (!"antlib".equals(ue.getTag())) {
/*  98 */         throw new BuildException("Unexpected tag " + ue
/*  99 */             .getTag() + " expecting " + "antlib", ue
/* 100 */             .getLocation());
/*     */       }
/* 102 */       Antlib antlib = new Antlib();
/* 103 */       antlib.setProject(project);
/* 104 */       antlib.setLocation(ue.getLocation());
/* 105 */       antlib.setTaskName("antlib");
/* 106 */       antlib.init();
/* 107 */       ue.configure(antlib);
/* 108 */       return antlib;
/*     */     } finally {
/* 110 */       helper.exitAntLib();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   private String uri = "";
/* 119 */   private List<Task> tasks = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setClassLoader(ClassLoader classLoader) {
/* 129 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setURI(String uri) {
/* 137 */     this.uri = uri;
/*     */   }
/*     */   
/*     */   private ClassLoader getClassLoader() {
/* 141 */     if (this.classLoader == null) {
/* 142 */       this.classLoader = Antlib.class.getClassLoader();
/*     */     }
/* 144 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTask(Task nestedTask) {
/* 154 */     this.tasks.add(nestedTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 165 */     for (Task task : this.tasks) {
/* 166 */       UnknownElement ue = (UnknownElement)task;
/* 167 */       setLocation(ue.getLocation());
/* 168 */       ue.maybeConfigure();
/* 169 */       Object configuredObject = ue.getRealThing();
/* 170 */       if (configuredObject == null) {
/*     */         continue;
/*     */       }
/* 173 */       if (!(configuredObject instanceof AntlibDefinition)) {
/* 174 */         throw new BuildException("Invalid task in antlib %s %s does not extend %s", new Object[] { ue
/*     */               
/* 176 */               .getTag(), configuredObject.getClass(), AntlibDefinition.class
/* 177 */               .getName() });
/*     */       }
/* 179 */       AntlibDefinition def = (AntlibDefinition)configuredObject;
/* 180 */       def.setURI(this.uri);
/* 181 */       def.setAntlibClassLoader(getClassLoader());
/* 182 */       def.init();
/* 183 */       def.execute();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Antlib.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */