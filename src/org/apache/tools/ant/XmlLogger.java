/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.apache.tools.ant.util.DOMElementWriter;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlLogger
/*     */   implements BuildLogger
/*     */ {
/*  60 */   private int msgOutputLevel = 4;
/*     */   
/*     */   private PrintStream outStream;
/*     */   
/*  64 */   private static DocumentBuilder builder = getDocumentBuilder(); private static final String BUILD_TAG = "build"; private static final String TARGET_TAG = "target"; private static final String TASK_TAG = "task"; private static final String MESSAGE_TAG = "message";
/*     */   private static final String NAME_ATTR = "name";
/*     */   private static final String TIME_ATTR = "time";
/*     */   private static final String PRIORITY_ATTR = "priority";
/*     */   private static final String LOCATION_ATTR = "location";
/*     */   private static final String ERROR_ATTR = "error";
/*     */   private static final String STACKTRACE_TAG = "stacktrace";
/*     */   
/*     */   private static DocumentBuilder getDocumentBuilder() {
/*     */     try {
/*  74 */       return DocumentBuilderFactory.newInstance().newDocumentBuilder();
/*  75 */     } catch (Exception exc) {
/*  76 */       throw new ExceptionInInitializerError(exc);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private Document doc = builder.newDocument();
/*     */ 
/*     */   
/* 114 */   private Map<Task, TimedElement> tasks = new Hashtable<>();
/*     */ 
/*     */   
/* 117 */   private Map<Target, TimedElement> targets = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   private Map<Thread, Stack<TimedElement>> threadStacks = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   private TimedElement buildElement = null;
/*     */ 
/*     */   
/*     */   private static class TimedElement
/*     */   {
/*     */     private long startTime;
/*     */     
/*     */     private Element element;
/*     */ 
/*     */     
/*     */     private TimedElement() {}
/*     */ 
/*     */     
/*     */     public String toString() {
/* 142 */       return this.element.getTagName() + ":" + this.element.getAttribute("name");
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
/*     */   public void buildStarted(BuildEvent event) {
/* 154 */     this.buildElement = new TimedElement();
/* 155 */     this.buildElement.startTime = System.currentTimeMillis();
/* 156 */     this.buildElement.element = this.doc.createElement("build");
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
/* 168 */     long totalTime = System.currentTimeMillis() - this.buildElement.startTime;
/* 169 */     this.buildElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
/*     */     
/* 171 */     if (event.getException() != null) {
/* 172 */       this.buildElement.element.setAttribute("error", event.getException().toString());
/*     */ 
/*     */       
/* 175 */       Throwable t = event.getException();
/* 176 */       Text errText = this.doc.createCDATASection(StringUtils.getStackTrace(t));
/* 177 */       Element stacktrace = this.doc.createElement("stacktrace");
/* 178 */       stacktrace.appendChild(errText);
/* 179 */       synchronizedAppend(this.buildElement.element, stacktrace);
/*     */     } 
/* 181 */     String outFilename = getProperty(event, "XmlLogger.file", "log.xml");
/* 182 */     String xslUri = getProperty(event, "ant.XmlLogger.stylesheet.uri", "log.xsl");
/*     */ 
/*     */     
/* 185 */     try { OutputStream stream = (this.outStream == null) ? Files.newOutputStream(Paths.get(outFilename, new String[0]), new java.nio.file.OpenOption[0]) : this.outStream; 
/* 186 */       try { Writer out = new OutputStreamWriter(stream, StandardCharsets.UTF_8); 
/* 187 */         try { out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
/* 188 */           if (!xslUri.isEmpty()) {
/* 189 */             out.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + xslUri + "\"?>\n\n");
/*     */           }
/*     */           
/* 192 */           (new DOMElementWriter()).write(this.buildElement.element, out, 0, "\t");
/* 193 */           out.flush();
/* 194 */           out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException exc)
/* 195 */     { throw new BuildException("Unable to write log file", exc); }
/*     */     
/* 197 */     this.buildElement = null;
/*     */   }
/*     */   
/*     */   private String getProperty(BuildEvent event, String propertyName, String defaultValue) {
/* 201 */     String rv = defaultValue;
/* 202 */     if (event != null && event.getProject() != null && event.getProject().getProperty(propertyName) != null) {
/* 203 */       rv = event.getProject().getProperty(propertyName);
/*     */     }
/* 205 */     return rv;
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
/*     */   private Stack<TimedElement> getStack() {
/* 219 */     return this.threadStacks.computeIfAbsent(Thread.currentThread(), k -> new Stack());
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
/*     */   public void targetStarted(BuildEvent event) {
/* 232 */     Target target = event.getTarget();
/* 233 */     TimedElement targetElement = new TimedElement();
/* 234 */     targetElement.startTime = System.currentTimeMillis();
/* 235 */     targetElement.element = this.doc.createElement("target");
/* 236 */     targetElement.element.setAttribute("name", target.getName());
/* 237 */     this.targets.put(target, targetElement);
/* 238 */     getStack().push(targetElement);
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
/*     */   public void targetFinished(BuildEvent event) {
/* 250 */     Target target = event.getTarget();
/* 251 */     TimedElement targetElement = this.targets.get(target);
/* 252 */     if (targetElement != null) {
/* 253 */       long totalTime = System.currentTimeMillis() - targetElement.startTime;
/* 254 */       targetElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
/*     */       
/* 256 */       TimedElement parentElement = null;
/* 257 */       Stack<TimedElement> threadStack = getStack();
/* 258 */       if (!threadStack.empty()) {
/* 259 */         TimedElement poppedStack = threadStack.pop();
/* 260 */         if (poppedStack != targetElement) {
/* 261 */           throw new RuntimeException("Mismatch - popped element = " + poppedStack + " finished target element = " + targetElement);
/*     */         }
/*     */         
/* 264 */         if (!threadStack.empty()) {
/* 265 */           parentElement = threadStack.peek();
/*     */         }
/*     */       } 
/* 268 */       if (parentElement == null) {
/* 269 */         synchronizedAppend(this.buildElement.element, targetElement.element);
/*     */       } else {
/* 271 */         synchronizedAppend(parentElement.element, targetElement
/* 272 */             .element);
/*     */       } 
/*     */     } 
/* 275 */     this.targets.remove(target);
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
/*     */   public void taskStarted(BuildEvent event) {
/* 288 */     TimedElement taskElement = new TimedElement();
/* 289 */     taskElement.startTime = System.currentTimeMillis();
/* 290 */     taskElement.element = this.doc.createElement("task");
/*     */     
/* 292 */     Task task = event.getTask();
/* 293 */     String name = event.getTask().getTaskName();
/* 294 */     if (name == null) {
/* 295 */       name = "";
/*     */     }
/* 297 */     taskElement.element.setAttribute("name", name);
/* 298 */     taskElement.element.setAttribute("location", event.getTask().getLocation().toString());
/* 299 */     this.tasks.put(task, taskElement);
/* 300 */     getStack().push(taskElement);
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
/*     */   public void taskFinished(BuildEvent event) {
/* 312 */     Task task = event.getTask();
/* 313 */     TimedElement taskElement = this.tasks.get(task);
/* 314 */     if (taskElement == null) {
/* 315 */       throw new RuntimeException("Unknown task " + task + " not in " + this.tasks);
/*     */     }
/* 317 */     long totalTime = System.currentTimeMillis() - taskElement.startTime;
/* 318 */     taskElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
/* 319 */     Target target = task.getOwningTarget();
/* 320 */     TimedElement targetElement = null;
/* 321 */     if (target != null) {
/* 322 */       targetElement = this.targets.get(target);
/*     */     }
/* 324 */     if (targetElement == null) {
/* 325 */       synchronizedAppend(this.buildElement.element, taskElement.element);
/*     */     } else {
/* 327 */       synchronizedAppend(targetElement.element, taskElement.element);
/*     */     } 
/* 329 */     Stack<TimedElement> threadStack = getStack();
/* 330 */     if (!threadStack.empty()) {
/* 331 */       TimedElement poppedStack = threadStack.pop();
/* 332 */       if (poppedStack != taskElement) {
/* 333 */         throw new RuntimeException("Mismatch - popped element = " + poppedStack + " finished task element = " + taskElement);
/*     */       }
/*     */     } 
/*     */     
/* 337 */     this.tasks.remove(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TimedElement getTaskElement(Task task) {
/* 347 */     TimedElement element = this.tasks.get(task);
/* 348 */     if (element != null) {
/* 349 */       return element;
/*     */     }
/* 351 */     Set<Task> knownTasks = new HashSet<>(this.tasks.keySet());
/* 352 */     for (Task t : knownTasks) {
/* 353 */       if (t instanceof UnknownElement && ((UnknownElement)t).getTask() == task) {
/* 354 */         return this.tasks.get(t);
/*     */       }
/*     */     } 
/* 357 */     return null;
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
/*     */   public void messageLogged(BuildEvent event) {
/*     */     String name;
/* 370 */     int priority = event.getPriority();
/* 371 */     if (priority > this.msgOutputLevel) {
/*     */       return;
/*     */     }
/* 374 */     Element messageElement = this.doc.createElement("message");
/*     */ 
/*     */     
/* 377 */     switch (priority) {
/*     */       case 0:
/* 379 */         name = "error";
/*     */         break;
/*     */       case 1:
/* 382 */         name = "warn";
/*     */         break;
/*     */       case 2:
/* 385 */         name = "info";
/*     */         break;
/*     */       default:
/* 388 */         name = "debug";
/*     */         break;
/*     */     } 
/* 391 */     messageElement.setAttribute("priority", name);
/*     */     
/* 393 */     Throwable ex = event.getException();
/* 394 */     if (4 <= this.msgOutputLevel && ex != null) {
/* 395 */       Text errText = this.doc.createCDATASection(StringUtils.getStackTrace(ex));
/* 396 */       Element stacktrace = this.doc.createElement("stacktrace");
/* 397 */       stacktrace.appendChild(errText);
/* 398 */       synchronizedAppend(this.buildElement.element, stacktrace);
/*     */     } 
/* 400 */     Text messageText = this.doc.createCDATASection(event.getMessage());
/* 401 */     messageElement.appendChild(messageText);
/*     */     
/* 403 */     TimedElement parentElement = null;
/*     */     
/* 405 */     Task task = event.getTask();
/*     */     
/* 407 */     Target target = event.getTarget();
/* 408 */     if (task != null) {
/* 409 */       parentElement = getTaskElement(task);
/*     */     }
/* 411 */     if (parentElement == null && target != null) {
/* 412 */       parentElement = this.targets.get(target);
/*     */     }
/* 414 */     if (parentElement != null) {
/* 415 */       synchronizedAppend(parentElement.element, messageElement);
/*     */     } else {
/* 417 */       synchronizedAppend(this.buildElement.element, messageElement);
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
/*     */   public void setMessageOutputLevel(int level) {
/* 432 */     this.msgOutputLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputPrintStream(PrintStream output) {
/* 443 */     this.outStream = new PrintStream(output, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmacsMode(boolean emacsMode) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorPrintStream(PrintStream err) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void synchronizedAppend(Node parent, Node child) {
/* 468 */     synchronized (parent) {
/* 469 */       parent.appendChild(child);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/XmlLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */