package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;

import java.io.IOException;
import java.io.InputStream;

/**
 * 此类代表的是一个适配器的包装类，其继承了 FSAdapter 同时拓展了子适配器的包装功能!
 * <p>
 * This class represents a wrapper class for an adapter, which inherits FSAdapter and expands the wrapper function of sub adapters!
 * <p>
 * 详细信息
 * 此类会包装一个适配器，并自动的将 FSAdapter 中的抽象方法按照给定的适配器的类型和配置进行实现！这样的实现操作相较于传统的实现方式，可能会造成一点点的内存占用，但是可以有效的降低代码的复杂度。
 * <p>
 * This class will package an adapter and automatically implement the abstract methods in FSAdapter according to the given adapter type and configuration! Compared to traditional implementation methods, this implementation may cause a slight memory footprint, but it can effectively reduce the complexity of the code.
 *
 * @author zhao
 */
public class AdapterPacking extends FSAdapter {

    private final DiskMirror diskMirror;
    private final FSAdapter subAdapter;

    /**
     * 构建一个适配器包装类，此类可以直接调用其所包装的适配器的方法，能够有效的实现将各种适配器对象接入到 FSAdapter 中。
     * <p>
     * Build an adapter wrapper class that can directly call the methods of the adapter it wraps, effectively integrating various adapter objects into FSAdapters.
     *
     * @param diskMirror    需要获取到的适配器对应的枚举对象！
     *                      <p>
     *                      The enumeration object corresponding to the adapter that needs to be obtained!
     * @param config        当前适配器包装类的配置类！
     * @param adapterConfig 当前包装类中要包装的适配器的配置类，请注意，此配置类并非是 AdapterPacking 的配置类！
     *                      <p>
     *                      The configuration class of the adapter to be wrapped in the current wrapper class. Please note that this configuration class is not the configuration class of AdapterPacking!
     */
    public AdapterPacking(DiskMirror diskMirror, Config config, Config adapterConfig) {
        super(config);
        this.diskMirror = diskMirror;
        final Adapter adapter = diskMirror.getAdapter(adapterConfig);
        if (adapter instanceof FSAdapter) {
            this.subAdapter = (FSAdapter) adapter;
        } else {
            throw new RuntimeException("The adapter is not a FSAdapter!");
        }
    }

    /**
     * 获取当前包装类所包装的适配器对应的枚举对象！
     * <p>
     * Get the enumeration object corresponding to the adapter wrapped by the current wrapper class!
     *
     * @return 枚举对象
     */
    public DiskMirror getDiskMirror() {
        return this.diskMirror;
    }

    /**
     * 获取子适配器，也就是被当前的包装类所包装的适配器对象！
     * <p>
     * Get the sub adapter, which is the adapter object wrapped by the current wrapper class!
     *
     * @return 子适配器的对象
     */
    public Adapter getSubAdapter() {
        return this.subAdapter;
    }


    @Override
    protected JSONObject pathProcessorUpload(String path, String path_res, JSONObject inJson, InputStream inputStream) throws IOException {
        return this.subAdapter.pathProcessorUpload(path, path_res, inJson, inputStream);
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject inJson) throws IOException {
        return this.subAdapter.pathProcessorGetUrls(path, path_res, inJson);
    }

    @Override
    protected JSONObject pathProcessorMkdirs(String path, JSONObject inJson) throws IOException {
        return this.subAdapter.pathProcessorMkdirs(path, inJson);
    }

    @Override
    protected InputStream pathProcessorDownLoad(String path, JSONObject inJson) throws IOException {
        return this.subAdapter.pathProcessorDownLoad(path, inJson);
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject inJson) throws IOException {
        return this.subAdapter.pathProcessorRemove(path, inJson);
    }

    @Override
    protected JSONObject pathProcessorReName(String path, JSONObject inJson) throws IOException {
        return this.subAdapter.pathProcessorReName(path, inJson);
    }

    @Override
    protected long pathProcessorUseSize(String path, JSONObject inJson) throws IOException {
        return this.subAdapter.pathProcessorUseSize(path, inJson);
    }
}
