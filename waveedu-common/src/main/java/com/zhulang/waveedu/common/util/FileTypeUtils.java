package com.zhulang.waveedu.common.util;

import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型工具类
 *
 * @author 狐狸半面添
 * @create 2023-02-09 0:13
 */
public class FileTypeUtils {
//    private static final Map<String, String> suffixMap = new HashMap<>();

//    /**
//     * 根据 mimetype 类型推断后缀名
//     *
//     * @param mimeType mimetype类型
//     * @return 所有可能的后缀名【数组】
//     */
//    public static String[] getSuffixesByMime(String mimeType) {
//        String suffixes = suffixMap.get(mimeType);
//        if (suffixes == null) {
//            return null;
//        } else {
//            return WaveStrUtils.strSplitToArr(suffixes, ",");
//        }
//    }

    /**
     * 获取文件的 mime 类型
     *
     * @param file 文件
     * @return mime类型
     */
    public static String getMimeType(File file) {
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<MediaType, Parser>());
        Metadata metadata = new Metadata();
        metadata.add(TikaCoreProperties.RESOURCE_NAME_KEY, file.getName());
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }

    /**
     * 根据 mimetype 获取文件的简单类型
     *
     * @param mimeType mime类型
     * @return 简单类型：文本，图片，音频，视频，其它
     */
    public static String getSimpleType(String mimeType) {
        String simpleType = mimeType.split("/")[0];
        switch (simpleType) {
            case "text":
                return "文本";
            case "image":
                return "图片";
            case "audio":
                return "音频";
            case "video":
                return "视频";
            case "application":
                return "其它";
            default:
                throw new RuntimeException("mimeType格式错误");
        }
    }

    public static void main(String[] args) {
        File file = new File("D:\\location语法规则.docx");
        String mimeType = getMimeType(file);
        System.out.println(mimeType);
        System.out.println(getSimpleType(mimeType));
    }

//    static {
//        suffixMap.put("text/html", ".html,.load,.htm");
//        suffixMap.put("application/vnd.lotus-1-2-3", ".123,.wk1,.wk3,.wk4");
//        suffixMap.put("image/x-3ds", ".3ds");
//        suffixMap.put("video/3gpp", ".3g2,.3ga,.3gp,.3gpp");
//        suffixMap.put("application/x-t602", ".602");
//        suffixMap.put("audio/x-mod", ".669,.m15,.med,.mod,.mtm,.ult,.uni");
//        suffixMap.put("application/x-7z-compressed", ".7z");
//        suffixMap.put("application/x-archive", ".a");
//        suffixMap.put("audio/mp4", ".aac,.m4a");
//        suffixMap.put("application/x-abiword", ".abw,.abw.crashed,.abw.gz,.zabw");
//        suffixMap.put("audio/ac3", ".ac3");
//        suffixMap.put("application/x-ace", ".ace");
//        suffixMap.put("text/x-adasrc", ".adb,.ads");
//        suffixMap.put("application/x-font-afm", ".afm");
//        suffixMap.put("image/x-applix-graphics", ".ag");
//        suffixMap.put("application/illustrator", ".ai");
//        suffixMap.put("audio/x-aiff", ".aif,.aifc,.aiff");
//        suffixMap.put("application/x-perl", ".al,.perl,.pl,.pm");
//        suffixMap.put("application/x-alz", ".alz");
//        suffixMap.put("audio/amr", ".amr");
//        suffixMap.put("application/x-navi-animation", ".ani");
//        suffixMap.put("video/x-anim", ".anim[1-9j]");
//        suffixMap.put("application/annodex", ".anx");
//        suffixMap.put("audio/x-ape", ".ape");
//        suffixMap.put("application/x-arj", ".arj");
//        suffixMap.put("image/x-sony-arw", ".arw");
//        suffixMap.put("application/x-applix-spreadsheet", ".as");
//        suffixMap.put("text/plain", ".txt,.asc");
//        suffixMap.put("video/x-ms-asf", ".asf");
//        suffixMap.put("application/x-asp", ".asp");
//        suffixMap.put("text/x-ssa", ".ass,.ssa");
//        suffixMap.put("audio/x-ms-asx", ".asx,.wax,.wmx,.wvx");
//        suffixMap.put("application/atom+xml", ".atom");
//        suffixMap.put("audio/basic", ".au,.snd");
//        suffixMap.put("video/x-msvideo", ".avi,.divx");
//        suffixMap.put("application/x-applix-word", ".aw");
//        suffixMap.put("audio/amr-wb", ".awb");
//        suffixMap.put("application/x-awk", ".awk");
//        suffixMap.put("audio/annodex", ".axa");
//        suffixMap.put("video/annodex", ".axv");
//        suffixMap.put("application/x-trash", ".bak,.old,.sik");
//        suffixMap.put("application/x-bcpio", ".bcpio");
//        suffixMap.put("application/x-font-bdf", ".bdf");
//        suffixMap.put("text/x-bibtex", ".bib");
//        suffixMap.put("application/octet-stream", ".bin,.gen");
//        suffixMap.put("application/x-blender", ".blend,.blender");
//        suffixMap.put("image/bmp", ".bmp");
//        suffixMap.put("application/x-bzip", ".bz,.bz2");
//        suffixMap.put("text/x-csrc", ".c");
//        suffixMap.put("text/x-c++src", ".cpp,.cc,.c++,.cxx");
//        suffixMap.put("application/vnd.ms-cab-compressed", ".cab");
//        suffixMap.put("application/x-cb7", ".cb7");
//        suffixMap.put("application/x-cbr", ".cbr");
//        suffixMap.put("application/x-cbt", ".cbt");
//        suffixMap.put("application/x-cbz", ".cbz");
//        suffixMap.put("application/x-netcdf", ".cdf,.nc");
//        suffixMap.put("application/vnd.corel-draw", ".cdr");
//        suffixMap.put("application/x-x509-ca-cert", ".cer,.cert,.crt,.der,.pem");
//        suffixMap.put("image/cgm", ".cgm");
//        suffixMap.put("application/x-chm", ".chm");
//        suffixMap.put("application/x-kchart", ".chrt");
//        suffixMap.put("application/x-java", ".class");
//        suffixMap.put("text/x-tex", ".cls,.dtx,.ins,.latex,.ltx,.sty,.tex");
//        suffixMap.put("text/x-cmake", ".cmake");
//        suffixMap.put("application/x-cpio", ".cpio");
//        suffixMap.put("application/x-cpio-compressed", ".cpio.gz");
//        suffixMap.put("image/x-canon-cr2", ".cr2");
//        suffixMap.put("image/x-canon-crw", ".crw");
//        suffixMap.put("text/x-csharp", ".cs");
//        suffixMap.put("application/x-csh", ".csh");
//        suffixMap.put("text/css", ".css,.cssl");
//        suffixMap.put("text/csv", ".csv");
//        suffixMap.put("application/x-cue", ".cue");
//        suffixMap.put("image/x-win-bitmap", ".cur");
//        suffixMap.put("text/x-dsrc", ".d");
//        suffixMap.put("application/x-dar", ".dar");
//        suffixMap.put("application/x-dbf", ".dbf");
//        suffixMap.put("application/x-dc-rom", ".dc");
//        suffixMap.put("text/x-dcl", ".dcl");
//        suffixMap.put("application/dicom", ".dcm");
//        suffixMap.put("image/x-kodak-dcr", ".dcr");
//        suffixMap.put("image/x-dds", ".dds");
//        suffixMap.put("application/x-deb", ".deb");
//        suffixMap.put("application/x-desktop", ".desktop,.kdelnk");
//        suffixMap.put("application/x-dia-diagram", ".dia");
//        suffixMap.put("text/x-patch", ".diff,.patch");
//        suffixMap.put("image/vnd.djvu", ".djv,.djvu");
//        suffixMap.put("image/x-adobe-dng", ".dng");
//        suffixMap.put("application/msword", ".doc");
//        suffixMap.put("application/docbook+xml", ".docbook");
//        suffixMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx,.docm");
//        suffixMap.put("text/vnd.graphviz", ".dot,.gv");
//        suffixMap.put("text/x-dsl", ".dsl");
//        suffixMap.put("application/xml-dtd", ".dtd");
//        suffixMap.put("video/dv", ".dv");
//        suffixMap.put("application/x-dvi", ".dvi");
//        suffixMap.put("application/x-bzdvi", ".dvi.bz2");
//        suffixMap.put("application/x-gzdvi", ".dvi.gz");
//        suffixMap.put("image/vnd.dwg", ".dwg");
//        suffixMap.put("image/vnd.dxf", ".dxf");
//        suffixMap.put("text/x-eiffel", ".e,.eif");
//        suffixMap.put("application/x-egon", ".egon");
//        suffixMap.put("text/x-emacs-lisp", ".el");
//        suffixMap.put("image/x-emf", ".emf");
//        suffixMap.put("application/vnd.emusic-emusic_package", ".emp");
//        suffixMap.put("application/xml-external-parsed-entity", ".ent");
//        suffixMap.put("image/x-eps", ".eps,.epsf,.epsi");
//        suffixMap.put("image/x-bzeps", ".eps.bz2,.epsf.bz2,.epsi.bz2");
//        suffixMap.put("image/x-gzeps", ".eps.gz,.epsf.gz,.epsi.gz");
//        suffixMap.put("application/epub+zip", ".epub");
//        suffixMap.put("text/x-erlang", ".erl");
//        suffixMap.put("application/ecmascript", ".es");
//        suffixMap.put("application/x-e-theme", ".etheme");
//        suffixMap.put("text/x-setext", ".etx");
//        suffixMap.put("application/x-ms-dos-executable", ".exe");
//        suffixMap.put("image/x-exr", ".exr");
//        suffixMap.put("application/andrew-inset", ".ez");
//        suffixMap.put("text/x-fortran", ".f,.f90,.f95,.for");
//        suffixMap.put("application/x-fictionbook+xml", ".fb2");
//        suffixMap.put("image/x-xfig", ".fig");
//        suffixMap.put("image/fits", ".fits");
//        suffixMap.put("application/x-fluid", ".fl");
//        suffixMap.put("audio/x-flac", ".flac");
//        suffixMap.put("video/x-flic", ".flc,.fli");
//        suffixMap.put("video/x-flv", ".flv");
//        suffixMap.put("application/x-kivio", ".flw");
//        suffixMap.put("text/x-xslfo", ".fo,.xslfo");
//        suffixMap.put("image/fax-g3", ".g3");
//        suffixMap.put("application/x-gameboy-rom", ".gb");
//        suffixMap.put("application/x-gba-rom", ".gba");
//        suffixMap.put("text/directory", ".gcrd,.vcf,.vct");
//        suffixMap.put("text/x-web-markdown", ".md");
//        suffixMap.put("application/x-gedcom", ".ged,.gedcom");
//        suffixMap.put("application/x-genesis-rom", ".gen");
//        suffixMap.put("application/x-tex-gf", ".gf");
//        suffixMap.put("application/x-sms-rom", ".gg,.sms");
//        suffixMap.put("image/gif", ".gif");
//        suffixMap.put("application/x-glade", ".glade");
//        suffixMap.put("application/x-gettext-translation", ".gmo,.mo");
//        suffixMap.put("application/x-gnucash", ".gnc,.gnucash,.xac");
//        suffixMap.put("application/gnunet-directory", ".gnd");
//        suffixMap.put("application/x-gnumeric", ".gnumeric");
//        suffixMap.put("application/x-gnuplot", ".gnuplot,.gp,.gplt");
//        suffixMap.put("application/pgp-encrypted", ".gpg,.pgp");
//        suffixMap.put("application/x-graphite", ".gra");
//        suffixMap.put("application/x-font-type1", ".gsf,.pfa,.pfb");
//        suffixMap.put("audio/x-gsm", ".gsm");
//        suffixMap.put("application/x-tar", ".gtar,.tar");
//        suffixMap.put("text/x-google-video-pointer", ".gvp");
//        suffixMap.put("application/x-gzip", ".gz");
//        suffixMap.put("text/x-chdr", ".h");
//        suffixMap.put("text/x-c++hdr", ".h++,.hh,.hp,.hpp,.hxx");
//        suffixMap.put("application/x-hdf", ".hdf");
//        suffixMap.put("application/vnd.hp-hpgl", ".hpgl");
//        suffixMap.put("text/x-haskell", ".hs");
//        suffixMap.put("application/x-hwp", ".hwp");
//        suffixMap.put("application/x-hwt", ".hwt");
//        suffixMap.put("application/x-ica", ".ica");
//        suffixMap.put("image/x-tga", ".icb,.tga,.tpic,.vda,.vst");
//        suffixMap.put("image/x-icns", ".icns");
//        suffixMap.put("image/vnd.microsoft.icon", ".ico");
//        suffixMap.put("text/calendar", ".ics,.vcs");
//        suffixMap.put("text/x-idl", ".idl");
//        suffixMap.put("image/ief", ".ief");
//        suffixMap.put("image/x-iff", ".iff");
//        suffixMap.put("image/x-ilbm", ".ilbm");
//        suffixMap.put("text/x-imelody", ".ime,.imy");
//        suffixMap.put("text/x-iptables", ".iptables");
//        suffixMap.put("application/x-cd-image", ".iso,.iso9660");
//        suffixMap.put("audio/x-it", ".it");
//        suffixMap.put("image/jp2", ".j2k,.jp2,.jpc,.jpf,.jpx");
//        suffixMap.put("text/vnd.sun.j2me.app-descriptor", ".jad");
//        suffixMap.put("application/x-java-archive", ".jar");
//        suffixMap.put("text/x-java", ".java");
//        suffixMap.put("image/x-jng", ".jng");
//        suffixMap.put("application/x-java-jnlp-file", ".jnlp");
//        suffixMap.put("image/jpeg", ".jpg,.jpeg,.jpe");
//        suffixMap.put("application/x-jbuilder-project", ".jpr");
//        suffixMap.put("application/javascript", ".js");
//        suffixMap.put("application/json", ".json");
//        suffixMap.put("application/jsonp", ".jsonp");
//        suffixMap.put("image/x-kodak-k25", ".k25");
//        suffixMap.put("audio/midi", ".kar,.mid,.midi");
//        suffixMap.put("application/x-karbon", ".karbon");
//        suffixMap.put("image/x-kodak-kdc", ".kdc");
//        suffixMap.put("application/x-kexiproject-sqlite3", ".kexi");
//        suffixMap.put("application/x-kexi-connectiondata", ".kexic");
//        suffixMap.put("application/x-kexiproject-shortcut", ".kexis");
//        suffixMap.put("application/x-kformula", ".kfo");
//        suffixMap.put("application/x-killustrator", ".kil");
//        suffixMap.put("application/smil", ".kino,.smil,.sml");
//        suffixMap.put("application/vnd.google-earth.kml+xml", ".kml");
//        suffixMap.put("application/vnd.google-earth.kmz", ".kmz");
//        suffixMap.put("application/x-kontour", ".kon");
//        suffixMap.put("application/x-kpovmodeler", ".kpm");
//        suffixMap.put("application/x-kpresenter", ".kpr,.kpt");
//        suffixMap.put("application/x-krita", ".kra");
//        suffixMap.put("application/x-kspread", ".ksp");
//        suffixMap.put("application/x-kugar", ".kud");
//        suffixMap.put("application/x-kword", ".kwd,.kwt");
//        suffixMap.put("application/x-shared-library-la", ".la");
//        suffixMap.put("text/x-ldif", ".ldif");
//        suffixMap.put("application/x-lha", ".lha,.lzh");
//        suffixMap.put("text/x-literate-haskell", ".lhs");
//        suffixMap.put("application/x-lhz", ".lhz");
//        suffixMap.put("text/x-log", ".log");
//        suffixMap.put("text/x-lua", ".lua");
//        suffixMap.put("image/x-lwo", ".lwo,.lwob");
//        suffixMap.put("image/x-lws", ".lws");
//        suffixMap.put("text/x-lilypond", ".ly");
//        suffixMap.put("application/x-lyx", ".lyx");
//        suffixMap.put("application/x-lzip", ".lz");
//        suffixMap.put("application/x-lzma", ".lzma");
//        suffixMap.put("application/x-lzop", ".lzo");
//        suffixMap.put("text/x-matlab", ".m");
//        suffixMap.put("video/mpeg", ".m2t,.mp2,.mpe,.mpeg,.mpg,.vob");
//        suffixMap.put("audio/x-mpegurl", ".m3u,.m3u8,.vlc");
//        suffixMap.put("application/x-m4", ".m4");
//        suffixMap.put("audio/x-m4b", ".m4b");
//        suffixMap.put("video/mp4", ".mp4,.m4v");
//        suffixMap.put("application/x-markaby", ".mab");
//        suffixMap.put("application/x-troff-man", ".man");
//        suffixMap.put("application/mbox", ".mbox");
//        suffixMap.put("application/vnd.ms-access", ".mdb");
//        suffixMap.put("image/vnd.ms-modi", ".mdi");
//        suffixMap.put("text/x-troff-me", ".me");
//        suffixMap.put("application/metalink+xml", ".metalink");
//        suffixMap.put("application/x-magicpoint", ".mgp");
//        suffixMap.put("application/x-mif", ".mif");
//        suffixMap.put("audio/x-minipsf", ".minipsf");
//        suffixMap.put("audio/x-matroska", ".mka");
//        suffixMap.put("video/x-matroska", ".mkv");
//        suffixMap.put("text/x-ocaml", ".ml,.mli");
//        suffixMap.put("text/x-troff-mm", ".mm");
//        suffixMap.put("application/x-smaf", ".mmf,.smaf");
//        suffixMap.put("text/mathml", ".mml");
//        suffixMap.put("video/x-mng", ".mng");
//        suffixMap.put("audio/x-mo3", ".mo3");
//        suffixMap.put("text/x-moc", ".moc");
//        suffixMap.put("text/x-mof", ".mof");
//        suffixMap.put("video/quicktime", ".moov,.mov,.qt,.qtvr");
//        suffixMap.put("video/x-sgi-movie", ".movie");
//        suffixMap.put("audio/x-musepack", ".mp+,.mpc,.mpp");
//        suffixMap.put("audio/mpeg", ".mp3,.mpga");
//        suffixMap.put("text/x-mrml", ".mrl,.mrml");
//        suffixMap.put("image/x-minolta-mrw", ".mrw");
//        suffixMap.put("text/x-troff-ms", ".ms");
//        suffixMap.put("application/x-msi", ".msi");
//        suffixMap.put("image/x-msod", ".msod");
//        suffixMap.put("application/x-msx-rom", ".msx");
//        suffixMap.put("text/x-mup", ".mup,.not");
//        suffixMap.put("application/mxf", ".mxf");
//        suffixMap.put("application/x-n64-rom", ".n64");
//        suffixMap.put("application/mathematica", ".nb");
//        suffixMap.put("application/x-nintendo-ds-rom", ".nds");
//        suffixMap.put("image/x-nikon-nef", ".nef");
//        suffixMap.put("application/x-nes-rom", ".nes");
//        suffixMap.put("text/x-nfo", ".nfo");
//        suffixMap.put("application/x-netshow-channel", ".nsc");
//        suffixMap.put("video/x-nsv", ".nsv");
//        suffixMap.put("application/x-object", ".o");
//        suffixMap.put("application/x-tgif", ".obj");
//        suffixMap.put("text/x-ocl", ".ocl");
//        suffixMap.put("application/oda", ".oda");
//        suffixMap.put("application/vnd.oasis.opendocument.database", ".odb");
//        suffixMap.put("application/vnd.oasis.opendocument.chart", ".odc");
//        suffixMap.put("application/vnd.oasis.opendocument.formula", ".odf");
//        suffixMap.put("application/vnd.oasis.opendocument.graphics", ".odg");
//        suffixMap.put("application/vnd.oasis.opendocument.image", ".odi");
//        suffixMap.put("application/vnd.oasis.opendocument.text-master", ".odm");
//        suffixMap.put("application/vnd.oasis.opendocument.presentation", ".odp");
//        suffixMap.put("application/vnd.oasis.opendocument.spreadsheet", ".ods");
//        suffixMap.put("application/vnd.oasis.opendocument.text", ".odt");
//        suffixMap.put("audio/ogg", ".oga");
//        suffixMap.put("video/x-theora+ogg", ".ogg");
//        suffixMap.put("video/x-ogm+ogg", ".ogm");
//        suffixMap.put("video/ogg", ".ogv");
//        suffixMap.put("application/ogg", ".ogx");
//        suffixMap.put("application/x-oleo", ".oleo");
//        suffixMap.put("text/x-opml+xml", ".opml");
//        suffixMap.put("image/openraster", ".ora");
//        suffixMap.put("image/x-olympus-orf", ".orf");
//        suffixMap.put("application/vnd.oasis.opendocument.chart-template", ".otc");
//        suffixMap.put("application/x-font-otf", ".otf");
//        suffixMap.put("application/vnd.oasis.opendocument.graphics-template", ".otg");
//        suffixMap.put("application/vnd.oasis.opendocument.text-web", ".oth");
//        suffixMap.put("application/vnd.oasis.opendocument.presentation-template", ".otp");
//        suffixMap.put("application/vnd.oasis.opendocument.spreadsheet-template", ".ots");
//        suffixMap.put("application/vnd.oasis.opendocument.text-template", ".ott");
//        suffixMap.put("application/rdf+xml", ".owl,.rdf,.rdfs");
//        suffixMap.put("application/vnd.openofficeorg.extension", ".oxt");
//        suffixMap.put("text/x-pascal", ".p,.pas");
//        suffixMap.put("application/pkcs10", ".p10");
//        suffixMap.put("application/x-pkcs12", ".p12,.pfx");
//        suffixMap.put("application/x-pkcs7-certificates", ".p7b,.spc");
//        suffixMap.put("application/pkcs7-signature", ".p7s");
//        suffixMap.put("application/x-java-pack200", ".pack");
//        suffixMap.put("application/x-pak", ".pak");
//        suffixMap.put("application/x-par2", ".par2");
//        suffixMap.put("image/x-portable-bitmap", ".pbm");
//        suffixMap.put("image/x-photo-cd", ".pcd");
//        suffixMap.put("application/x-cisco-vpn-settings", ".pcf");
//        suffixMap.put("application/x-font-pcf", ".pcf.gz,.pcf.z");
//        suffixMap.put("application/vnd.hp-pcl", ".pcl");
//        suffixMap.put("image/x-pcx", ".pcx");
//        suffixMap.put("chemical/x-pdb", ".pdb,.xyz");
//        suffixMap.put("application/x-aportisdoc", ".pdc");
//        suffixMap.put("application/pdf", ".pdf");
//        suffixMap.put("application/x-bzpdf", ".pdf.bz2");
//        suffixMap.put("application/x-gzpdf", ".pdf.gz");
//        suffixMap.put("image/x-pentax-pef", ".pef");
//        suffixMap.put("image/x-portable-graymap", ".pgm");
//        suffixMap.put("application/x-chess-pgn", ".pgn");
//        suffixMap.put("application/x-php", ".php,.php3,.php4");
//        suffixMap.put("image/x-pict", ".pict,.pict1,.pict2");
//        suffixMap.put("application/python-pickle", ".pickle");
//        suffixMap.put("application/x-tex-pk", ".pk");
//        suffixMap.put("application/pkix-pkipath", ".pkipath");
//        suffixMap.put("application/pgp-keys", ".pkr,.skr");
//        suffixMap.put("audio/x-iriver-pla", ".pla");
//        suffixMap.put("application/x-planperfect", ".pln");
//        suffixMap.put("audio/x-scpls", ".pls");
//        suffixMap.put("image/png", ".png");
//        suffixMap.put("image/x-portable-anymap", ".pnm");
//        suffixMap.put("image/x-macpaint", ".pntg");
//        suffixMap.put("text/x-gettext-translation", ".po");
//        suffixMap.put("application/x-spss-por", ".por");
//        suffixMap.put("text/x-gettext-translation-template", ".pot");
//        suffixMap.put("image/x-portable-pixmap", ".ppm");
//        suffixMap.put("application/vnd.ms-powerpoint", ".ppt,.pps,.ppz");
//        suffixMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx,.pptm");
//        suffixMap.put("application/x-palm-database", ".prc");
//        suffixMap.put("application/postscript", ".ps");
//        suffixMap.put("application/x-bzpostscript", ".ps.bz2");
//        suffixMap.put("application/x-gzpostscript", ".ps.gz");
//        suffixMap.put("image/vnd.adobe.photoshop", ".psd");
//        suffixMap.put("audio/x-psf", ".psf");
//        suffixMap.put("application/x-gz-font-linux-psf", ".psf.gz");
//        suffixMap.put("audio/x-psflib", ".psflib");
//        suffixMap.put("audio/prs.sid", ".psid,.sid");
//        suffixMap.put("application/x-pocket-word", ".psw");
//        suffixMap.put("application/x-pw", ".pw");
//        suffixMap.put("text/x-python", ".py");
//        suffixMap.put("application/x-python-bytecode", ".pyc,.pyo");
//        suffixMap.put("image/x-quicktime", ".qif,.qtif");
//        suffixMap.put("application/x-quicktime-media-link", ".qtl");
//        suffixMap.put("audio/vnd.rn-realaudio", ".ra,.rax");
//        suffixMap.put("image/x-fuji-raf", ".raf");
//        suffixMap.put("application/ram", ".ram");
//        suffixMap.put("application/x-rar", ".rar");
//        suffixMap.put("image/x-cmu-raster", ".ras");
//        suffixMap.put("image/x-panasonic-raw", ".raw");
//        suffixMap.put("application/x-ruby", ".rb");
//        suffixMap.put("text/x-ms-regedit", ".reg");
//        suffixMap.put("application/x-reject", ".rej");
//        suffixMap.put("image/x-rgb", ".rgb");
//        suffixMap.put("image/rle", ".rle");
//        suffixMap.put("application/vnd.rn-realmedia", ".rm,.rmj,.rmm,.rms,.rmvb,.rmx");
//        suffixMap.put("text/troff", ".roff,.t,.tr");
//        suffixMap.put("image/vnd.rn-realpix", ".rp");
//        suffixMap.put("application/x-rpm", ".rpm");
//        suffixMap.put("application/rss+xml", ".rss");
//        suffixMap.put("text/vnd.rn-realtext", ".rt");
//        suffixMap.put("application/rtf", ".rtf");
//        suffixMap.put("text/richtext", ".rtx");
//        suffixMap.put("video/vnd.rn-realvideo", ".rv,.rvx");
//        suffixMap.put("audio/x-s3m", ".s3m");
//        suffixMap.put("application/x-amipro", ".sam");
//        suffixMap.put("application/x-sami", ".sami,.smi");
//        suffixMap.put("application/x-spss-sav", ".sav");
//        suffixMap.put("text/x-scheme", ".scm");
//        suffixMap.put("application/vnd.stardivision.draw", ".sda");
//        suffixMap.put("application/vnd.stardivision.calc", ".sdc");
//        suffixMap.put("application/vnd.stardivision.impress", ".sdd");
//        suffixMap.put("application/sdp", ".sdp");
//        suffixMap.put("application/vnd.stardivision.chart", ".sds");
//        suffixMap.put("application/vnd.stardivision.writer", ".sdw,.sgl,.vor");
//        suffixMap.put("application/x-go-sgf", ".sgf");
//        suffixMap.put("image/x-sgi", ".sgi");
//        suffixMap.put("text/sgml", ".sgm,.sgml");
//        suffixMap.put("application/x-shellscript", ".sh");
//        suffixMap.put("application/x-shar", ".shar");
//        suffixMap.put("application/x-shorten", ".shn");
//        suffixMap.put("application/x-siag", ".siag");
//        suffixMap.put("application/vnd.symbian.install", ".sis");
//        suffixMap.put("x-epoc/x-sisx-app", ".sisx");
//        suffixMap.put("application/x-stuffit", ".sit");
//        suffixMap.put("application/sieve", ".siv");
//        suffixMap.put("image/x-skencil", ".sk,.sk1");
//        suffixMap.put("text/spreadsheet", ".slk,.sylk");
//        suffixMap.put("application/x-snes-rom", ".smc");
//        suffixMap.put("application/vnd.stardivision.mail", ".smd");
//        suffixMap.put("application/vnd.stardivision.math", ".smf");
//        suffixMap.put("application/x-sharedlib", ".so");
//        suffixMap.put("application/x-font-speedo", ".spd");
//        suffixMap.put("text/x-rpm-spec", ".spec");
//        suffixMap.put("application/x-shockwave-flash", ".spl,.swf");
//        suffixMap.put("audio/x-speex", ".spx");
//        suffixMap.put("text/x-sql", ".sql");
//        suffixMap.put("image/x-sony-sr2", ".sr2");
//        suffixMap.put("application/x-wais-source", ".src");
//        suffixMap.put("image/x-sony-srf", ".srf");
//        suffixMap.put("application/x-subrip", ".srt");
//        suffixMap.put("application/vnd.sun.xml.calc.template", ".stc");
//        suffixMap.put("application/vnd.sun.xml.draw.template", ".std");
//        suffixMap.put("application/vnd.sun.xml.impress.template", ".sti");
//        suffixMap.put("audio/x-stm", ".stm");
//        suffixMap.put("application/vnd.sun.xml.writer.template", ".stw");
//        suffixMap.put("text/x-subviewer", ".sub");
//        suffixMap.put("image/x-sun-raster", ".sun");
//        suffixMap.put("application/x-sv4cpio", ".sv4cpio");
//        suffixMap.put("application/x-sv4crc", ".sv4crc");
//        suffixMap.put("image/svg+xml", ".svg");
//        suffixMap.put("image/svg+xml-compressed", ".svgz");
//        suffixMap.put("application/vnd.sun.xml.calc", ".sxc");
//        suffixMap.put("application/vnd.sun.xml.draw", ".sxd");
//        suffixMap.put("application/vnd.sun.xml.writer.global", ".sxg");
//        suffixMap.put("application/vnd.sun.xml.impress", ".sxi");
//        suffixMap.put("application/vnd.sun.xml.math", ".sxm");
//        suffixMap.put("application/vnd.sun.xml.writer", ".sxw");
//        suffixMap.put("text/x-txt2tags", ".t2t");
//        suffixMap.put("application/x-bzip-compressed-tar", ".tar.bz,.tar.bz2,.tbz,.tbz2");
//        suffixMap.put("application/x-compressed-tar", ".tar.gz,.tgz");
//        suffixMap.put("application/x-lzma-compressed-tar", ".tar.lzma,.tlz");
//        suffixMap.put("application/x-tzo", ".tar.lzo,.tzo");
//        suffixMap.put("application/x-xz-compressed-tar", ".tar.xz,.txz");
//        suffixMap.put("application/x-tarz", ".tar.z");
//        suffixMap.put("text/x-tcl", ".tcl,.tk");
//        suffixMap.put("text/x-texinfo", ".texi,.texinfo");
//        suffixMap.put("application/x-theme", ".theme");
//        suffixMap.put("application/x-windows-themepack", ".themepack");
//        suffixMap.put("image/tiff", ".tif,.tiff");
//        suffixMap.put("application/vnd.ms-tnef", ".tnef,.tnf");
//        suffixMap.put("application/x-cdrdao-toc", ".toc");
//        suffixMap.put("application/x-bittorrent", ".torrent");
//        suffixMap.put("application/x-linguist", ".ts");
//        suffixMap.put("text/tab-separated-values", ".tsv");
//        suffixMap.put("audio/x-tta", ".tta");
//        suffixMap.put("application/x-font-ttf", ".ttc,.ttf");
//        suffixMap.put("application/x-font-ttx", ".ttx");
//        suffixMap.put("application/x-ufraw", ".ufraw");
//        suffixMap.put("application/x-designer", ".ui");
//        suffixMap.put("text/x-uil", ".uil");
//        suffixMap.put("text/x-uri", ".uri,.url");
//        suffixMap.put("application/x-ustar", ".ustar");
//        suffixMap.put("text/x-vala", ".vala,.vapi");
//        suffixMap.put("text/x-vhdl", ".vhd,.vhdl");
//        suffixMap.put("video/vivo", ".viv,.vivo");
//        suffixMap.put("audio/x-voc", ".voc");
//        suffixMap.put("audio/x-wav", ".wav");
//        suffixMap.put("application/x-quattropro", ".wb1,.wb2,.wb3");
//        suffixMap.put("image/vnd.wap.wbmp", ".wbmp");
//        suffixMap.put("application/vnd.ms-works", ".wcm,.wdb,.wks,.wps");
//        suffixMap.put("video/webm", ".webm");
//        suffixMap.put("audio/x-ms-wma", ".wma");
//        suffixMap.put("image/x-wmf", ".wmf");
//        suffixMap.put("text/vnd.wap.wml", ".wml");
//        suffixMap.put("text/vnd.wap.wmlscript", ".wmls");
//        suffixMap.put("video/x-ms-wmv", ".wmv");
//        suffixMap.put("application/vnd.wordperfect", ".wp,.wp4,.wp5,.wp6,.wpd,.wpp");
//        suffixMap.put("application/x-wpg", ".wpg");
//        suffixMap.put("application/vnd.ms-wpl", ".wpl");
//        suffixMap.put("application/x-mswrite", ".wri");
//        suffixMap.put("model/vrml", ".wrl");
//        suffixMap.put("audio/x-wavpack", ".wv,.wvp");
//        suffixMap.put("audio/x-wavpack-correction", ".wvc");
//        suffixMap.put("image/x-sigma-x3f", ".x3f");
//        suffixMap.put("application/x-xbel", ".xbel");
//        suffixMap.put("application/xml", ".xbl,.xml,.xsl,.xslt");
//        suffixMap.put("image/x-xbitmap", ".xbm");
//        suffixMap.put("image/x-xcf", ".xcf");
//        suffixMap.put("image/x-compressed-xcf", ".xcf.bz2,.xcf.gz");
//        suffixMap.put("application/xhtml+xml", ".xhtml");
//        suffixMap.put("audio/x-xi", ".xi");
//        suffixMap.put("application/vnd.ms-excel", ".xla,.xlc,.xld,.xll,.xlm,.xls,.xlt,.xlw");
//        suffixMap.put("application/x-xliff", ".xlf,.xliff");
//        suffixMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsm,.xlsx");
//        suffixMap.put("audio/x-xm", ".xm");
//        suffixMap.put("audio/x-xmf", ".xmf");
//        suffixMap.put("text/x-xmi", ".xmi");
//        suffixMap.put("image/x-xpixmap", ".xpm");
//        suffixMap.put("application/vnd.ms-xpsdocument", ".xps");
//        suffixMap.put("application/xspf+xml", ".xspf");
//        suffixMap.put("application/vnd.mozilla.xul+xml", ".xul");
//        suffixMap.put("image/x-xwindowdump", ".xwd");
//        suffixMap.put("application/x-xz", ".xz");
//        suffixMap.put("application/w2p", ".w2p");
//        suffixMap.put("application/x-compress", ".z");
//        suffixMap.put("application/zip", ".zip");
//    }
}
