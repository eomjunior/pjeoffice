/*      */ package com.google.common.net;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.CharMatcher;
/*      */ import com.google.common.base.Charsets;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableListMultimap;
/*      */ import com.google.common.collect.ImmutableMultiset;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.ListMultimap;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Multimap;
/*      */ import com.google.common.collect.Multimaps;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.Immutable;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Collection;
/*      */ import java.util.Map;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Immutable
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible
/*      */ public final class MediaType
/*      */ {
/*      */   private static final String CHARSET_ATTRIBUTE = "charset";
/*   79 */   private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
/*      */ 
/*      */ 
/*      */   
/*   83 */   private static final CharMatcher TOKEN_MATCHER = CharMatcher.ascii()
/*   84 */     .and(CharMatcher.javaIsoControl().negate())
/*   85 */     .and(CharMatcher.isNot(' '))
/*   86 */     .and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
/*      */   
/*   88 */   private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   94 */   private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
/*      */   
/*      */   private static final String APPLICATION_TYPE = "application";
/*      */   
/*      */   private static final String AUDIO_TYPE = "audio";
/*      */   
/*      */   private static final String IMAGE_TYPE = "image";
/*      */   
/*      */   private static final String TEXT_TYPE = "text";
/*      */   private static final String VIDEO_TYPE = "video";
/*      */   private static final String FONT_TYPE = "font";
/*      */   private static final String WILDCARD = "*";
/*  106 */   private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
/*      */ 
/*      */   
/*      */   private static MediaType createConstant(String type, String subtype) {
/*  110 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
/*  111 */     mediaType.parsedCharset = Optional.absent();
/*  112 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType createConstantUtf8(String type, String subtype) {
/*  116 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
/*  117 */     mediaType.parsedCharset = Optional.of(Charsets.UTF_8);
/*  118 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType addKnownType(MediaType mediaType) {
/*  122 */     KNOWN_TYPES.put(mediaType, mediaType);
/*  123 */     return mediaType;
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
/*  136 */   public static final MediaType ANY_TYPE = createConstant("*", "*");
/*  137 */   public static final MediaType ANY_TEXT_TYPE = createConstant("text", "*");
/*  138 */   public static final MediaType ANY_IMAGE_TYPE = createConstant("image", "*");
/*  139 */   public static final MediaType ANY_AUDIO_TYPE = createConstant("audio", "*");
/*  140 */   public static final MediaType ANY_VIDEO_TYPE = createConstant("video", "*");
/*  141 */   public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", "*");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  148 */   public static final MediaType ANY_FONT_TYPE = createConstant("font", "*");
/*      */ 
/*      */ 
/*      */   
/*  152 */   public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
/*  153 */   public static final MediaType CSS_UTF_8 = createConstantUtf8("text", "css");
/*  154 */   public static final MediaType CSV_UTF_8 = createConstantUtf8("text", "csv");
/*  155 */   public static final MediaType HTML_UTF_8 = createConstantUtf8("text", "html");
/*  156 */   public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
/*  157 */   public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  164 */   public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  171 */   public static final MediaType TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
/*      */   
/*  173 */   public static final MediaType VCARD_UTF_8 = createConstantUtf8("text", "vcard");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  181 */   public static final MediaType WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  188 */   public static final MediaType XML_UTF_8 = createConstantUtf8("text", "xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  196 */   public static final MediaType VTT_UTF_8 = createConstantUtf8("text", "vtt");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  205 */   public static final MediaType BMP = createConstant("image", "bmp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  216 */   public static final MediaType CRW = createConstant("image", "x-canon-crw");
/*      */   
/*  218 */   public static final MediaType GIF = createConstant("image", "gif");
/*  219 */   public static final MediaType ICO = createConstant("image", "vnd.microsoft.icon");
/*  220 */   public static final MediaType JPEG = createConstant("image", "jpeg");
/*  221 */   public static final MediaType PNG = createConstant("image", "png");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  240 */   public static final MediaType PSD = createConstant("image", "vnd.adobe.photoshop");
/*      */   
/*  242 */   public static final MediaType SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
/*  243 */   public static final MediaType TIFF = createConstant("image", "tiff");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  250 */   public static final MediaType WEBP = createConstant("image", "webp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  257 */   public static final MediaType HEIF = createConstant("image", "heif");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  264 */   public static final MediaType JP2K = createConstant("image", "jp2");
/*      */ 
/*      */   
/*  267 */   public static final MediaType MP4_AUDIO = createConstant("audio", "mp4");
/*  268 */   public static final MediaType MPEG_AUDIO = createConstant("audio", "mpeg");
/*  269 */   public static final MediaType OGG_AUDIO = createConstant("audio", "ogg");
/*  270 */   public static final MediaType WEBM_AUDIO = createConstant("audio", "webm");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  277 */   public static final MediaType L16_AUDIO = createConstant("audio", "l16");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  284 */   public static final MediaType L24_AUDIO = createConstant("audio", "l24");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  292 */   public static final MediaType BASIC_AUDIO = createConstant("audio", "basic");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  300 */   public static final MediaType AAC_AUDIO = createConstant("audio", "aac");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  307 */   public static final MediaType VORBIS_AUDIO = createConstant("audio", "vorbis");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  316 */   public static final MediaType WMA_AUDIO = createConstant("audio", "x-ms-wma");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  325 */   public static final MediaType WAX_AUDIO = createConstant("audio", "x-ms-wax");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  333 */   public static final MediaType VND_REAL_AUDIO = createConstant("audio", "vnd.rn-realaudio");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  340 */   public static final MediaType VND_WAVE_AUDIO = createConstant("audio", "vnd.wave");
/*      */ 
/*      */   
/*  343 */   public static final MediaType MP4_VIDEO = createConstant("video", "mp4");
/*  344 */   public static final MediaType MPEG_VIDEO = createConstant("video", "mpeg");
/*  345 */   public static final MediaType OGG_VIDEO = createConstant("video", "ogg");
/*  346 */   public static final MediaType QUICKTIME = createConstant("video", "quicktime");
/*  347 */   public static final MediaType WEBM_VIDEO = createConstant("video", "webm");
/*  348 */   public static final MediaType WMV = createConstant("video", "x-ms-wmv");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  357 */   public static final MediaType FLV_VIDEO = createConstant("video", "x-flv");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  366 */   public static final MediaType THREE_GPP_VIDEO = createConstant("video", "3gpp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  375 */   public static final MediaType THREE_GPP2_VIDEO = createConstant("video", "3gpp2");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  385 */   public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
/*      */   
/*  387 */   public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
/*  388 */   public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  396 */   public static final MediaType DART_UTF_8 = createConstantUtf8("application", "dart");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  404 */   public static final MediaType APPLE_PASSBOOK = createConstant("application", "vnd.apple.pkpass");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  413 */   public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  424 */   public static final MediaType EPUB = createConstant("application", "epub+zip");
/*      */ 
/*      */   
/*  427 */   public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  436 */   public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  448 */   public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  456 */   public static final MediaType GEO_JSON = createConstant("application", "geo+json");
/*      */   
/*  458 */   public static final MediaType GZIP = createConstant("application", "x-gzip");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  466 */   public static final MediaType HAL_JSON = createConstant("application", "hal+json");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  474 */   public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  482 */   public static final MediaType JOSE = createConstant("application", "jose");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  490 */   public static final MediaType JOSE_JSON = createConstant("application", "jose+json");
/*      */   
/*  492 */   public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  499 */   public static final MediaType JWT = createConstant("application", "jwt");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  507 */   public static final MediaType MANIFEST_JSON_UTF_8 = createConstantUtf8("application", "manifest+json");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  512 */   public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  518 */   public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  525 */   public static final MediaType MBOX = createConstant("application", "mbox");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  533 */   public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
/*      */ 
/*      */   
/*  536 */   public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  544 */   public static final MediaType MICROSOFT_OUTLOOK = createConstant("application", "vnd.ms-outlook");
/*      */ 
/*      */ 
/*      */   
/*  548 */   public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
/*      */ 
/*      */   
/*  551 */   public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  563 */   public static final MediaType MEDIA_PRESENTATION_DESCRIPTION = createConstant("application", "dash+xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  571 */   public static final MediaType WASM_APPLICATION = createConstant("application", "wasm");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  580 */   public static final MediaType NACL_APPLICATION = createConstant("application", "x-nacl");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  590 */   public static final MediaType NACL_PORTABLE_APPLICATION = createConstant("application", "x-pnacl");
/*      */   
/*  592 */   public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
/*      */   
/*  594 */   public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
/*      */   
/*  596 */   public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
/*      */ 
/*      */   
/*  599 */   public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
/*      */ 
/*      */   
/*  602 */   public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*      */   
/*  604 */   public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
/*      */   
/*  606 */   public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
/*      */   
/*  608 */   public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
/*      */   
/*  610 */   public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  620 */   public static final MediaType OPENSEARCH_DESCRIPTION_UTF_8 = createConstantUtf8("application", "opensearchdescription+xml");
/*      */   
/*  622 */   public static final MediaType PDF = createConstant("application", "pdf");
/*  623 */   public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  630 */   public static final MediaType PROTOBUF = createConstant("application", "protobuf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  640 */   public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
/*      */   
/*  642 */   public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  651 */   public static final MediaType SFNT = createConstant("application", "font-sfnt");
/*      */ 
/*      */   
/*  654 */   public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  662 */   public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  675 */   public static final MediaType SOAP_XML_UTF_8 = createConstantUtf8("application", "soap+xml");
/*      */   
/*  677 */   public static final MediaType TAR = createConstant("application", "x-tar");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  686 */   public static final MediaType WOFF = createConstant("application", "font-woff");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  695 */   public static final MediaType WOFF2 = createConstant("application", "font-woff2");
/*      */   
/*  697 */   public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  707 */   public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
/*      */   
/*  709 */   public static final MediaType ZIP = createConstant("application", "zip");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  719 */   public static final MediaType FONT_COLLECTION = createConstant("font", "collection");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  727 */   public static final MediaType FONT_OTF = createConstant("font", "otf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  737 */   public static final MediaType FONT_SFNT = createConstant("font", "sfnt");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  745 */   public static final MediaType FONT_TTF = createConstant("font", "ttf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  755 */   public static final MediaType FONT_WOFF = createConstant("font", "woff");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  765 */   public static final MediaType FONT_WOFF2 = createConstant("font", "woff2"); private final String type;
/*      */   private final String subtype;
/*      */   private final ImmutableListMultimap<String, String> parameters;
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private String toString;
/*      */   @LazyInit
/*      */   private int hashCode;
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private Optional<Charset> parsedCharset;
/*      */   
/*      */   private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters) {
/*  778 */     this.type = type;
/*  779 */     this.subtype = subtype;
/*  780 */     this.parameters = parameters;
/*      */   }
/*      */ 
/*      */   
/*      */   public String type() {
/*  785 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   public String subtype() {
/*  790 */     return this.subtype;
/*      */   }
/*      */ 
/*      */   
/*      */   public ImmutableListMultimap<String, String> parameters() {
/*  795 */     return this.parameters;
/*      */   }
/*      */   
/*      */   private Map<String, ImmutableMultiset<String>> parametersAsMap() {
/*  799 */     return Maps.transformValues((Map)this.parameters.asMap(), ImmutableMultiset::copyOf);
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
/*      */   public Optional<Charset> charset() {
/*  812 */     Optional<Charset> local = this.parsedCharset;
/*  813 */     if (local == null) {
/*  814 */       String value = null;
/*  815 */       local = Optional.absent();
/*  816 */       for (UnmodifiableIterator<String> unmodifiableIterator = this.parameters.get("charset").iterator(); unmodifiableIterator.hasNext(); ) { String currentValue = unmodifiableIterator.next();
/*  817 */         if (value == null) {
/*  818 */           value = currentValue;
/*  819 */           local = Optional.of(Charset.forName(value)); continue;
/*  820 */         }  if (!value.equals(currentValue)) {
/*  821 */           throw new IllegalStateException("Multiple charset values defined: " + value + ", " + currentValue);
/*      */         } }
/*      */ 
/*      */       
/*  825 */       this.parsedCharset = local;
/*      */     } 
/*  827 */     return local;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withoutParameters() {
/*  835 */     return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withParameters(Multimap<String, String> parameters) {
/*  844 */     return create(this.type, this.subtype, parameters);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withParameters(String attribute, Iterable<String> values) {
/*  855 */     Preconditions.checkNotNull(attribute);
/*  856 */     Preconditions.checkNotNull(values);
/*  857 */     String normalizedAttribute = normalizeToken(attribute);
/*  858 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/*  859 */     for (UnmodifiableIterator<Map.Entry<String, String>> unmodifiableIterator = this.parameters.entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<String, String> entry = unmodifiableIterator.next();
/*  860 */       String key = entry.getKey();
/*  861 */       if (!normalizedAttribute.equals(key)) {
/*  862 */         builder.put(key, entry.getValue());
/*      */       } }
/*      */     
/*  865 */     for (String value : values) {
/*  866 */       builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
/*      */     }
/*  868 */     MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
/*      */     
/*  870 */     if (!normalizedAttribute.equals("charset")) {
/*  871 */       mediaType.parsedCharset = this.parsedCharset;
/*      */     }
/*      */     
/*  874 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
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
/*      */   public MediaType withParameter(String attribute, String value) {
/*  886 */     return withParameters(attribute, (Iterable<String>)ImmutableSet.of(value));
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
/*      */   public MediaType withCharset(Charset charset) {
/*  899 */     Preconditions.checkNotNull(charset);
/*  900 */     MediaType withCharset = withParameter("charset", charset.name());
/*      */     
/*  902 */     withCharset.parsedCharset = Optional.of(charset);
/*  903 */     return withCharset;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasWildcard() {
/*  908 */     return ("*".equals(this.type) || "*".equals(this.subtype));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean is(MediaType mediaTypeRange) {
/*  941 */     return ((mediaTypeRange.type.equals("*") || mediaTypeRange.type.equals(this.type)) && (mediaTypeRange.subtype
/*  942 */       .equals("*") || mediaTypeRange.subtype.equals(this.subtype)) && this.parameters
/*  943 */       .entries().containsAll((Collection)mediaTypeRange.parameters.entries()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MediaType create(String type, String subtype) {
/*  953 */     MediaType mediaType = create(type, subtype, (Multimap<String, String>)ImmutableListMultimap.of());
/*  954 */     mediaType.parsedCharset = Optional.absent();
/*  955 */     return mediaType;
/*      */   }
/*      */ 
/*      */   
/*      */   private static MediaType create(String type, String subtype, Multimap<String, String> parameters) {
/*  960 */     Preconditions.checkNotNull(type);
/*  961 */     Preconditions.checkNotNull(subtype);
/*  962 */     Preconditions.checkNotNull(parameters);
/*  963 */     String normalizedType = normalizeToken(type);
/*  964 */     String normalizedSubtype = normalizeToken(subtype);
/*  965 */     Preconditions.checkArgument((
/*  966 */         !"*".equals(normalizedType) || "*".equals(normalizedSubtype)), "A wildcard type cannot be used with a non-wildcard subtype");
/*      */     
/*  968 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/*  969 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)parameters.entries()) {
/*  970 */       String attribute = normalizeToken(entry.getKey());
/*  971 */       builder.put(attribute, normalizeParameterValue(attribute, entry.getValue()));
/*      */     } 
/*  973 */     MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
/*      */     
/*  975 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createApplicationType(String subtype) {
/*  984 */     return create("application", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createAudioType(String subtype) {
/*  993 */     return create("audio", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createFontType(String subtype) {
/* 1002 */     return create("font", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createImageType(String subtype) {
/* 1011 */     return create("image", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createTextType(String subtype) {
/* 1020 */     return create("text", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createVideoType(String subtype) {
/* 1029 */     return create("video", subtype);
/*      */   }
/*      */   
/*      */   private static String normalizeToken(String token) {
/* 1033 */     Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
/* 1034 */     Preconditions.checkArgument(!token.isEmpty());
/* 1035 */     return Ascii.toLowerCase(token);
/*      */   }
/*      */   
/*      */   private static String normalizeParameterValue(String attribute, String value) {
/* 1039 */     Preconditions.checkNotNull(value);
/* 1040 */     Preconditions.checkArgument(CharMatcher.ascii().matchesAllOf(value), "parameter values must be ASCII: %s", value);
/* 1041 */     return "charset".equals(attribute) ? Ascii.toLowerCase(value) : value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static MediaType parse(String input) {
/* 1051 */     Preconditions.checkNotNull(input);
/* 1052 */     Tokenizer tokenizer = new Tokenizer(input);
/*      */     try {
/* 1054 */       String type = tokenizer.consumeToken(TOKEN_MATCHER);
/* 1055 */       consumeSeparator(tokenizer, '/');
/* 1056 */       String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
/* 1057 */       ImmutableListMultimap.Builder<String, String> parameters = ImmutableListMultimap.builder();
/* 1058 */       while (tokenizer.hasMore()) {
/* 1059 */         String value; consumeSeparator(tokenizer, ';');
/* 1060 */         String attribute = tokenizer.consumeToken(TOKEN_MATCHER);
/* 1061 */         consumeSeparator(tokenizer, '=');
/*      */         
/* 1063 */         if ('"' == tokenizer.previewChar()) {
/* 1064 */           tokenizer.consumeCharacter('"');
/* 1065 */           StringBuilder valueBuilder = new StringBuilder();
/* 1066 */           while ('"' != tokenizer.previewChar()) {
/* 1067 */             if ('\\' == tokenizer.previewChar()) {
/* 1068 */               tokenizer.consumeCharacter('\\');
/* 1069 */               valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ascii())); continue;
/*      */             } 
/* 1071 */             valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
/*      */           } 
/*      */           
/* 1074 */           value = valueBuilder.toString();
/* 1075 */           tokenizer.consumeCharacter('"');
/*      */         } else {
/* 1077 */           value = tokenizer.consumeToken(TOKEN_MATCHER);
/*      */         } 
/* 1079 */         parameters.put(attribute, value);
/*      */       } 
/* 1081 */       return create(type, subtype, (Multimap<String, String>)parameters.build());
/* 1082 */     } catch (IllegalStateException e) {
/* 1083 */       throw new IllegalArgumentException("Could not parse '" + input + "'", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void consumeSeparator(Tokenizer tokenizer, char c) {
/* 1088 */     tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/* 1089 */     tokenizer.consumeCharacter(c);
/* 1090 */     tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/*      */   }
/*      */   
/*      */   private static final class Tokenizer {
/*      */     final String input;
/* 1095 */     int position = 0;
/*      */     
/*      */     Tokenizer(String input) {
/* 1098 */       this.input = input;
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     String consumeTokenIfPresent(CharMatcher matcher) {
/* 1103 */       Preconditions.checkState(hasMore());
/* 1104 */       int startPosition = this.position;
/* 1105 */       this.position = matcher.negate().indexIn(this.input, startPosition);
/* 1106 */       return hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
/*      */     }
/*      */     
/*      */     String consumeToken(CharMatcher matcher) {
/* 1110 */       int startPosition = this.position;
/* 1111 */       String token = consumeTokenIfPresent(matcher);
/* 1112 */       Preconditions.checkState((this.position != startPosition));
/* 1113 */       return token;
/*      */     }
/*      */     
/*      */     char consumeCharacter(CharMatcher matcher) {
/* 1117 */       Preconditions.checkState(hasMore());
/* 1118 */       char c = previewChar();
/* 1119 */       Preconditions.checkState(matcher.matches(c));
/* 1120 */       this.position++;
/* 1121 */       return c;
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     char consumeCharacter(char c) {
/* 1126 */       Preconditions.checkState(hasMore());
/* 1127 */       Preconditions.checkState((previewChar() == c));
/* 1128 */       this.position++;
/* 1129 */       return c;
/*      */     }
/*      */     
/*      */     char previewChar() {
/* 1133 */       Preconditions.checkState(hasMore());
/* 1134 */       return this.input.charAt(this.position);
/*      */     }
/*      */     
/*      */     boolean hasMore() {
/* 1138 */       return (this.position >= 0 && this.position < this.input.length());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(@CheckForNull Object obj) {
/* 1144 */     if (obj == this)
/* 1145 */       return true; 
/* 1146 */     if (obj instanceof MediaType) {
/* 1147 */       MediaType that = (MediaType)obj;
/* 1148 */       return (this.type.equals(that.type) && this.subtype
/* 1149 */         .equals(that.subtype) && 
/*      */         
/* 1151 */         parametersAsMap().equals(that.parametersAsMap()));
/*      */     } 
/* 1153 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1160 */     int h = this.hashCode;
/* 1161 */     if (h == 0) {
/* 1162 */       h = Objects.hashCode(new Object[] { this.type, this.subtype, parametersAsMap() });
/* 1163 */       this.hashCode = h;
/*      */     } 
/* 1165 */     return h;
/*      */   }
/*      */   
/* 1168 */   private static final Joiner.MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1177 */     String result = this.toString;
/* 1178 */     if (result == null) {
/* 1179 */       result = computeToString();
/* 1180 */       this.toString = result;
/*      */     } 
/* 1182 */     return result;
/*      */   }
/*      */   
/*      */   private String computeToString() {
/* 1186 */     StringBuilder builder = (new StringBuilder()).append(this.type).append('/').append(this.subtype);
/* 1187 */     if (!this.parameters.isEmpty()) {
/* 1188 */       builder.append("; ");
/*      */       
/* 1190 */       ListMultimap listMultimap = Multimaps.transformValues((ListMultimap)this.parameters, value -> 
/*      */ 
/*      */           
/* 1193 */           (TOKEN_MATCHER.matchesAllOf(value) && !value.isEmpty()) ? value : escapeAndQuote(value));
/*      */ 
/*      */       
/* 1196 */       PARAMETER_JOINER.appendTo(builder, listMultimap.entries());
/*      */     } 
/* 1198 */     return builder.toString();
/*      */   }
/*      */   
/*      */   private static String escapeAndQuote(String value) {
/* 1202 */     StringBuilder escaped = (new StringBuilder(value.length() + 16)).append('"');
/* 1203 */     for (int i = 0; i < value.length(); i++) {
/* 1204 */       char ch = value.charAt(i);
/* 1205 */       if (ch == '\r' || ch == '\\' || ch == '"') {
/* 1206 */         escaped.append('\\');
/*      */       }
/* 1208 */       escaped.append(ch);
/*      */     } 
/* 1210 */     return escaped.append('"').toString();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/net/MediaType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */