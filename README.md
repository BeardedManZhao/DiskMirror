# ![image](https://github.com/BeardedManZhao/DiskMirror/assets/113756063/b8a15b22-5ca0-4552-aab2-7131c63dc727) DiskMirror

用于进行磁盘文件管理的一面镜子，其包含许多的适配器，能够将任何类型的文件数据流中的数据接入到管理中，并将保存之后的 url 返回，支持不同文件所属空间的管控，您还可以通过此API 获取到指定 userid 下面的所有文件的
url，在诸多场景中可以简化IO相关的实现操作，能够降低开发量，例如web服务器中的磁盘管理操作!

## 什么是适配器

适配器在这里是用于进行文件传输的桥梁，能够让您将自己的数据源（例如您的后端服务器）与指定的数据终端（例如您的各类文件系统）进行连接，将数据提供给数据终端，减少了您手动开发IO代码的时间。

在未来，我们将会提供更多的适配器选项，让适配器的数据终端具有更多的支持。

### 我如何获取 盘镜

您可以通过配置 maven 依赖的方式实现库的获取，下面就演示了如何获取到 盘镜。

```xml

<dependencies>
    <dependency>
        <groupId>io.github.BeardedManZhao</groupId>
        <artifactId>diskMirror</artifactId>
        <version>1.0.8</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba.fastjson2</groupId>
        <artifactId>fastjson2</artifactId>
        <version>2.0.25</version>
        <!--        <scope>provided</scope>-->
    </dependency>
    <dependency>
        <groupId>io.github.BeardedManZhao</groupId>
        <artifactId>zhao-utils</artifactId>
        <version>1.0.20231112</version>
        <!--        <scope>provided</scope>-->
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-client</artifactId>
        <version>3.3.2</version>
        <!--        <scope>provided</scope>-->
    </dependency>
</dependencies>
```

## 基本使用示例

在这里我们将演示如何使用 盘镜 进行一些基本的 CRUD 操作，能够实现在文件系统中的文件管理操作。

### 实例化适配器

适配器就是在 盘镜 中进行数据的传输的通道，而如果想要使用 盘镜，就需要实例化适配器，下面我们将演示如何实例化本地文件系统的适配器。

```java
package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        // 实例化盘镜配置类 配置类中包含很多的配置项目 对于本地文件系统来说 可以按照下面的方式来进行配置实例化
        final Config config = new Config();
        // 配置根目录 也是能够被盘镜 管理的目录，所有的管理操作只会在这个目录中生效，默认是/DiskMirror!
        config.put(Config.ROOT_DIR, "/DiskMirror");
        // 配置所有的 url 中的协议前缀，这会影响 getUrls 的结果， 如果您只是在本地文件系统中获取这些数据 就是文件系统的协议前缀，也就是什么都不加
        // 如果您要在 hdfs 文件系统中获取这些数据 这就是 hdfs 的协议前缀
        // 如果您要在 web JS 或者通过 url 中获取这些数据 这就是 web 的 http 协议前缀
        // 在这里我们给定空字符串就是代表使用本地文件系统
        config.put(Config.PROTOCOL_PREFIX, "");
        // 开始构建盘镜 由于我们在这里使用的是本地文件系统 所以我们使用 DiskMirror.LocalFSAdapter.getAdapter(config) 来实例化 本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
    }
}

```

### 向适配器中写入数据

在这里我们将演示如何向适配器中写入数据，并将写入的数据的 url 返回给您，当您向适配器中写入数据的时候，一切的管理和落盘操作都将由盘镜来处理。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 实例化盘镜配置类 配置类中包含很多的配置项目 对于本地文件系统来说 可以按照下面的方式来进行配置实例化
        final Config config = new Config();
        // 配置根目录 也是能够被盘镜 管理的目录，所有的管理操作只会在这个目录中生效，默认是/DiskMirror!
        config.put(Config.ROOT_DIR, "/DiskMirror");
        // 配置所有的 url 中的协议前缀，这会影响 getUrls 的结果， 如果您只是在本地文件系统中获取这些数据 就是文件系统的协议前缀，也就是什么都不加
        // 如果您要在 hdfs 文件系统中获取这些数据 这就是 hdfs 的协议前缀
        // 如果您要在 web JS 或者通过 url 中获取这些数据 这就是 web 的 http 协议前缀
        // 在这里我们给定空字符串就是代表使用本地文件系统
        config.put(Config.PROTOCOL_PREFIX, "");
        // 开始构建盘镜 由于我们在这里使用的是本地文件系统 所以我们使用 DiskMirror.LocalFSAdapter.getAdapter(config) 来实例化 本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
        // 准备需要被操作的文件
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\arc.png")) {
            // 输出数据
            final JSONObject save = save(fileInputStream, new JSONObject(), adapter);
            // 打印结果
            System.out.println(save);
        }
    }

    /**
     * 保存文件
     *
     * @param inputStream 数据流对象 包含需要保存的文件数据
     * @param jsonObject  任务参数对象 其中包含很多的写数据相关的信息 您可以在这里配置一些参数 upload 方法的注释中描述了您要提供的参数有哪些
     * @param adapter     适配器对象 数据写到适配器对象中 由适配器对象自动的处理 并实现落盘等操作
     * @return 操作之后会返回一个 json 对象 其中包含了一些操作的结果 例如是否成功 或者是错误信息 等 在这里会返回写好的数据的信息
     * @throws IOException 当落盘操作发生异常 会抛出此错误
     */
    public static JSONObject save(InputStream inputStream, JSONObject jsonObject, Adapter adapter) throws IOException {
        // 设置文件名字
        jsonObject.put("fileName", "arc.png");
        // 设置文件所属空间id 不同id 的空间相互隔离 不能互相访问
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型 在适配内部可能会由一些优化效果 同时也可以实现文本数据和二进制数据的分类
        jsonObject.put("type", Type.Binary);
        // 返回处理结果
        return adapter.upload(inputStream, jsonObject);
    }
}

```

适配器的 upload 函数返回的结果如下所示

| 参数名称         | 参数类型    | 参数解释                                                             |
|--------------|---------|------------------------------------------------------------------|
| fileName     | String  | 被落盘的文件名称                                                         |
| useAgreement | boolean | 是否使用了协议前缀 如过此值不为 undefined/null 且 为 true 则代表使用了协议前缀 否则代表没有使用协议前缀 |
| useSize      | long    | 当前用户空间的某个类型的文件总共使用量，按照字节为单位                                      |
| userId       | int     | 落盘文件所在的空间id                                                      |
| type         | String  | 落盘文件的类型                                                          |
| res          | String  | 落盘结果正常/错误信息                                                      |
| url          | String  | 落盘文件的读取 url 这个url 会根据协议前缀拼接字符串                                   |

```json
{
  "fileName": "arc.png",
  "userId": 1024,
  "type": "Binary",
  "useSize": 4237376,
  "useAgreement": false,
  "res": "ok!!!!",
  "url": "/DiskMirror/1024/Binary/arc.png"
}
```

### 从适配器中读取数据

在这里我们将演示如何从适配器中读取数据，并将读取的数据的 url 返回给您，当您从适配器中读取数据的时候，盘镜中的适配器将自动的根据您设置的各种参数将可以访问的 url 返回给您!

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Config config = new Config();
        config.put(Config.ROOT_DIR, "/DiskMirror");
        config.put(Config.PROTOCOL_PREFIX, "");
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
        // 使用适配器获取到文件url
        final JSONObject read = read(adapter, 1024);
        System.out.println(read);
    }

    private static JSONObject read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        return adapter.getUrls(jsonObject);
    }
}
```

在下面就是读取出来的 json 以及所有参数的解释

```json
{
  "userId": 1024,
  "type": "Binary",
  "useSize": 4237376,
  "useAgreement": false,
  "urls": [
    {
      "fileName": "arc.png",
      "url": "/DiskMirror/1024/Binary//arc.png",
      "lastModified": 1702895984184,
      "size": 4237376
    }
  ],
  "res": "ok!!!!"
}
```

在这里您可以清晰查看到返回的 json 对象中的结构

```
{
  "userId":文件空间的id,
  "type":文件类型,
  "useSize":当前用户空间的某个类型的文件总共使用量，按照字节为单位 ,
  "useAgreement":是否使用了协议前缀 如过此值不为 undefined 且 为 true 则代表使用了协议前缀 否则代表没有使用协议前缀,
  "urls":[
    {
      "fileName":"文件1的名字",
      "url":"文件1的路径",
      "lastModified":文件1上一次修改的时间,
      "size":文件1的大小
    },
    {
      "fileName":"文件2的名字",
      "url":"文件2的路径",
      "lastModified":文件1上一次修改的时间,
      "size":文件2的大小
    },
    ......
  ],
  "res":"ok!!!!"
}
```

### 从适配器中删除数据

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        final Config config = new Config();
        config.put(Config.ROOT_DIR, "/DiskMirror");
        config.put(Config.PROTOCOL_PREFIX, "");
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
        // 删除 1024 空间中的文件 arc.png
        final JSONObject jsonObject = new JSONObject();
        // 设置文件名字
        jsonObject.put("fileName", "arc.png");
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        final JSONObject remove = adapter.remove(jsonObject);
        System.out.println(remove);
    }
}
```

### 从适配器中重命名数据

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 实例化盘镜配置类 配置类中包含很多的配置项目 对于本地文件系统来说 可以按照下面的方式来进行配置实例化
        final Config config = new Config();
        // 配置根目录 也是能够被盘镜 管理的目录，所有的管理操作只会在这个目录中生效，默认是/DiskMirror!
        config.put(Config.ROOT_DIR, "/DiskMirror");
        // 获取到本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
        // 开始查询 1024 空间目录
        System.out.println(read(adapter, 1024));
        // 对目录中的文件 《defimage2.svg.png》 重命名为 test.png
        System.out.println(reName(adapter, 1024, "defimage2.svg.png", "test.png"));
        // 再一次查询 1024 空间目录
        System.out.println(read(adapter, 1024));
    }

    private static JSONObject read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        return adapter.getUrls(jsonObject);
    }

    private static JSONObject reName(Adapter adapter, int userId, String oldFileName, String newFileName) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 设置文件的旧名字 和 新名字
        jsonObject.put("fileName", oldFileName);
        jsonObject.put("newName", newFileName);
        return adapter.reName(jsonObject);
    }
}
```

在下面就是运行结果

```
{"userId":1024,"type":"Binary","useSize":493969,"useAgreement":true,"urls":[{"fileName":"defimage2.svg.png","url":"http://localhost:8080/1024/Binary//defimage2.svg.png","lastModified":1702903450767,"size":493969}],"res":"ok!!!!"}
{"userId":1024,"type":"Binary","newName":"test.png","fileName":"defimage2.svg.png","useSize":493969,"res":"ok!!!!"}
{"userId":1024,"type":"Binary","useSize":493969,"useAgreement":true,"urls":[{"fileName":"test.png","url":"http://localhost:8080/1024/Binary//test.png","lastModified":1702903450767,"size":493969}],"res":"ok!!!!"}

进程已结束,退出代码0
```

## 综合使用示例

### 本地文件系统 适配器使用示例

在下面，我们演示了如何将文件数据流和本地文件系统对接，提供给 DiskMirror 进行管理，并获取到结果 url 的示例。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 实例化配置类
        final Config config = new Config();
        // 设置 路径的协议前缀 默认是 http://localhost:8080
        config.put(Config.PROTOCOL_PREFIX, "http://xxx.xxx");
        // 装载到适配器 在这里使用本地文件系统
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
        // 获取到当前适配器所属的版本
        System.out.println("当前适配器版本：" + DiskMirror.LocalFSAdapter.getVersion());
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\headSculpture - 副本.jpg")) {
            // 将文件保存到 1024 号空间
            // 打印结果: {"fileName":"arc.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"http://xxx.xxx/1024/Binary/arc.png","useSize":311720}
            save(adapter, fileInputStream, 1024, "arc.png");
        }
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\defimage2.svg.png")) {
            // 再将一个新文件保存到 1024 号空间
            // 打印结果: {"fileName":"defimage2.svg.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"http://xxx.xxx/1024/Binary/defimage2.svg.png","useSize":805689}
            save(adapter, fileInputStream, 1024, "defimage2.svg.png");
        }

        // 读取 1024 号空间中的所有 url 由于刚刚保存文件的操作出现在这里 所以 1024 号空间应该是有刚才的文件的
        // 打印结果: {"userId":1024,"type":"Binary","useSize":805689,"useAgreement":true,"urls":[{"fileName":"arc.png","url":"http://xxx.xxx/1024/Binary//arc.png","lastModified":1702902167167,"size":311720},{"fileName":"defimage2.svg.png","url":"http://xxx.xxx/1024/Binary//defimage2.svg.png","lastModified":1702902167270,"size":493969}],"res":"ok!!!!"}
        read(adapter, 1024);


        // 读取 2048 号空间中的所有 url 这里并没有进行过保存 所以在这里的空间是没有数据的
        // 打印结果: {"userId":2048,"type":"Binary","useSize":0,"useAgreement":true,"res":"空间 [/DiskMirror/2048/Binary/] 不可读!!!"}
        read(adapter, 2048);

        // 删除 1024 空间中的文件 arc.png
        final JSONObject jsonObject = new JSONObject();
        // 设置文件名字
        jsonObject.put("fileName", "arc.png");
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        final JSONObject remove = adapter.remove(jsonObject);
        // 打印结果：{"fileName":"arc.png","userId":1024,"type":"Binary","useSize":493969,"res":"ok!!!!"}
        System.out.println(remove);

        // 删除之后再次查看 1024 空间中的目录
        // 打印结果：{"userId":1024,"type":"Binary","useSize":493969,"useAgreement":true,"urls":[{"fileName":"defimage2.svg.png","url":"http://xxx.xxx/1024/Binary//defimage2.svg.png","lastModified":1702902167270,"size":493969}],"res":"ok!!!!"}
        read(adapter, 1024);
    }

    private static void read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        final JSONObject urls = adapter.getUrls(jsonObject);
        // 打印结果
        System.out.println(urls);
    }

    /**
     * @param adapter         需要使用的适配器对象
     * @param fileInputStream 需要使用的文件数据流
     * @param userId          空间id
     * @param fileName        文件名
     * @throws IOException 操作异常
     */
    private static void save(Adapter adapter, FileInputStream fileInputStream, int userId, String fileName) throws IOException {
        // 开始写数据 准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件名字
        jsonObject.put("fileName", fileName);
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 存储文件
        final JSONObject upload = adapter.upload(fileInputStream, jsonObject);
        // 打印保存结果
        System.out.println(upload.toString());
    }
}
```

### HDFS 文件系统 适配器使用示例

做为一种文件系统适配器，当然也要支持 HDFS 这类大数据文件系统，下面我们演示了如何将文件数据流和 HDFS 文件系统对接，提供给 DiskMirror 进行管理，并获取到结果 url 的示例。 被进行 ‘TODO’
标记的地方都是与本地文件系统适配器有一点不同的操作，只不过在这里我们使用的是 HDFS 文件系统。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 实例化配置类
        final Config config = new Config();
        // TODO 设置 路径的协议前缀 在这里指向的是 HDFS 文件系统 盘镜目录 访问地址
        config.put(Config.PROTOCOL_PREFIX, "http://192.168.0.141:9870/webhdfs/v1/DiskMirror");
        // TODO 设置 HDFS 协议前缀 在这里指向的是 HDFS 文件系统访问地址 是 hdfs 开头
        config.put(Config.FS_DEFAULT_FS, "hdfs://192.168.0.141:8020");
        // TODO 设置请求参数 HDFS 要求需要设置 OP 参数
        final JSONObject params = config.putObject(Config.PARAMS);
        params.put("op", "OPEN");
        // TODO 装载到适配器 在这里使用HDFS文件系统
        final Adapter adapter = DiskMirror.HDFSAdapter.getAdapter(config);

        // 获取到当前适配器所属的版本
        System.out.println("当前适配器版本：" + DiskMirror.LocalFSAdapter.getVersion());
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\headSculpture - 副本.jpg")) {
            // 将文件保存到 1024 号空间
            // 打印结果: {"fileName":"arc.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary/arc.png?op=OPEN&","useSize":311720}
            save(adapter, fileInputStream, 1024, "arc.png");
        }
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\defimage2.svg.png")) {
            // 再将一个新文件保存到 1024 号空间
            // 打印结果: {"fileName":"defimage2.svg.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary/defimage2.svg.png?op=OPEN&","useSize":805689}
            save(adapter, fileInputStream, 1024, "defimage2.svg.png");
        }

        // 读取 1024 号空间中的所有 url 由于刚刚保存文件的操作出现在这里 所以 1024 号空间应该是有刚才的文件的
        // 打印结果: {"userId":1024,"type":"Binary","useSize":805689,"useAgreement":true,"urls":[{"fileName":"arc.png","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary//arc.png?op=OPEN&","lastModified":1702902733377,"size":311720},{"fileName":"defimage2.svg.png","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary//defimage2.svg.png?op=OPEN&","lastModified":1702902733869,"size":493969}],"res":"ok!!!!"}
        read(adapter, 1024);


        // 读取 2048 号空间中的所有 url 这里并没有进行过保存 所以在这里的空间是没有数据的
        // 打印结果: {"userId":2048,"type":"Binary","useSize":0,"useAgreement":true,"urls":[],"res":"ok!!!!"}
        read(adapter, 2048);

        // 删除 1024 空间中的文件 arc.png
        final JSONObject jsonObject = new JSONObject();
        // 设置文件名字
        jsonObject.put("fileName", "arc.png");
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        final JSONObject remove = adapter.remove(jsonObject);
        // 打印结果：{"fileName":"arc.png","userId":1024,"type":"Binary","useSize":493969,"res":"ok!!!!"}
        System.out.println(remove);

        // 删除之后再次查看 1024 空间中的目录
        // 打印结果：{"userId":1024,"type":"Binary","useSize":493969,"useAgreement":true,"urls":[{"fileName":"defimage2.svg.png","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary//defimage2.svg.png?op=OPEN&","lastModified":1702903280574,"size":493969}],"res":"ok!!!!"}
        read(adapter, 1024);
    }

    private static void read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        final JSONObject urls = adapter.getUrls(jsonObject);
        // 打印结果
        System.out.println(urls);
    }

    /**
     * @param adapter         需要使用的适配器对象
     * @param fileInputStream 需要使用的文件数据流
     * @param userId          空间id
     * @param fileName        文件名
     * @throws IOException 操作异常
     */
    private static void save(Adapter adapter, FileInputStream fileInputStream, int userId, String fileName) throws IOException {
        // 开始写数据 准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件名字
        jsonObject.put("fileName", fileName);
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 存储文件
        final JSONObject upload = adapter.upload(fileInputStream, jsonObject);
        // 打印保存结果
        System.out.println(upload.toString());
    }
}
```

### 设置空间的最大容量

在新版本中 您可以直接手动的指定某个空间的最大容量，当空间中的文件超过了这个容量时，会阻止文件上传操作。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 准备适配器对象
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(new Config());
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 打印此空间中的所有文件 其中会包含一个 maxSize 参数 由于我们没有设置，所以这里是默认的
        System.out.println(adapter.getUrls(jsonObject.clone()));
        // 设置 1024 空间的最大空间大小
        adapter.setSpaceMaxSize("1024", 999999999);
        // 再一次打印 TODO 其中的 maxSize 已经被修改为 999999999 在校验的时候也会按照这个参数进行校验
        System.out.println(adapter.getUrls(jsonObject.clone()));
    }
}
```

---- 

### 更新记录

- 2024-01-04 1.0.8 版本发布

```
1. 针对所有的操作，返回的json中包含了 maxSize 参数，代表的就是当前操作的空间的最大容量
```

- 2023-12-22 1.0.7 版本发布

```
1. 能够通过适配器对于文件系统中的文件进行重命名操作
```

----

- 2023-12-19 1.0.6 版本发布

```
1. 针对所有的操作返回值都增加了实时文件空间占用字节数"useSize"的结果
2. 针对所有已存在的文件进行增删将会抛出错误，您可以不去进行文件是否存在的检测
3. 针对稳定性进行升级，修复了部分bug
4. 针对文件上传的接口增加了文件大小限制的配置项目，默认为 128Mb
```

----

- diskMirror 后端服务器版本：https://github.com/BeardedManZhao/DiskMirrorBackEnd.git
- diskMirror Java API 版本：https://github.com/BeardedManZhao/DiskMirror.git
