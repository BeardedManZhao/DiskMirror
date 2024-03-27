package top.lingyuzhao.diskMirror.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.function.Consumer;

/**
 * @author zhao
 */
public class JsonUtils {

    /**
     * 将两个 json 对象进行合并操作。
     *
     * @param json1      需要被合并的 json 对象 1
     * @param json2      需要被合并的 json 对象 2
     * @param mergedJson 合并之后的结果存储的 json 对象
     * @return 合并之后的新的 json 对象
     */
    public static JSONObject mergeJsonTrees(JSONObject json1, JSONObject json2, JSONObject mergedJson) {

        for (String key : json1.keySet()) {
            if (json2.containsKey(key)) {
                final Object o = json1.get(key);
                final Object o1 = json2.get(key);
                if (o instanceof JSONObject && o1 instanceof JSONObject) {
                    mergedJson.put(key, mergeJsonTrees((JSONObject) o, (JSONObject) o1, new JSONObject()));
                } else if (o instanceof JSONArray && o1 instanceof JSONArray) {
                    ((JSONArray) o).addAll((JSONArray) o1);
                }
            } else {
                mergedJson.put(key, json1.get(key));
            }
        }

        for (String key : json2.keySet()) {
            if (!json1.containsKey(key)) {
                mergedJson.put(key, json2.get(key));
            }
        }

        return mergedJson;
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
