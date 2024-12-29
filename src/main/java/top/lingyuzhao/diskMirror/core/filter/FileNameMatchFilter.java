package top.lingyuzhao.diskMirror.core.filter;

import top.lingyuzhao.utils.CacheUtils;
import top.lingyuzhao.utils.dataContainer.KeyValue;
import top.lingyuzhao.utils.transformation.Transformation;

import java.util.regex.Pattern;

/**
 * 文件名称匹配过滤器
 *
 * @author 赵凌宇
 */
public class FileNameMatchFilter implements Transformation<KeyValue<Long, String>, Boolean> {


    private final static CacheUtils cacheUtils = CacheUtils.getCacheUtils("FileNameMatchFilter", 1000 * 60 * 60 * 24);

    private final Pattern pattern;

    private FileNameMatchFilter(String match) {
        this.pattern = Pattern.compile(match);
    }

    public static FileNameMatchFilter getInstance(String match) {
        Object o = cacheUtils.get(match);
        if (o == null) {
            o = new FileNameMatchFilter(match);
            cacheUtils.put(match, o);
        }
        return (FileNameMatchFilter) o;
    }

    @Override
    public Boolean function(KeyValue<Long, String> file) {
        // 提取出文件名
        return pattern.matcher(file.getValue()).matches();
    }
}
