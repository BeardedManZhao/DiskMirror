package top.lingyuzhao.diskMirror.core.function;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.core.FSAdapter;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 回滚使用空间
 *
 * @author 赵凌宇
 */
public class UseSizeRollBack implements Consumer<JSONObject> {

    private final FSAdapter fsAdapter;
    private final int userId;
    private final String type;
    private final Long rollBackSize;


    public UseSizeRollBack(FSAdapter fsAdapter, int userId, String type, Long rollBackSize) {
        this.fsAdapter = fsAdapter;
        this.userId = userId;
        this.type = type;
        this.rollBackSize = rollBackSize;
    }

    /**
     * Performs this operation on the given argument.
     *
     * @param jsonObject the input argument
     */
    @Override
    public void accept(JSONObject jsonObject) {
        try {
            jsonObject.put("useSize", this.fsAdapter.diffUseSize(this.userId, this.type, this.rollBackSize));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
