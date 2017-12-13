package cn.meiauto.matnetwork.download;

/**
 * <pre>
 *  author : LiYang
 *  email  : yang.li@meiauto.cn
 *  time   : 2017-08-24
 */
public class DownloadState {
    public static final int DEFAULT = 0;
    public static final int START = 1;
    public static final int DOWNLOADING = 2;
    public static final int PAUSE = 3;
    public static final int CANCEL = 4;
    public static final int FINISH = 5;
    public static final int ERROR = 6;
}
