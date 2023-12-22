package top.lingyuzhao.diskMirror.core;

import top.lingyuzhao.diskMirror.conf.Config;

/**
 * 盘镜 门户类 在这里您可以非常方便获取到您需要的适配器
 */
public enum DiskMirror {

    /**
     * 本地文件系统适配器
     */
    LocalFSAdapter {
        /**
         * 获取到适配器对象
         *
         * @param config 配置类
         * @return 适配器对象 能够用于管理磁盘文件
         */
        @Override
        public Adapter getAdapter(Config config) {
            return new LocalFSAdapter(config);
        }
    },
    HDFSAdapter {
        /**
         * 获取到适配器对象
         *
         * @param config 配置类
         * @return 适配器对象 能够用于管理磁盘文件
         */
        @Override
        public Adapter getAdapter(Config config) {
            return new HDFSAdapter(config);
        }
    };

    /**
     * 当前盘镜的版本
     */
    public final static String VERSION = "1.0.7";

    /**
     * 获取到当前 盘镜 的版本 以及 适配器的名称
     *
     * @return 盘镜 适配器版本
     */
    public String getVersion() {
        return "             'WWWKXXXXNWWNk,     ,kkd7               KWWb,                     \n" +
                "             ;WWN3.....,lNWWk.                       KWWb,                     \n" +
                "             ;WWNl        XWWk.  :XXk,   oKNNWNKo    KWWb,   dXXO:             \n" +
                "             ;WWNl        3WWX7  7WWO7  0WWo:,:O0d,  KWWb, lNWKb:              \n" +
                "             ;WWNl        :WWNl  7WWO7  0WWO,.       KWWbbXWKb:.               \n" +
                "             ;WWNl        kWW03  7WWO7   lXWWWN0o.   KWWNWWW0;                 \n" +
                "             ;WWNl       lWWNo,  7WWO7     .,7dWWN;  KWWOolWWN7                \n" +
                "             'WWNo,..,'oXWWKo'   7WWO7 .lb:    XWNl. KWWb, .KWWk.              \n" +
                "             ;WWWWWWWWWNKOo:.    7WWO7  oNWX0KWWKb:  KWWb,   bWWX'             \n" +
                "              ,'''''''',,.        ,'',    ,;777;,.    '''.    .''',            \n" +
                "KWWNWK,        ,WWNWWd.   ;33:                                                 \n" +
                "KWWbWWO.       XWXkWWd.   ...    ...  .,,   ...  ,,.      .,,,,        ...  .,,\n" +
                "KWWodWWd      OWNlOWWd.  .WWN7   KWW3OWNWl.:WWOlNWNO:  3KWWXXNWWXo.   ,WWX3XWNK\n" +
                "KWWo.OWWo    oWWb;xWWd.  .WWXl   0WWXkl',, ;WWNKb:,,, XWWkl,..,oWWN'  ,WWNKd7,,\n" +
                "KWWo  XWN7  ;WWx3 dWWd.  .WWXl   0WWO3     ;WWWl,    bWW03      OWWk, ,WWWo'   \n" +
                "KWWo  ,NWK',NW0l  dWWd.  .WWXl   0WWd,     ;WWX3     kWWO:      dWMO: ,WWNl    \n" +
                "KWWo   ;WWkKWXl.  dWWd.  .WWXl   0WWd.     ;WWK7     7WWX7      XWWd; ,WWN3    \n" +
                "KWWo    lWWWNo,   dWWd.  .WWXl   0WWd.     ;WWK7      oWWX3,.,7XWWk3  ,WWN3    \n" +
                "kXXo     dXXd:    oXXb.  .KX0l   xXXb.     'KXO7       .o0XNNNXKkl'   .KXKl    \n" +
                this + ":" + VERSION;
    }

    /**
     * 获取到适配器对象
     *
     * @param config 配置类
     * @return 适配器对象 能够用于管理磁盘文件
     */
    public abstract Adapter getAdapter(Config config);

}
