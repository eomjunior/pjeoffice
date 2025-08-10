/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.input.DefaultInputHandler;
/*     */ import org.apache.tools.ant.input.GreedyInputHandler;
/*     */ import org.apache.tools.ant.input.InputHandler;
/*     */ import org.apache.tools.ant.input.InputRequest;
/*     */ import org.apache.tools.ant.input.MultipleChoiceInputRequest;
/*     */ import org.apache.tools.ant.input.PropertyFileInputHandler;
/*     */ import org.apache.tools.ant.input.SecureInputHandler;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.util.ClasspathUtils;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Input
/*     */   extends Task
/*     */ {
/*     */   public class Handler
/*     */     extends DefBase
/*     */   {
/*  49 */     private String refid = null;
/*  50 */     private Input.HandlerType type = null;
/*  51 */     private String classname = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setRefid(String refid) {
/*  59 */       this.refid = refid;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getRefid() {
/*  67 */       return this.refid;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClassname(String classname) {
/*  75 */       this.classname = classname;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getClassname() {
/*  83 */       return this.classname;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setType(Input.HandlerType type) {
/*  91 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Input.HandlerType getType() {
/*  99 */       return this.type;
/*     */     }
/*     */     
/*     */     private InputHandler getInputHandler() {
/* 103 */       if (this.type != null) {
/* 104 */         return this.type.getInputHandler();
/*     */       }
/* 106 */       if (this.refid != null) {
/*     */         try {
/* 108 */           return (InputHandler)getProject().getReference(this.refid);
/* 109 */         } catch (ClassCastException e) {
/* 110 */           throw new BuildException(this.refid + " does not denote an InputHandler", e);
/*     */         } 
/*     */       }
/*     */       
/* 114 */       if (this.classname != null) {
/* 115 */         return (InputHandler)ClasspathUtils.newInstance(this.classname, 
/* 116 */             createLoader(), InputHandler.class);
/*     */       }
/* 118 */       throw new BuildException("Must specify refid, classname or type");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class HandlerType
/*     */     extends EnumeratedAttribute
/*     */   {
/* 128 */     private static final String[] VALUES = new String[] { "default", "propertyfile", "greedy", "secure" };
/*     */     
/* 130 */     private static final InputHandler[] HANDLERS = new InputHandler[] { (InputHandler)new DefaultInputHandler(), (InputHandler)new PropertyFileInputHandler(), (InputHandler)new GreedyInputHandler(), (InputHandler)new SecureInputHandler() };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 138 */       return VALUES;
/*     */     }
/*     */     
/*     */     private InputHandler getInputHandler() {
/* 142 */       return HANDLERS[getIndex()];
/*     */     }
/*     */   }
/*     */   
/* 146 */   private String validargs = null;
/* 147 */   private String message = "";
/* 148 */   private String addproperty = null;
/* 149 */   private String defaultvalue = null;
/* 150 */   private Handler handler = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean messageAttribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidargs(String validargs) {
/* 162 */     this.validargs = validargs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddproperty(String addproperty) {
/* 173 */     this.addproperty = addproperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(String message) {
/* 181 */     this.message = message;
/* 182 */     this.messageAttribute = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultvalue(String defaultvalue) {
/* 193 */     this.defaultvalue = defaultvalue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String msg) {
/* 201 */     if (this.messageAttribute && msg.trim().isEmpty()) {
/*     */       return;
/*     */     }
/* 204 */     this.message += getProject().replaceProperties(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 213 */     if (this.addproperty != null && 
/* 214 */       getProject().getProperty(this.addproperty) != null) {
/* 215 */       log("skipping " + getTaskName() + " as property " + this.addproperty + " has already been set.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 220 */     InputRequest request = null;
/* 221 */     if (this.validargs != null) {
/* 222 */       List<String> accept = StringUtils.split(this.validargs, 44);
/* 223 */       MultipleChoiceInputRequest multipleChoiceInputRequest = new MultipleChoiceInputRequest(this.message, accept);
/*     */     } else {
/* 225 */       request = new InputRequest(this.message);
/*     */     } 
/* 227 */     request.setDefaultValue(this.defaultvalue);
/*     */ 
/*     */ 
/*     */     
/* 231 */     InputHandler h = (this.handler == null) ? getProject().getInputHandler() : this.handler.getInputHandler();
/*     */     
/* 233 */     h.handleInput(request);
/*     */     
/* 235 */     String value = request.getInput();
/* 236 */     if ((value == null || value.trim().isEmpty()) && this.defaultvalue != null)
/*     */     {
/* 238 */       value = this.defaultvalue;
/*     */     }
/* 240 */     if (this.addproperty != null && value != null) {
/* 241 */       getProject().setNewProperty(this.addproperty, value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Handler createHandler() {
/* 250 */     if (this.handler != null) {
/* 251 */       throw new BuildException("Cannot define > 1 nested input handler");
/*     */     }
/*     */     
/* 254 */     this.handler = new Handler();
/* 255 */     return this.handler;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Input.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */