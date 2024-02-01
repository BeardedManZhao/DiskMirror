package top.lingyuzhao.diskMirror.utils;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.utils.transformation.Transformation;

/**
 * 路径生成器 这里的函数的返回值是一个数组，[空间路径（无协议）, 空间路径（有协议）, 文件路径（无协议）, 文件路径（有协议）]
 * <p>
 * The return value of the function in the path generator is an array, [spatial path (no protocol), spatial path (with protocol), file path (without protocol), file path (with protocol)]
 *
 * @author zhao
 */
public interface PathGeneration extends Transformation<JSONObject, String[]> {
}
