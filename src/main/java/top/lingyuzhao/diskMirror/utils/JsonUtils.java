package top.lingyuzhao.diskMirror.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * @author zhao
 */
public class JsonUtils {

    /**
     * 将两个 json 对象进行合并操作。
     *
     * @param json1 需要被合并的 json 对象 1
     * @param json2 需要被合并的 json 对象 2
     */
    public static void mergeJsonTrees(JSONObject json1, JSONObject json2) {
        final JSONArray jsonArray = json1.getJSONArray("urls");
        final JSONArray jsonArray2 = json2.getJSONArray("urls");
        if (jsonArray == null) {
            if (jsonArray2 != null) {
                json1.putAll(json2);
            }
            return;
        }
        if (jsonArray2 == null) return;
        // 准备一个index 表，记录 json1 中所有的 目录名称 以及 对应的 urls 数组
        final HashMap<String, JSONObject> hashMap = new HashMap<>();
        for (Object o : jsonArray) {
            if (o instanceof JSONObject) {
                final String string = ((JSONObject) o).getString("fileName");
                if (string == null) {
                    continue;
                }
                final JSONArray jsonArray1 = ((JSONObject) o).getJSONArray("urls");
                if (jsonArray1 != null) {
                    hashMap.put(string, (JSONObject) o);
                }
            }
        }
        // 代表 json2 有数据
        for (Object o : jsonArray2) {
            // 查看当前文件对象是否存在于 json1 中
            if (o instanceof JSONObject) {
                final String string = ((JSONObject) o).getString("fileName");
                if (hashMap.containsKey(string)) {
                    // 代表 json1 中有一个同名的文件夹，直接将同名文件夹的 json对象 和 当前的对象进行合并
                    mergeJsonTrees(hashMap.get(string), (JSONObject) o);
                    continue;
                }
                // 代表 json1 中没有同名的文件夹，直接将当前对象添加到 json1 的 urls 中
                jsonArray.add(o);
            }
        }
    }

    /**
     * 将一个 嵌套的 json 转换为 使用前缀的列表
     *
     * @param jsonObject 需要被转换的 json 对象
     * @param listKey    需要被拆为列表的 key
     * @param pKey       前缀k
     * @param result     转换结果的处理函数
     * @param pValue     前缀值
     * @param splitChar  分隔符
     */
    public static void jsonToList(JSONObject jsonObject, String listKey, String pKey, Consumer<String> result, String pValue, char splitChar) {
        final JSONArray jsonArray = jsonObject.getJSONArray(listKey);
        if (jsonArray == null) {
            // 直接追加
            result.accept(pValue + splitChar + jsonObject.getString("fileName"));
            return;
        }
        // 首先获取到前缀
        String string1 = jsonObject.getString(pKey);
        final String s;
        if (string1 == null) {
            s = pValue;
        } else {
            s = pValue + splitChar + string1;
        }

        // 直接追加
        result.accept(s);
        for (Object o : jsonArray) {
            if (o instanceof JSONObject) {
                jsonToList((JSONObject) o, listKey, pKey, result, s, splitChar);
            }
        }
    }

}
