package top.lingyuzhao.diskMirror.core;

import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;

/**
 * 盘镜 门户类 在这里您可以非常方便获取到您需要的适配器
 * <p>
 * Here, you can easily obtain the adapter you need for the Panjing Portal class
 *
 * @author zhao lingyu 此项目中使用的别名 zhao
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
            return "\n        <dependency>\n" +
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
    },

    /**
     * DM-DFS 分布式集群存储适配器，此适配器可以实现将多个子适配器统一管理，实现多个适配器一起进行数据存储的效果！
     * <p>
     * DM-DFS distributed storage adapter, this adapter can achieve unified management of multiple sub adapters, achieving the effect of multiple adapters storing data together!
     */
    DM_DfsAdapter {
        @Override
        protected DM_DfsAdapter getAdapterInDM(Config config) {
            return new DM_DfsAdapter(config);
        }

        @Override
        protected String needDependent() {
            return "----";
        }
    },
    TCP_Adapter {
        @Override
        protected String needDependent() {
            return "";
        }

        @Override
        protected AdapterPacking getAdapterPackingInDM(DiskMirror diskMirror, Config config, Config adapterConfig) {
            return new TcpAdapter(diskMirror, config, adapterConfig);
        }
    },
    TCP_CLIENT_Adapter {
        @Override
        protected String needDependent() {
            return "";
        }

        @Override
        protected Adapter getAdapterInDM(Config config) {
            return new TcpClientAdapter(config);
        }
    };

    /**
     * 当前盘镜库的版本
     * <p>
     * The current version of the disk mirror library
     */
    public final static String VERSION = "1.4.4";

    /**
     * 获取到当前 盘镜 的版本 以及 适配器的名称
     *
     * @return 盘镜 适配器版本
     */
    public String getVersion() {
        return
                "+--------------------------------------------------+\n" +
                        "| https://github.com/BeardedManZhao/DiskMirror.git |\n" +
                        "+--------------------------------------------------+\n" +
                        "     \\   _     _  \n" +
                        "      \\ (c).-.(c) \n" +
                        "         / ._. \\  \n" +
                        "       __\\( Y )/__\n" +
                        "      (_.-/'-'\\-._) " + this + "\n" +
                        "    ____  _      __   __  ____                 ____ \n" +
                        "   / __ \\(_)____/ /__/  |/  (_)_____________  / __ \\\n" +
                        "  / / / / / ___/ //_/ /|_/ / / ___/ ___/ __ \\/ /_/ /\n" +
                        " / /_/ / (__  ) ,< / /  / / / /  / /  / /_/ / _, _/ \n" +
                        "/_____/_/____/_/|_/_/  /_/_/_/  /_/   \\____/_/ |_|  \n";
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
    protected Adapter getAdapterInDM(Config config) {
        throw new UnsupportedOperationException(this + " is not a valid adapter, it is an adapter wrapper. Please obtain the adapter wrapper class!");
    }


    /**
     * 构建一个适配器包装类，此类可以直接调用其所包装的适配器的方法，能够有效的实现将各种适配器对象接入到 FSAdapter 中。
     * <p>
     * Build an adapter wrapper class that can directly call the methods of the adapter it wraps, effectively integrating various adapter objects into FSAdapters.
     *
     * @param diskMirror    需要获取到的适配器对应的枚举对象！
     *                      <p>
     *                      The enumeration object corresponding to the adapter that needs to be obtained!
     * @param config        当前适配器包装类的配置类！
     * @param adapterConfig 当前包装类中要包装的适配器的配置类，请注意，此配置类并非是 AdapterPacking 的配置类！
     *                      <p>
     *                      The configuration class of the adapter to be wrapped in the current wrapper class. Please note that this configuration class is not the configuration class of AdapterPacking!
     * @return 一个包装类，能够直接调用其包装的适配器的方法，能够有效的实现将各种适配器对象接入到 FSAdapter 中。
     * <p>
     * A wrapper class that can directly call the methods of its wrapped adapters and effectively integrate various adapter objects into FSAdapters.
     */
    protected AdapterPacking getAdapterPackingInDM(DiskMirror diskMirror, Config config, Config adapterConfig) {
        throw new UnsupportedOperationException(this + " is an adapter, not a wrapper class. Please directly obtain the adapter object!");
    }

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
                            this.needDependent(), e
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

    public AdapterPacking getAdapterPacking(DiskMirror diskMirror, Config config, Config adapterConfig) {
        try {
            return this.getAdapterPackingInDM(diskMirror, config, adapterConfig);
        } catch (NoClassDefFoundError e) {
            throw new UnsupportedOperationException(
                    "不支持您进行【" + super.toString() + "】适配器的实例化操作，因为您的项目中缺少必须的依赖，下面是依赖信息\nYou are not supported to instantiate the [" + super.toString() + "] adapter because your project lacks the necessary dependencies. Here is the dependency information\n" +
                            this.needDependent() + "\n+-----------------------+'\n" + diskMirror.needDependent()
            );
        }
    }

    public AdapterPacking getAdapterPacking(DiskMirror diskMirror, Class<?> configMain, Class<?> adapterConfigMain) {
        try {
            final DiskMirrorConfig annotation0 = configMain.getAnnotation(DiskMirrorConfig.class);
            final DiskMirrorConfig annotation1 = adapterConfigMain.getAnnotation(DiskMirrorConfig.class);
            return this.getAdapterPacking(diskMirror, new Config(annotation0), new Config(annotation1));
        } catch (NoClassDefFoundError e) {
            throw new UnsupportedOperationException(
                    "不支持您进行【" + super.toString() + "】适配器的实例化操作，因为您的项目中缺少必须的依赖，下面是依赖信息\nYou are not supported to instantiate the [" + super.toString() + "] adapter because your project lacks the necessary dependencies. Here is the dependency information\n" +
                            this.needDependent() + "+-----------------------+" + diskMirror.needDependent()
            );
        }
    }

    @Override
    public String toString() {
        return super.toString() + ":" + VERSION;
    }

}
