/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import java.util.TreeMap;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.types.resources.MappedResource;
/*     */ import org.apache.tools.ant.types.resources.PropertyResource;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.regexp.RegexpMatcher;
/*     */ import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySet
/*     */   extends DataType
/*     */   implements ResourceCollection
/*     */ {
/*     */   private boolean dynamic = true;
/*     */   private boolean negate = false;
/*     */   private Set<String> cachedNames;
/*  55 */   private List<PropertyRef> ptyRefs = new ArrayList<>();
/*  56 */   private List<PropertySet> setRefs = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private Mapper mapper;
/*     */ 
/*     */   
/*     */   public static class PropertyRef
/*     */   {
/*     */     private int count;
/*     */     
/*     */     private String name;
/*     */     
/*     */     private String regex;
/*     */     
/*     */     private String prefix;
/*     */     
/*     */     private String builtin;
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/*  76 */       assertValid("name", name);
/*  77 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setRegex(String regex) {
/*  85 */       assertValid("regex", regex);
/*  86 */       this.regex = regex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPrefix(String prefix) {
/*  94 */       assertValid("prefix", prefix);
/*  95 */       this.prefix = prefix;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setBuiltin(PropertySet.BuiltinPropertySetName b) {
/* 103 */       String pBuiltIn = b.getValue();
/* 104 */       assertValid("builtin", pBuiltIn);
/* 105 */       this.builtin = pBuiltIn;
/*     */     }
/*     */     
/*     */     private void assertValid(String attr, String value) {
/* 109 */       if (value == null || value.length() < 1) {
/* 110 */         throw new BuildException("Invalid attribute: " + attr);
/*     */       }
/*     */       
/* 113 */       if (++this.count != 1) {
/* 114 */         throw new BuildException("Attributes name, regex, and prefix are mutually exclusive");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 125 */       return "name=" + this.name + ", regex=" + this.regex + ", prefix=" + this.prefix + ", builtin=" + this.builtin;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendName(String name) {
/* 136 */     PropertyRef r = new PropertyRef();
/* 137 */     r.setName(name);
/* 138 */     addPropertyref(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendRegex(String regex) {
/* 146 */     PropertyRef r = new PropertyRef();
/* 147 */     r.setRegex(regex);
/* 148 */     addPropertyref(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendPrefix(String prefix) {
/* 156 */     PropertyRef r = new PropertyRef();
/* 157 */     r.setPrefix(prefix);
/* 158 */     addPropertyref(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendBuiltin(BuiltinPropertySetName b) {
/* 166 */     PropertyRef r = new PropertyRef();
/* 167 */     r.setBuiltin(b);
/* 168 */     addPropertyref(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMapper(String type, String from, String to) {
/* 178 */     Mapper m = createMapper();
/* 179 */     Mapper.MapperType mapperType = new Mapper.MapperType();
/* 180 */     mapperType.setValue(type);
/* 181 */     m.setType(mapperType);
/* 182 */     m.setFrom(from);
/* 183 */     m.setTo(to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyref(PropertyRef ref) {
/* 191 */     assertNotReference();
/* 192 */     setChecked(false);
/* 193 */     this.ptyRefs.add(ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyset(PropertySet ref) {
/* 201 */     assertNotReference();
/* 202 */     setChecked(false);
/* 203 */     this.setRefs.add(ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper createMapper() {
/* 211 */     assertNotReference();
/* 212 */     if (this.mapper != null) {
/* 213 */       throw new BuildException("Too many <mapper>s!");
/*     */     }
/* 215 */     this.mapper = new Mapper(getProject());
/* 216 */     setChecked(false);
/* 217 */     return this.mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/* 226 */     createMapper().add(fileNameMapper);
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
/*     */   public void setDynamic(boolean dynamic) {
/* 239 */     assertNotReference();
/* 240 */     this.dynamic = dynamic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNegate(boolean negate) {
/* 250 */     assertNotReference();
/* 251 */     this.negate = negate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getDynamic() {
/* 259 */     if (isReference()) {
/* 260 */       return (getRef()).dynamic;
/*     */     }
/* 262 */     dieOnCircularReference();
/* 263 */     return this.dynamic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper getMapper() {
/* 271 */     if (isReference()) {
/* 272 */       return (getRef()).mapper;
/*     */     }
/* 274 */     dieOnCircularReference();
/* 275 */     return this.mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> getAllSystemProperties() {
/* 284 */     return (Map<String, Object>)System.getProperties().stringPropertyNames().stream()
/* 285 */       .collect(Collectors.toMap(name -> name, name -> System.getProperties().getProperty(name), (a, b) -> b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties getProperties() {
/* 293 */     Properties result = new Properties();
/* 294 */     result.putAll(getPropertyMap());
/* 295 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> getPropertyMap() {
/* 304 */     if (isReference()) {
/* 305 */       return getRef().getPropertyMap();
/*     */     }
/* 307 */     dieOnCircularReference();
/* 308 */     Mapper myMapper = getMapper();
/* 309 */     FileNameMapper m = (myMapper == null) ? null : myMapper.getImplementation();
/*     */     
/* 311 */     Map<String, Object> effectiveProperties = getEffectiveProperties();
/* 312 */     Set<String> propertyNames = getPropertyNames(effectiveProperties);
/* 313 */     Map<String, Object> result = new HashMap<>();
/*     */ 
/*     */     
/* 316 */     for (String name : propertyNames) {
/* 317 */       Object value = effectiveProperties.get(name);
/*     */ 
/*     */       
/* 320 */       if (value != null) {
/*     */ 
/*     */         
/* 323 */         if (m != null) {
/*     */           
/* 325 */           String[] newname = m.mapFileName(name);
/* 326 */           if (newname != null) {
/* 327 */             name = newname[0];
/*     */           }
/*     */         } 
/* 330 */         result.put(name, value);
/*     */       } 
/*     */     } 
/* 333 */     return result;
/*     */   }
/*     */   
/*     */   private Map<String, Object> getEffectiveProperties() {
/*     */     Map<String, Object> result;
/* 338 */     Project prj = getProject();
/*     */     
/* 340 */     if (prj == null) {
/* 341 */       result = getAllSystemProperties();
/*     */     } else {
/* 343 */       PropertyHelper ph = PropertyHelper.getPropertyHelper(prj);
/*     */ 
/*     */ 
/*     */       
/* 347 */       result = (Map<String, Object>)prj.getPropertyNames().stream().map(n -> new AbstractMap.SimpleImmutableEntry<>(n, ph.getProperty(n))).filter(kv -> (kv.getValue() != null)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
/*     */     } 
/*     */     
/* 350 */     for (PropertySet set : this.setRefs) {
/* 351 */       result.putAll(set.getPropertyMap());
/*     */     }
/* 353 */     return result;
/*     */   }
/*     */   
/*     */   private Set<String> getPropertyNames(Map<String, Object> props) {
/*     */     Set<String> names;
/* 358 */     if (getDynamic() || this.cachedNames == null) {
/* 359 */       names = new HashSet<>();
/* 360 */       addPropertyNames(names, props);
/*     */       
/* 362 */       for (PropertySet set : this.setRefs) {
/* 363 */         names.addAll(set.getPropertyMap().keySet());
/*     */       }
/* 365 */       if (this.negate) {
/*     */         
/* 367 */         HashSet<String> complement = new HashSet<>(props.keySet());
/* 368 */         complement.removeAll(names);
/* 369 */         names = complement;
/*     */       } 
/* 371 */       if (!getDynamic()) {
/* 372 */         this.cachedNames = names;
/*     */       }
/*     */     } else {
/* 375 */       names = this.cachedNames;
/*     */     } 
/* 377 */     return names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addPropertyNames(Set<String> names, Map<String, Object> props) {
/* 387 */     if (isReference()) {
/* 388 */       getRef().addPropertyNames(names, props);
/*     */     }
/* 390 */     dieOnCircularReference();
/*     */     
/* 392 */     for (PropertyRef r : this.ptyRefs) {
/* 393 */       if (r.name != null) {
/* 394 */         if (props.get(r.name) != null)
/* 395 */           names.add(r.name);  continue;
/*     */       } 
/* 397 */       if (r.prefix != null) {
/* 398 */         for (String name : props.keySet()) {
/* 399 */           if (name.startsWith(r.prefix))
/* 400 */             names.add(name); 
/*     */         }  continue;
/*     */       } 
/* 403 */       if (r.regex != null) {
/* 404 */         RegexpMatcherFactory matchMaker = new RegexpMatcherFactory();
/* 405 */         RegexpMatcher matcher = matchMaker.newRegexpMatcher();
/* 406 */         matcher.setPattern(r.regex);
/* 407 */         for (String name : props.keySet()) {
/* 408 */           if (matcher.matches(name))
/* 409 */             names.add(name); 
/*     */         }  continue;
/*     */       } 
/* 412 */       if (r.builtin != null) {
/* 413 */         switch (r.builtin) {
/*     */           case "all":
/* 415 */             names.addAll(props.keySet());
/*     */             continue;
/*     */           case "system":
/* 418 */             names.addAll(getAllSystemProperties().keySet());
/*     */             continue;
/*     */           case "commandline":
/* 421 */             names.addAll(getProject().getUserProperties().keySet());
/*     */             continue;
/*     */         } 
/* 424 */         throw new BuildException("Impossible: Invalid builtin attribute!");
/*     */       } 
/*     */ 
/*     */       
/* 428 */       throw new BuildException("Impossible: Invalid PropertyRef!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertySet getRef() {
/* 439 */     return getCheckedRef(PropertySet.class);
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
/*     */   public final void setRefid(Reference r) {
/* 451 */     if (!this.noAttributeSet) {
/* 452 */       throw tooManyAttributes();
/*     */     }
/* 454 */     super.setRefid(r);
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
/*     */   protected final void assertNotReference() {
/* 468 */     if (isReference()) {
/* 469 */       throw tooManyAttributes();
/*     */     }
/* 471 */     this.noAttributeSet = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean noAttributeSet = true;
/*     */ 
/*     */   
/*     */   public static class BuiltinPropertySetName
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     static final String ALL = "all";
/*     */     
/*     */     static final String SYSTEM = "system";
/*     */     
/*     */     static final String COMMANDLINE = "commandline";
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 490 */       return new String[] { "all", "system", "commandline" };
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
/*     */   public String toString() {
/* 503 */     if (isReference()) {
/* 504 */       return getRef().toString();
/*     */     }
/* 506 */     dieOnCircularReference();
/* 507 */     return (new TreeMap<>(getPropertyMap())).entrySet().stream()
/* 508 */       .map(e -> (String)e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", "));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/* 518 */     if (isReference()) {
/* 519 */       return getRef().iterator();
/*     */     }
/* 521 */     dieOnCircularReference();
/*     */     
/* 523 */     Stream<Resource> result = getPropertyNames(getEffectiveProperties()).stream().map(name -> new PropertyResource(getProject(), name));
/*     */     
/* 525 */     Optional<FileNameMapper> m = Optional.<Mapper>ofNullable(getMapper()).map(Mapper::getImplementation);
/* 526 */     if (m.isPresent()) {
/* 527 */       result = result.map(p -> new MappedResource(p, m.get()));
/*     */     }
/* 529 */     return result.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 538 */     return isReference() ? getRef().size() : getProperties().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 547 */     if (isReference()) {
/* 548 */       return getRef().isFilesystemOnly();
/*     */     }
/* 550 */     dieOnCircularReference();
/* 551 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 557 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 560 */     if (isReference()) {
/* 561 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 563 */       if (this.mapper != null) {
/* 564 */         pushAndInvokeCircularReferenceCheck(this.mapper, stk, p);
/*     */       }
/* 566 */       for (PropertySet propertySet : this.setRefs) {
/* 567 */         pushAndInvokeCircularReferenceCheck(propertySet, stk, p);
/*     */       }
/* 569 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/PropertySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */