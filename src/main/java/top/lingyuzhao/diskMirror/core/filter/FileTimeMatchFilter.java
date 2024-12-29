package top.lingyuzhao.diskMirror.core.filter;

import top.lingyuzhao.utils.dataContainer.KeyValue;
import top.lingyuzhao.utils.transformation.Transformation;

/**
 * 文件修改时间匹配过滤器
 *
 * @author 赵凌宇
 */
public class FileTimeMatchFilter implements Transformation<KeyValue<Long, String>, Boolean> {

    public final long ms;

    /**
     * 构造函数
     *
     * @param timeMs 匹配时间阈值 ms，文件的修改时间小于此值，则返回true
     */
    public FileTimeMatchFilter(long timeMs) {
        ms = timeMs;
    }

    /**
     * @param file 来自内部的待转换数据。
     *             Data to be converted from inside.
     * @return 如果指定的文件的修改时间小于指定的时间，则返回true。
     * If the modification time of the specified file is less than the specified time, return true.
     */
    @Override
    public Boolean function(KeyValue<Long, String> file) {
        return file.getKey() < ms;
    }
}
