/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ClassConstants
/*     */ {
/*     */   public static final int MAGIC = -889275714;
/*     */   public static final int MINOR_VERSION_MAX = 3;
/*     */   public static final int MAJOR_VERSION = 61;
/*     */   public static final int ACC_PUBLIC = 1;
/*     */   public static final int ACC_PRIVATE = 2;
/*     */   public static final int ACC_PROTECTED = 4;
/*     */   public static final int ACC_STATIC = 8;
/*     */   public static final int ACC_FINAL = 16;
/*     */   public static final int ACC_SUPER = 32;
/*     */   public static final int ACC_SYNCHRONIZED = 32;
/*     */   public static final int ACC_VOLATILE = 64;
/*     */   public static final int ACC_TRANSIENT = 128;
/*     */   public static final int ACC_NATIVE = 256;
/*     */   public static final int ACC_INTERFACE = 512;
/*     */   public static final int ACC_ABSTRACT = 1024;
/*     */   public static final int ACC_SYNTHETIC = 4096;
/*     */   public static final int ACC_ANNOTATION = 8192;
/*     */   public static final int ACC_ENUM = 16384;
/*     */   public static final int ACC_BRIDGE = 64;
/*     */   public static final int ACC_VARARGS = 128;
/*     */   public static final int CONSTANT_Utf8 = 1;
/*     */   public static final int CONSTANT_Integer = 3;
/*     */   public static final int CONSTANT_Float = 4;
/*     */   public static final int CONSTANT_Long = 5;
/*     */   public static final int CONSTANT_Double = 6;
/*     */   public static final int CONSTANT_Class = 7;
/*     */   public static final int CONSTANT_String = 8;
/*     */   public static final int CONSTANT_Fieldref = 9;
/*     */   public static final int CONSTANT_Methodref = 10;
/*     */   public static final int CONSTANT_InterfaceMethodref = 11;
/*     */   public static final int CONSTANT_NameAndType = 12;
/*     */   public static final int CONSTANT_MethodHandle = 15;
/*     */   public static final int CONSTANT_MethodType = 16;
/*     */   public static final int CONSTANT_Dynamic = 17;
/*     */   public static final int CONSTANT_InvokeDynamic = 18;
/*     */   public static final int CONSTANT_Module = 19;
/*     */   public static final int CONSTANT_Package = 20;
/*     */   public static final String ATTR_Unknown = "Unknown";
/*     */   public static final String ATTR_Code = "Code";
/*     */   public static final String ATTR_ConstantValue = "ConstantValue";
/*     */   public static final String ATTR_Exceptions = "Exceptions";
/*     */   public static final String ATTR_StackMapTable = "StackMapTable";
/*     */   public static final String ATTR_LineNumberTable = "LineNumberTable";
/*     */   public static final String ATTR_SourceFile = "SourceFile";
/*     */   public static final String ATTR_SourceDebug = "SourceDebug";
/*     */   public static final String ATTR_LocalVariableTable = "LocalVariableTable";
/*     */   public static final String ATTR_InnerClasses = "InnerClasses";
/*     */   public static final String ATTR_Synthetic = "Synthetic";
/*     */   public static final String ATTR_Deprecated = "Deprecated";
/*     */   public static final String ATTR_LocalVariableTypeTable = "LocalVariableTypeTable";
/*     */   public static final String ATTR_Signature = "Signature";
/*     */   public static final String ATTR_EnclosingMethod = "EnclosingMethod";
/*     */   public static final String ATTR_RuntimeVisibleAnnotations = "RuntimeVisibleAnnotations";
/*     */   public static final String ATTR_RuntimeInvisibleAnnotations = "RuntimeInvisibleAnnotations";
/*     */   public static final String ATTR_RuntimeVisibleParameterAnnotations = "RuntimeVisibleParameterAnnotations";
/*     */   public static final String ATTR_RuntimeInvisibleParameterAnnotations = "RuntimeInvisibleParameterAnnotations";
/*     */   public static final String ATTR_AnnotationDefault = "AnnotationDefault";
/*     */   public static final String ATTR_Bridge = "Bridge";
/*     */   public static final String ATTR_Enum = "Enum";
/*     */   public static final String ATTR_Varargs = "Varargs";
/*     */   public static final String ATTR_BootstrapMethods = "BootstrapMethods";
/*     */   public static final String ATTR_RuntimeVisibleTypeAnnotations = "RuntimeVisibleTypeAnnotations";
/*     */   public static final String ATTR_RuntimeInvisibleTypeAnnotations = "RuntimeInvisibleTypeAnnotations";
/*     */   public static final String ATTR_MethodParameters = "MethodParameters";
/*     */   public static final String ATTR_Module = "Module";
/*     */   public static final String ATTR_ModulePackages = "ModulePackages";
/*     */   public static final String ATTR_ModuleMainClass = "ModuleMainClass";
/*     */   public static final String ATTR_NestHost = "NestHost";
/*     */   public static final String ATTR_NestMembers = "NestMembers";
/*     */   public static final String ATTR_SourceDebugExtension = "SourceDebugExtension";
/*     */   public static final String ATTR_Record = "Record";
/*     */   public static final String ATTR_PermittedSubclasses = "PermittedSubclasses";
/*     */   public static final int REF_getField = 1;
/*     */   public static final int REF_getStatic = 2;
/*     */   public static final int REF_putField = 3;
/*     */   public static final int REF_putStatic = 4;
/*     */   public static final int REF_invokeVirtual = 5;
/*     */   public static final int REF_invokeStatic = 6;
/*     */   public static final int REF_invokeSpecial = 7;
/*     */   public static final int REF_newInvokeSpecial = 8;
/*     */   public static final int REF_invokeInterface = 9;
/* 370 */   public static final String[] KNOWN_ATTRS = new String[] { "Code", "ConstantValue", "Exceptions", "LineNumberTable", "SourceFile", "LocalVariableTable", "InnerClasses", "Synthetic", "Deprecated", "Signature", "LocalVariableTypeTable", "EnclosingMethod", "AnnotationDefault", "RuntimeVisibleAnnotations", "RuntimeInvisibleAnnotations", "RuntimeVisibleParameterAnnotations", "RuntimeInvisibleParameterAnnotations", "BootstrapMethods", "Bridge", "Enum", "StackMapTable", "Varargs", "MethodParameters", "Module", "ModulePackages", "ModuleMainClass", "NestHost", "NestMembers" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 404 */   public static final String[] REQUIRED_ATTRS = new String[] { "Code", "ConstantValue", "Exceptions", "InnerClasses", "Synthetic", "Signature", "EnclosingMethod", "AnnotationDefault", "RuntimeInvisibleAnnotations", "RuntimeInvisibleParameterAnnotations", "RuntimeInvisibleTypeAnnotations", "RuntimeVisibleAnnotations", "RuntimeVisibleParameterAnnotations", "RuntimeVisibleTypeAnnotations", "StackMapTable", "BootstrapMethods", "Module", "ModulePackages", "ModuleMainClass", "NestHost", "NestMembers", "PermittedSubclasses", "Record" };
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ClassConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */