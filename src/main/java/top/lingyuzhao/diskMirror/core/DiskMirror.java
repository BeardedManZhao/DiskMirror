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
    public final static String VERSION = "1.0.5";


    /**
     * 获取到当前 盘镜 的版本 以及 适配器的名称
     *
     * @return 盘镜 适配器版本
     */
    public String getVersion() {
        return this + ":" + VERSION;
    }

    /**
     * 获取到适配器对象
     *
     * @param config 配置类
     * @return 适配器对象 能够用于管理磁盘文件
     */
    public abstract Adapter getAdapter(Config config);
}
