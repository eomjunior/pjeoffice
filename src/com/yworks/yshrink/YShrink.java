/*     */ package com.yworks.yshrink;
/*     */ 
/*     */ import com.yworks.common.ShrinkBag;
/*     */ import com.yworks.logging.ConsoleLogger;
/*     */ import com.yworks.logging.Logger;
/*     */ import com.yworks.logging.XmlLogger;
/*     */ import com.yworks.yshrink.ant.filters.AllMainMethodsFilter;
/*     */ import com.yworks.yshrink.ant.filters.EntryPointFilter;
/*     */ import com.yworks.yshrink.core.Analyzer;
/*     */ import com.yworks.yshrink.core.ClassResolver;
/*     */ import com.yworks.yshrink.core.Shrinker;
/*     */ import com.yworks.yshrink.core.Writer;
/*     */ import com.yworks.yshrink.model.AbstractDescriptor;
/*     */ import com.yworks.yshrink.model.ClassDescriptor;
/*     */ import com.yworks.yshrink.model.FieldDescriptor;
/*     */ import com.yworks.yshrink.model.MethodDescriptor;
/*     */ import com.yworks.yshrink.model.Model;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class YShrink
/*     */ {
/*     */   private static final String CENTER_CLASS = "";
/*     */   private static final String CENTER_METHOD_NAME = "";
/*     */   private static final String CENTER_METHOD_DESC = "";
/*     */   private final boolean createStubs;
/*     */   private String digests;
/*     */   
/*     */   public YShrink() {
/*  62 */     this.createStubs = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public YShrink(boolean createStubs, String digests) {
/*  72 */     this.createStubs = createStubs;
/*  73 */     this.digests = digests;
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
/*     */   public void doShrinkPairs(List<ShrinkBag> pairs, EntryPointFilter epf, ClassResolver resolver) throws IOException {
/*  93 */     Analyzer analyzer = new Analyzer();
/*     */     
/*  95 */     Model model = new Model();
/*     */     
/*  97 */     if (null != resolver) model.setClassResolver(resolver);
/*     */ 
/*     */     
/* 100 */     if (!model.isSimpleModelSet()) {
/* 101 */       analyzer.initModel(model, pairs);
/*     */     }
/*     */ 
/*     */     
/* 105 */     List<AbstractDescriptor> entryPoints = markEntryPoints(model, epf);
/*     */ 
/*     */     
/* 108 */     if (model.isSimpleModelSet()) {
/* 109 */       analyzer.createDependencyEdges(model);
/*     */     } else {
/* 111 */       analyzer.createEdges(model);
/*     */     } 
/* 113 */     model.createEntryPointEdges(entryPoints);
/*     */     
/* 115 */     Shrinker shrinker = new Shrinker();
/* 116 */     shrinker.shrink(model);
/*     */     
/* 118 */     Writer writer = new Writer(this.createStubs, this.digests);
/*     */     
/* 120 */     for (ShrinkBag bag : pairs) {
/* 121 */       if (!bag.isEntryPointJar()) {
/* 122 */         writer.write(model, bag);
/*     */       }
/*     */     } 
/*     */     
/* 126 */     if (!model.isAllResolved()) {
/* 127 */       Logger.warn("Not all dependencies could be resolved. Please see the logfile for details.");
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
/*     */   private List<AbstractDescriptor> markEntryPoints(Model model, EntryPointFilter epFilter) {
/* 142 */     StringBuilder buf = new StringBuilder();
/* 143 */     buf.append("<entrypoints>\n");
/*     */     
/* 145 */     List<AbstractDescriptor> entryPoints = new ArrayList<>();
/*     */     
/* 147 */     for (ClassDescriptor cd : model.getAllClassDescriptors()) {
/*     */       
/* 149 */       epFilter.setRetainAttribute(cd);
/*     */       
/* 151 */       if (epFilter.isEntryPointClass(model, cd)) {
/* 152 */         buf.append("\t<class name=\"");
/* 153 */         buf.append(Util.toJavaClass(cd.getName()));
/* 154 */         buf.append("\" />\n");
/* 155 */         entryPoints.add(cd);
/* 156 */         cd.setEntryPoint(true);
/* 157 */         model.markNotObsolete(cd.getNode());
/*     */       } 
/* 159 */       for (MethodDescriptor md : cd.getMethods()) {
/* 160 */         if (epFilter.isEntryPointMethod(model, cd, md)) {
/* 161 */           buf.append("\t<method signature=\"");
/*     */           
/* 163 */           buf.append(XmlLogger.replaceSpecialChars(md.getSignature()));
/* 164 */           buf.append("\" class=\"");
/* 165 */           buf.append(Util.toJavaClass(cd
/* 166 */                 .getName()));
/* 167 */           buf.append("\" />\n");
/*     */           
/* 169 */           entryPoints.add(md);
/* 170 */           md.setEntryPoint(true);
/*     */           
/* 172 */           if (md.getName().equals("<init>")) {
/* 173 */             AbstractDescriptor newNodeDesc = model.getDescriptor(cd.getNewNode());
/* 174 */             if (!newNodeDesc.isEntryPoint()) {
/* 175 */               newNodeDesc.setEntryPoint(true);
/* 176 */               entryPoints.add(newNodeDesc);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 181 */       for (FieldDescriptor fd : cd.getFields()) {
/* 182 */         if (epFilter.isEntryPointField(model, cd, fd)) {
/* 183 */           buf.append("\t<field name=\"");
/* 184 */           buf.append(Util.toJavaClass(fd.getName()));
/* 185 */           buf.append("\" class=\"");
/* 186 */           buf.append(Util.toJavaClass(cd.getName()));
/* 187 */           buf.append("\" />\n");
/* 188 */           entryPoints.add(fd);
/* 189 */           fd.setEntryPoint(true);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 194 */     buf.append("</entrypoints>\n");
/* 195 */     Logger.shrinkLog(buf.toString());
/*     */     
/* 197 */     return entryPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 207 */     new ConsoleLogger();
/*     */ 
/*     */     
/*     */     try {
/* 211 */       boolean showGraph = false;
/*     */       
/* 213 */       File in = null;
/* 214 */       File out = null;
/*     */       
/* 216 */       if (args.length > 0) {
/* 217 */         in = new File(args[0]);
/*     */       }
/*     */       
/* 220 */       if (null == in) {
/* 221 */         in = new File(ClassLoader.getSystemResource("yshrink.jar").getFile());
/*     */       }
/*     */       
/* 224 */       if (args.length > 1) {
/* 225 */         out = new File(args[1]);
/*     */       }
/*     */       
/* 228 */       if (null == out) {
/* 229 */         out = new File(System.getProperty("user.dir") + "/out.jar");
/*     */       }
/*     */       
/* 232 */       if (args.length > 2) {
/* 233 */         showGraph = Boolean.parseBoolean(args[2]);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 239 */       URL[] externalLibs = { ClassLoader.getSystemResource("external.jar") };
/*     */ 
/*     */       
/* 242 */       YShrink yshrink = new YShrink();
/*     */       
/* 244 */       AllMainMethodsFilter allMainMethodsFilter = new AllMainMethodsFilter();
/*     */     }
/* 246 */     catch (Exception e) {
/* 247 */       Logger.err("An Exception occured.", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/YShrink.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */