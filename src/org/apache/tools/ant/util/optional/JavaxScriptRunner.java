/*     */ package org.apache.tools.ant.util.optional;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.script.Bindings;
/*     */ import javax.script.Compilable;
/*     */ import javax.script.CompiledScript;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.SimpleBindings;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ import org.apache.tools.ant.util.ScriptRunnerBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaxScriptRunner
/*     */   extends ScriptRunnerBase
/*     */ {
/*     */   private ScriptEngine keptEngine;
/*     */   private CompiledScript compiledScript;
/*     */   private static final String DROP_GRAAL_SECURITY_RESTRICTIONS = "polyglot.js.allowAllAccess";
/*     */   private static final String ENABLE_NASHORN_COMPAT_IN_GRAAL = "polyglot.js.nashorn-compat";
/*     */   
/*     */   public String getManagerName() {
/*  56 */     return "javax";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsLanguage() {
/*  62 */     if (this.keptEngine != null) {
/*  63 */       return true;
/*     */     }
/*  65 */     checkLanguage();
/*  66 */     ClassLoader origLoader = replaceContextLoader();
/*     */     try {
/*  68 */       return (createEngine() != null);
/*  69 */     } catch (Exception ex) {
/*  70 */       return false;
/*     */     } finally {
/*  72 */       restoreContextLoader(origLoader);
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
/*     */   public void executeScript(String execName) throws BuildException {
/*  86 */     evaluateScript(execName);
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
/*     */   public Object evaluateScript(String execName) throws BuildException {
/*  98 */     checkLanguage();
/*  99 */     ClassLoader origLoader = replaceContextLoader();
/*     */     try {
/* 101 */       if (getCompiled()) {
/*     */         
/* 103 */         String compiledScriptRefName = String.format("%s.%s.%d.%d", new Object[] {
/* 104 */               "org.apache.ant.scriptcache", getLanguage(), Integer.valueOf(Objects.hashCode(getScript())), 
/* 105 */               Integer.valueOf(Objects.hashCode(getClass().getClassLoader()))
/*     */             });
/* 107 */         if (null == this.compiledScript) {
/* 108 */           this.compiledScript = (CompiledScript)getProject().getReference(compiledScriptRefName);
/*     */         }
/* 110 */         if (null == this.compiledScript) {
/* 111 */           ScriptEngine scriptEngine = createEngine();
/* 112 */           if (scriptEngine == null)
/* 113 */             throw new BuildException("Unable to create javax script engine for %s", new Object[] {
/*     */                   
/* 115 */                   getLanguage()
/*     */                 }); 
/* 117 */           if (scriptEngine instanceof Compilable) {
/* 118 */             getProject().log("compile script " + execName, 3);
/*     */ 
/*     */             
/* 121 */             this
/* 122 */               .compiledScript = ((Compilable)scriptEngine).compile(getScript());
/*     */           } else {
/* 124 */             getProject().log("script compilation not available for " + execName, 3);
/*     */ 
/*     */             
/* 127 */             this.compiledScript = null;
/*     */           } 
/* 129 */           getProject().addReference(compiledScriptRefName, this.compiledScript);
/*     */         } 
/*     */         
/* 132 */         if (null != this.compiledScript) {
/* 133 */           Bindings bindings = new SimpleBindings();
/*     */           
/* 135 */           Objects.requireNonNull(bindings); applyBindings(bindings::put);
/*     */           
/* 137 */           getProject().log("run compiled script " + compiledScriptRefName, 4);
/*     */ 
/*     */ 
/*     */           
/* 141 */           return this.compiledScript.eval(bindings);
/*     */         } 
/*     */       } 
/*     */       
/* 145 */       ScriptEngine engine = createEngine();
/* 146 */       if (engine == null) {
/* 147 */         throw new BuildException("Unable to create javax script engine for " + 
/*     */             
/* 149 */             getLanguage());
/*     */       }
/*     */       
/* 152 */       Objects.requireNonNull(engine); applyBindings(engine::put);
/*     */       
/* 154 */       return engine.eval(getScript());
/*     */     }
/* 156 */     catch (BuildException be) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 163 */       throw unwrap(be);
/* 164 */     } catch (Exception be) {
/*     */       
/* 166 */       Throwable t = be;
/* 167 */       Throwable te = be.getCause();
/* 168 */       if (te != null) {
/* 169 */         if (te instanceof BuildException) {
/* 170 */           throw (BuildException)te;
/*     */         }
/* 172 */         t = te;
/*     */       } 
/*     */       
/* 175 */       throw new BuildException(t);
/*     */     } finally {
/* 177 */       restoreContextLoader(origLoader);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyBindings(BiConsumer<String, Object> target) {
/* 182 */     Map<String, Object> source = getBeans();
/*     */     
/* 184 */     if ("FX".equalsIgnoreCase(getLanguage()))
/*     */     {
/* 186 */       source = (Map<String, Object>)source.entrySet().stream().collect(Collectors.toMap(e -> String.format("%s:%s", new Object[] { e.getKey(), e.getValue().getClass().getName() }), Map.Entry::getValue));
/*     */     }
/*     */     
/* 189 */     source.forEach(target);
/*     */   }
/*     */   
/*     */   private ScriptEngine createEngine() {
/* 193 */     if (this.keptEngine != null) {
/* 194 */       return this.keptEngine;
/*     */     }
/* 196 */     if (languageIsJavaScript()) {
/* 197 */       maybeEnableNashornCompatibility();
/*     */     }
/*     */     
/* 200 */     ScriptEngine result = (new ScriptEngineManager()).getEngineByName(getLanguage());
/* 201 */     if (result == null && JavaEnvUtils.isAtLeastJavaVersion("15") && 
/* 202 */       languageIsJavaScript()) {
/* 203 */       getProject()
/* 204 */         .log("Java 15 has removed Nashorn, you must provide an engine for running JavaScript yourself. GraalVM JavaScript currently is the preferred option.", 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 209 */     maybeApplyGraalJsProperties(result);
/* 210 */     if (result != null && getKeepEngine()) {
/* 211 */       this.keptEngine = result;
/*     */     }
/* 213 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeApplyGraalJsProperties(ScriptEngine engine) {
/* 219 */     if (engine != null && engine.getClass().getName().contains("Graal")) {
/* 220 */       engine.getBindings(100)
/* 221 */         .put("polyglot.js.allowAllAccess", Boolean.valueOf(true));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeEnableNashornCompatibility() {
/* 228 */     if (getProject() != null) {
/* 229 */       System.setProperty("polyglot.js.nashorn-compat", 
/* 230 */           Project.toBoolean(getProject().getProperty("ant.disable.graal.nashorn.compat")) ? 
/* 231 */           "false" : "true");
/*     */     }
/*     */   }
/*     */   
/* 235 */   private static final List<String> JS_LANGUAGES = Arrays.asList(new String[] { "js", "javascript" });
/*     */   
/*     */   private boolean languageIsJavaScript() {
/* 238 */     return JS_LANGUAGES.contains(getLanguage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static BuildException unwrap(Throwable t) {
/* 247 */     BuildException deepest = (t instanceof BuildException) ? (BuildException)t : null;
/* 248 */     Throwable current = t;
/* 249 */     while (current.getCause() != null) {
/* 250 */       current = current.getCause();
/* 251 */       if (current instanceof BuildException) {
/* 252 */         deepest = (BuildException)current;
/*     */       }
/*     */     } 
/* 255 */     return deepest;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/optional/JavaxScriptRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */