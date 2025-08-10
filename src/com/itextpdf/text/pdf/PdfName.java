/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import java.lang.reflect.Field;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
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
/*      */ 
/*      */ 
/*      */ public class PdfName
/*      */   extends PdfObject
/*      */   implements Comparable<PdfName>
/*      */ {
/*   79 */   public static final PdfName _3D = new PdfName("3D");
/*      */   
/*   81 */   public static final PdfName A = new PdfName("A");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   public static final PdfName A85 = new PdfName("A85");
/*      */   
/*   88 */   public static final PdfName AA = new PdfName("AA");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   93 */   public static final PdfName ABSOLUTECOLORIMETRIC = new PdfName("AbsoluteColorimetric");
/*      */   
/*   95 */   public static final PdfName AC = new PdfName("AC");
/*      */   
/*   97 */   public static final PdfName ACROFORM = new PdfName("AcroForm");
/*      */   
/*   99 */   public static final PdfName ACTION = new PdfName("Action");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  104 */   public static final PdfName ACTIVATION = new PdfName("Activation");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  109 */   public static final PdfName ADBE = new PdfName("ADBE");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  114 */   public static final PdfName ACTUALTEXT = new PdfName("ActualText");
/*      */   
/*  116 */   public static final PdfName ADBE_PKCS7_DETACHED = new PdfName("adbe.pkcs7.detached");
/*      */   
/*  118 */   public static final PdfName ADBE_PKCS7_S4 = new PdfName("adbe.pkcs7.s4");
/*      */   
/*  120 */   public static final PdfName ADBE_PKCS7_S5 = new PdfName("adbe.pkcs7.s5");
/*      */   
/*  122 */   public static final PdfName ADBE_PKCS7_SHA1 = new PdfName("adbe.pkcs7.sha1");
/*      */   
/*  124 */   public static final PdfName ADBE_X509_RSA_SHA1 = new PdfName("adbe.x509.rsa_sha1");
/*      */   
/*  126 */   public static final PdfName ADOBE_PPKLITE = new PdfName("Adobe.PPKLite");
/*      */   
/*  128 */   public static final PdfName ADOBE_PPKMS = new PdfName("Adobe.PPKMS");
/*      */   
/*  130 */   public static final PdfName AESV2 = new PdfName("AESV2");
/*      */   
/*  132 */   public static final PdfName AESV3 = new PdfName("AESV3");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  137 */   public static final PdfName AF = new PdfName("AF");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  142 */   public static final PdfName AFRELATIONSHIP = new PdfName("AFRelationship");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  147 */   public static final PdfName AHX = new PdfName("AHx");
/*      */   
/*  149 */   public static final PdfName AIS = new PdfName("AIS");
/*      */   
/*  151 */   public static final PdfName ALL = new PdfName("All");
/*      */   
/*  153 */   public static final PdfName ALLPAGES = new PdfName("AllPages");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  158 */   public static final PdfName ALT = new PdfName("Alt");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  164 */   public static final PdfName ALTERNATE = new PdfName("Alternate");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  169 */   public static final PdfName ALTERNATEPRESENTATION = new PdfName("AlternatePresentations");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  174 */   public static final PdfName ALTERNATES = new PdfName("Alternates");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  179 */   public static final PdfName AND = new PdfName("And");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  184 */   public static final PdfName ANIMATION = new PdfName("Animation");
/*      */   
/*  186 */   public static final PdfName ANNOT = new PdfName("Annot");
/*      */   
/*  188 */   public static final PdfName ANNOTS = new PdfName("Annots");
/*      */   
/*  190 */   public static final PdfName ANTIALIAS = new PdfName("AntiAlias");
/*      */   
/*  192 */   public static final PdfName AP = new PdfName("AP");
/*      */   
/*  194 */   public static final PdfName APP = new PdfName("App");
/*      */   
/*  196 */   public static final PdfName APPDEFAULT = new PdfName("AppDefault");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  201 */   public static final PdfName ART = new PdfName("Art");
/*      */   
/*  203 */   public static final PdfName ARTBOX = new PdfName("ArtBox");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  208 */   public static final PdfName ARTIFACT = new PdfName("Artifact");
/*      */   
/*  210 */   public static final PdfName ASCENT = new PdfName("Ascent");
/*      */   
/*  212 */   public static final PdfName AS = new PdfName("AS");
/*      */   
/*  214 */   public static final PdfName ASCII85DECODE = new PdfName("ASCII85Decode");
/*      */   
/*  216 */   public static final PdfName ASCIIHEXDECODE = new PdfName("ASCIIHexDecode");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  221 */   public static final PdfName ASSET = new PdfName("Asset");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  226 */   public static final PdfName ASSETS = new PdfName("Assets");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  231 */   public static final PdfName ATTACHED = new PdfName("Attached");
/*      */   
/*  233 */   public static final PdfName AUTHEVENT = new PdfName("AuthEvent");
/*      */   
/*  235 */   public static final PdfName AUTHOR = new PdfName("Author");
/*      */   
/*  237 */   public static final PdfName B = new PdfName("B");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  242 */   public static final PdfName BACKGROUND = new PdfName("Background");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  247 */   public static final PdfName BACKGROUNDCOLOR = new PdfName("BackgroundColor");
/*      */   
/*  249 */   public static final PdfName BASEENCODING = new PdfName("BaseEncoding");
/*      */   
/*  251 */   public static final PdfName BASEFONT = new PdfName("BaseFont");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  256 */   public static final PdfName BASEVERSION = new PdfName("BaseVersion");
/*      */   
/*  258 */   public static final PdfName BBOX = new PdfName("BBox");
/*      */   
/*  260 */   public static final PdfName BC = new PdfName("BC");
/*      */   
/*  262 */   public static final PdfName BG = new PdfName("BG");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  267 */   public static final PdfName BIBENTRY = new PdfName("BibEntry");
/*      */   
/*  269 */   public static final PdfName BIGFIVE = new PdfName("BigFive");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  274 */   public static final PdfName BINDING = new PdfName("Binding");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  279 */   public static final PdfName BINDINGMATERIALNAME = new PdfName("BindingMaterialName");
/*      */   
/*  281 */   public static final PdfName BITSPERCOMPONENT = new PdfName("BitsPerComponent");
/*      */   
/*  283 */   public static final PdfName BITSPERSAMPLE = new PdfName("BitsPerSample");
/*      */   
/*  285 */   public static final PdfName BL = new PdfName("Bl");
/*      */   
/*  287 */   public static final PdfName BLACKIS1 = new PdfName("BlackIs1");
/*      */   
/*  289 */   public static final PdfName BLACKPOINT = new PdfName("BlackPoint");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  294 */   public static final PdfName BLOCKQUOTE = new PdfName("BlockQuote");
/*      */   
/*  296 */   public static final PdfName BLEEDBOX = new PdfName("BleedBox");
/*      */   
/*  298 */   public static final PdfName BLINDS = new PdfName("Blinds");
/*      */   
/*  300 */   public static final PdfName BM = new PdfName("BM");
/*      */   
/*  302 */   public static final PdfName BORDER = new PdfName("Border");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  307 */   public static final PdfName BOTH = new PdfName("Both");
/*      */   
/*  309 */   public static final PdfName BOUNDS = new PdfName("Bounds");
/*      */   
/*  311 */   public static final PdfName BOX = new PdfName("Box");
/*      */   
/*  313 */   public static final PdfName BS = new PdfName("BS");
/*      */   
/*  315 */   public static final PdfName BTN = new PdfName("Btn");
/*      */   
/*  317 */   public static final PdfName BYTERANGE = new PdfName("ByteRange");
/*      */   
/*  319 */   public static final PdfName C = new PdfName("C");
/*      */   
/*  321 */   public static final PdfName C0 = new PdfName("C0");
/*      */   
/*  323 */   public static final PdfName C1 = new PdfName("C1");
/*      */   
/*  325 */   public static final PdfName CA = new PdfName("CA");
/*      */   
/*  327 */   public static final PdfName ca = new PdfName("ca");
/*      */   
/*  329 */   public static final PdfName CALGRAY = new PdfName("CalGray");
/*      */   
/*  331 */   public static final PdfName CALRGB = new PdfName("CalRGB");
/*      */   
/*  333 */   public static final PdfName CAPHEIGHT = new PdfName("CapHeight");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  338 */   public static final PdfName CARET = new PdfName("Caret");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  343 */   public static final PdfName CAPTION = new PdfName("Caption");
/*      */   
/*  345 */   public static final PdfName CATALOG = new PdfName("Catalog");
/*      */   
/*  347 */   public static final PdfName CATEGORY = new PdfName("Category");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  352 */   public static final PdfName CB = new PdfName("cb");
/*      */   
/*  354 */   public static final PdfName CCITTFAXDECODE = new PdfName("CCITTFaxDecode");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  359 */   public static final PdfName CENTER = new PdfName("Center");
/*      */   
/*  361 */   public static final PdfName CENTERWINDOW = new PdfName("CenterWindow");
/*      */   
/*  363 */   public static final PdfName CERT = new PdfName("Cert");
/*      */ 
/*      */ 
/*      */   
/*  367 */   public static final PdfName CERTS = new PdfName("Certs");
/*      */   
/*  369 */   public static final PdfName CF = new PdfName("CF");
/*      */   
/*  371 */   public static final PdfName CFM = new PdfName("CFM");
/*      */   
/*  373 */   public static final PdfName CH = new PdfName("Ch");
/*      */   
/*  375 */   public static final PdfName CHARPROCS = new PdfName("CharProcs");
/*      */   
/*  377 */   public static final PdfName CHECKSUM = new PdfName("CheckSum");
/*      */   
/*  379 */   public static final PdfName CI = new PdfName("CI");
/*      */   
/*  381 */   public static final PdfName CIDFONTTYPE0 = new PdfName("CIDFontType0");
/*      */   
/*  383 */   public static final PdfName CIDFONTTYPE2 = new PdfName("CIDFontType2");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  388 */   public static final PdfName CIDSET = new PdfName("CIDSet");
/*      */   
/*  390 */   public static final PdfName CIDSYSTEMINFO = new PdfName("CIDSystemInfo");
/*      */   
/*  392 */   public static final PdfName CIDTOGIDMAP = new PdfName("CIDToGIDMap");
/*      */   
/*  394 */   public static final PdfName CIRCLE = new PdfName("Circle");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  399 */   public static final PdfName CLASSMAP = new PdfName("ClassMap");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  404 */   public static final PdfName CLOUD = new PdfName("Cloud");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  409 */   public static final PdfName CMD = new PdfName("CMD");
/*      */   
/*  411 */   public static final PdfName CO = new PdfName("CO");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  416 */   public static final PdfName CODE = new PdfName("Code");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  421 */   public static final PdfName COLOR = new PdfName("Color");
/*  422 */   public static final PdfName COLORANTS = new PdfName("Colorants");
/*      */   
/*  424 */   public static final PdfName COLORS = new PdfName("Colors");
/*      */   
/*  426 */   public static final PdfName COLORSPACE = new PdfName("ColorSpace");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  431 */   public static final PdfName COLORTRANSFORM = new PdfName("ColorTransform");
/*      */   
/*  433 */   public static final PdfName COLLECTION = new PdfName("Collection");
/*      */   
/*  435 */   public static final PdfName COLLECTIONFIELD = new PdfName("CollectionField");
/*      */   
/*  437 */   public static final PdfName COLLECTIONITEM = new PdfName("CollectionItem");
/*      */   
/*  439 */   public static final PdfName COLLECTIONSCHEMA = new PdfName("CollectionSchema");
/*      */   
/*  441 */   public static final PdfName COLLECTIONSORT = new PdfName("CollectionSort");
/*      */   
/*  443 */   public static final PdfName COLLECTIONSUBITEM = new PdfName("CollectionSubitem");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  448 */   public static final PdfName COLSPAN = new PdfName("ColSpan");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  453 */   public static final PdfName COLUMN = new PdfName("Column");
/*      */   
/*  455 */   public static final PdfName COLUMNS = new PdfName("Columns");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  460 */   public static final PdfName CONDITION = new PdfName("Condition");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  465 */   public static final PdfName CONFIGS = new PdfName("Configs");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  470 */   public static final PdfName CONFIGURATION = new PdfName("Configuration");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  475 */   public static final PdfName CONFIGURATIONS = new PdfName("Configurations");
/*      */   
/*  477 */   public static final PdfName CONTACTINFO = new PdfName("ContactInfo");
/*      */   
/*  479 */   public static final PdfName CONTENT = new PdfName("Content");
/*      */   
/*  481 */   public static final PdfName CONTENTS = new PdfName("Contents");
/*      */   
/*  483 */   public static final PdfName COORDS = new PdfName("Coords");
/*      */   
/*  485 */   public static final PdfName COUNT = new PdfName("Count");
/*      */   
/*  487 */   public static final PdfName COURIER = new PdfName("Courier");
/*      */   
/*  489 */   public static final PdfName COURIER_BOLD = new PdfName("Courier-Bold");
/*      */   
/*  491 */   public static final PdfName COURIER_OBLIQUE = new PdfName("Courier-Oblique");
/*      */   
/*  493 */   public static final PdfName COURIER_BOLDOBLIQUE = new PdfName("Courier-BoldOblique");
/*      */   
/*  495 */   public static final PdfName CREATIONDATE = new PdfName("CreationDate");
/*      */   
/*  497 */   public static final PdfName CREATOR = new PdfName("Creator");
/*      */   
/*  499 */   public static final PdfName CREATORINFO = new PdfName("CreatorInfo");
/*      */ 
/*      */ 
/*      */   
/*  503 */   public static final PdfName CRL = new PdfName("CRL");
/*      */ 
/*      */ 
/*      */   
/*  507 */   public static final PdfName CRLS = new PdfName("CRLs");
/*      */   
/*  509 */   public static final PdfName CROPBOX = new PdfName("CropBox");
/*      */   
/*  511 */   public static final PdfName CRYPT = new PdfName("Crypt");
/*      */   
/*  513 */   public static final PdfName CS = new PdfName("CS");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  518 */   public static final PdfName CUEPOINT = new PdfName("CuePoint");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  523 */   public static final PdfName CUEPOINTS = new PdfName("CuePoints");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  528 */   public static final PdfName CYX = new PdfName("CYX");
/*      */   
/*  530 */   public static final PdfName D = new PdfName("D");
/*      */   
/*  532 */   public static final PdfName DA = new PdfName("DA");
/*      */   
/*  534 */   public static final PdfName DATA = new PdfName("Data");
/*      */   
/*  536 */   public static final PdfName DC = new PdfName("DC");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  541 */   public static final PdfName DCS = new PdfName("DCS");
/*      */   
/*  543 */   public static final PdfName DCTDECODE = new PdfName("DCTDecode");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  548 */   public static final PdfName DECIMAL = new PdfName("Decimal");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  553 */   public static final PdfName DEACTIVATION = new PdfName("Deactivation");
/*      */   
/*  555 */   public static final PdfName DECODE = new PdfName("Decode");
/*      */   
/*  557 */   public static final PdfName DECODEPARMS = new PdfName("DecodeParms");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  562 */   public static final PdfName DEFAULT = new PdfName("Default");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  567 */   public static final PdfName DEFAULTCRYPTFILTER = new PdfName("DefaultCryptFilter");
/*      */   
/*  569 */   public static final PdfName DEFAULTCMYK = new PdfName("DefaultCMYK");
/*      */   
/*  571 */   public static final PdfName DEFAULTGRAY = new PdfName("DefaultGray");
/*      */   
/*  573 */   public static final PdfName DEFAULTRGB = new PdfName("DefaultRGB");
/*      */   
/*  575 */   public static final PdfName DESC = new PdfName("Desc");
/*      */   
/*  577 */   public static final PdfName DESCENDANTFONTS = new PdfName("DescendantFonts");
/*      */   
/*  579 */   public static final PdfName DESCENT = new PdfName("Descent");
/*      */   
/*  581 */   public static final PdfName DEST = new PdfName("Dest");
/*      */   
/*  583 */   public static final PdfName DESTOUTPUTPROFILE = new PdfName("DestOutputProfile");
/*      */   
/*  585 */   public static final PdfName DESTS = new PdfName("Dests");
/*      */   
/*  587 */   public static final PdfName DEVICEGRAY = new PdfName("DeviceGray");
/*      */   
/*  589 */   public static final PdfName DEVICERGB = new PdfName("DeviceRGB");
/*      */   
/*  591 */   public static final PdfName DEVICECMYK = new PdfName("DeviceCMYK");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  596 */   public static final PdfName DEVICEN = new PdfName("DeviceN");
/*      */   
/*  598 */   public static final PdfName DI = new PdfName("Di");
/*      */   
/*  600 */   public static final PdfName DIFFERENCES = new PdfName("Differences");
/*      */   
/*  602 */   public static final PdfName DISSOLVE = new PdfName("Dissolve");
/*      */   
/*  604 */   public static final PdfName DIRECTION = new PdfName("Direction");
/*      */   
/*  606 */   public static final PdfName DISPLAYDOCTITLE = new PdfName("DisplayDocTitle");
/*      */   
/*  608 */   public static final PdfName DIV = new PdfName("Div");
/*      */   
/*  610 */   public static final PdfName DL = new PdfName("DL");
/*      */   
/*  612 */   public static final PdfName DM = new PdfName("Dm");
/*      */   
/*  614 */   public static final PdfName DOCMDP = new PdfName("DocMDP");
/*      */   
/*  616 */   public static final PdfName DOCOPEN = new PdfName("DocOpen");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  621 */   public static final PdfName DOCTIMESTAMP = new PdfName("DocTimeStamp");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  626 */   public static final PdfName DOCUMENT = new PdfName("Document");
/*      */   
/*  628 */   public static final PdfName DOMAIN = new PdfName("Domain");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  633 */   public static final PdfName DOS = new PdfName("DOS");
/*      */   
/*  635 */   public static final PdfName DP = new PdfName("DP");
/*      */   
/*  637 */   public static final PdfName DR = new PdfName("DR");
/*      */   
/*  639 */   public static final PdfName DS = new PdfName("DS");
/*      */ 
/*      */ 
/*      */   
/*  643 */   public static final PdfName DSS = new PdfName("DSS");
/*      */   
/*  645 */   public static final PdfName DUR = new PdfName("Dur");
/*      */   
/*  647 */   public static final PdfName DUPLEX = new PdfName("Duplex");
/*      */   
/*  649 */   public static final PdfName DUPLEXFLIPSHORTEDGE = new PdfName("DuplexFlipShortEdge");
/*      */   
/*  651 */   public static final PdfName DUPLEXFLIPLONGEDGE = new PdfName("DuplexFlipLongEdge");
/*      */   
/*  653 */   public static final PdfName DV = new PdfName("DV");
/*      */   
/*  655 */   public static final PdfName DW = new PdfName("DW");
/*      */   
/*  657 */   public static final PdfName E = new PdfName("E");
/*      */   
/*  659 */   public static final PdfName EARLYCHANGE = new PdfName("EarlyChange");
/*      */   
/*  661 */   public static final PdfName EF = new PdfName("EF");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  666 */   public static final PdfName EFF = new PdfName("EFF");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  671 */   public static final PdfName EFOPEN = new PdfName("EFOpen");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  676 */   public static final PdfName EMBEDDED = new PdfName("Embedded");
/*      */   
/*  678 */   public static final PdfName EMBEDDEDFILE = new PdfName("EmbeddedFile");
/*      */   
/*  680 */   public static final PdfName EMBEDDEDFILES = new PdfName("EmbeddedFiles");
/*      */   
/*  682 */   public static final PdfName ENCODE = new PdfName("Encode");
/*      */   
/*  684 */   public static final PdfName ENCODEDBYTEALIGN = new PdfName("EncodedByteAlign");
/*      */   
/*  686 */   public static final PdfName ENCODING = new PdfName("Encoding");
/*      */   
/*  688 */   public static final PdfName ENCRYPT = new PdfName("Encrypt");
/*      */   
/*  690 */   public static final PdfName ENCRYPTMETADATA = new PdfName("EncryptMetadata");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  695 */   public static final PdfName END = new PdfName("End");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  700 */   public static final PdfName ENDINDENT = new PdfName("EndIndent");
/*      */   
/*  702 */   public static final PdfName ENDOFBLOCK = new PdfName("EndOfBlock");
/*      */   
/*  704 */   public static final PdfName ENDOFLINE = new PdfName("EndOfLine");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  709 */   public static final PdfName EPSG = new PdfName("EPSG");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  714 */   public static final PdfName ESIC = new PdfName("ESIC");
/*      */ 
/*      */ 
/*      */   
/*  718 */   public static final PdfName ETSI_CADES_DETACHED = new PdfName("ETSI.CAdES.detached");
/*      */   
/*  720 */   public static final PdfName ETSI_RFC3161 = new PdfName("ETSI.RFC3161");
/*      */   
/*  722 */   public static final PdfName EXCLUDE = new PdfName("Exclude");
/*      */   
/*  724 */   public static final PdfName EXTEND = new PdfName("Extend");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  729 */   public static final PdfName EXTENSIONS = new PdfName("Extensions");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  734 */   public static final PdfName EXTENSIONLEVEL = new PdfName("ExtensionLevel");
/*      */   
/*  736 */   public static final PdfName EXTGSTATE = new PdfName("ExtGState");
/*      */   
/*  738 */   public static final PdfName EXPORT = new PdfName("Export");
/*      */   
/*  740 */   public static final PdfName EXPORTSTATE = new PdfName("ExportState");
/*      */   
/*  742 */   public static final PdfName EVENT = new PdfName("Event");
/*      */   
/*  744 */   public static final PdfName F = new PdfName("F");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  749 */   public static final PdfName FAR = new PdfName("Far");
/*      */   
/*  751 */   public static final PdfName FB = new PdfName("FB");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  756 */   public static final PdfName FD = new PdfName("FD");
/*      */   
/*  758 */   public static final PdfName FDECODEPARMS = new PdfName("FDecodeParms");
/*      */   
/*  760 */   public static final PdfName FDF = new PdfName("FDF");
/*      */   
/*  762 */   public static final PdfName FF = new PdfName("Ff");
/*      */   
/*  764 */   public static final PdfName FFILTER = new PdfName("FFilter");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  769 */   public static final PdfName FG = new PdfName("FG");
/*      */   
/*  771 */   public static final PdfName FIELDMDP = new PdfName("FieldMDP");
/*      */   
/*  773 */   public static final PdfName FIELDS = new PdfName("Fields");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  778 */   public static final PdfName FIGURE = new PdfName("Figure");
/*      */   
/*  780 */   public static final PdfName FILEATTACHMENT = new PdfName("FileAttachment");
/*      */   
/*  782 */   public static final PdfName FILESPEC = new PdfName("Filespec");
/*      */   
/*  784 */   public static final PdfName FILTER = new PdfName("Filter");
/*      */   
/*  786 */   public static final PdfName FIRST = new PdfName("First");
/*      */   
/*  788 */   public static final PdfName FIRSTCHAR = new PdfName("FirstChar");
/*      */   
/*  790 */   public static final PdfName FIRSTPAGE = new PdfName("FirstPage");
/*      */   
/*  792 */   public static final PdfName FIT = new PdfName("Fit");
/*      */   
/*  794 */   public static final PdfName FITH = new PdfName("FitH");
/*      */   
/*  796 */   public static final PdfName FITV = new PdfName("FitV");
/*      */   
/*  798 */   public static final PdfName FITR = new PdfName("FitR");
/*      */   
/*  800 */   public static final PdfName FITB = new PdfName("FitB");
/*      */   
/*  802 */   public static final PdfName FITBH = new PdfName("FitBH");
/*      */   
/*  804 */   public static final PdfName FITBV = new PdfName("FitBV");
/*      */   
/*  806 */   public static final PdfName FITWINDOW = new PdfName("FitWindow");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  811 */   public static final PdfName FL = new PdfName("Fl");
/*      */   
/*  813 */   public static final PdfName FLAGS = new PdfName("Flags");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  818 */   public static final PdfName FLASH = new PdfName("Flash");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  823 */   public static final PdfName FLASHVARS = new PdfName("FlashVars");
/*      */   
/*  825 */   public static final PdfName FLATEDECODE = new PdfName("FlateDecode");
/*      */   
/*  827 */   public static final PdfName FO = new PdfName("Fo");
/*      */   
/*  829 */   public static final PdfName FONT = new PdfName("Font");
/*      */   
/*  831 */   public static final PdfName FONTBBOX = new PdfName("FontBBox");
/*      */   
/*  833 */   public static final PdfName FONTDESCRIPTOR = new PdfName("FontDescriptor");
/*      */   
/*  835 */   public static final PdfName FONTFAMILY = new PdfName("FontFamily");
/*      */   
/*  837 */   public static final PdfName FONTFILE = new PdfName("FontFile");
/*      */   
/*  839 */   public static final PdfName FONTFILE2 = new PdfName("FontFile2");
/*      */   
/*  841 */   public static final PdfName FONTFILE3 = new PdfName("FontFile3");
/*      */   
/*  843 */   public static final PdfName FONTMATRIX = new PdfName("FontMatrix");
/*      */   
/*  845 */   public static final PdfName FONTNAME = new PdfName("FontName");
/*      */   
/*  847 */   public static final PdfName FONTWEIGHT = new PdfName("FontWeight");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  852 */   public static final PdfName FOREGROUND = new PdfName("Foreground");
/*      */   
/*  854 */   public static final PdfName FORM = new PdfName("Form");
/*      */   
/*  856 */   public static final PdfName FORMTYPE = new PdfName("FormType");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  861 */   public static final PdfName FORMULA = new PdfName("Formula");
/*      */   
/*  863 */   public static final PdfName FREETEXT = new PdfName("FreeText");
/*      */   
/*  865 */   public static final PdfName FRM = new PdfName("FRM");
/*      */   
/*  867 */   public static final PdfName FS = new PdfName("FS");
/*      */   
/*  869 */   public static final PdfName FT = new PdfName("FT");
/*      */   
/*  871 */   public static final PdfName FULLSCREEN = new PdfName("FullScreen");
/*      */   
/*  873 */   public static final PdfName FUNCTION = new PdfName("Function");
/*      */   
/*  875 */   public static final PdfName FUNCTIONS = new PdfName("Functions");
/*      */   
/*  877 */   public static final PdfName FUNCTIONTYPE = new PdfName("FunctionType");
/*      */   
/*  879 */   public static final PdfName GAMMA = new PdfName("Gamma");
/*      */   
/*  881 */   public static final PdfName GBK = new PdfName("GBK");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  886 */   public static final PdfName GCS = new PdfName("GCS");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  891 */   public static final PdfName GEO = new PdfName("GEO");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  896 */   public static final PdfName GEOGCS = new PdfName("GEOGCS");
/*      */   
/*  898 */   public static final PdfName GLITTER = new PdfName("Glitter");
/*      */   
/*  900 */   public static final PdfName GOTO = new PdfName("GoTo");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  905 */   public static final PdfName GOTO3DVIEW = new PdfName("GoTo3DView");
/*      */   
/*  907 */   public static final PdfName GOTOE = new PdfName("GoToE");
/*      */   
/*  909 */   public static final PdfName GOTOR = new PdfName("GoToR");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  914 */   public static final PdfName GPTS = new PdfName("GPTS");
/*      */   
/*  916 */   public static final PdfName GROUP = new PdfName("Group");
/*      */   
/*  918 */   public static final PdfName GTS_PDFA1 = new PdfName("GTS_PDFA1");
/*      */   
/*  920 */   public static final PdfName GTS_PDFX = new PdfName("GTS_PDFX");
/*      */   
/*  922 */   public static final PdfName GTS_PDFXVERSION = new PdfName("GTS_PDFXVersion");
/*      */   
/*  924 */   public static final PdfName H = new PdfName("H");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  929 */   public static final PdfName H1 = new PdfName("H1");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  934 */   public static final PdfName H2 = new PdfName("H2");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  939 */   public static final PdfName H3 = new PdfName("H3");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  944 */   public static final PdfName H4 = new PdfName("H4");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  949 */   public static final PdfName H5 = new PdfName("H5");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  954 */   public static final PdfName H6 = new PdfName("H6");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  959 */   public static final PdfName HALFTONENAME = new PdfName("HalftoneName");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  964 */   public static final PdfName HALFTONETYPE = new PdfName("HalftoneType");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  970 */   public static final PdfName HALIGN = new PdfName("HAlign");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  975 */   public static final PdfName HEADERS = new PdfName("Headers");
/*      */   
/*  977 */   public static final PdfName HEIGHT = new PdfName("Height");
/*      */   
/*  979 */   public static final PdfName HELV = new PdfName("Helv");
/*      */   
/*  981 */   public static final PdfName HELVETICA = new PdfName("Helvetica");
/*      */   
/*  983 */   public static final PdfName HELVETICA_BOLD = new PdfName("Helvetica-Bold");
/*      */   
/*  985 */   public static final PdfName HELVETICA_OBLIQUE = new PdfName("Helvetica-Oblique");
/*      */   
/*  987 */   public static final PdfName HELVETICA_BOLDOBLIQUE = new PdfName("Helvetica-BoldOblique");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  992 */   public static final PdfName HF = new PdfName("HF");
/*      */   
/*  994 */   public static final PdfName HID = new PdfName("Hid");
/*      */   
/*  996 */   public static final PdfName HIDE = new PdfName("Hide");
/*      */   
/*  998 */   public static final PdfName HIDEMENUBAR = new PdfName("HideMenubar");
/*      */   
/* 1000 */   public static final PdfName HIDETOOLBAR = new PdfName("HideToolbar");
/*      */   
/* 1002 */   public static final PdfName HIDEWINDOWUI = new PdfName("HideWindowUI");
/*      */   
/* 1004 */   public static final PdfName HIGHLIGHT = new PdfName("Highlight");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1009 */   public static final PdfName HOFFSET = new PdfName("HOffset");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1014 */   public static final PdfName HT = new PdfName("HT");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1019 */   public static final PdfName HTP = new PdfName("HTP");
/*      */   
/* 1021 */   public static final PdfName I = new PdfName("I");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1026 */   public static final PdfName IC = new PdfName("IC");
/*      */   
/* 1028 */   public static final PdfName ICCBASED = new PdfName("ICCBased");
/*      */   
/* 1030 */   public static final PdfName ID = new PdfName("ID");
/*      */   
/* 1032 */   public static final PdfName IDENTITY = new PdfName("Identity");
/*      */   
/* 1034 */   public static final PdfName IDTREE = new PdfName("IDTree");
/*      */   
/* 1036 */   public static final PdfName IF = new PdfName("IF");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1041 */   public static final PdfName IM = new PdfName("IM");
/*      */   
/* 1043 */   public static final PdfName IMAGE = new PdfName("Image");
/*      */   
/* 1045 */   public static final PdfName IMAGEB = new PdfName("ImageB");
/*      */   
/* 1047 */   public static final PdfName IMAGEC = new PdfName("ImageC");
/*      */   
/* 1049 */   public static final PdfName IMAGEI = new PdfName("ImageI");
/*      */   
/* 1051 */   public static final PdfName IMAGEMASK = new PdfName("ImageMask");
/*      */   
/* 1053 */   public static final PdfName INCLUDE = new PdfName("Include");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1058 */   public static final PdfName IND = new PdfName("Ind");
/*      */   
/* 1060 */   public static final PdfName INDEX = new PdfName("Index");
/*      */   
/* 1062 */   public static final PdfName INDEXED = new PdfName("Indexed");
/*      */   
/* 1064 */   public static final PdfName INFO = new PdfName("Info");
/*      */   
/* 1066 */   public static final PdfName INK = new PdfName("Ink");
/*      */   
/* 1068 */   public static final PdfName INKLIST = new PdfName("InkList");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1073 */   public static final PdfName INSTANCES = new PdfName("Instances");
/*      */   
/* 1075 */   public static final PdfName IMPORTDATA = new PdfName("ImportData");
/*      */   
/* 1077 */   public static final PdfName INTENT = new PdfName("Intent");
/*      */   
/* 1079 */   public static final PdfName INTERPOLATE = new PdfName("Interpolate");
/*      */   
/* 1081 */   public static final PdfName ISMAP = new PdfName("IsMap");
/*      */   
/* 1083 */   public static final PdfName IRT = new PdfName("IRT");
/*      */   
/* 1085 */   public static final PdfName ITALICANGLE = new PdfName("ItalicAngle");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1090 */   public static final PdfName ITXT = new PdfName("ITXT");
/*      */   
/* 1092 */   public static final PdfName IX = new PdfName("IX");
/*      */   
/* 1094 */   public static final PdfName JAVASCRIPT = new PdfName("JavaScript");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1099 */   public static final PdfName JBIG2DECODE = new PdfName("JBIG2Decode");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1104 */   public static final PdfName JBIG2GLOBALS = new PdfName("JBIG2Globals");
/*      */   
/* 1106 */   public static final PdfName JPXDECODE = new PdfName("JPXDecode");
/*      */   
/* 1108 */   public static final PdfName JS = new PdfName("JS");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1113 */   public static final PdfName JUSTIFY = new PdfName("Justify");
/*      */   
/* 1115 */   public static final PdfName K = new PdfName("K");
/*      */   
/* 1117 */   public static final PdfName KEYWORDS = new PdfName("Keywords");
/*      */   
/* 1119 */   public static final PdfName KIDS = new PdfName("Kids");
/*      */   
/* 1121 */   public static final PdfName L = new PdfName("L");
/*      */   
/* 1123 */   public static final PdfName L2R = new PdfName("L2R");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1128 */   public static final PdfName LAB = new PdfName("Lab");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1134 */   public static final PdfName LANG = new PdfName("Lang");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1139 */   public static final PdfName LANGUAGE = new PdfName("Language");
/*      */   
/* 1141 */   public static final PdfName LAST = new PdfName("Last");
/*      */   
/* 1143 */   public static final PdfName LASTCHAR = new PdfName("LastChar");
/*      */   
/* 1145 */   public static final PdfName LASTPAGE = new PdfName("LastPage");
/*      */   
/* 1147 */   public static final PdfName LAUNCH = new PdfName("Launch");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1152 */   public static final PdfName LAYOUT = new PdfName("Layout");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1157 */   public static final PdfName LBL = new PdfName("Lbl");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1162 */   public static final PdfName LBODY = new PdfName("LBody");
/*      */   
/* 1164 */   public static final PdfName LENGTH = new PdfName("Length");
/*      */   
/* 1166 */   public static final PdfName LENGTH1 = new PdfName("Length1");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1171 */   public static final PdfName LI = new PdfName("LI");
/*      */   
/* 1173 */   public static final PdfName LIMITS = new PdfName("Limits");
/*      */   
/* 1175 */   public static final PdfName LINE = new PdfName("Line");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1180 */   public static final PdfName LINEAR = new PdfName("Linear");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1185 */   public static final PdfName LINEHEIGHT = new PdfName("LineHeight");
/*      */   
/* 1187 */   public static final PdfName LINK = new PdfName("Link");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1192 */   public static final PdfName LIST = new PdfName("List");
/*      */   
/* 1194 */   public static final PdfName LISTMODE = new PdfName("ListMode");
/*      */   
/* 1196 */   public static final PdfName LISTNUMBERING = new PdfName("ListNumbering");
/*      */   
/* 1198 */   public static final PdfName LOCATION = new PdfName("Location");
/*      */   
/* 1200 */   public static final PdfName LOCK = new PdfName("Lock");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1205 */   public static final PdfName LOCKED = new PdfName("Locked");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1210 */   public static final PdfName LOWERALPHA = new PdfName("LowerAlpha");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1215 */   public static final PdfName LOWERROMAN = new PdfName("LowerRoman");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1220 */   public static final PdfName LPTS = new PdfName("LPTS");
/*      */   
/* 1222 */   public static final PdfName LZWDECODE = new PdfName("LZWDecode");
/*      */   
/* 1224 */   public static final PdfName M = new PdfName("M");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1229 */   public static final PdfName MAC = new PdfName("Mac");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1234 */   public static final PdfName MATERIAL = new PdfName("Material");
/*      */   
/* 1236 */   public static final PdfName MATRIX = new PdfName("Matrix");
/*      */   
/* 1238 */   public static final PdfName MAC_EXPERT_ENCODING = new PdfName("MacExpertEncoding");
/*      */   
/* 1240 */   public static final PdfName MAC_ROMAN_ENCODING = new PdfName("MacRomanEncoding");
/*      */   
/* 1242 */   public static final PdfName MARKED = new PdfName("Marked");
/*      */   
/* 1244 */   public static final PdfName MARKINFO = new PdfName("MarkInfo");
/*      */   
/* 1246 */   public static final PdfName MASK = new PdfName("Mask");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1251 */   public static final PdfName MAX_LOWER_CASE = new PdfName("max");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1256 */   public static final PdfName MAX_CAMEL_CASE = new PdfName("Max");
/*      */   
/* 1258 */   public static final PdfName MAXLEN = new PdfName("MaxLen");
/*      */   
/* 1260 */   public static final PdfName MEDIABOX = new PdfName("MediaBox");
/*      */   
/* 1262 */   public static final PdfName MCID = new PdfName("MCID");
/*      */   
/* 1264 */   public static final PdfName MCR = new PdfName("MCR");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1269 */   public static final PdfName MEASURE = new PdfName("Measure");
/*      */   
/* 1271 */   public static final PdfName METADATA = new PdfName("Metadata");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1276 */   public static final PdfName MIN_LOWER_CASE = new PdfName("min");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1281 */   public static final PdfName MIN_CAMEL_CASE = new PdfName("Min");
/*      */   
/* 1283 */   public static final PdfName MK = new PdfName("MK");
/*      */   
/* 1285 */   public static final PdfName MMTYPE1 = new PdfName("MMType1");
/*      */   
/* 1287 */   public static final PdfName MODDATE = new PdfName("ModDate");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1292 */   public static final PdfName MOVIE = new PdfName("Movie");
/*      */   
/* 1294 */   public static final PdfName N = new PdfName("N");
/*      */   
/* 1296 */   public static final PdfName N0 = new PdfName("n0");
/*      */   
/* 1298 */   public static final PdfName N1 = new PdfName("n1");
/*      */   
/* 1300 */   public static final PdfName N2 = new PdfName("n2");
/*      */   
/* 1302 */   public static final PdfName N3 = new PdfName("n3");
/*      */   
/* 1304 */   public static final PdfName N4 = new PdfName("n4");
/*      */   
/* 1306 */   public static final PdfName NAME = new PdfName("Name");
/*      */   
/* 1308 */   public static final PdfName NAMED = new PdfName("Named");
/*      */   
/* 1310 */   public static final PdfName NAMES = new PdfName("Names");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1315 */   public static final PdfName NAVIGATION = new PdfName("Navigation");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1320 */   public static final PdfName NAVIGATIONPANE = new PdfName("NavigationPane");
/* 1321 */   public static final PdfName NCHANNEL = new PdfName("NChannel");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1326 */   public static final PdfName NEAR = new PdfName("Near");
/*      */   
/* 1328 */   public static final PdfName NEEDAPPEARANCES = new PdfName("NeedAppearances");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1333 */   public static final PdfName NEEDRENDERING = new PdfName("NeedsRendering");
/*      */   
/* 1335 */   public static final PdfName NEWWINDOW = new PdfName("NewWindow");
/*      */   
/* 1337 */   public static final PdfName NEXT = new PdfName("Next");
/*      */   
/* 1339 */   public static final PdfName NEXTPAGE = new PdfName("NextPage");
/*      */   
/* 1341 */   public static final PdfName NM = new PdfName("NM");
/*      */   
/* 1343 */   public static final PdfName NONE = new PdfName("None");
/*      */   
/* 1345 */   public static final PdfName NONFULLSCREENPAGEMODE = new PdfName("NonFullScreenPageMode");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1350 */   public static final PdfName NONSTRUCT = new PdfName("NonStruct");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1355 */   public static final PdfName NOT = new PdfName("Not");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1360 */   public static final PdfName NOTE = new PdfName("Note");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1365 */   public static final PdfName NUMBERFORMAT = new PdfName("NumberFormat");
/*      */   
/* 1367 */   public static final PdfName NUMCOPIES = new PdfName("NumCopies");
/*      */   
/* 1369 */   public static final PdfName NUMS = new PdfName("Nums");
/*      */   
/* 1371 */   public static final PdfName O = new PdfName("O");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1376 */   public static final PdfName OBJ = new PdfName("Obj");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1381 */   public static final PdfName OBJR = new PdfName("OBJR");
/*      */   
/* 1383 */   public static final PdfName OBJSTM = new PdfName("ObjStm");
/*      */   
/* 1385 */   public static final PdfName OC = new PdfName("OC");
/*      */   
/* 1387 */   public static final PdfName OCG = new PdfName("OCG");
/*      */   
/* 1389 */   public static final PdfName OCGS = new PdfName("OCGs");
/*      */   
/* 1391 */   public static final PdfName OCMD = new PdfName("OCMD");
/*      */   
/* 1393 */   public static final PdfName OCPROPERTIES = new PdfName("OCProperties");
/*      */ 
/*      */ 
/*      */   
/* 1397 */   public static final PdfName OCSP = new PdfName("OCSP");
/*      */ 
/*      */ 
/*      */   
/* 1401 */   public static final PdfName OCSPS = new PdfName("OCSPs");
/*      */   
/* 1403 */   public static final PdfName OE = new PdfName("OE");
/*      */   
/* 1405 */   public static final PdfName Off = new PdfName("Off");
/*      */   
/* 1407 */   public static final PdfName OFF = new PdfName("OFF");
/*      */   
/* 1409 */   public static final PdfName ON = new PdfName("ON");
/*      */   
/* 1411 */   public static final PdfName ONECOLUMN = new PdfName("OneColumn");
/*      */   
/* 1413 */   public static final PdfName OPEN = new PdfName("Open");
/*      */   
/* 1415 */   public static final PdfName OPENACTION = new PdfName("OpenAction");
/*      */   
/* 1417 */   public static final PdfName OP = new PdfName("OP");
/*      */   
/* 1419 */   public static final PdfName op = new PdfName("op");
/*      */ 
/*      */ 
/*      */   
/* 1423 */   public static final PdfName OPI = new PdfName("OPI");
/*      */   
/* 1425 */   public static final PdfName OPM = new PdfName("OPM");
/*      */   
/* 1427 */   public static final PdfName OPT = new PdfName("Opt");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1432 */   public static final PdfName OR = new PdfName("Or");
/*      */   
/* 1434 */   public static final PdfName ORDER = new PdfName("Order");
/*      */   
/* 1436 */   public static final PdfName ORDERING = new PdfName("Ordering");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1441 */   public static final PdfName ORG = new PdfName("Org");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1446 */   public static final PdfName OSCILLATING = new PdfName("Oscillating");
/*      */ 
/*      */   
/* 1449 */   public static final PdfName OUTLINES = new PdfName("Outlines");
/*      */   
/* 1451 */   public static final PdfName OUTPUTCONDITION = new PdfName("OutputCondition");
/*      */   
/* 1453 */   public static final PdfName OUTPUTCONDITIONIDENTIFIER = new PdfName("OutputConditionIdentifier");
/*      */   
/* 1455 */   public static final PdfName OUTPUTINTENT = new PdfName("OutputIntent");
/*      */   
/* 1457 */   public static final PdfName OUTPUTINTENTS = new PdfName("OutputIntents");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1462 */   public static final PdfName OVERLAYTEXT = new PdfName("OverlayText");
/*      */   
/* 1464 */   public static final PdfName P = new PdfName("P");
/*      */   
/* 1466 */   public static final PdfName PAGE = new PdfName("Page");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1471 */   public static final PdfName PAGEELEMENT = new PdfName("PageElement");
/*      */   
/* 1473 */   public static final PdfName PAGELABELS = new PdfName("PageLabels");
/*      */   
/* 1475 */   public static final PdfName PAGELAYOUT = new PdfName("PageLayout");
/*      */   
/* 1477 */   public static final PdfName PAGEMODE = new PdfName("PageMode");
/*      */   
/* 1479 */   public static final PdfName PAGES = new PdfName("Pages");
/*      */   
/* 1481 */   public static final PdfName PAINTTYPE = new PdfName("PaintType");
/*      */   
/* 1483 */   public static final PdfName PANOSE = new PdfName("Panose");
/*      */   
/* 1485 */   public static final PdfName PARAMS = new PdfName("Params");
/*      */   
/* 1487 */   public static final PdfName PARENT = new PdfName("Parent");
/*      */   
/* 1489 */   public static final PdfName PARENTTREE = new PdfName("ParentTree");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1494 */   public static final PdfName PARENTTREENEXTKEY = new PdfName("ParentTreeNextKey");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1499 */   public static final PdfName PART = new PdfName("Part");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1504 */   public static final PdfName PASSCONTEXTCLICK = new PdfName("PassContextClick");
/*      */   
/* 1506 */   public static final PdfName PATTERN = new PdfName("Pattern");
/*      */   
/* 1508 */   public static final PdfName PATTERNTYPE = new PdfName("PatternType");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1513 */   public static final PdfName PB = new PdfName("pb");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1518 */   public static final PdfName PC = new PdfName("PC");
/*      */   
/* 1520 */   public static final PdfName PDF = new PdfName("PDF");
/*      */   
/* 1522 */   public static final PdfName PDFDOCENCODING = new PdfName("PDFDocEncoding");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1527 */   public static final PdfName PDU = new PdfName("PDU");
/*      */   
/* 1529 */   public static final PdfName PERCEPTUAL = new PdfName("Perceptual");
/*      */   
/* 1531 */   public static final PdfName PERMS = new PdfName("Perms");
/*      */   
/* 1533 */   public static final PdfName PG = new PdfName("Pg");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1538 */   public static final PdfName PI = new PdfName("PI");
/*      */   
/* 1540 */   public static final PdfName PICKTRAYBYPDFSIZE = new PdfName("PickTrayByPDFSize");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1545 */   public static final PdfName PIECEINFO = new PdfName("PieceInfo");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1550 */   public static final PdfName PLAYCOUNT = new PdfName("PlayCount");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1555 */   public static final PdfName PO = new PdfName("PO");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1560 */   public static final PdfName POLYGON = new PdfName("Polygon");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1565 */   public static final PdfName POLYLINE = new PdfName("PolyLine");
/*      */   
/* 1567 */   public static final PdfName POPUP = new PdfName("Popup");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1572 */   public static final PdfName POSITION = new PdfName("Position");
/*      */   
/* 1574 */   public static final PdfName PREDICTOR = new PdfName("Predictor");
/*      */   
/* 1576 */   public static final PdfName PREFERRED = new PdfName("Preferred");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1581 */   public static final PdfName PRESENTATION = new PdfName("Presentation");
/*      */   
/* 1583 */   public static final PdfName PRESERVERB = new PdfName("PreserveRB");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1588 */   public static final PdfName PRESSTEPS = new PdfName("PresSteps");
/*      */   
/* 1590 */   public static final PdfName PREV = new PdfName("Prev");
/*      */   
/* 1592 */   public static final PdfName PREVPAGE = new PdfName("PrevPage");
/*      */   
/* 1594 */   public static final PdfName PRINT = new PdfName("Print");
/*      */   
/* 1596 */   public static final PdfName PRINTAREA = new PdfName("PrintArea");
/*      */   
/* 1598 */   public static final PdfName PRINTCLIP = new PdfName("PrintClip");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1603 */   public static final PdfName PRINTERMARK = new PdfName("PrinterMark");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1608 */   public static final PdfName PRINTFIELD = new PdfName("PrintField");
/*      */   
/* 1610 */   public static final PdfName PRINTPAGERANGE = new PdfName("PrintPageRange");
/*      */   
/* 1612 */   public static final PdfName PRINTSCALING = new PdfName("PrintScaling");
/*      */   
/* 1614 */   public static final PdfName PRINTSTATE = new PdfName("PrintState");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1619 */   public static final PdfName PRIVATE = new PdfName("Private");
/*      */   
/* 1621 */   public static final PdfName PROCSET = new PdfName("ProcSet");
/*      */   
/* 1623 */   public static final PdfName PRODUCER = new PdfName("Producer");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1628 */   public static final PdfName PROJCS = new PdfName("PROJCS");
/*      */   
/* 1630 */   public static final PdfName PROP_BUILD = new PdfName("Prop_Build");
/*      */   
/* 1632 */   public static final PdfName PROPERTIES = new PdfName("Properties");
/*      */   
/* 1634 */   public static final PdfName PS = new PdfName("PS");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1639 */   public static final PdfName PTDATA = new PdfName("PtData");
/*      */   
/* 1641 */   public static final PdfName PUBSEC = new PdfName("Adobe.PubSec");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1646 */   public static final PdfName PV = new PdfName("PV");
/*      */   
/* 1648 */   public static final PdfName Q = new PdfName("Q");
/*      */   
/* 1650 */   public static final PdfName QUADPOINTS = new PdfName("QuadPoints");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1655 */   public static final PdfName QUOTE = new PdfName("Quote");
/*      */   
/* 1657 */   public static final PdfName R = new PdfName("R");
/*      */   
/* 1659 */   public static final PdfName R2L = new PdfName("R2L");
/*      */   
/* 1661 */   public static final PdfName RANGE = new PdfName("Range");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1666 */   public static final PdfName RB = new PdfName("RB");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1671 */   public static final PdfName rb = new PdfName("rb");
/*      */   
/* 1673 */   public static final PdfName RBGROUPS = new PdfName("RBGroups");
/*      */   
/* 1675 */   public static final PdfName RC = new PdfName("RC");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1680 */   public static final PdfName RD = new PdfName("RD");
/*      */   
/* 1682 */   public static final PdfName REASON = new PdfName("Reason");
/*      */   
/* 1684 */   public static final PdfName RECIPIENTS = new PdfName("Recipients");
/*      */   
/* 1686 */   public static final PdfName RECT = new PdfName("Rect");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1691 */   public static final PdfName REDACT = new PdfName("Redact");
/*      */   
/* 1693 */   public static final PdfName REFERENCE = new PdfName("Reference");
/*      */   
/* 1695 */   public static final PdfName REGISTRY = new PdfName("Registry");
/*      */   
/* 1697 */   public static final PdfName REGISTRYNAME = new PdfName("RegistryName");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1702 */   public static final PdfName RELATIVECOLORIMETRIC = new PdfName("RelativeColorimetric");
/*      */   
/* 1704 */   public static final PdfName RENDITION = new PdfName("Rendition");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1709 */   public static final PdfName REPEAT = new PdfName("Repeat");
/*      */   
/* 1711 */   public static final PdfName RESETFORM = new PdfName("ResetForm");
/*      */   
/* 1713 */   public static final PdfName RESOURCES = new PdfName("Resources");
/* 1714 */   public static final PdfName REQUIREMENTS = new PdfName("Requirements");
/* 1715 */   public static final PdfName REVERSEDCHARS = new PdfName("ReversedChars");
/*      */   
/* 1717 */   public static final PdfName RI = new PdfName("RI");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1722 */   public static final PdfName RICHMEDIA = new PdfName("RichMedia");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1727 */   public static final PdfName RICHMEDIAACTIVATION = new PdfName("RichMediaActivation");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1732 */   public static final PdfName RICHMEDIAANIMATION = new PdfName("RichMediaAnimation");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1737 */   public static final PdfName RICHMEDIACOMMAND = new PdfName("RichMediaCommand");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1742 */   public static final PdfName RICHMEDIACONFIGURATION = new PdfName("RichMediaConfiguration");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1747 */   public static final PdfName RICHMEDIACONTENT = new PdfName("RichMediaContent");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1752 */   public static final PdfName RICHMEDIADEACTIVATION = new PdfName("RichMediaDeactivation");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1757 */   public static final PdfName RICHMEDIAEXECUTE = new PdfName("RichMediaExecute");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1762 */   public static final PdfName RICHMEDIAINSTANCE = new PdfName("RichMediaInstance");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1767 */   public static final PdfName RICHMEDIAPARAMS = new PdfName("RichMediaParams");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1772 */   public static final PdfName RICHMEDIAPOSITION = new PdfName("RichMediaPosition");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1777 */   public static final PdfName RICHMEDIAPRESENTATION = new PdfName("RichMediaPresentation");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1782 */   public static final PdfName RICHMEDIASETTINGS = new PdfName("RichMediaSettings");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1787 */   public static final PdfName RICHMEDIAWINDOW = new PdfName("RichMediaWindow");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1792 */   public static final PdfName RL = new PdfName("RL");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1797 */   public static final PdfName ROLE = new PdfName("Role");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1802 */   public static final PdfName RO = new PdfName("RO");
/*      */   
/* 1804 */   public static final PdfName ROLEMAP = new PdfName("RoleMap");
/*      */   
/* 1806 */   public static final PdfName ROOT = new PdfName("Root");
/*      */   
/* 1808 */   public static final PdfName ROTATE = new PdfName("Rotate");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1813 */   public static final PdfName ROW = new PdfName("Row");
/*      */   
/* 1815 */   public static final PdfName ROWS = new PdfName("Rows");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1820 */   public static final PdfName ROWSPAN = new PdfName("RowSpan");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1825 */   public static final PdfName RP = new PdfName("RP");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1830 */   public static final PdfName RT = new PdfName("RT");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1835 */   public static final PdfName RUBY = new PdfName("Ruby");
/*      */   
/* 1837 */   public static final PdfName RUNLENGTHDECODE = new PdfName("RunLengthDecode");
/*      */   
/* 1839 */   public static final PdfName RV = new PdfName("RV");
/*      */   
/* 1841 */   public static final PdfName S = new PdfName("S");
/*      */   
/* 1843 */   public static final PdfName SATURATION = new PdfName("Saturation");
/*      */   
/* 1845 */   public static final PdfName SCHEMA = new PdfName("Schema");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1850 */   public static final PdfName SCOPE = new PdfName("Scope");
/*      */   
/* 1852 */   public static final PdfName SCREEN = new PdfName("Screen");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1857 */   public static final PdfName SCRIPTS = new PdfName("Scripts");
/*      */   
/* 1859 */   public static final PdfName SECT = new PdfName("Sect");
/*      */   
/* 1861 */   public static final PdfName SEPARATION = new PdfName("Separation");
/*      */   
/* 1863 */   public static final PdfName SETOCGSTATE = new PdfName("SetOCGState");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1868 */   public static final PdfName SETTINGS = new PdfName("Settings");
/*      */   
/* 1870 */   public static final PdfName SHADING = new PdfName("Shading");
/*      */   
/* 1872 */   public static final PdfName SHADINGTYPE = new PdfName("ShadingType");
/*      */   
/* 1874 */   public static final PdfName SHIFT_JIS = new PdfName("Shift-JIS");
/*      */   
/* 1876 */   public static final PdfName SIG = new PdfName("Sig");
/*      */   
/* 1878 */   public static final PdfName SIGFIELDLOCK = new PdfName("SigFieldLock");
/*      */   
/* 1880 */   public static final PdfName SIGFLAGS = new PdfName("SigFlags");
/*      */   
/* 1882 */   public static final PdfName SIGREF = new PdfName("SigRef");
/*      */   
/* 1884 */   public static final PdfName SIMPLEX = new PdfName("Simplex");
/*      */   
/* 1886 */   public static final PdfName SINGLEPAGE = new PdfName("SinglePage");
/*      */   
/* 1888 */   public static final PdfName SIZE = new PdfName("Size");
/*      */   
/* 1890 */   public static final PdfName SMASK = new PdfName("SMask");
/*      */   
/* 1892 */   public static final PdfName SMASKINDATA = new PdfName("SMaskInData");
/*      */   
/* 1894 */   public static final PdfName SORT = new PdfName("Sort");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1899 */   public static final PdfName SOUND = new PdfName("Sound");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1904 */   public static final PdfName SPACEAFTER = new PdfName("SpaceAfter");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1909 */   public static final PdfName SPACEBEFORE = new PdfName("SpaceBefore");
/*      */   
/* 1911 */   public static final PdfName SPAN = new PdfName("Span");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1916 */   public static final PdfName SPEED = new PdfName("Speed");
/*      */   
/* 1918 */   public static final PdfName SPLIT = new PdfName("Split");
/*      */   
/* 1920 */   public static final PdfName SQUARE = new PdfName("Square");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1925 */   public static final PdfName SQUIGGLY = new PdfName("Squiggly");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1930 */   public static final PdfName SS = new PdfName("SS");
/*      */   
/* 1932 */   public static final PdfName ST = new PdfName("St");
/*      */   
/* 1934 */   public static final PdfName STAMP = new PdfName("Stamp");
/*      */   
/* 1936 */   public static final PdfName STATUS = new PdfName("Status");
/*      */   
/* 1938 */   public static final PdfName STANDARD = new PdfName("Standard");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1943 */   public static final PdfName START = new PdfName("Start");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1948 */   public static final PdfName STARTINDENT = new PdfName("StartIndent");
/*      */   
/* 1950 */   public static final PdfName STATE = new PdfName("State");
/*      */   
/* 1952 */   public static final PdfName STDCF = new PdfName("StdCF");
/*      */   
/* 1954 */   public static final PdfName STEMV = new PdfName("StemV");
/*      */   
/* 1956 */   public static final PdfName STMF = new PdfName("StmF");
/*      */   
/* 1958 */   public static final PdfName STRF = new PdfName("StrF");
/*      */   
/* 1960 */   public static final PdfName STRIKEOUT = new PdfName("StrikeOut");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1965 */   public static final PdfName STRUCTELEM = new PdfName("StructElem");
/*      */   
/* 1967 */   public static final PdfName STRUCTPARENT = new PdfName("StructParent");
/*      */   
/* 1969 */   public static final PdfName STRUCTPARENTS = new PdfName("StructParents");
/*      */   
/* 1971 */   public static final PdfName STRUCTTREEROOT = new PdfName("StructTreeRoot");
/*      */   
/* 1973 */   public static final PdfName STYLE = new PdfName("Style");
/*      */   
/* 1975 */   public static final PdfName SUBFILTER = new PdfName("SubFilter");
/*      */   
/* 1977 */   public static final PdfName SUBJECT = new PdfName("Subject");
/*      */   
/* 1979 */   public static final PdfName SUBMITFORM = new PdfName("SubmitForm");
/*      */   
/* 1981 */   public static final PdfName SUBTYPE = new PdfName("Subtype");
/*      */ 
/*      */ 
/*      */   
/* 1985 */   public static final PdfName SUMMARY = new PdfName("Summary");
/*      */   
/* 1987 */   public static final PdfName SUPPLEMENT = new PdfName("Supplement");
/*      */   
/* 1989 */   public static final PdfName SV = new PdfName("SV");
/*      */   
/* 1991 */   public static final PdfName SW = new PdfName("SW");
/*      */   
/* 1993 */   public static final PdfName SYMBOL = new PdfName("Symbol");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1998 */   public static final PdfName T = new PdfName("T");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2003 */   public static final PdfName TA = new PdfName("TA");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2008 */   public static final PdfName TABLE = new PdfName("Table");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2013 */   public static final PdfName TABS = new PdfName("Tabs");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2018 */   public static final PdfName TBODY = new PdfName("TBody");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2023 */   public static final PdfName TD = new PdfName("TD");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2028 */   public static final PdfName TR = new PdfName("TR");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2033 */   public static final PdfName TR2 = new PdfName("TR2");
/*      */   
/* 2035 */   public static final PdfName TEXT = new PdfName("Text");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2040 */   public static final PdfName TEXTALIGN = new PdfName("TextAlign");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2045 */   public static final PdfName TEXTDECORATIONCOLOR = new PdfName("TextDecorationColor");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2050 */   public static final PdfName TEXTDECORATIONTHICKNESS = new PdfName("TextDecorationThickness");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2055 */   public static final PdfName TEXTDECORATIONTYPE = new PdfName("TextDecorationType");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2060 */   public static final PdfName TEXTINDENT = new PdfName("TextIndent");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2065 */   public static final PdfName TFOOT = new PdfName("TFoot");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2070 */   public static final PdfName TH = new PdfName("TH");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2075 */   public static final PdfName THEAD = new PdfName("THead");
/*      */   
/* 2077 */   public static final PdfName THUMB = new PdfName("Thumb");
/*      */   
/* 2079 */   public static final PdfName THREADS = new PdfName("Threads");
/*      */   
/* 2081 */   public static final PdfName TI = new PdfName("TI");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2086 */   public static final PdfName TIME = new PdfName("Time");
/*      */   
/* 2088 */   public static final PdfName TILINGTYPE = new PdfName("TilingType");
/*      */   
/* 2090 */   public static final PdfName TIMES_ROMAN = new PdfName("Times-Roman");
/*      */   
/* 2092 */   public static final PdfName TIMES_BOLD = new PdfName("Times-Bold");
/*      */   
/* 2094 */   public static final PdfName TIMES_ITALIC = new PdfName("Times-Italic");
/*      */   
/* 2096 */   public static final PdfName TIMES_BOLDITALIC = new PdfName("Times-BoldItalic");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2102 */   public static final PdfName TITLE = new PdfName("Title");
/*      */   
/* 2104 */   public static final PdfName TK = new PdfName("TK");
/*      */   
/* 2106 */   public static final PdfName TM = new PdfName("TM");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2111 */   public static final PdfName TOC = new PdfName("TOC");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2116 */   public static final PdfName TOCI = new PdfName("TOCI");
/*      */   
/* 2118 */   public static final PdfName TOGGLE = new PdfName("Toggle");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2123 */   public static final PdfName TOOLBAR = new PdfName("Toolbar");
/*      */   
/* 2125 */   public static final PdfName TOUNICODE = new PdfName("ToUnicode");
/*      */   
/* 2127 */   public static final PdfName TP = new PdfName("TP");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2132 */   public static final PdfName TABLEROW = new PdfName("TR");
/*      */   
/* 2134 */   public static final PdfName TRANS = new PdfName("Trans");
/*      */   
/* 2136 */   public static final PdfName TRANSFORMPARAMS = new PdfName("TransformParams");
/*      */   
/* 2138 */   public static final PdfName TRANSFORMMETHOD = new PdfName("TransformMethod");
/*      */   
/* 2140 */   public static final PdfName TRANSPARENCY = new PdfName("Transparency");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2145 */   public static final PdfName TRANSPARENT = new PdfName("Transparent");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2150 */   public static final PdfName TRAPNET = new PdfName("TrapNet");
/*      */   
/* 2152 */   public static final PdfName TRAPPED = new PdfName("Trapped");
/*      */   
/* 2154 */   public static final PdfName TRIMBOX = new PdfName("TrimBox");
/*      */   
/* 2156 */   public static final PdfName TRUETYPE = new PdfName("TrueType");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2161 */   public static final PdfName TS = new PdfName("TS");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2166 */   public static final PdfName TTL = new PdfName("Ttl");
/*      */   
/* 2168 */   public static final PdfName TU = new PdfName("TU");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2173 */   public static final PdfName TV = new PdfName("tv");
/*      */   
/* 2175 */   public static final PdfName TWOCOLUMNLEFT = new PdfName("TwoColumnLeft");
/*      */   
/* 2177 */   public static final PdfName TWOCOLUMNRIGHT = new PdfName("TwoColumnRight");
/*      */   
/* 2179 */   public static final PdfName TWOPAGELEFT = new PdfName("TwoPageLeft");
/*      */   
/* 2181 */   public static final PdfName TWOPAGERIGHT = new PdfName("TwoPageRight");
/*      */   
/* 2183 */   public static final PdfName TX = new PdfName("Tx");
/*      */   
/* 2185 */   public static final PdfName TYPE = new PdfName("Type");
/*      */   
/* 2187 */   public static final PdfName TYPE0 = new PdfName("Type0");
/*      */   
/* 2189 */   public static final PdfName TYPE1 = new PdfName("Type1");
/*      */   
/* 2191 */   public static final PdfName TYPE3 = new PdfName("Type3");
/*      */   
/* 2193 */   public static final PdfName U = new PdfName("U");
/*      */   
/* 2195 */   public static final PdfName UE = new PdfName("UE");
/*      */   
/* 2197 */   public static final PdfName UF = new PdfName("UF");
/*      */   
/* 2199 */   public static final PdfName UHC = new PdfName("UHC");
/*      */   
/* 2201 */   public static final PdfName UNDERLINE = new PdfName("Underline");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2206 */   public static final PdfName UNIX = new PdfName("Unix");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2211 */   public static final PdfName UPPERALPHA = new PdfName("UpperAlpha");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2216 */   public static final PdfName UPPERROMAN = new PdfName("UpperRoman");
/*      */   
/* 2218 */   public static final PdfName UR = new PdfName("UR");
/*      */   
/* 2220 */   public static final PdfName UR3 = new PdfName("UR3");
/*      */   
/* 2222 */   public static final PdfName URI = new PdfName("URI");
/*      */   
/* 2224 */   public static final PdfName URL = new PdfName("URL");
/*      */   
/* 2226 */   public static final PdfName USAGE = new PdfName("Usage");
/*      */   
/* 2228 */   public static final PdfName USEATTACHMENTS = new PdfName("UseAttachments");
/*      */   
/* 2230 */   public static final PdfName USENONE = new PdfName("UseNone");
/*      */   
/* 2232 */   public static final PdfName USEOC = new PdfName("UseOC");
/*      */   
/* 2234 */   public static final PdfName USEOUTLINES = new PdfName("UseOutlines");
/*      */   
/* 2236 */   public static final PdfName USER = new PdfName("User");
/*      */   
/* 2238 */   public static final PdfName USERPROPERTIES = new PdfName("UserProperties");
/*      */   
/* 2240 */   public static final PdfName USERUNIT = new PdfName("UserUnit");
/*      */   
/* 2242 */   public static final PdfName USETHUMBS = new PdfName("UseThumbs");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2247 */   public static final PdfName UTF_8 = new PdfName("utf_8");
/*      */   
/* 2249 */   public static final PdfName V = new PdfName("V");
/*      */   
/* 2251 */   public static final PdfName V2 = new PdfName("V2");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2256 */   public static final PdfName VALIGN = new PdfName("VAlign");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2261 */   public static final PdfName VE = new PdfName("VE");
/*      */   
/* 2263 */   public static final PdfName VERISIGN_PPKVS = new PdfName("VeriSign.PPKVS");
/*      */   
/* 2265 */   public static final PdfName VERSION = new PdfName("Version");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2270 */   public static final PdfName VERTICES = new PdfName("Vertices");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2275 */   public static final PdfName VIDEO = new PdfName("Video");
/*      */   
/* 2277 */   public static final PdfName VIEW = new PdfName("View");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2282 */   public static final PdfName VIEWS = new PdfName("Views");
/*      */   
/* 2284 */   public static final PdfName VIEWAREA = new PdfName("ViewArea");
/*      */   
/* 2286 */   public static final PdfName VIEWCLIP = new PdfName("ViewClip");
/*      */   
/* 2288 */   public static final PdfName VIEWERPREFERENCES = new PdfName("ViewerPreferences");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2293 */   public static final PdfName VIEWPORT = new PdfName("Viewport");
/*      */   
/* 2295 */   public static final PdfName VIEWSTATE = new PdfName("ViewState");
/*      */   
/* 2297 */   public static final PdfName VISIBLEPAGES = new PdfName("VisiblePages");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2302 */   public static final PdfName VOFFSET = new PdfName("VOffset");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2307 */   public static final PdfName VP = new PdfName("VP");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2312 */   public static final PdfName VRI = new PdfName("VRI");
/*      */   
/* 2314 */   public static final PdfName W = new PdfName("W");
/*      */   
/* 2316 */   public static final PdfName W2 = new PdfName("W2");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2321 */   public static final PdfName WARICHU = new PdfName("Warichu");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2326 */   public static final PdfName WATERMARK = new PdfName("Watermark");
/*      */   
/* 2328 */   public static final PdfName WC = new PdfName("WC");
/*      */   
/* 2330 */   public static final PdfName WIDGET = new PdfName("Widget");
/*      */   
/* 2332 */   public static final PdfName WIDTH = new PdfName("Width");
/*      */   
/* 2334 */   public static final PdfName WIDTHS = new PdfName("Widths");
/*      */   
/* 2336 */   public static final PdfName WIN = new PdfName("Win");
/*      */   
/* 2338 */   public static final PdfName WIN_ANSI_ENCODING = new PdfName("WinAnsiEncoding");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2343 */   public static final PdfName WINDOW = new PdfName("Window");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2348 */   public static final PdfName WINDOWED = new PdfName("Windowed");
/*      */   
/* 2350 */   public static final PdfName WIPE = new PdfName("Wipe");
/*      */   
/* 2352 */   public static final PdfName WHITEPOINT = new PdfName("WhitePoint");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2357 */   public static final PdfName WKT = new PdfName("WKT");
/*      */   
/* 2359 */   public static final PdfName WP = new PdfName("WP");
/*      */   
/* 2361 */   public static final PdfName WS = new PdfName("WS");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2366 */   public static final PdfName WT = new PdfName("WT");
/*      */   
/* 2368 */   public static final PdfName X = new PdfName("X");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2373 */   public static final PdfName XA = new PdfName("XA");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2378 */   public static final PdfName XD = new PdfName("XD");
/*      */   
/* 2380 */   public static final PdfName XFA = new PdfName("XFA");
/*      */   
/* 2382 */   public static final PdfName XML = new PdfName("XML");
/*      */   
/* 2384 */   public static final PdfName XOBJECT = new PdfName("XObject");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2389 */   public static final PdfName XPTS = new PdfName("XPTS");
/*      */   
/* 2391 */   public static final PdfName XREF = new PdfName("XRef");
/*      */   
/* 2393 */   public static final PdfName XREFSTM = new PdfName("XRefStm");
/*      */   
/* 2395 */   public static final PdfName XSTEP = new PdfName("XStep");
/*      */   
/* 2397 */   public static final PdfName XYZ = new PdfName("XYZ");
/*      */   
/* 2399 */   public static final PdfName YSTEP = new PdfName("YStep");
/*      */   
/* 2401 */   public static final PdfName ZADB = new PdfName("ZaDb");
/*      */   
/* 2403 */   public static final PdfName ZAPFDINGBATS = new PdfName("ZapfDingbats");
/*      */   
/* 2405 */   public static final PdfName ZOOM = new PdfName("Zoom");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, PdfName> staticNames;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/* 2422 */     Field[] fields = PdfName.class.getDeclaredFields();
/* 2423 */     staticNames = new HashMap<String, PdfName>(fields.length);
/* 2424 */     int flags = 25;
/*      */     try {
/* 2426 */       for (int fldIdx = 0; fldIdx < fields.length; fldIdx++) {
/* 2427 */         Field curFld = fields[fldIdx];
/* 2428 */         if ((curFld.getModifiers() & 0x19) == 25 && curFld
/* 2429 */           .getType().equals(PdfName.class)) {
/* 2430 */           PdfName name = (PdfName)curFld.get(null);
/* 2431 */           staticNames.put(decodeName(name.toString()), name);
/*      */         } 
/*      */       } 
/* 2434 */     } catch (Exception e) {
/* 2435 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2440 */   private int hash = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfName(String name) {
/* 2450 */     this(name, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfName(String name, boolean lengthCheck) {
/* 2460 */     super(4);
/*      */     
/* 2462 */     int length = name.length();
/* 2463 */     if (lengthCheck && length > 127)
/* 2464 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.name.1.is.too.long.2.characters", new Object[] { name, String.valueOf(length) })); 
/* 2465 */     this.bytes = encodeName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfName(byte[] bytes) {
/* 2474 */     super(4, bytes);
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
/*      */   public int compareTo(PdfName name) {
/* 2491 */     byte[] myBytes = this.bytes;
/* 2492 */     byte[] objBytes = name.bytes;
/* 2493 */     int len = Math.min(myBytes.length, objBytes.length);
/* 2494 */     for (int i = 0; i < len; i++) {
/* 2495 */       if (myBytes[i] > objBytes[i])
/* 2496 */         return 1; 
/* 2497 */       if (myBytes[i] < objBytes[i])
/* 2498 */         return -1; 
/*      */     } 
/* 2500 */     if (myBytes.length < objBytes.length)
/* 2501 */       return -1; 
/* 2502 */     if (myBytes.length > objBytes.length)
/* 2503 */       return 1; 
/* 2504 */     return 0;
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
/*      */   public boolean equals(Object obj) {
/* 2516 */     if (this == obj)
/* 2517 */       return true; 
/* 2518 */     if (obj instanceof PdfName)
/* 2519 */       return (compareTo((PdfName)obj) == 0); 
/* 2520 */     return false;
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
/*      */   public int hashCode() {
/* 2532 */     int h = this.hash;
/* 2533 */     if (h == 0) {
/* 2534 */       int ptr = 0;
/* 2535 */       int len = this.bytes.length;
/* 2536 */       for (int i = 0; i < len; i++)
/* 2537 */         h = 31 * h + (this.bytes[ptr++] & 0xFF); 
/* 2538 */       this.hash = h;
/*      */     } 
/* 2540 */     return h;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeName(String name) {
/* 2551 */     int length = name.length();
/* 2552 */     ByteBuffer buf = new ByteBuffer(length + 20);
/* 2553 */     buf.append('/');
/*      */     
/* 2555 */     char[] chars = name.toCharArray();
/* 2556 */     for (int k = 0; k < length; k++) {
/* 2557 */       char c = (char)(chars[k] & 0xFF);
/*      */       
/* 2559 */       switch (c) {
/*      */         case ' ':
/*      */         case '#':
/*      */         case '%':
/*      */         case '(':
/*      */         case ')':
/*      */         case '/':
/*      */         case '<':
/*      */         case '>':
/*      */         case '[':
/*      */         case ']':
/*      */         case '{':
/*      */         case '}':
/* 2572 */           buf.append('#');
/* 2573 */           buf.append(Integer.toString(c, 16));
/*      */           break;
/*      */         default:
/* 2576 */           if (c >= ' ' && c <= '~') {
/* 2577 */             buf.append(c); break;
/*      */           } 
/* 2579 */           buf.append('#');
/* 2580 */           if (c < '\020')
/* 2581 */             buf.append('0'); 
/* 2582 */           buf.append(Integer.toString(c, 16));
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/* 2587 */     return buf.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String decodeName(String name) {
/* 2597 */     StringBuffer buf = new StringBuffer();
/*      */     try {
/* 2599 */       int len = name.length();
/* 2600 */       for (int k = 1; k < len; k++) {
/* 2601 */         char c = name.charAt(k);
/* 2602 */         if (c == '#') {
/* 2603 */           char c1 = name.charAt(k + 1);
/* 2604 */           char c2 = name.charAt(k + 2);
/* 2605 */           c = (char)((PRTokeniser.getHex(c1) << 4) + PRTokeniser.getHex(c2));
/* 2606 */           k += 2;
/*      */         } 
/* 2608 */         buf.append(c);
/*      */       }
/*      */     
/* 2611 */     } catch (IndexOutOfBoundsException indexOutOfBoundsException) {}
/*      */ 
/*      */     
/* 2614 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */