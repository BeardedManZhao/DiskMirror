package top.lingyuzhao.diskMirror.core.filter;

import top.lingyuzhao.utils.dataContainer.KeyValue;
import top.lingyuzhao.utils.transformation.Transformation;

/**
 * 文件过滤器 内部使用
 *
 * @author 赵凌宇
 */
public enum FileMatchManager {

    /**
     * 允许所有文件通过！
     */
    ALLOW_ALL(file -> Boolean.TRUE, false) {
        @Override
        public Transformation<KeyValue<Long, String>, Boolean> getOrNew(String match) {
            return this.filter;
        }
    },
    /**
     * 文件名 正则匹配
     */
    FILE_NAME_MATCH(null, true) {
        @Override
        public Transformation<KeyValue<Long, String>, Boolean> getOrNew(String match) {
            return FileNameMatchFilter.getInstance(match);
        }
    },

    /**
     * 文件时间戳 阈值匹配
     */
    FILE_TIME_MATCH(null, true) {
        @Override
        public Transformation<KeyValue<Long, String>, Boolean> getOrNew(String match) {
            return new FileTimeMatchFilter(Long.parseLong(match));
        }
    };

    protected final Transformation<KeyValue<Long, String>, Boolean> filter;

    protected final boolean allowDirNoDelete;

    FileMatchManager(Transformation<KeyValue<Long, String>, Boolean> filter, boolean allowDirNoDelete) {
        this.filter = filter;
        this.allowDirNoDelete = allowDirNoDelete;
    }

    /**
     * 获取一个文件过滤器
     *
     * @param match 文件过滤表达式 不同的过滤器类型有不同的语法~
     * @return 创建出来的或直接获取到的 过滤器 输入的 key 是文件 size value 是文件名
     */
    public abstract Transformation<KeyValue<Long, String>, Boolean> getOrNew(String match);

    /**
     * @return 是否允许目录在删除后有剩余文件，如果允许则在最后删除目录不成功的时候不会报错
     */
    public boolean allowDirNoDelete() {
        return this.allowDirNoDelete;
    }
}
