/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XfdfReader
/*     */   implements SimpleXMLDocHandler
/*     */ {
/*     */   private boolean foundRoot = false;
/*  67 */   private final Stack<String> fieldNames = new Stack<String>();
/*  68 */   private final Stack<String> fieldValues = new Stack<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HashMap<String, String> fields;
/*     */ 
/*     */ 
/*     */   
/*     */   protected HashMap<String, List<String>> listFields;
/*     */ 
/*     */ 
/*     */   
/*     */   String fileSpec;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XfdfReader(String filename) throws IOException {
/*  87 */     FileInputStream fin = null;
/*     */     try {
/*  89 */       fin = new FileInputStream(filename);
/*  90 */       SimpleXMLParser.parse(this, fin);
/*     */     } finally {
/*     */       
/*  93 */       try { if (fin != null) fin.close();  } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XfdfReader(byte[] xfdfIn) throws IOException {
/* 103 */     this(new ByteArrayInputStream(xfdfIn));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XfdfReader(InputStream is) throws IOException {
/* 113 */     SimpleXMLParser.parse(this, is);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap<String, String> getFields() {
/* 122 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getField(String name) {
/* 130 */     return this.fields.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldValue(String name) {
/* 139 */     String field = this.fields.get(name);
/* 140 */     if (field == null) {
/* 141 */       return null;
/*     */     }
/* 143 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getListValues(String name) {
/* 154 */     return this.listFields.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileSpec() {
/* 161 */     return this.fileSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String tag, Map<String, String> h) {
/* 171 */     if (!this.foundRoot) {
/* 172 */       if (!tag.equals("xfdf")) {
/* 173 */         throw new RuntimeException(MessageLocalization.getComposedMessage("root.element.is.not.xfdf.1", new Object[] { tag }));
/*     */       }
/* 175 */       this.foundRoot = true;
/*     */     } 
/*     */     
/* 178 */     if (!tag.equals("xfdf"))
/*     */     {
/* 180 */       if (tag.equals("f")) {
/* 181 */         this.fileSpec = h.get("href");
/* 182 */       } else if (tag.equals("fields")) {
/* 183 */         this.fields = new HashMap<String, String>();
/* 184 */         this.listFields = new HashMap<String, List<String>>();
/* 185 */       } else if (tag.equals("field")) {
/* 186 */         String fName = h.get("name");
/* 187 */         this.fieldNames.push(fName);
/* 188 */       } else if (tag.equals("value")) {
/* 189 */         this.fieldValues.push("");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String tag) {
/* 197 */     if (tag.equals("value")) {
/* 198 */       String fName = "";
/* 199 */       for (int k = 0; k < this.fieldNames.size(); k++) {
/* 200 */         fName = fName + "." + (String)this.fieldNames.elementAt(k);
/*     */       }
/* 202 */       if (fName.startsWith("."))
/* 203 */         fName = fName.substring(1); 
/* 204 */       String fVal = this.fieldValues.pop();
/* 205 */       String old = this.fields.put(fName, fVal);
/* 206 */       if (old != null) {
/* 207 */         List<String> l = this.listFields.get(fName);
/* 208 */         if (l == null) {
/* 209 */           l = new ArrayList<String>();
/* 210 */           l.add(old);
/*     */         } 
/* 212 */         l.add(fVal);
/* 213 */         this.listFields.put(fName, l);
/*     */       }
/*     */     
/* 216 */     } else if (tag.equals("field") && 
/* 217 */       !this.fieldNames.isEmpty()) {
/* 218 */       this.fieldNames.pop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() {
/* 227 */     this.fileSpec = "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endDocument() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String str) {
/* 242 */     if (this.fieldNames.isEmpty() || this.fieldValues.isEmpty()) {
/*     */       return;
/*     */     }
/* 245 */     String val = this.fieldValues.pop();
/* 246 */     val = val + str;
/* 247 */     this.fieldValues.push(val);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/XfdfReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */