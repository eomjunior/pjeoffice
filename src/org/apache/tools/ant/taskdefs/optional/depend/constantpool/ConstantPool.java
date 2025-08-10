/*     */ package org.apache.tools.ant.taskdefs.optional.depend.constantpool;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstantPool
/*     */ {
/*  40 */   private final List<ConstantPoolEntry> entries = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private final Map<String, Integer> utf8Indexes = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantPool() {
/*  52 */     this.entries.add(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void read(DataInputStream classStream) throws IOException {
/*  63 */     int numEntries = classStream.readUnsignedShort();
/*     */     
/*  65 */     for (int i = 1; i < numEntries; ) {
/*     */       
/*  67 */       ConstantPoolEntry nextEntry = ConstantPoolEntry.readEntry(classStream);
/*     */       
/*  69 */       i += nextEntry.getNumEntries();
/*     */       
/*  71 */       addEntry(nextEntry);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  81 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int addEntry(ConstantPoolEntry entry) {
/*  92 */     int index = this.entries.size();
/*     */     
/*  94 */     this.entries.add(entry);
/*     */     
/*  96 */     int numSlots = entry.getNumEntries();
/*     */ 
/*     */     
/*  99 */     for (int j = 0; j < numSlots - 1; j++) {
/* 100 */       this.entries.add(null);
/*     */     }
/*     */     
/* 103 */     if (entry instanceof Utf8CPInfo) {
/* 104 */       Utf8CPInfo utf8Info = (Utf8CPInfo)entry;
/*     */       
/* 106 */       this.utf8Indexes.put(utf8Info.getValue(), Integer.valueOf(index));
/*     */     } 
/*     */     
/* 109 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resolve() {
/* 118 */     for (ConstantPoolEntry poolInfo : this.entries) {
/* 119 */       if (poolInfo != null && !poolInfo.isResolved()) {
/* 120 */         poolInfo.resolve(this);
/*     */       }
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
/*     */   public ConstantPoolEntry getEntry(int index) {
/* 133 */     return this.entries.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUTF8Entry(String value) {
/* 144 */     int index = -1;
/* 145 */     Integer indexInteger = this.utf8Indexes.get(value);
/*     */     
/* 147 */     if (indexInteger != null) {
/* 148 */       index = indexInteger.intValue();
/*     */     }
/*     */     
/* 151 */     return index;
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
/*     */   public int getClassEntry(String className) {
/* 163 */     int index = -1;
/*     */     
/* 165 */     int size = this.entries.size();
/* 166 */     for (int i = 0; i < size && index == -1; i++) {
/* 167 */       Object element = this.entries.get(i);
/*     */       
/* 169 */       if (element instanceof ClassCPInfo) {
/* 170 */         ClassCPInfo classinfo = (ClassCPInfo)element;
/*     */         
/* 172 */         if (classinfo.getClassName().equals(className)) {
/* 173 */           index = i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     return index;
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
/*     */   public int getConstantEntry(Object constantValue) {
/* 190 */     int index = -1;
/*     */     
/* 192 */     int size = this.entries.size();
/* 193 */     for (int i = 0; i < size && index == -1; i++) {
/* 194 */       Object element = this.entries.get(i);
/*     */       
/* 196 */       if (element instanceof ConstantCPInfo) {
/* 197 */         ConstantCPInfo constantEntry = (ConstantCPInfo)element;
/*     */         
/* 199 */         if (constantEntry.getValue().equals(constantValue)) {
/* 200 */           index = i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 205 */     return index;
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
/*     */   public int getMethodRefEntry(String methodClassName, String methodName, String methodType) {
/* 221 */     int index = -1;
/*     */     
/* 223 */     int size = this.entries.size();
/* 224 */     for (int i = 0; i < size && index == -1; i++) {
/* 225 */       Object element = this.entries.get(i);
/*     */       
/* 227 */       if (element instanceof MethodRefCPInfo) {
/* 228 */         MethodRefCPInfo methodRefEntry = (MethodRefCPInfo)element;
/*     */         
/* 230 */         if (methodRefEntry.getMethodClassName().equals(methodClassName) && methodRefEntry
/* 231 */           .getMethodName().equals(methodName) && methodRefEntry
/* 232 */           .getMethodType().equals(methodType)) {
/* 233 */           index = i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     return index;
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
/*     */   public int getInterfaceMethodRefEntry(String interfaceMethodClassName, String interfaceMethodName, String interfaceMethodType) {
/* 256 */     int index = -1;
/*     */     
/* 258 */     int size = this.entries.size();
/* 259 */     for (int i = 0; i < size && index == -1; i++) {
/* 260 */       Object element = this.entries.get(i);
/*     */       
/* 262 */       if (element instanceof InterfaceMethodRefCPInfo) {
/* 263 */         InterfaceMethodRefCPInfo interfaceMethodRefEntry = (InterfaceMethodRefCPInfo)element;
/*     */ 
/*     */         
/* 266 */         if (interfaceMethodRefEntry.getInterfaceMethodClassName().equals(interfaceMethodClassName) && interfaceMethodRefEntry
/*     */           
/* 268 */           .getInterfaceMethodName().equals(interfaceMethodName) && interfaceMethodRefEntry
/*     */           
/* 270 */           .getInterfaceMethodType().equals(interfaceMethodType))
/*     */         {
/* 272 */           index = i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     return index;
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
/*     */   public int getFieldRefEntry(String fieldClassName, String fieldName, String fieldType) {
/* 293 */     int index = -1;
/*     */     
/* 295 */     int size = this.entries.size();
/* 296 */     for (int i = 0; i < size && index == -1; i++) {
/* 297 */       Object element = this.entries.get(i);
/*     */       
/* 299 */       if (element instanceof FieldRefCPInfo) {
/* 300 */         FieldRefCPInfo fieldRefEntry = (FieldRefCPInfo)element;
/*     */         
/* 302 */         if (fieldRefEntry.getFieldClassName().equals(fieldClassName) && fieldRefEntry
/* 303 */           .getFieldName().equals(fieldName) && fieldRefEntry
/* 304 */           .getFieldType().equals(fieldType)) {
/* 305 */           index = i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 310 */     return index;
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
/*     */   public int getNameAndTypeEntry(String name, String type) {
/* 323 */     int index = -1;
/*     */     
/* 325 */     int size = this.entries.size();
/* 326 */     for (int i = 0; i < size && index == -1; i++) {
/* 327 */       Object element = this.entries.get(i);
/*     */       
/* 329 */       if (element instanceof NameAndTypeCPInfo) {
/* 330 */         NameAndTypeCPInfo nameAndTypeEntry = (NameAndTypeCPInfo)element;
/*     */ 
/*     */         
/* 333 */         if (nameAndTypeEntry.getName().equals(name) && nameAndTypeEntry
/* 334 */           .getType().equals(type)) {
/* 335 */           index = i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 340 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 350 */     return IntStream.range(0, this.entries.size())
/* 351 */       .<CharSequence>mapToObj(i -> String.format("[%d] = %s", new Object[] { Integer.valueOf(i), getEntry(i)
/* 352 */           })).collect(Collectors.joining("\n", "\n", "\n"));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/ConstantPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */