/*     */ package org.apache.tools.ant.taskdefs.optional.script;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.tools.ant.AntTypeDefinition;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ComponentHelper;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.taskdefs.DefBase;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.util.ClasspathUtils;
/*     */ import org.apache.tools.ant.util.ScriptRunnerBase;
/*     */ import org.apache.tools.ant.util.ScriptRunnerHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptDef
/*     */   extends DefBase
/*     */ {
/*  50 */   private ScriptRunnerHelper helper = new ScriptRunnerHelper();
/*     */ 
/*     */   
/*     */   private String name;
/*     */ 
/*     */   
/*  56 */   private List<Attribute> attributes = new ArrayList<>();
/*     */ 
/*     */   
/*  59 */   private List<NestedElement> nestedElements = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> attributeSet;
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, NestedElement> nestedElementMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/*  73 */     super.setProject(project);
/*  74 */     this.helper.setProjectComponent((ProjectComponent)this);
/*  75 */     this.helper.setSetBeans(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  85 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAttributeSupported(String attributeName) {
/*  96 */     return this.attributeSet.contains(attributeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Attribute
/*     */   {
/*     */     private String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 112 */       this.name = name.toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(Attribute attribute) {
/* 122 */     this.attributes.add(attribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NestedElement
/*     */   {
/*     */     private String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private String type;
/*     */ 
/*     */ 
/*     */     
/*     */     private String className;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 144 */       this.name = name.toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setType(String type) {
/* 156 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClassName(String className) {
/* 168 */       this.className = className;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElement(NestedElement nestedElement) {
/* 178 */     this.nestedElements.add(nestedElement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 186 */     if (this.name == null) {
/* 187 */       throw new BuildException("scriptdef requires a name attribute to name the script");
/*     */     }
/*     */ 
/*     */     
/* 191 */     if (this.helper.getLanguage() == null) {
/* 192 */       throw new BuildException("scriptdef requires a language attribute to specify the script language");
/*     */     }
/*     */ 
/*     */     
/* 196 */     if (this.helper.getSrc() == null && this.helper.getEncoding() != null) {
/* 197 */       throw new BuildException("scriptdef requires a src attribute if the encoding is set");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 202 */     if (getAntlibClassLoader() != null || hasCpDelegate()) {
/* 203 */       this.helper.setClassLoader(createLoader());
/*     */     }
/*     */     
/* 206 */     this.attributeSet = new HashSet<>();
/* 207 */     for (Attribute attribute : this.attributes) {
/* 208 */       if (attribute.name == null) {
/* 209 */         throw new BuildException("scriptdef <attribute> elements must specify an attribute name");
/*     */       }
/*     */       
/* 212 */       if (this.attributeSet.contains(attribute.name)) {
/* 213 */         throw new BuildException("scriptdef <%s> declares the %s attribute more than once", new Object[] { this.name, 
/*     */               
/* 215 */               Attribute.access$000(attribute) });
/*     */       }
/* 217 */       this.attributeSet.add(attribute.name);
/*     */     } 
/*     */     
/* 220 */     this.nestedElementMap = new HashMap<>();
/* 221 */     for (NestedElement nestedElement : this.nestedElements) {
/* 222 */       if (nestedElement.name == null) {
/* 223 */         throw new BuildException("scriptdef <element> elements must specify an element name");
/*     */       }
/*     */       
/* 226 */       if (this.nestedElementMap.containsKey(nestedElement.name)) {
/* 227 */         throw new BuildException("scriptdef <%s> declares the %s nested element more than once", new Object[] { this.name, 
/*     */               
/* 229 */               NestedElement.access$100(nestedElement) });
/*     */       }
/*     */       
/* 232 */       if (nestedElement.className == null && nestedElement
/* 233 */         .type == null) {
/* 234 */         throw new BuildException("scriptdef <element> elements must specify either a classname or type attribute");
/*     */       }
/*     */       
/* 237 */       if (nestedElement.className != null && nestedElement
/* 238 */         .type != null) {
/* 239 */         throw new BuildException("scriptdef <element> elements must specify only one of the classname and type attributes");
/*     */       }
/*     */       
/* 242 */       this.nestedElementMap.put(nestedElement.name, nestedElement);
/*     */     } 
/*     */ 
/*     */     
/* 246 */     Map<String, ScriptDef> scriptRepository = lookupScriptRepository();
/* 247 */     this.name = ProjectHelper.genComponentName(getURI(), this.name);
/* 248 */     scriptRepository.put(this.name, this);
/* 249 */     AntTypeDefinition def = new AntTypeDefinition();
/* 250 */     def.setName(this.name);
/* 251 */     def.setClass(ScriptDefBase.class);
/* 252 */     ComponentHelper.getComponentHelper(
/* 253 */         getProject()).addDataTypeDefinition(def);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, ScriptDef> lookupScriptRepository() {
/*     */     Map<String, ScriptDef> scriptRepository;
/* 263 */     Project p = getProject();
/* 264 */     synchronized (p) {
/*     */       
/* 266 */       scriptRepository = (Map<String, ScriptDef>)p.getReference("org.apache.ant.scriptrepo");
/* 267 */       if (scriptRepository == null) {
/* 268 */         scriptRepository = new HashMap<>();
/* 269 */         p.addReference("org.apache.ant.scriptrepo", scriptRepository);
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     return scriptRepository;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createNestedElement(String elementName) {
/*     */     Object instance;
/* 283 */     NestedElement definition = this.nestedElementMap.get(elementName);
/* 284 */     if (definition == null) {
/* 285 */       throw new BuildException("<%s> does not support the <%s> nested element", new Object[] { this.name, elementName });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     String classname = definition.className;
/* 292 */     if (classname == null) {
/* 293 */       instance = getProject().createTask(definition.type);
/* 294 */       if (instance == null) {
/* 295 */         instance = getProject().createDataType(definition.type);
/*     */       }
/*     */     } else {
/* 298 */       ClassLoader loader = createLoader();
/*     */       
/*     */       try {
/* 301 */         instance = ClasspathUtils.newInstance(classname, loader);
/* 302 */       } catch (BuildException e) {
/* 303 */         instance = ClasspathUtils.newInstance(classname, ScriptDef.class.getClassLoader());
/*     */       } 
/* 305 */       getProject().setProjectReference(instance);
/*     */     } 
/*     */     
/* 308 */     if (instance == null) {
/* 309 */       throw new BuildException("<%s> is unable to create the <%s> nested element", new Object[] { this.name, elementName });
/*     */     }
/*     */ 
/*     */     
/* 313 */     return instance;
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
/*     */   @Deprecated
/*     */   public void executeScript(Map<String, String> attributes, Map<String, List<Object>> elements) {
/* 327 */     executeScript(attributes, elements, (ScriptDefBase)null);
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
/*     */   public void executeScript(Map<String, String> attributes, Map<String, List<Object>> elements, ScriptDefBase instance) {
/* 341 */     ScriptRunnerBase runner = this.helper.getScriptRunner();
/* 342 */     runner.addBean("attributes", attributes);
/* 343 */     runner.addBean("elements", elements);
/* 344 */     runner.addBean("project", getProject());
/* 345 */     if (instance != null) {
/* 346 */       runner.addBean("self", instance);
/*     */     }
/* 348 */     runner.executeScript("scriptdef_" + this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManager(String manager) {
/* 357 */     this.helper.setManager(manager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/* 366 */     this.helper.setLanguage(language);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompiled(boolean compiled) {
/* 376 */     this.helper.setCompiled(compiled);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File file) {
/* 385 */     this.helper.setSrc(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 395 */     this.helper.setEncoding(encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/* 404 */     this.helper.addText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection resource) {
/* 413 */     this.helper.add(resource);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/script/ScriptDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */