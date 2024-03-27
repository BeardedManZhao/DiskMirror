package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.utils.JsonUtils;


/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        // 获取到两个json
        final JSONObject jsonObject1 = JSONObject.parseObject("{\"userId\":1,\"type\":\"Binary\",\"secure.key\":2139494269,\"okSize\":2,\"adapterSize\":2,\"fileName\":\"/test/diskMirror.js\",\"useSize\":21586,\"useAgreement\":true,\"maxSize\":134217728,\"urls\":[{\"fileName\":\"test\",\"url\":\"http://192.168.0.105:8080//1/Binary//test\",\"lastModified\":1711542798359,\"size\":0,\"type\":\"Binary\",\"isDir\":true,\"urls\":[{\"fileName\":\"application.yaml\",\"url\":\"http://192.168.0.105:8080//1/Binary//test/application.yaml\",\"lastModified\":1711542798296,\"size\":1437,\"type\":\"Binary\",\"isDir\":false},{\"fileName\":\"diskMirror.js\",\"url\":\"http://192.168.0.105:8080//1/Binary//test/diskMirror.js\",\"lastModified\":1711542798359,\"size\":20149,\"type\":\"Binary\",\"isDir\":false}]}],\"res\":\"ok!!!!\"}\n");
        final JSONObject jsonObject2 = JSONObject.parseObject("{\"userId\":1,\"type\":\"Binary\",\"secure.key\":2139494269,\"okSize\":2,\"adapterSize\":2,\"fileName\":\"/test/diskMirror.js\",\"useSize\":907,\"useAgreement\":true,\"maxSize\":134217728,\"urls\":[{\"fileName\":\"test\",\"url\":\"http://localhost:8080//1/Binary//test\",\"lastModified\":1711542797599,\"size\":0,\"type\":\"Binary\",\"isDir\":true,\"urls\":[{\"fileName\":\"工程进度计划.txt\",\"url\":\"http://localhost:8080//1/Binary//test/工程进度计划.txt\",\"lastModified\":1711542797599,\"size\":907,\"type\":\"Binary\",\"isDir\":false}]}],\"res\":\"ok!!!!\"}\n");

        // 开始合并
        final JSONObject mergeJsonTrees = JsonUtils.mergeJsonTrees(jsonObject1, jsonObject2, jsonObject1);
        // 打印结果
        System.out.println(mergeJsonTrees);
    }
}