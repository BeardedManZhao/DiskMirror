package top.lingyuzhao.diskMirror.utils;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.utils.progressEvent.ProgressFileNumber;

import java.util.HashMap;

/**
 * 进度条对象，用于描述文件上传进度的状态对象。
 * <p>
 * Progress bar object, used to describe the status object of file upload progress.
 *
 * @author 赵凌宇 别名 zhao
 */
public class ProgressBar extends ProgressFileNumber {
    private final static HashMap<String, JSONObject> hash = new HashMap<>();

    /**
     * 当前进度条所属的空间id
     */
    private final String spaceId;

    /**
     * 当前进度条的 id
     */
    private final String progressId;

    public ProgressBar(String spaceId, String progressId) {
        this.spaceId = spaceId;
        this.progressId = progressId;
    }

    /**
     * @param spaceId 要获取到的进度对应的空间id，此函数会将您指定的id 中的所有的文件进度对象提取出来！
     *                <p>
     *                To obtain the spatial ID corresponding to the progress, this function will extract all the file progress objects in the ID you specified!
     * @return 当前进度条对象所属空间的进度条对象集合
     * <p>
     * The collection of progress bar objects in the space to which the current progress bar object belongs
     */
    public static JSONObject getBySpaceId(String spaceId) {
        return hash.computeIfAbsent(spaceId, k -> new JSONObject());
    }

    /**
     * @return 当前进度条对象的所属空间的 id
     */
    public String getSpaceId() {
        return spaceId;
    }

    /**
     * @return 当前进度条对象在空间内 自己的id
     */
    public String getProgressId() {
        return progressId;
    }

    @Override
    public void function1(Integer type) {
        getBySpaceId(this.spaceId).put(this.progressId, this);
    }

    @Override
    public void function2(Integer type) {
        count += type;
    }

    @Override
    public void function3(Integer type) {
        if (type <= 0) {
            // 代表完毕或出现了错误 清理列表
            final JSONObject jsonObject = hash.get(this.spaceId);
            if (jsonObject != null) {
                jsonObject.remove(this.progressId);
            }
        } else {
            // 其它情况就回调
            this.function2(type);
        }
    }

    /**
     * 获取当前进度条对象的进度
     *
     * @return 目前写入成功的字节数
     */
    public long getCount() {
        return this.count;
    }

    /**
     * 获取当前进度条对象的最大进度
     *
     * @return 当前进度条一共需要写入的字节数（是所有的！）
     */
    public double getMaxCount() {
        return this.maxSize;
    }
}
