package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.utils.IOUtils;
import top.lingyuzhao.utils.StrUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP 协议的适配器 此适配器中提供了诸多的无参函数，是专属于TCP协议的适配器，无参函数可以阻塞线程等待输入。
 * <p>
 * The TCP protocol adapter provides many parameter free functions, which are specific to the TCP protocol and can block threads waiting for input.
 *
 * @author zhao
 */
public class TcpAdapter extends AdapterPacking {

    private final ServerSocket serverSocket, fileServerSocket;

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
    public TcpAdapter(DiskMirror diskMirror, Config config, Config adapterConfig) {
        super(diskMirror, config, adapterConfig);
        // 获取到配置类中端口号的字符串 这里应该是两个端口，以逗号分割
        // 第一个代表的就是发送文件元数据和请求的端口 第二个就是发送文件本身的端口
        final String[] string = StrUtils.splitBy(config.getString(Config.FS_DEFAULT_FS), ',');
        try {
            serverSocket = new ServerSocket(Integer.parseInt(string[0]));
            fileServerSocket = new ServerSocket(Integer.parseInt(string[1]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 开始进行监听处理
     *
     * @throws IOException 处理错误时弹出的数据
     */
    public void run() throws IOException {
        try (final Socket accept = serverSocket.accept();
             final DataInputStream inputStream = new DataInputStream(accept.getInputStream());
             final DataOutputStream outputStream = new DataOutputStream(accept.getOutputStream())
        ) {
            final String s1 = inputStream.readUTF();
            // 判断元数据类型
            if (!"upload".equals(s1)) {
                switch (s1) {
                    case "getUrls":
                        outputStream.writeUTF(getUrls(JSONObject.parse(inputStream.readUTF())).toString());
                        break;
                    case "remove":
                        outputStream.writeUTF(remove(JSONObject.parse(inputStream.readUTF())).toString());
                        break;
                    case "reName":
                        outputStream.writeUTF(reName(JSONObject.parse(inputStream.readUTF())).toString());
                        break;
                    case "mkdirs":
                        outputStream.writeUTF(mkdirs(JSONObject.parse(inputStream.readUTF())).toString());
                        break;
                    case "useSize":
                        outputStream.writeLong(getUseSize(JSONObject.parse(inputStream.readUTF())));
                        break;
                    case "download":
                        IOUtils.copy(downLoad(JSONObject.parse(inputStream.readUTF())), outputStream, true);
                        break;
                    case "transferDeposit":
                        outputStream.writeUTF(transferDeposit(JSONObject.parse(inputStream.readUTF())).toString());
                        break;
                    case "transferDepositStatus":
                        outputStream.writeUTF(transferDepositStatus(JSONObject.parse(inputStream.readUTF())).toString());
                        break;
                }
                return;
            }
            // 获取到元数据信息
            final String s = inputStream.readUTF();
            try {
                // 回复情况
                outputStream.writeUTF("ok");
                // 获取到文件大小
                final long size = inputStream.readLong();
                try (final Socket accept1 = fileServerSocket.accept();
                     final BufferedInputStream inputStream1 = new BufferedInputStream(accept1.getInputStream())) {
                    // 开始上传文件
                    final String string = upload(inputStream1, JSONObject.parse(s), size).toString();
                    // 最后返回结果
                    outputStream.writeUTF(string);
                    outputStream.flush();
                }
            } catch (Exception e) {
                outputStream.writeUTF(e.getMessage());
            }
        }
    }
}
