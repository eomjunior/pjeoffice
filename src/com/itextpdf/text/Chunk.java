/*      */ package com.itextpdf.text;
/*      */ 
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.pdf.HyphenationEvent;
/*      */ import com.itextpdf.text.pdf.PdfAction;
/*      */ import com.itextpdf.text.pdf.PdfAnnotation;
/*      */ import com.itextpdf.text.pdf.PdfName;
/*      */ import com.itextpdf.text.pdf.PdfObject;
/*      */ import com.itextpdf.text.pdf.PdfString;
/*      */ import com.itextpdf.text.pdf.draw.DrawInterface;
/*      */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Chunk
/*      */   implements Element, IAccessibleElement
/*      */ {
/*      */   public static final String OBJECT_REPLACEMENT_CHARACTER = "￼";
/*   86 */   public static final Chunk NEWLINE = new Chunk("\n");
/*      */   static {
/*   88 */     NEWLINE.setRole(PdfName.P);
/*      */   }
/*      */ 
/*      */   
/*   92 */   public static final Chunk NEXTPAGE = new Chunk("");
/*      */   static {
/*   94 */     NEXTPAGE.setNewPage();
/*      */   }
/*      */   
/*   97 */   public static final Chunk TABBING = new Chunk(Float.valueOf(Float.NaN), false);
/*      */   
/*   99 */   public static final Chunk SPACETABBING = new Chunk(Float.valueOf(Float.NaN), true);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  104 */   protected StringBuffer content = null;
/*      */ 
/*      */   
/*  107 */   protected Font font = null;
/*      */ 
/*      */   
/*  110 */   protected HashMap<String, Object> attributes = null;
/*      */   
/*  112 */   protected PdfName role = null;
/*  113 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*  114 */   private AccessibleElementId id = null;
/*      */ 
/*      */   
/*      */   public static final String SEPARATOR = "SEPARATOR";
/*      */   
/*      */   public static final String TAB = "TAB";
/*      */   
/*      */   public static final String TABSETTINGS = "TABSETTINGS";
/*      */   
/*      */   private String contentWithNoTabs;
/*      */   
/*      */   public static final String HSCALE = "HSCALE";
/*      */   
/*      */   public static final String UNDERLINE = "UNDERLINE";
/*      */   
/*      */   public static final String SUBSUPSCRIPT = "SUBSUPSCRIPT";
/*      */   
/*      */   public static final String SKEW = "SKEW";
/*      */   
/*      */   public static final String BACKGROUND = "BACKGROUND";
/*      */   
/*      */   public static final String TEXTRENDERMODE = "TEXTRENDERMODE";
/*      */   
/*      */   public static final String SPLITCHARACTER = "SPLITCHARACTER";
/*      */   
/*      */   public static final String HYPHENATION = "HYPHENATION";
/*      */   
/*      */   public static final String REMOTEGOTO = "REMOTEGOTO";
/*      */   
/*      */   public static final String LOCALGOTO = "LOCALGOTO";
/*      */   
/*      */   public static final String LOCALDESTINATION = "LOCALDESTINATION";
/*      */   
/*      */   public static final String GENERICTAG = "GENERICTAG";
/*      */   
/*      */   public static final String LINEHEIGHT = "LINEHEIGHT";
/*      */   
/*      */   public static final String IMAGE = "IMAGE";
/*      */   
/*      */   public static final String ACTION = "ACTION";
/*      */   
/*      */   public static final String NEWPAGE = "NEWPAGE";
/*      */   
/*      */   public static final String PDFANNOTATION = "PDFANNOTATION";
/*      */   
/*      */   public static final String COLOR = "COLOR";
/*      */   
/*      */   public static final String ENCODING = "ENCODING";
/*      */   
/*      */   public static final String CHAR_SPACING = "CHAR_SPACING";
/*      */   
/*      */   public static final String WORD_SPACING = "WORD_SPACING";
/*      */   
/*      */   public static final String WHITESPACE = "WHITESPACE";
/*      */ 
/*      */   
/*      */   public Chunk(String content) {
/*  171 */     this(content, new Font());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk(char c) {
/*  197 */     this(c, new Font());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk(Image image, float offsetX, float offsetY) {
/*  211 */     this("￼", new Font());
/*  212 */     Image copyImage = Image.getInstance(image);
/*  213 */     copyImage.setAbsolutePosition(Float.NaN, Float.NaN);
/*  214 */     setAttribute("IMAGE", new Object[] { copyImage, new Float(offsetX), new Float(offsetY), Boolean.FALSE });
/*      */     
/*  216 */     this.role = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk(DrawInterface separator) {
/*  232 */     this(separator, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk(DrawInterface separator, boolean vertical) {
/*  243 */     this("￼", new Font());
/*  244 */     setAttribute("SEPARATOR", new Object[] { separator, Boolean.valueOf(vertical) });
/*  245 */     this.role = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk() {
/*  258 */     this.contentWithNoTabs = null; this.content = new StringBuffer(); this.font = new Font(); this.role = PdfName.SPAN; } public Chunk(Chunk ck) { this.contentWithNoTabs = null; if (ck.content != null) this.content = new StringBuffer(ck.content.toString());  if (ck.font != null) this.font = new Font(ck.font);  if (ck.attributes != null) this.attributes = new HashMap<String, Object>(ck.attributes);  this.role = ck.role; if (ck.accessibleAttributes != null) this.accessibleAttributes = new HashMap<PdfName, PdfObject>(ck.accessibleAttributes);  this.id = ck.getId(); } public Chunk(String content, Font font) { this.contentWithNoTabs = null; this.content = new StringBuffer(content); this.font = font; this.role = PdfName.SPAN; } public Chunk(char c, Font font) { this.contentWithNoTabs = null;
/*      */     this.content = new StringBuffer();
/*      */     this.content.append(c);
/*      */     this.font = font;
/*      */     this.role = PdfName.SPAN; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Chunk(DrawInterface separator, float tabPosition) {
/*  269 */     this(separator, tabPosition, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Chunk(DrawInterface separator, float tabPosition, boolean newline) {
/*  282 */     this("￼", new Font());
/*  283 */     if (tabPosition < 0.0F) {
/*  284 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.tab.position.may.not.be.lower.than.0.yours.is.1", new Object[] { String.valueOf(tabPosition) }));
/*      */     }
/*  286 */     setAttribute("TAB", new Object[] { separator, new Float(tabPosition), Boolean.valueOf(newline), new Float(0.0F) });
/*  287 */     this.role = PdfName.ARTIFACT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Chunk(Float tabInterval, boolean isWhitespace) {
/*  298 */     this("￼", new Font());
/*  299 */     if (tabInterval.floatValue() < 0.0F) {
/*  300 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.tab.position.may.not.be.lower.than.0.yours.is.1", new Object[] { String.valueOf(tabInterval) }));
/*      */     }
/*  302 */     setAttribute("TAB", new Object[] { tabInterval, Boolean.valueOf(isWhitespace) });
/*  303 */     setAttribute("SPLITCHARACTER", TabSplitCharacter.TAB);
/*      */     
/*  305 */     setAttribute("TABSETTINGS", null);
/*  306 */     this.role = PdfName.ARTIFACT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk(Image image, float offsetX, float offsetY, boolean changeLeading) {
/*  323 */     this("￼", new Font());
/*  324 */     setAttribute("IMAGE", new Object[] { image, new Float(offsetX), new Float(offsetY), 
/*  325 */           Boolean.valueOf(changeLeading) });
/*  326 */     this.role = PdfName.ARTIFACT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean process(ElementListener listener) {
/*      */     try {
/*  341 */       return listener.add(this);
/*  342 */     } catch (DocumentException de) {
/*  343 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int type() {
/*  353 */     return 10;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Chunk> getChunks() {
/*  362 */     List<Chunk> tmp = new ArrayList<Chunk>();
/*  363 */     tmp.add(this);
/*  364 */     return tmp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer append(String string) {
/*  377 */     this.contentWithNoTabs = null;
/*  378 */     return this.content.append(string);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFont(Font font) {
/*  388 */     this.font = font;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Font getFont() {
/*  399 */     return this.font;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getContent() {
/*  408 */     if (this.contentWithNoTabs == null)
/*  409 */       this.contentWithNoTabs = this.content.toString().replaceAll("\t", ""); 
/*  410 */     return this.contentWithNoTabs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  420 */     return getContent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  430 */     return (this.content.toString().trim().length() == 0 && this.content
/*  431 */       .toString().indexOf("\n") == -1 && this.attributes == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWidthPoint() {
/*  441 */     if (getImage() != null) {
/*  442 */       return getImage().getScaledWidth();
/*      */     }
/*  444 */     return this.font.getCalculatedBaseFont(true).getWidthPoint(getContent(), this.font
/*  445 */         .getCalculatedSize()) * 
/*  446 */       getHorizontalScaling();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAttributes() {
/*  458 */     return (this.attributes != null && !this.attributes.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAccessibleAttributes() {
/*  467 */     return (this.accessibleAttributes != null && !this.accessibleAttributes.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HashMap<String, Object> getAttributes() {
/*  479 */     return this.attributes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAttributes(HashMap<String, Object> attributes) {
/*  487 */     this.attributes = attributes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Chunk setAttribute(String name, Object obj) {
/*  501 */     if (this.attributes == null)
/*  502 */       this.attributes = new HashMap<String, Object>(); 
/*  503 */     this.attributes.put(name, obj);
/*  504 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setHorizontalScaling(float scale) {
/*  521 */     return setAttribute("HSCALE", new Float(scale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getHorizontalScaling() {
/*  530 */     if (this.attributes == null)
/*  531 */       return 1.0F; 
/*  532 */     Float f = (Float)this.attributes.get("HSCALE");
/*  533 */     if (f == null)
/*  534 */       return 1.0F; 
/*  535 */     return f.floatValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setUnderline(float thickness, float yPosition) {
/*  554 */     return setUnderline(null, thickness, 0.0F, yPosition, 0.0F, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setUnderline(BaseColor color, float thickness, float thicknessMul, float yPosition, float yPositionMul, int cap) {
/*  583 */     if (this.attributes == null)
/*  584 */       this.attributes = new HashMap<String, Object>(); 
/*  585 */     Object[] obj = { color, { thickness, thicknessMul, yPosition, yPositionMul, cap } };
/*      */ 
/*      */     
/*  588 */     Object[][] unders = Utilities.addToArray((Object[][])this.attributes.get("UNDERLINE"), obj);
/*      */     
/*  590 */     return setAttribute("UNDERLINE", unders);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setTextRise(float rise) {
/*  608 */     return setAttribute("SUBSUPSCRIPT", new Float(rise));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getTextRise() {
/*  617 */     if (this.attributes != null && this.attributes.containsKey("SUBSUPSCRIPT")) {
/*  618 */       Float f = (Float)this.attributes.get("SUBSUPSCRIPT");
/*  619 */       return f.floatValue();
/*      */     } 
/*  621 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setSkew(float alpha, float beta) {
/*  638 */     alpha = (float)Math.tan(alpha * Math.PI / 180.0D);
/*  639 */     beta = (float)Math.tan(beta * Math.PI / 180.0D);
/*  640 */     return setAttribute("SKEW", new float[] { alpha, beta });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setBackground(BaseColor color) {
/*  654 */     return setBackground(color, 0.0F, 0.0F, 0.0F, 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setBackground(BaseColor color, float extraLeft, float extraBottom, float extraRight, float extraTop) {
/*  674 */     return setAttribute("BACKGROUND", new Object[] { color, { extraLeft, extraBottom, extraRight, extraTop } });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setTextRenderMode(int mode, float strokeWidth, BaseColor strokeColor) {
/*  702 */     return setAttribute("TEXTRENDERMODE", new Object[] { Integer.valueOf(mode), new Float(strokeWidth), strokeColor });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setSplitCharacter(SplitCharacter splitCharacter) {
/*  718 */     return setAttribute("SPLITCHARACTER", splitCharacter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setHyphenation(HyphenationEvent hyphenation) {
/*  732 */     return setAttribute("HYPHENATION", hyphenation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setRemoteGoto(String filename, String name) {
/*  749 */     return setAttribute("REMOTEGOTO", new Object[] { filename, name });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setRemoteGoto(String filename, int page) {
/*  763 */     return setAttribute("REMOTEGOTO", new Object[] { filename, 
/*  764 */           Integer.valueOf(page) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setLocalGoto(String name) {
/*  781 */     return setAttribute("LOCALGOTO", name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setLocalDestination(String name) {
/*  795 */     return setAttribute("LOCALDESTINATION", name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setGenericTag(String text) {
/*  812 */     return setAttribute("GENERICTAG", text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setLineHeight(float lineheight) {
/*  825 */     return setAttribute("LINEHEIGHT", Float.valueOf(lineheight));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Image getImage() {
/*  839 */     if (this.attributes == null)
/*  840 */       return null; 
/*  841 */     Object[] obj = (Object[])this.attributes.get("IMAGE");
/*  842 */     if (obj == null) {
/*  843 */       return null;
/*      */     }
/*  845 */     return (Image)obj[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setAction(PdfAction action) {
/*  861 */     setRole(PdfName.LINK);
/*  862 */     return setAttribute("ACTION", action);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setAnchor(URL url) {
/*  874 */     setRole(PdfName.LINK);
/*  875 */     String urlStr = url.toExternalForm();
/*  876 */     setAccessibleAttribute(PdfName.ALT, (PdfObject)new PdfString(urlStr));
/*  877 */     return setAttribute("ACTION", new PdfAction(urlStr));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setAnchor(String url) {
/*  889 */     setRole(PdfName.LINK);
/*  890 */     setAccessibleAttribute(PdfName.ALT, (PdfObject)new PdfString(url));
/*  891 */     return setAttribute("ACTION", new PdfAction(url));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setNewPage() {
/*  904 */     return setAttribute("NEWPAGE", null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setAnnotation(PdfAnnotation annotation) {
/*  918 */     return setAttribute("PDFANNOTATION", annotation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isContent() {
/*  926 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNestable() {
/*  934 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HyphenationEvent getHyphenation() {
/*  943 */     if (this.attributes == null) return null; 
/*  944 */     return (HyphenationEvent)this.attributes.get("HYPHENATION");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setCharacterSpacing(float charSpace) {
/*  967 */     return setAttribute("CHAR_SPACING", new Float(charSpace));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCharacterSpacing() {
/*  976 */     if (this.attributes != null && this.attributes.containsKey("CHAR_SPACING")) {
/*  977 */       Float f = (Float)this.attributes.get("CHAR_SPACING");
/*  978 */       return f.floatValue();
/*      */     } 
/*  980 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Chunk setWordSpacing(float wordSpace) {
/*  995 */     return setAttribute("WORD_SPACING", new Float(wordSpace));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWordSpacing() {
/* 1004 */     if (this.attributes != null && this.attributes.containsKey("WORD_SPACING")) {
/* 1005 */       Float f = (Float)this.attributes.get("WORD_SPACING");
/* 1006 */       return f.floatValue();
/*      */     } 
/* 1008 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Chunk createWhitespace(String content) {
/* 1014 */     return createWhitespace(content, false);
/*      */   }
/*      */   
/*      */   public static Chunk createWhitespace(String content, boolean preserve) {
/* 1018 */     Chunk whitespace = null;
/* 1019 */     if (!preserve) {
/* 1020 */       whitespace = new Chunk(' ');
/* 1021 */       whitespace.setAttribute("WHITESPACE", content);
/*      */     } else {
/* 1023 */       whitespace = new Chunk(content);
/*      */     } 
/*      */     
/* 1026 */     return whitespace;
/*      */   }
/*      */   
/*      */   public boolean isWhitespace() {
/* 1030 */     return (this.attributes != null && this.attributes.containsKey("WHITESPACE"));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static Chunk createTabspace() {
/* 1035 */     return createTabspace(60.0F);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static Chunk createTabspace(float spacing) {
/* 1040 */     Chunk tabspace = new Chunk(Float.valueOf(spacing), true);
/* 1041 */     return tabspace;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public boolean isTabspace() {
/* 1046 */     return (this.attributes != null && this.attributes.containsKey("TAB"));
/*      */   }
/*      */   
/*      */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 1050 */     if (getImage() != null)
/* 1051 */       return getImage().getAccessibleAttribute(key); 
/* 1052 */     if (this.accessibleAttributes != null) {
/* 1053 */       return this.accessibleAttributes.get(key);
/*      */     }
/* 1055 */     return null;
/*      */   }
/*      */   
/*      */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 1059 */     if (getImage() != null) {
/* 1060 */       getImage().setAccessibleAttribute(key, value);
/*      */     } else {
/* 1062 */       if (this.accessibleAttributes == null)
/* 1063 */         this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 1064 */       this.accessibleAttributes.put(key, value);
/*      */     } 
/*      */   }
/*      */   
/*      */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 1069 */     if (getImage() != null) {
/* 1070 */       return getImage().getAccessibleAttributes();
/*      */     }
/* 1072 */     return this.accessibleAttributes;
/*      */   }
/*      */   
/*      */   public PdfName getRole() {
/* 1076 */     if (getImage() != null) {
/* 1077 */       return getImage().getRole();
/*      */     }
/* 1079 */     return this.role;
/*      */   }
/*      */   
/*      */   public void setRole(PdfName role) {
/* 1083 */     if (getImage() != null) {
/* 1084 */       getImage().setRole(role);
/*      */     } else {
/* 1086 */       this.role = role;
/*      */     } 
/*      */   }
/*      */   public AccessibleElementId getId() {
/* 1090 */     if (this.id == null)
/* 1091 */       this.id = new AccessibleElementId(); 
/* 1092 */     return this.id;
/*      */   }
/*      */   
/*      */   public void setId(AccessibleElementId id) {
/* 1096 */     this.id = id;
/*      */   }
/*      */   
/*      */   public boolean isInline() {
/* 1100 */     return true;
/*      */   }
/*      */   
/*      */   public String getTextExpansion() {
/* 1104 */     PdfObject o = getAccessibleAttribute(PdfName.E);
/* 1105 */     if (o instanceof PdfString)
/* 1106 */       return ((PdfString)o).toUnicodeString(); 
/* 1107 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTextExpansion(String value) {
/* 1116 */     setAccessibleAttribute(PdfName.E, (PdfObject)new PdfString(value));
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Chunk.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */