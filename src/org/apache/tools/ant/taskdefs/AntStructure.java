/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.IntrospectionHelper;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.TaskContainer;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntStructure
/*     */   extends Task
/*     */ {
/*     */   private File output;
/*  56 */   private StructurePrinter printer = new DTDPrinter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(File output) {
/*  63 */     this.output = output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(StructurePrinter p) {
/*  72 */     this.printer = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  83 */     if (this.output == null) {
/*  84 */       throw new BuildException("output attribute is required", getLocation());
/*     */     }
/*     */ 
/*     */     
/*  88 */     try { PrintWriter out = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(this.output.toPath(), new java.nio.file.OpenOption[0]), StandardCharsets.UTF_8));
/*     */       
/*  90 */       try { this.printer.printHead(out, getProject(), new Hashtable<>(
/*  91 */               getProject().getTaskDefinitions()), new Hashtable<>(
/*  92 */               getProject().getDataTypeDefinitions()));
/*     */         
/*  94 */         this.printer.printTargetDecl(out);
/*     */         
/*  96 */         for (String typeName : getProject().getCopyOfDataTypeDefinitions().keySet()) {
/*  97 */           this.printer.printElementDecl(out, getProject(), typeName, (Class)
/*  98 */               getProject().getDataTypeDefinitions().get(typeName));
/*     */         }
/*     */         
/* 101 */         for (String tName : getProject().getCopyOfTaskDefinitions().keySet()) {
/* 102 */           this.printer.printElementDecl(out, getProject(), tName, (Class)
/* 103 */               getProject().getTaskDefinitions().get(tName));
/*     */         }
/*     */         
/* 106 */         this.printer.printTail(out);
/*     */         
/* 108 */         if (out.checkError()) {
/* 109 */           throw new IOException("Encountered an error writing Ant structure");
/*     */         }
/* 111 */         out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 112 */     { throw new BuildException("Error writing " + this.output
/* 113 */           .getAbsolutePath(), ioe, getLocation()); }
/*     */   
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
/*     */   private static class DTDPrinter
/*     */     implements StructurePrinter
/*     */   {
/*     */     private static final String BOOLEAN = "%boolean;";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final String TASKS = "%tasks;";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final String TYPES = "%types;";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     private final Hashtable<String, String> visited = new Hashtable<>();
/*     */ 
/*     */     
/*     */     public void printTail(PrintWriter out) {
/* 171 */       this.visited.clear();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void printHead(PrintWriter out, Project p, Hashtable<String, Class<?>> tasks, Hashtable<String, Class<?>> types) {
/* 177 */       printHead(out, tasks.keySet(), types.keySet());
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
/*     */     private void printHead(PrintWriter out, Set<String> tasks, Set<String> types) {
/* 189 */       out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
/* 190 */       out.println("<!ENTITY % boolean \"(true|false|on|off|yes|no)\">");
/*     */       
/* 192 */       out.println(tasks.stream().collect(
/* 193 */             Collectors.joining(" | ", "<!ENTITY % tasks \"", "\">")));
/*     */       
/* 195 */       out.println(types.stream().collect(
/* 196 */             Collectors.joining(" | ", "<!ENTITY % types \"", "\">")));
/*     */       
/* 198 */       out.println();
/*     */       
/* 200 */       out.print("<!ELEMENT project (target | extension-point | ");
/* 201 */       out.print("%tasks;");
/* 202 */       out.print(" | ");
/* 203 */       out.print("%types;");
/* 204 */       out.println(")*>");
/* 205 */       out.println("<!ATTLIST project");
/* 206 */       out.println("          name    CDATA #IMPLIED");
/* 207 */       out.println("          default CDATA #IMPLIED");
/* 208 */       out.println("          basedir CDATA #IMPLIED>");
/* 209 */       out.println("");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void printTargetDecl(PrintWriter out) {
/* 217 */       out.print("<!ELEMENT target (");
/* 218 */       out.print("%tasks;");
/* 219 */       out.print(" | ");
/* 220 */       out.print("%types;");
/* 221 */       out.println(")*>");
/* 222 */       out.println("");
/* 223 */       printTargetAttrs(out, "target");
/* 224 */       out.println("<!ELEMENT extension-point EMPTY>");
/* 225 */       out.println("");
/* 226 */       printTargetAttrs(out, "extension-point");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void printTargetAttrs(PrintWriter out, String tag) {
/* 233 */       out.print("<!ATTLIST ");
/* 234 */       out.println(tag);
/* 235 */       out.println("          id                      ID    #IMPLIED");
/* 236 */       out.println("          name                    CDATA #REQUIRED");
/* 237 */       out.println("          if                      CDATA #IMPLIED");
/* 238 */       out.println("          unless                  CDATA #IMPLIED");
/* 239 */       out.println("          depends                 CDATA #IMPLIED");
/* 240 */       out.println("          extensionOf             CDATA #IMPLIED");
/* 241 */       out.println("          onMissingExtensionPoint CDATA #IMPLIED");
/* 242 */       out.println("          description             CDATA #IMPLIED>");
/* 243 */       out.println("");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void printElementDecl(PrintWriter out, Project p, String name, Class<?> element) {
/*     */       IntrospectionHelper ih;
/* 253 */       if (this.visited.containsKey(name)) {
/*     */         return;
/*     */       }
/* 256 */       this.visited.put(name, "");
/*     */ 
/*     */       
/*     */       try {
/* 260 */         ih = IntrospectionHelper.getHelper(p, element);
/* 261 */       } catch (Throwable t) {
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 270 */       StringBuilder sb = (new StringBuilder("<!ELEMENT ")).append(name).append(" ");
/*     */       
/* 272 */       if (Reference.class.equals(element)) {
/* 273 */         sb.append(String.format("EMPTY>%n<!ATTLIST %s%n          id ID #IMPLIED%n          refid IDREF #IMPLIED>%n", new Object[] { name }));
/*     */ 
/*     */         
/* 276 */         out.println(sb);
/*     */         
/*     */         return;
/*     */       } 
/* 280 */       List<String> v = new ArrayList<>();
/* 281 */       if (ih.supportsCharacters()) {
/* 282 */         v.add("#PCDATA");
/*     */       }
/*     */       
/* 285 */       if (TaskContainer.class.isAssignableFrom(element)) {
/* 286 */         v.add("%tasks;");
/*     */       }
/*     */       
/* 289 */       v.addAll(Collections.list(ih.getNestedElements()));
/*     */ 
/*     */       
/* 292 */       Collector<CharSequence, ?, String> joinAlts = Collectors.joining(" | ", "(", ")");
/*     */       
/* 294 */       if (v.isEmpty()) {
/* 295 */         sb.append("EMPTY");
/*     */       } else {
/* 297 */         sb.append(v.stream().collect(joinAlts));
/* 298 */         if (v.size() > 1 || !"#PCDATA".equals(v.get(0))) {
/* 299 */           sb.append("*");
/*     */         }
/*     */       } 
/* 302 */       sb.append(">");
/* 303 */       out.println(sb);
/*     */       
/* 305 */       sb = new StringBuilder();
/* 306 */       sb.append(String.format("<!ATTLIST %s%n          id ID #IMPLIED", new Object[] { name }));
/*     */       
/* 308 */       for (String attrName : Collections.list(ih.getAttributes())) {
/* 309 */         if ("id".equals(attrName)) {
/*     */           continue;
/*     */         }
/*     */         
/* 313 */         sb.append(String.format("%n          %s ", new Object[] { attrName }));
/* 314 */         Class<?> type = ih.getAttributeType(attrName);
/* 315 */         if (type.equals(Boolean.class) || type.equals(boolean.class)) {
/* 316 */           sb.append("%boolean;").append(" ");
/* 317 */         } else if (Reference.class.isAssignableFrom(type)) {
/* 318 */           sb.append("IDREF ");
/* 319 */         } else if (EnumeratedAttribute.class.isAssignableFrom(type)) {
/*     */           
/*     */           try {
/* 322 */             EnumeratedAttribute ea = type.<EnumeratedAttribute>asSubclass(EnumeratedAttribute.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 323 */             String[] values = ea.getValues();
/* 324 */             if (values == null || values.length == 0 || 
/* 325 */               !areNmtokens(values)) {
/* 326 */               sb.append("CDATA ");
/*     */             } else {
/* 328 */               sb.append(Stream.<CharSequence>of((CharSequence[])values).collect(joinAlts)).append(" ");
/*     */             } 
/* 330 */           } catch (InstantiationException|IllegalAccessException|NoSuchMethodException|java.lang.reflect.InvocationTargetException ie) {
/*     */             
/* 332 */             sb.append("CDATA ");
/*     */           } 
/* 334 */         } else if (Enum.class.isAssignableFrom(type)) {
/*     */           
/*     */           try {
/* 337 */             Enum[] arrayOfEnum = (Enum[])type.getMethod("values", new Class[0]).invoke(null, new Object[0]);
/* 338 */             if (arrayOfEnum.length == 0) {
/* 339 */               sb.append("CDATA ");
/*     */             } else {
/* 341 */               sb.append(Stream.<Enum>of(arrayOfEnum).map(Enum::name)
/* 342 */                   .collect(joinAlts)).append(" ");
/*     */             } 
/* 344 */           } catch (Exception x) {
/* 345 */             sb.append("CDATA ");
/*     */           } 
/*     */         } else {
/* 348 */           sb.append("CDATA ");
/*     */         } 
/* 350 */         sb.append("#IMPLIED");
/*     */       } 
/* 352 */       sb.append(String.format(">%n", new Object[0]));
/* 353 */       out.println(sb);
/*     */       
/* 355 */       for (String nestedName : v) {
/* 356 */         if (!"#PCDATA".equals(nestedName) && 
/* 357 */           !"%tasks;".equals(nestedName) && 
/* 358 */           !"%types;".equals(nestedName)) {
/* 359 */           printElementDecl(out, p, nestedName, ih.getElementType(nestedName));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final boolean isNmtoken(String s) {
/* 370 */       int length = s.length();
/* 371 */       for (int i = 0; i < length; i++) {
/* 372 */         char c = s.charAt(i);
/*     */         
/* 374 */         if (!Character.isLetterOrDigit(c) && c != '.' && c != '-' && c != '_' && c != ':')
/*     */         {
/* 376 */           return false;
/*     */         }
/*     */       } 
/* 379 */       return true;
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
/*     */     public static final boolean areNmtokens(String[] s) {
/* 391 */       for (String value : s) {
/* 392 */         if (!isNmtoken(value)) {
/* 393 */           return false;
/*     */         }
/*     */       } 
/* 396 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private DTDPrinter() {}
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNmtoken(String s) {
/* 406 */     return DTDPrinter.isNmtoken(s);
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
/*     */   protected boolean areNmtokens(String[] s) {
/* 418 */     return DTDPrinter.areNmtokens(s);
/*     */   }
/*     */   
/*     */   public static interface StructurePrinter {
/*     */     void printHead(PrintWriter param1PrintWriter, Project param1Project, Hashtable<String, Class<?>> param1Hashtable1, Hashtable<String, Class<?>> param1Hashtable2);
/*     */     
/*     */     void printTargetDecl(PrintWriter param1PrintWriter);
/*     */     
/*     */     void printElementDecl(PrintWriter param1PrintWriter, Project param1Project, String param1String, Class<?> param1Class);
/*     */     
/*     */     void printTail(PrintWriter param1PrintWriter);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/AntStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */