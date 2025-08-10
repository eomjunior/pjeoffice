/*     */ package org.apache.tools.ant.input;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.KeepAliveInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultInputHandler
/*     */   implements InputHandler
/*     */ {
/*     */   public void handleInput(InputRequest request) throws BuildException {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual getPrompt : (Lorg/apache/tools/ant/input/InputRequest;)Ljava/lang/String;
/*     */     //   5: astore_2
/*     */     //   6: aconst_null
/*     */     //   7: astore_3
/*     */     //   8: iconst_0
/*     */     //   9: istore #4
/*     */     //   11: new java/io/BufferedReader
/*     */     //   14: dup
/*     */     //   15: new java/io/InputStreamReader
/*     */     //   18: dup
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual getInputStream : ()Ljava/io/InputStream;
/*     */     //   23: invokespecial <init> : (Ljava/io/InputStream;)V
/*     */     //   26: invokespecial <init> : (Ljava/io/Reader;)V
/*     */     //   29: astore_3
/*     */     //   30: getstatic java/lang/System.err : Ljava/io/PrintStream;
/*     */     //   33: aload_2
/*     */     //   34: invokevirtual println : (Ljava/lang/String;)V
/*     */     //   37: getstatic java/lang/System.err : Ljava/io/PrintStream;
/*     */     //   40: invokevirtual flush : ()V
/*     */     //   43: aload_3
/*     */     //   44: invokevirtual readLine : ()Ljava/lang/String;
/*     */     //   47: astore #5
/*     */     //   49: aload #5
/*     */     //   51: ifnonnull -> 64
/*     */     //   54: new org/apache/tools/ant/BuildException
/*     */     //   57: dup
/*     */     //   58: ldc 'unexpected end of stream while reading input'
/*     */     //   60: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   63: athrow
/*     */     //   64: aload_1
/*     */     //   65: aload #5
/*     */     //   67: invokevirtual setInput : (Ljava/lang/String;)V
/*     */     //   70: goto -> 87
/*     */     //   73: astore #5
/*     */     //   75: new org/apache/tools/ant/BuildException
/*     */     //   78: dup
/*     */     //   79: ldc 'Failed to read input from Console.'
/*     */     //   81: aload #5
/*     */     //   83: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   86: athrow
/*     */     //   87: aload_1
/*     */     //   88: invokevirtual isInputValid : ()Z
/*     */     //   91: ifeq -> 30
/*     */     //   94: iconst_1
/*     */     //   95: istore #4
/*     */     //   97: aload_3
/*     */     //   98: ifnull -> 165
/*     */     //   101: aload_3
/*     */     //   102: invokevirtual close : ()V
/*     */     //   105: goto -> 165
/*     */     //   108: astore #5
/*     */     //   110: iload #4
/*     */     //   112: ifeq -> 127
/*     */     //   115: new org/apache/tools/ant/BuildException
/*     */     //   118: dup
/*     */     //   119: ldc 'Failed to close input.'
/*     */     //   121: aload #5
/*     */     //   123: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   126: athrow
/*     */     //   127: goto -> 165
/*     */     //   130: astore #6
/*     */     //   132: aload_3
/*     */     //   133: ifnull -> 162
/*     */     //   136: aload_3
/*     */     //   137: invokevirtual close : ()V
/*     */     //   140: goto -> 162
/*     */     //   143: astore #7
/*     */     //   145: iload #4
/*     */     //   147: ifeq -> 162
/*     */     //   150: new org/apache/tools/ant/BuildException
/*     */     //   153: dup
/*     */     //   154: ldc 'Failed to close input.'
/*     */     //   156: aload #7
/*     */     //   158: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   161: athrow
/*     */     //   162: aload #6
/*     */     //   164: athrow
/*     */     //   165: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #49	-> 0
/*     */     //   #50	-> 6
/*     */     //   #51	-> 8
/*     */     //   #53	-> 11
/*     */     //   #55	-> 30
/*     */     //   #56	-> 37
/*     */     //   #58	-> 43
/*     */     //   #59	-> 49
/*     */     //   #60	-> 54
/*     */     //   #62	-> 64
/*     */     //   #66	-> 70
/*     */     //   #63	-> 73
/*     */     //   #64	-> 75
/*     */     //   #67	-> 87
/*     */     //   #68	-> 94
/*     */     //   #70	-> 97
/*     */     //   #72	-> 101
/*     */     //   #77	-> 105
/*     */     //   #73	-> 108
/*     */     //   #74	-> 110
/*     */     //   #75	-> 115
/*     */     //   #77	-> 127
/*     */     //   #70	-> 130
/*     */     //   #72	-> 136
/*     */     //   #77	-> 140
/*     */     //   #73	-> 143
/*     */     //   #74	-> 145
/*     */     //   #75	-> 150
/*     */     //   #79	-> 162
/*     */     //   #80	-> 165
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   49	21	5	input	Ljava/lang/String;
/*     */     //   75	12	5	e	Ljava/io/IOException;
/*     */     //   110	17	5	e	Ljava/io/IOException;
/*     */     //   145	17	7	e	Ljava/io/IOException;
/*     */     //   0	166	0	this	Lorg/apache/tools/ant/input/DefaultInputHandler;
/*     */     //   0	166	1	request	Lorg/apache/tools/ant/input/InputRequest;
/*     */     //   6	160	2	prompt	Ljava/lang/String;
/*     */     //   8	158	3	r	Ljava/io/BufferedReader;
/*     */     //   11	155	4	success	Z
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   11	97	130	finally
/*     */     //   43	70	73	java/io/IOException
/*     */     //   101	105	108	java/io/IOException
/*     */     //   130	132	130	finally
/*     */     //   136	140	143	java/io/IOException
/*     */   }
/*     */   
/*     */   protected String getPrompt(InputRequest request) {
/*  93 */     String prompt = request.getPrompt();
/*  94 */     String def = request.getDefaultValue();
/*  95 */     if (request instanceof MultipleChoiceInputRequest) {
/*  96 */       StringBuilder sb = (new StringBuilder(prompt)).append(" (");
/*  97 */       boolean first = true;
/*  98 */       for (String next : ((MultipleChoiceInputRequest)request).getChoices()) {
/*  99 */         if (!first) {
/* 100 */           sb.append(", ");
/*     */         }
/* 102 */         if (next.equals(def)) {
/* 103 */           sb.append('[');
/*     */         }
/* 105 */         sb.append(next);
/* 106 */         if (next.equals(def)) {
/* 107 */           sb.append(']');
/*     */         }
/* 109 */         first = false;
/*     */       } 
/* 111 */       sb.append(")");
/* 112 */       return sb.toString();
/* 113 */     }  if (def != null) {
/* 114 */       return prompt + " [" + def + "]";
/*     */     }
/* 116 */     return prompt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream getInputStream() {
/* 125 */     return KeepAliveInputStream.wrapSystemIn();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/input/DefaultInputHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */