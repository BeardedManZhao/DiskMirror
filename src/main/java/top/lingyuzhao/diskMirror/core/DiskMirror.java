package top.lingyuzhao.diskMirror.core;

import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;

/**
 * 盘镜 门户类 在这里您可以非常方便获取到您需要的适配器
 * <p>
 * Here, you can easily obtain the adapter you need for the Panjing Portal class
 *
 * @author zhao lingyu
 */
public enum DiskMirror {

    /**
     * 本地文件系统适配器
     * <p>
     * Local file system adapter
     */
    LocalFSAdapter {
        /**
         * 获取到适配器对象
         *
         * @param config 配置类
         * @return 适配器对象 能够用于管理磁盘文件
         */
        @Override
        public Adapter getAdapterInDM(Config config) {
            return new LocalFSAdapter(config);
        }

        /**
         * @return 能够返回出当前的配置类中需要的依赖包，以 xml 展示。
         * <p>
         * The dependency package required in the current configuration class can be returned and displayed in XML.
         */
        @Override
        protected String needDependent() {
            return "        <dependency>\n" +
                    "            <groupId>com.alibaba.fastjson2</groupId>\n" +
                    "            <artifactId>fastjson2</artifactId>\n" +
                    "            <version>x.x.x</version>\n" +
                    "        </dependency>\n" +
                    "        <dependency>\n" +
                    "            <groupId>io.github.BeardedManZhao</groupId>\n" +
                    "            <artifactId>zhao-utils</artifactId>\n" +
                    "            <version>x.x.x</version>\n" +
                    "        </dependency>\n";
        }
    },

    /**
     * HDFS 分布式文件系统适配器，通过此适配器您可以对接到 HDFS 中。
     * <p>
     * HDFS distributed file system adapter, through which you can connect to HDFS.
     */
    HDFSAdapter {
        /**
         * 获取到适配器对象
         *
         * @param config 配置类
         * @return 适配器对象 能够用于管理磁盘文件
         */
        @Override
        public Adapter getAdapterInDM(Config config) {
            return new HDFSAdapter(config);
        }

        /**
         * @return 能够返回出当前的配置类中需要的依赖包，以 xml 展示。
         * <p>
         * The dependency package required in the current configuration class can be returned and displayed in XML.
         */
        @Override
        protected String needDependent() {
            return LocalFSAdapter.needDependent() + "        <!-- 如果您要使用 HDFSAdapter 请添加Hadoop核心库 -->\n" +
                    "        <dependency>\n" +
                    "            <groupId>org.apache.hadoop</groupId>\n" +
                    "            <artifactId>hadoop-client</artifactId>\n" +
                    "            <version>x.x.x</version>\n" +
                    "        </dependency>";
        }
    },

    /**
     * DiskMirror 后端服务器 适配器 您可以直接通过此适配器操作一个远程的 diskMirror 后端服务器。
     * <p>
     * The DiskMirror backend server adapter allows you to directly operate a remote diskMirror backend server through this adapter.
     */
    DiskMirrorHttpAdapter {
        /**
         * 通过配置类，创建出对应的适配器对象
         * <p>
         * Create the corresponding adapter object by configuring the class
         *
         * @param config 配置类
         * @return 适配器对象 能够用于管理磁盘文件
         * <p>
         * The adapter object can be used to manage disk files
         */
        @Override
        public Adapter getAdapterInDM(Config config) {
            return this.getAdapter(config, "FsCrud", null);
        }

        /**
         * @return 能够返回出当前的配置类中需要的依赖包，以 xml 展示。
         * <p>
         * The dependency package required in the current configuration class can be returned and displayed in XML.
         */
        @Override
        protected String needDependent() {
            return LocalFSAdapter.needDependent() + "        <!-- 如果您要使用 DiskMirrorHttpAdapter 请添加 httpClient 核心库 -->\n" +
                    "        <dependency>\n" +
                    "            <groupId>org.apache.httpcomponents</groupId>\n" +
                    "            <artifactId>httpclient</artifactId>\n" +
                    "            <version>x.x.x</version>\n" +
                    "        </dependency>\n" +
                    "        <dependency>\n" +
                    "            <groupId>org.apache.httpcomponents</groupId>\n" +
                    "            <artifactId>httpmime</artifactId>\n" +
                    "            <version>x.x.x</version>\n" +
                    "        </dependency>";
        }

        /**
         * 通过配置类，创建出对应的适配器对象
         * <p>
         * Create the corresponding adapter object by configuring the class
         * @param config 配置类
         * @param controller 远程服务器中 用于处理diskMirror 的控制器名称
         * <p>
         *                   The name of the controller used to handle diskMirror in the remote server
         * @param httpClient 如果您已经创建了一个 http 客户端，您可以传入此客户端对象。如果没有可以直接设置未 null
         * <p>
         *                   If you have already created an HTTP client, you can pass in this client object. If not, you can directly set non null
         * @return 适配器对象 能够用于管理磁盘文件
         * <p>
         * The adapter object can be used to manage disk files
         */
        public Adapter getAdapter(Config config, String controller, Object httpClient) {
            return new DiskMirrorHttpAdapter(config, controller, httpClient);
        }
    };

    /**
     * 当前盘镜库的版本
     * <p>
     * The current version of the disk mirror library
     */
    public final static String VERSION = "1.1.4";

    /**
     * 获取到当前 盘镜 的版本 以及 适配器的名称
     *
     * @return 盘镜 适配器版本
     */
    public String getVersion() {
        return "             'WWWKXXXXNWWNk,     ,kkd7               KWWb,                     \n" +
                "             ;WWN3.....,lNWWk.                       KWWb,                     \n" +
                "             ;WWNl        XWWk.  :XXk,   oKNNWNKo    KWWb,   dXXO:             \n" +
                "             ;WWNl  ^  ^  3WWX7  7WWO7  0WWo:,:O0d,  KWWb, lNWKb:              \n" +
                "             ;WWNl  -__-  :WWNl  7WWO7  0WWO,.       KWWbbXWKb:.               \n" +
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
                this;
    }

    /**
     * 通过配置类，创建出对应的适配器对象
     * <p>
     * Create the corresponding adapter object by configuring the class
     *
     * @param config 配置类
     * @return 适配器对象 能够用于管理磁盘文件
     * <p>
     * The adapter object can be used to manage disk files
     */
    protected abstract Adapter getAdapterInDM(Config config);

    /**
     * @return 能够返回出当前的配置类中需要的依赖包，以 xml 展示。
     * <p>
     * The dependency package required in the current configuration class can be returned and displayed in XML.
     */
    protected abstract String needDependent();

    /**
     * 通过配置类，创建出对应的适配器对象
     * <p>
     * Create the corresponding adapter object by configuring the class
     *
     * @param config 配置类
     * @return 适配器对象 能够用于管理磁盘文件
     * <p>
     * The adapter object can be used to manage disk files
     */
    public Adapter getAdapter(Config config) {
        try {
            return this.getAdapterInDM(config);
        } catch (NoClassDefFoundError e) {
            throw new UnsupportedOperationException(
                    "不支持您进行【" + super.toString() + "】适配器的实例化操作，因为您的项目中缺少必须的依赖，下面是依赖信息\nYou are not supported to instantiate the [" + super.toString() + "] adapter because your project lacks the necessary dependencies. Here is the dependency information\n" +
                            this.needDependent()
            );
        }
    }

    /**
     * 通过配置类，创建出对应的适配器对象
     * <p>
     * Create the corresponding adapter object by configuring the class
     *
     * @param configClass 被 DiskMirrorConfig 注解的类对象
     * @return 适配器对象 能够用于管理磁盘文件
     * <p>
     * The adapter object can be used to manage disk files
     */
    public Adapter getAdapter(Class<?> configClass) {
        final DiskMirrorConfig annotation = configClass.getAnnotation(DiskMirrorConfig.class);
        if (annotation != null) {
            return getAdapter(new Config(annotation));
        }
        throw new UnsupportedOperationException(configClass.getTypeName() + " Not find annotation: @DiskMirrorConfig");
    }

    @Override
    public String toString() {
        return super.toString() + ":" + VERSION;
    }

}
