package top.lingyuzhao.cloudPool.core;

import top.lingyuzhao.cloudPool.conf.Config;

public enum DiskMirror {
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
    };

    /**
     * 获取到适配器对象
     *
     * @param config 配置类
     * @return 适配器对象 能够用于管理磁盘文件
     */
    public abstract Adapter getAdapter(Config config);
}
