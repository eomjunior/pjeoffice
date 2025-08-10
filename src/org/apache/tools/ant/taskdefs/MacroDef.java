/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.tools.ant.AntTypeDefinition;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ComponentHelper;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.RuntimeConfigurable;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.TaskContainer;
/*     */ import org.apache.tools.ant.UnknownElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MacroDef
/*     */   extends AntlibDefinition
/*     */ {
/*     */   private NestedSequential nestedSequential;
/*     */   private String name;
/*     */   private boolean backTrace = true;
/*  48 */   private List<Attribute> attributes = new ArrayList<>();
/*  49 */   private Map<String, TemplateElement> elements = new HashMap<>();
/*  50 */   private String textName = null;
/*  51 */   private Text text = null;
/*     */ 
/*     */   
/*     */   private boolean hasImplicitElement = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  59 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredText(Text text) {
/*  68 */     if (this.text != null) {
/*  69 */       throw new BuildException("Only one nested text element allowed");
/*     */     }
/*     */     
/*  72 */     if (text.getName() == null) {
/*  73 */       throw new BuildException("the text nested element needed a \"name\" attribute");
/*     */     }
/*     */ 
/*     */     
/*  77 */     for (Attribute attribute : this.attributes) {
/*  78 */       if (text.getName().equals(attribute.getName())) {
/*  79 */         throw new BuildException("the name \"%s\" is already used as an attribute", new Object[] { text
/*     */               
/*  81 */               .getName() });
/*     */       }
/*     */     } 
/*  84 */     this.text = text;
/*  85 */     this.textName = text.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Text getText() {
/*  93 */     return this.text;
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
/*     */   public void setBackTrace(boolean backTrace) {
/* 107 */     this.backTrace = backTrace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBackTrace() {
/* 115 */     return this.backTrace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedSequential createSequential() {
/* 124 */     if (this.nestedSequential != null) {
/* 125 */       throw new BuildException("Only one sequential allowed");
/*     */     }
/* 127 */     this.nestedSequential = new NestedSequential();
/* 128 */     return this.nestedSequential;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NestedSequential
/*     */     implements TaskContainer
/*     */   {
/* 136 */     private List<Task> nested = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addTask(Task task) {
/* 145 */       this.nested.add(task);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<Task> getNested() {
/* 152 */       return this.nested;
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
/*     */     public boolean similar(NestedSequential other) {
/* 164 */       int size = this.nested.size();
/* 165 */       if (size != other.nested.size()) {
/* 166 */         return false;
/*     */       }
/* 168 */       for (int i = 0; i < size; i++) {
/* 169 */         UnknownElement me = (UnknownElement)this.nested.get(i);
/* 170 */         UnknownElement o = (UnknownElement)other.nested.get(i);
/* 171 */         if (!me.similar(o)) {
/* 172 */           return false;
/*     */         }
/*     */       } 
/* 175 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnknownElement getNestedTask() {
/* 184 */     UnknownElement ret = new UnknownElement("sequential");
/* 185 */     ret.setTaskName("sequential");
/* 186 */     ret.setNamespace("");
/* 187 */     ret.setQName("sequential");
/*     */ 
/*     */     
/* 190 */     new RuntimeConfigurable(ret, "sequential");
/* 191 */     int size = this.nestedSequential.getNested().size();
/* 192 */     for (int i = 0; i < size; i++) {
/*     */       
/* 194 */       UnknownElement e = (UnknownElement)this.nestedSequential.getNested().get(i);
/* 195 */       ret.addChild(e);
/* 196 */       ret.getWrapper().addChild(e.getWrapper());
/*     */     } 
/* 198 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Attribute> getAttributes() {
/* 207 */     return this.attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, TemplateElement> getElements() {
/* 217 */     return this.elements;
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
/*     */   public static boolean isValidNameCharacter(char c) {
/* 230 */     return (Character.isLetterOrDigit(c) || c == '.' || c == '-');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidName(String name) {
/* 240 */     if (name.isEmpty()) {
/* 241 */       return false;
/*     */     }
/* 243 */     for (int i = 0; i < name.length(); i++) {
/* 244 */       if (!isValidNameCharacter(name.charAt(i))) {
/* 245 */         return false;
/*     */       }
/*     */     } 
/* 248 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredAttribute(Attribute attribute) {
/* 257 */     if (attribute.getName() == null) {
/* 258 */       throw new BuildException("the attribute nested element needed a \"name\" attribute");
/*     */     }
/*     */     
/* 261 */     if (attribute.getName().equals(this.textName)) {
/* 262 */       throw new BuildException("the name \"%s\" has already been used by the text element", new Object[] { attribute
/*     */             
/* 264 */             .getName() });
/*     */     }
/* 266 */     for (Attribute att : this.attributes) {
/* 267 */       if (att.getName().equals(attribute.getName())) {
/* 268 */         throw new BuildException("the name \"%s\" has already been used in another attribute element", new Object[] { attribute
/*     */               
/* 270 */               .getName() });
/*     */       }
/*     */     } 
/* 273 */     this.attributes.add(attribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredElement(TemplateElement element) {
/* 282 */     if (element.getName() == null) {
/* 283 */       throw new BuildException("the element nested element needed a \"name\" attribute");
/*     */     }
/*     */     
/* 286 */     if (this.elements.get(element.getName()) != null) {
/* 287 */       throw new BuildException("the element %s has already been specified", new Object[] { element
/* 288 */             .getName() });
/*     */     }
/* 290 */     if (this.hasImplicitElement || (element
/* 291 */       .isImplicit() && !this.elements.isEmpty())) {
/* 292 */       throw new BuildException("Only one element allowed when using implicit elements");
/*     */     }
/*     */     
/* 295 */     this.hasImplicitElement = element.isImplicit();
/* 296 */     this.elements.put(element.getName(), element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 304 */     if (this.nestedSequential == null) {
/* 305 */       throw new BuildException("Missing sequential element");
/*     */     }
/* 307 */     if (this.name == null) {
/* 308 */       throw new BuildException("Name not specified");
/*     */     }
/*     */     
/* 311 */     this.name = ProjectHelper.genComponentName(getURI(), this.name);
/*     */     
/* 313 */     MyAntTypeDefinition def = new MyAntTypeDefinition(this);
/* 314 */     def.setName(this.name);
/* 315 */     def.setClass(MacroInstance.class);
/*     */     
/* 317 */     ComponentHelper helper = ComponentHelper.getComponentHelper(
/* 318 */         getProject());
/*     */     
/* 320 */     helper.addDataTypeDefinition(def);
/* 321 */     log("creating macro  " + this.name, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Attribute
/*     */   {
/*     */     private String name;
/*     */ 
/*     */     
/*     */     private String defaultValue;
/*     */ 
/*     */     
/*     */     private String description;
/*     */     
/*     */     private boolean doubleExpanding = true;
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 340 */       if (!MacroDef.isValidName(name)) {
/* 341 */         throw new BuildException("Illegal name [%s] for attribute", new Object[] { name });
/*     */       }
/*     */       
/* 344 */       this.name = name.toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 351 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDefault(String defaultValue) {
/* 361 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDefault() {
/* 368 */       return this.defaultValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDescription(String desc) {
/* 376 */       this.description = desc;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 385 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDoubleExpanding(boolean doubleExpanding) {
/* 394 */       this.doubleExpanding = doubleExpanding;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDoubleExpanding() {
/* 404 */       return this.doubleExpanding;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 415 */       if (obj == null) {
/* 416 */         return false;
/*     */       }
/* 418 */       if (obj.getClass() != getClass()) {
/* 419 */         return false;
/*     */       }
/* 421 */       Attribute other = (Attribute)obj;
/* 422 */       if (this.name == null) {
/* 423 */         if (other.name != null) {
/* 424 */           return false;
/*     */         }
/* 426 */       } else if (!this.name.equals(other.name)) {
/* 427 */         return false;
/*     */       } 
/* 429 */       return (this.defaultValue == null) ? ((other.defaultValue == null)) : 
/* 430 */         this.defaultValue.equals(other.defaultValue);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 438 */       return Objects.hashCode(this.defaultValue) + Objects.hashCode(this.name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Text
/*     */   {
/*     */     private String name;
/*     */ 
/*     */     
/*     */     private boolean optional;
/*     */     
/*     */     private boolean trim;
/*     */     
/*     */     private String description;
/*     */     
/*     */     private String defaultString;
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 459 */       if (!MacroDef.isValidName(name)) {
/* 460 */         throw new BuildException("Illegal name [%s] for element", new Object[] { name });
/*     */       }
/*     */       
/* 463 */       this.name = name.toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 470 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setOptional(boolean optional) {
/* 479 */       this.optional = optional;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getOptional() {
/* 486 */       return this.optional;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTrim(boolean trim) {
/* 496 */       this.trim = trim;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getTrim() {
/* 503 */       return this.trim;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDescription(String desc) {
/* 510 */       this.description = desc;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 518 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDefault(String defaultString) {
/* 525 */       this.defaultString = defaultString;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDefault() {
/* 532 */       return this.defaultString;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 543 */       if (obj == null) {
/* 544 */         return false;
/*     */       }
/* 546 */       if (obj.getClass() != getClass()) {
/* 547 */         return false;
/*     */       }
/* 549 */       Text other = (Text)obj;
/* 550 */       return (Objects.equals(this.name, other.name) && this.optional == other.optional && this.trim == other.trim && 
/*     */ 
/*     */         
/* 553 */         Objects.equals(this.defaultString, other.defaultString));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 561 */       return Objects.hashCode(this.name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TemplateElement
/*     */   {
/*     */     private String name;
/*     */ 
/*     */     
/*     */     private String description;
/*     */ 
/*     */     
/*     */     private boolean optional = false;
/*     */     
/*     */     private boolean implicit = false;
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 581 */       if (!MacroDef.isValidName(name)) {
/* 582 */         throw new BuildException("Illegal name [%s] for macro element", new Object[] { name });
/*     */       }
/*     */       
/* 585 */       this.name = name.toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 594 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDescription(String desc) {
/* 605 */       this.description = desc;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 616 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setOptional(boolean optional) {
/* 626 */       this.optional = optional;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isOptional() {
/* 635 */       return this.optional;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setImplicit(boolean implicit) {
/* 645 */       this.implicit = implicit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isImplicit() {
/* 654 */       return this.implicit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 665 */       if (obj == this) {
/* 666 */         return true;
/*     */       }
/* 668 */       if (obj == null || !obj.getClass().equals(getClass())) {
/* 669 */         return false;
/*     */       }
/* 671 */       TemplateElement t = (TemplateElement)obj;
/* 672 */       if ((this.name == null) ? (t.name == null) : this.name
/* 673 */         .equals(t.name)) if (this.optional == t.optional && this.implicit == t.implicit);
/*     */       
/*     */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 683 */       return Objects.hashCode(this.name) + (
/* 684 */         this.optional ? 1 : 0) + (this.implicit ? 1 : 0);
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
/*     */   private boolean sameOrSimilar(Object obj, boolean same) {
/* 698 */     if (obj == this) {
/* 699 */       return true;
/*     */     }
/*     */     
/* 702 */     if (obj == null || !obj.getClass().equals(getClass())) {
/* 703 */       return false;
/*     */     }
/*     */     
/* 706 */     MacroDef other = (MacroDef)obj;
/* 707 */     if (this.name == null) {
/* 708 */       return (other.name == null);
/*     */     }
/* 710 */     if (!this.name.equals(other.name)) {
/* 711 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 715 */     if (other.getLocation() != null && other
/* 716 */       .getLocation().equals(getLocation()) && !same)
/*     */     {
/* 718 */       return true;
/*     */     }
/* 720 */     if (this.text == null) {
/* 721 */       if (other.text != null) {
/* 722 */         return false;
/*     */       }
/* 724 */     } else if (!this.text.equals(other.text)) {
/* 725 */       return false;
/*     */     } 
/* 727 */     if (getURI() == null || getURI().isEmpty() || 
/* 728 */       getURI().equals("antlib:org.apache.tools.ant")) {
/* 729 */       if (other.getURI() != null && !other.getURI().isEmpty() && 
/* 730 */         !other.getURI().equals("antlib:org.apache.tools.ant")) {
/* 731 */         return false;
/*     */       }
/* 733 */     } else if (!getURI().equals(other.getURI())) {
/* 734 */       return false;
/*     */     } 
/*     */     
/* 737 */     return (this.nestedSequential.similar(other.nestedSequential) && this.attributes
/* 738 */       .equals(other.attributes) && this.elements.equals(other.elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean similar(Object obj) {
/* 748 */     return sameOrSimilar(obj, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean sameDefinition(Object obj) {
/* 758 */     return sameOrSimilar(obj, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MyAntTypeDefinition
/*     */     extends AntTypeDefinition
/*     */   {
/*     */     private MacroDef macroDef;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MyAntTypeDefinition(MacroDef macroDef) {
/* 775 */       this.macroDef = macroDef;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object create(Project project) {
/* 786 */       Object o = super.create(project);
/* 787 */       if (o == null) {
/* 788 */         return null;
/*     */       }
/* 790 */       ((MacroInstance)o).setMacroDef(this.macroDef);
/* 791 */       return o;
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
/*     */     public boolean sameDefinition(AntTypeDefinition other, Project project) {
/* 803 */       if (!super.sameDefinition(other, project)) {
/* 804 */         return false;
/*     */       }
/* 806 */       MyAntTypeDefinition otherDef = (MyAntTypeDefinition)other;
/* 807 */       return this.macroDef.sameDefinition(otherDef.macroDef);
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
/*     */     
/*     */     public boolean similarDefinition(AntTypeDefinition other, Project project) {
/* 820 */       if (!super.similarDefinition(other, project)) {
/* 821 */         return false;
/*     */       }
/* 823 */       MyAntTypeDefinition otherDef = (MyAntTypeDefinition)other;
/* 824 */       return this.macroDef.similar(otherDef.macroDef);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/MacroDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */