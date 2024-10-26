package top.lingyuzhao.diskMirror.core.module;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.utils.PalettePng;
import top.lingyuzhao.utils.StrUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 处理模块类，其可以处理一个数据流 如果其被添加到了适配器对象中，则适配器会直接将接收到的数据流传递给处理模块，并由处理模块进行数据处理
 *
 * @author zhao
 */
public enum HandleModule {

    ImageCompressModule {

        private PalettePng imageCompress = PalettePng.RGB_8;

        public void setImageCompress(PalettePng imageCompress) {
            this.imageCompress = imageCompress;
        }

        @Override
        public InputStream handler(InputStream inputStream, JSONObject inJson) throws IOException {
            try (ByteArrayOutputStream byteArrayOutputStream = imageCompress.translate(ImageIO.read(inputStream))) {
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            }
        }

        @Override
        public boolean isSupport(JSONObject inJson) {
            final String s = StrUtils.splitBy(
                    inJson.getString("fileName"), '.', 2
            )[1].toLowerCase();
            switch (s) {
                case "png":
                case "apng":
                    inJson.put("suffix", s);
                    return true;
            }
            return false;
        }
    };

    /**
     * 处理一个数据流 外界会自动关闭数据流的
     *
     * @param inputStream 需要被处理的数据流对象
     * @param inJson      数据流对象对应的 json 请求数据
     * @return 处理后的数据流对象 不可以与输入流对象为同一对象。输入流会被关闭的
     * @throws IOException 处理模块处理操作失败的时候可以抛出此异常
     */
    public abstract InputStream handler(InputStream inputStream, JSONObject inJson) throws IOException;

    /**
     * 根据请求信息 判断是否可以推送到该模块处理
     *
     * @param inJson 需要被判断的用户元数据信息
     * @return 是否可以推送到该模块处理
     */
    public abstract boolean isSupport(JSONObject inJson);

    /**
     * 设置图片压缩的模式
     *
     * @param imageCompress 图片压缩的模式
     */
    public void setImageCompress(PalettePng imageCompress) {
        throw new UnsupportedOperationException(this.name() + " 无法调用：setImageCompress，此函数适用模块：" + HandleModule.ImageCompressModule);
    }
}
