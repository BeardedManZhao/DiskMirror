# ![image](https://github.com/BeardedManZhao/DiskMirror/assets/113756063/b8a15b22-5ca0-4552-aab2-7131c63dc727) DiskMirror

用于进行磁盘文件管理的一面镜子，其包含许多的适配器，能够将任何类型的文件数据流中的数据接入到管理中，并将保存之后的 url
返回，支持不同文件所属空间的管控，您还可以通过此API 获取到指定 userid 下面的所有文件的
url，在诸多场景中可以简化IO相关的实现操作，能够降低开发量，例如web服务器中的磁盘管理操作!

diskMirror 的处理方式能够将多种文件系统的操作统一成为一样的，降低了开发难度，同时可以将磁盘文件管理统一到一起，方便进行管理。

## 什么是适配器

适配器在这里是用于进行文件传输的桥梁，能够让您将自己的数据源（例如您的后端服务器）与指定的数据终端（例如您的各类文件系统）进行连接，将数据提供给数据终端，减少了您手动开发IO代码的时间。

在未来，我们将会提供更多的适配器选项，让适配器的数据终端具有更多的支持。

### 支持哪些适配器

所有支持的适配器都会在 `top.lingyuzhao.diskMirror.core.DiskMirror` 类中找到，您可以查阅类文件，也可以在这里查看已支持的适配器的表格。

| 适配器                              | 自从何时支持 | 需要引入的外部依赖（fastjson2 和 zhao-utils 除外） | 功能介绍                                                                                                                                                                                          |
|----------------------------------|--------|--------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| DiskMirror.LocalFSAdapter        | v1.0   | 无外部依赖                                | 能够将适配器运行所在的主机的磁盘做为 diskMirror 管理，一般来说是作用于本机的本地文件系统磁盘管理。                                                                                                                                       |
| DiskMirror.HDFSAdapter           | v1.0   | hadoop-client                        | 将 HDFS 文件系统提供给 diskMirror 管理，能够通过 diskMirror 操作 HDFS                                                                                                                                          |
| DiskMirror.DiskMirrorHttpAdapter | v1.1.2 | httpclient, httpmime                 | 将 [后端版本的 diskMirror](https://github.com/BeardedManZhao/DiskMirrorBackEnd.git) 接入到 diskMirror 管理，能够通过 diskMirror 操作 [后端 diskMirror]((https://github.com/BeardedManZhao/DiskMirrorBackEnd.git)) |

### 我如何获取 盘镜

您可以通过配置 maven 依赖的方式实现库的获取，下面就演示了如何获取到 盘镜。

> 库的 maven 版本在一般情况下是与 diskMirror 内部的版本号一致的，您可以在 [更新列表](#-更多详细) 中查询到支持 maven 下载的版本，若不存在于列表中的版本，则会自动尝试使用相近的 SNAPSHOT 版本，若您需要使用 SNAPSHOT 版本。
> SNAPSHOT 版本是一些已发布版本的中间过渡版本，他们最显著的特点就是框架内部的版本号与maven版本号不一致！
> 因此，若您没有特殊的需求，请在[更新列表](#-更多详细) 中选择您需要的版本哦！！！

```xml

<dependencies>
    <dependency>
        <groupId>io.github.BeardedManZhao</groupId>
        <artifactId>diskMirror</artifactId>
        <version>1.1.7</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba.fastjson2</groupId>
        <artifactId>fastjson2</artifactId>
        <version>2.0.25</version>
        <!--        <scope>provided</scope>-->
    </dependency>
    <!-- 从 disk Mirror 1.1.0 版本开始 请确保 zhao-utils 的版本 >= 1.0.20240121 -->
    <dependency>
        <groupId>io.github.BeardedManZhao</groupId>
        <artifactId>zhao-utils</artifactId>
        <version>1.0.20240121</version>
        <!--        <scope>provided</scope>-->
    </dependency>
    <!-- 如果需要对接 HDFS 才导入 如果不需要就不导入此依赖 -->
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-client</artifactId>
        <version>3.3.2</version>
        <!--        <scope>provided</scope>-->
    </dependency>
    <!-- 如果您要使用 DiskMirrorHttpAdapter 请添加 httpClient 核心库 反之不需要 -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.14</version>
        <!--        <scope>provided</scope>-->
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpmime</artifactId>
        <version>4.5.14</version>
        <!--        <scope>provided</scope>-->
    </dependency>
</dependencies>
```

## 基本使用示例

在这里我们将演示如何使用 盘镜 进行一些基本的 CRUD 操作，能够实现在文件系统中的文件管理操作。

### 实例化适配器

适配器就是在 盘镜 中进行数据的传输的通道，而如果想要使用 盘镜，就需要实例化适配器，下面我们将演示如何实例化本地文件系统的适配器。

#### 使用配置类实例化盘镜

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

#### 使用配置注解实例化盘镜

从 1.1.1 版本开始 支持通过配置注解来实例化一个盘镜适配器对象，相对于使用配置类来进行实例化而言，这样更简洁。

```java
package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // 配置根目录 也是能够被盘镜 管理的目录，所有的管理操作只会在这个目录中生效，默认是/DiskMirror!
        rootDir = "/DiskMirrorDir",
        // 配置所有的 url 中的协议前缀，这会影响 getUrls 的结果， 如果您只是在本地文件系统中获取这些数据 就是文件系统的协议前缀，也就是什么都不加
        // 如果您要在 hdfs 文件系统中获取这些数据 这就是 hdfs 的协议前缀
        // 如果您要在 web JS 或者通过 url 中获取这些数据 这就是 web 的 http 协议前缀
        // 在这里我们给定空字符串就是代表使用本地文件系统
        protocolPrefix = ""
)
public final class MAIN {
    public static void main(String[] args) {
        // 开始构建盘镜 由于我们在这里使用的是本地文件系统 所以我们使用 DiskMirror.LocalFSAdapter.getAdapter(被注解的类对象) 来实例化 本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
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

在这里我们将演示如何从适配器中读取数据，并将读取的数据的 url 返回给您，当您从适配器中读取数据的时候，盘镜中的适配器将自动的根据您设置的各种参数将可以访问的
url 返回给您!

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
  "useSize": 787141,
  "useAgreement": false,
  "maxSize": 134217728,
  "urls": [
    {
      "fileName": "fsdownload",
      "url": "/DiskMirror/1024/Binary//fsdownload",
      "lastModified": 1705762229601,
      "size": 0,
      "type": "Binary",
      "isDir": true,
      "urls": [
        {
          "fileName": "myFile.png",
          "url": "/DiskMirror/1024/Binary//fsdownload/myFile.png",
          "lastModified": 1705762229664,
          "size": 293172,
          "type": "Binary",
          "isDir": false
        }
      ]
    },
    {
      "fileName": "test.png",
      "url": "/DiskMirror/1024/Binary//test.png",
      "lastModified": 1702903450767,
      "size": 493969,
      "type": "Binary",
      "isDir": false
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
  "maxSize": 当前用户空间的最大使用量,
  "urls": [
    {
      "fileName":"目录1的名字",
      "url":"目录1的路径",
      "lastModified":目录1上一次修改的时间,
      "size":目录1的大小
      "type": 目录1所属空间类型,
      "isDir": 如果是 true 代表是目录 反之是文件,
      "urls": [
        {
          "fileName":"文件1的名字",
          "url":"文件1的路径",
          "lastModified":文件1上一次修改的时间,
          "size":文件1的大小
          "type": 文件1所属空间类型,
          "isDir": 如果是 true 代表是目录 反之是文件,
        }
      ]
    },
    {
      "fileName":"文件2的名字",
      "url":"文件2的路径",
      "lastModified":文件2上一次修改的时间,
      "size":文件2的大小
      "type": 文件2所属空间类型,
      "isDir": 如果是 true 代表是目录 反之是文件,
    },
    ......
  ],
  "res": "ok!!!!"
}
```

### 从适配器中获取数据的数据流对象 下载文件

在 1.1.4 版本之后，diskMirror 的文件存储可以获取到数据流对象，而不是只能使用 url 下载文件，这有助于您在使用 diskMirror
的时候，可以使用更灵活的下载方式，也可以避免一些配置！

在下面就是一个使用的示例！

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        rootDir = "/diskMirror"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 diskMirror 适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 准备我们需要的文件的描述信息
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 设置要获取的文件的文件名字
        jsonObject.put("fileName", "test.txt");
        // 获取到数据流对象
        try (
                final InputStream inputStream = adapter.downLoad(jsonObject);
                final FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\zhao\\Desktop\\fsdownload\\res.png")
        ) {
            // 在这里可以使用数据流 数据流中就是我们需要的文件！
            IOUtils.copy(inputStream, fileOutputStream, true);
        }
    }
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

### 通过适配器创建一个文件目录

从 diskMirror 1.1.1 版本开始 针对文件目录的创建功能开始被支持，1.0.0 版本之后，diskMirror 能够实现路径的功能，提高了灵活性，而在最新的
1.1.1 版本中，可以显式的创建一个文件目录。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 准备参数
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1024);
        jsonObject.put("type", Type.Binary);
        // 设置要创建的目录
        jsonObject.put("fileName", "MyDir");
        // 直接创建
        // {"userId":1024,"type":"Binary","fileName":"MyDir","useSize":0,"res":"ok!!!!"}
        System.out.println(adapter.mkdirs(jsonObject));
        // 查看文件系统结构 这里由于只需要 userId type 而恰巧在上面我们设置好了 所以直接将上面的json 输入
        // {"userId":1024,"type":"Binary","fileName":"MyDir","useSize":0,"res":"ok!!!!","useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"MyDir","url":"http://localhost:8080/1024/Binary//MyDir","lastModified":1706779978911,"size":0,"type":"Binary","isDir":true,"urls":[]}]}
        System.out.println(adapter.getUrls(jsonObject));
    }
}
```

### 通过适配器下载一个文件

在 1.1.4 以及之后的版本中，针对所有的适配器对象新增了一个 `download` 函数，其可以越过 url
的限制，直接使用内部的组件来进行文件下载链接的生成，这能够最大程度上解决文件下载问题！在下面就是一个示例。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.utils.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        rootDir = "/diskMirror",
        // TODO 设置 diskMirror的 http 服务器访问地址 当然，您如果使用本地适配器也可以不设置！
        fsDefaultFS = "http://localhost:8080/"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 diskMirror 适配器
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        // 准备我们需要的文件的描述信息
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 设置要获取的文件的文件名字
        jsonObject.put("fileName", "/myDir/Test.png");
        // 从 适配器 中获取到数据流对象
        try (
                final InputStream inputStream = adapter.downLoad(jsonObject);
                final FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\zhao\\Desktop\\fsdownload\\backImageFromHttp.svg.jpg")
        ) {
            // 在这里可以使用来自 diskMirror 中的数据流 数据流中就是我们需要的文件！
            IOUtils.copy(inputStream, fileOutputStream, true);
        }
    }
}
```

## 进阶使用示例

### 密码设置

当我们将这个适配器部署到如 web后端服务器 这类服务器后，任何人都可以以任何http协议将 json 请求发送到服务器，从而获取到文件的
url 或者未经过允许访问您的服务器，这是不安全的，所以需要对 diskMirror
进行安全密钥设置。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Config config = new Config();
        config.setSecureKey("123123");
        // 查看密钥的值 结果是 1450572480 这个是根据设置的 key 计算来的
        System.out.println(config.getSecureKey());
        // 构建适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
        // 查看文件目录
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1024);
        jsonObject.put("type", Type.Binary);
        // 这里就是密钥设置了 在这里的密钥是 1020000000 与上面的密钥不一致 所以会被拒接访问
        jsonObject.put(Config.SECURE_KEY, 1020000000);
        // 在这里可以尝试一下是否会被拒绝访问
        final JSONObject urls = adapter.getUrls(jsonObject);
        System.out.println(urls);
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

## 综合使用示例

### 本地文件系统 适配器使用示例

在下面，我们演示了如何将文件数据流和本地文件系统对接，提供给 DiskMirror 进行管理，并获取到结果 url 的示例。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        protocolPrefix = "https://xxx.xxx"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 由于我们在 MAIN 类中使用了配置注解，因此不再需要实例化配置类了
        // final Config config = new Config();
        // 设置 路径的协议前缀 默认是 http://localhost:8080
        // config.put(Config.PROTOCOL_PREFIX, "https://xxx.xxx");

        // 获取到适配器 在这里使用本地文件系统 在这里是通过注解实现的配置
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 获取到当前适配器所属的版本
        System.out.println("当前适配器版本：" + DiskMirror.LocalFSAdapter.getVersion());
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\headSculpture - 副本.jpg")) {
            // 将文件保存到 1024 号空间
            // 打印结果：{"fileName":"arc.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"https://xxx.xxx/1024/Binary/arc.png","useSize":311720,"maxSize":134217728}
            save(adapter, fileInputStream, 1024, "arc.png");
        }
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\defimage2.svg.png")) {
            // 再将一个新文件保存到 1024 号空间
            // 打印结果：{"fileName":"defimage2.svg.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"https://xxx.xxx/1024/Binary/defimage2.svg.png","useSize":805689,"maxSize":134217728}
            save(adapter, fileInputStream, 1024, "defimage2.svg.png");
        }

        // 读取 1024 号空间中的所有 url 由于刚刚保存文件的操作出现在这里 所以 1024 号空间应该是有刚才的文件的
        // 打印结果：{"userId":1024,"type":"Binary","useSize":805689,"useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"arc.png","url":"https://xxx.xxx/1024/Binary//arc.png","lastModified":1706775810959,"size":311720,"type":"Binary","isDir":false},{"fileName":"defimage2.svg.png","url":"https://xxx.xxx/1024/Binary//defimage2.svg.png","lastModified":1706775811064,"size":493969,"type":"Binary","isDir":false}],"res":"ok!!!!"}
        read(adapter, 1024);


        // 读取 2048 号空间中的所有 url 这里并没有进行过保存 所以在这里的空间是没有数据的
        // 打印结果：{"userId":2048,"type":"Binary","useSize":0,"useAgreement":true,"maxSize":134217728,"res":"空间 [\\DiskMirror\\2048\\Binary] 不可读!!!"}
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
        // 打印结果：{"fileName":"arc.png","userId":1024,"type":"Binary","maxSize":134217728,"useSize":493969,"res":"ok!!!!"}
        System.out.println(remove);

        // 删除之后再次查看 1024 空间中的目录
        // 打印结果：{"userId":1024,"type":"Binary","useSize":493969,"useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"defimage2.svg.png","url":"https://xxx.xxx/1024/Binary//defimage2.svg.png","lastModified":1706775811064,"size":493969,"type":"Binary","isDir":false}],"res":"ok!!!!"}
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

做为一种文件系统适配器，当然也要支持 HDFS 这类大数据文件系统，下面我们演示了如何将文件数据流和 HDFS 文件系统对接，提供给
DiskMirror 进行管理，并获取到结果 url 的示例。 被进行 ‘TODO’
标记的地方都是与本地文件系统适配器有一点不同的操作，只不过在这里我们使用的是 HDFS 文件系统。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        //  TODO 设置 路径的协议前缀 在这里指向的是 HDFS 文件系统 盘镜目录 访问地址
        protocolPrefix = "http://192.168.0.141:9870/webhdfs/v1/DiskMirror",
        // TODO 设置 HDFS 服务器地址 在这里指向的是 HDFS 文件系统访问地址 是 hdfs 开头
        fsDefaultFS = "hdfs://192.168.0.141:8020",
        // TODO 设置请求参数 HDFS 要求需要设置 OP 参数 在读取返回 url 的时候会使用这个参数进行拼接
        params = "{\"op\": \"OPEN\"}"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // TODO 获取到适配器 在这里使用HDFS文件系统
        final Adapter adapter = DiskMirror.HDFSAdapter.getAdapter(MAIN.class);

        // 获取到当前适配器所属的版本
        System.out.println("当前适配器版本：" + DiskMirror.LocalFSAdapter.getVersion());
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\headSculpture - 副本.jpg")) {
            // 将文件保存到 1024 号空间
            // 打印结果：{"fileName":"arc.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary/arc.png?op=OPEN&","useSize":311720,"maxSize":134217728}
            save(adapter, fileInputStream, 1024, "arc.png");
        }
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\defimage2.svg.png")) {
            // 再将一个新文件保存到 1024 号空间
            // 打印结果：{"fileName":"defimage2.svg.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary/defimage2.svg.png?op=OPEN&","useSize":805689,"maxSize":134217728}
            save(adapter, fileInputStream, 1024, "defimage2.svg.png");
        }

        // 读取 1024 号空间中的所有 url 由于刚刚保存文件的操作出现在这里 所以 1024 号空间应该是有刚才的文件的
        // 打印结果：{"userId":1024,"type":"Binary","useSize":805689,"useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"arc.png","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary//arc.png?op=OPEN&","lastModified":1706778315852,"size":311720,"type":"Binary","isDir":false},{"fileName":"defimage2.svg.png","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary//defimage2.svg.png?op=OPEN&","lastModified":1706778315938,"size":493969,"type":"Binary","isDir":false}],"res":"ok!!!!"}
        read(adapter, 1024);


        // 读取 2048 号空间中的所有 url 这里并没有进行过保存 所以在这里的空间是没有数据的
        // 打印结果: {"userId":2048,"type":"Binary","useSize":0,"useAgreement":true,"maxSize":134217728,"urls":[],"res":"ok!!!!"}
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
        // 打印结果：{"fileName":"arc.png","userId":1024,"type":"Binary","maxSize":134217728,"useSize":493969,"res":"ok!!!!"}
        System.out.println(remove);

        // 删除之后再次查看 1024 空间中的目录
        // 打印结果：{"userId":1024,"type":"Binary","useSize":493969,"useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"defimage2.svg.png","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary//defimage2.svg.png?op=OPEN&","lastModified":1706778315938,"size":493969,"type":"Binary","isDir":false}],"res":"ok!!!!"}
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

### 更新记录

#### 2024-03-26 1.1.7 版本发布

- 修复 HTTP 适配器组件中的 `download` 函数无法使用的问题！您在 2024-03-26 之后的后端服务器中，可以正常使用此函数！
- 优化每个函数中的参数检查逻辑。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        rootDir = "/diskMirror",
        // TODO 设置 diskMirror的 http 服务器访问地址
        fsDefaultFS = "https://diskMirror.lingyuzhao.top/DiskMirrorBackEnd"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 diskMirror 适配器
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        // 准备我们需要的文件的描述信息
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", 1);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 设置要获取的文件的文件名字
        jsonObject.put("fileName", "1702811591685.jpg");
        // 设置访问时要使用的 sk 这个数值要与后端服务器设置的一致！
        jsonObject.put("secure.key", 1234);
        // 从 适配器 中获取到数据流对象
        try (
                final InputStream inputStream = adapter.downLoad(jsonObject);
                final FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\zhao\\Desktop\\fsdownload\\backImageFromHttp.svg.jpg")
        ) {
            // 在这里可以使用数据流 数据流中就是我们需要的文件！
            IOUtils.copy(inputStream, fileOutputStream, true);
        }
    }
}
```

#### 2024-03-24 1.1.4 版本发布

> PS:此版本的更新 HTTP 适配器的 `download` 函数有些问题，在新版中已成功解决！

1. 针对每个适配器对象，新增 `download` 函数，能将一个文件以数据流的方式获取到，这能够最大程度上的提升文件获取的灵活性，避免了被
   url 协议所限制的问题！
2. 为了避免 sk 泄漏，在异常发生时 如果涉及到 sk 的输出，则会自动被进行掩码掩盖！
3. 日志 logo 小更新！

#### 2024-02-17 1.1.3 版本发布

1. 新增 CHAR_SET 配置项 能够在将一个字符串保存的时候指定操作字符集
2. 适配器中新增 `writer` 函数 能够写入一个字符串，并将字符串保存为一个文件，下面是一个示例

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        final JSONObject text = adapter.writer(
                "这是一个即将会被写进文件中的字符串！！！！",
                // 设置在 diskMirror 空间中用于存储字符串的文件名字
                "test.txt",
                // 设置用于存储数据的 diskMirror 空间的id
                1,
                // 设置文件类型
                "text",
                // 设置需要使用的安全密钥 由于我们没有设置密钥 所以写为 空
                0
        );
        System.out.println(text);
    }
}
```

下面就是运行结果

```json
{
  "fileName": "test.txt",
  "userId": 1,
  "type": "text",
  "secure.key": 0,
  "useAgreement": true,
  "res": "ok!!!!",
  "url": "http://localhost:8080/1/text/test.txt",
  "useSize": 63,
  "maxSize": 134217728
}
```

3. 针对实例化盘镜时 依赖丢失的错误进行了详细的解答和处理，并给出了依赖的 xml 配置。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 在 没有引入 httpclient 的情况下直接获取到 DiskMirrorHttpAdapter 会直接报错 并在错误信息中告知错误
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        final JSONObject text = adapter.upload(null, null);
        /*
        下面是报错信息
Exception in thread "main" java.lang.UnsupportedOperationException: 不支持您进行【DiskMirrorHttpAdapter】适配器的实例化操作，因为您的项目中缺少必须的依赖，下面是依赖信息
You are not supported to instantiate the [DiskMirrorHttpAdapter] adapter because your project lacks the necessary dependencies. Here is the dependency information
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
            <version>x.x.x</version>
        </dependency>
        <dependency>
            <groupId>io.github.BeardedManZhao</groupId>
            <artifactId>zhao-utils</artifactId>
            <version>x.x.x</version>
        </dependency>
        <!-- 如果您要使用 DiskMirrorHttpAdapter 请添加 httpClient 核心库 -->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>x.x.x</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpmime</artifactId>
            <version>x.x.x</version>
        </dependency>
	at top.lingyuzhao.diskMirror.core.DiskMirror.getAdapter(DiskMirror.java:217)
	at top.lingyuzhao.diskMirror.core.DiskMirror.getAdapter(DiskMirror.java:235)
	at top.lingyuzhao.diskMirror.test.MAIN.main(MAIN.java:16)

    进程已结束,退出代码1

         */
    }
}
```

4. 针对函数中 jsonObject 参数为 null 的校验进行处理，如果发现为 null 则直接报错，报错信息示例如下所示。

```
Exception in thread "main" java.lang.UnsupportedOperationException: 您提供的 json 对象为空，diskMirror 拒绝了您的访问
The json object you provided is empty, and diskMirror has denied your access
error json = null
	at top.lingyuzhao.diskMirror.core.Adapter.checkJsonObj(Adapter.java:24)
	at top.lingyuzhao.diskMirror.core.FSAdapter.upload(FSAdapter.java:243)
	at top.lingyuzhao.diskMirror.test.MAIN.main(MAIN.java:17)
```

5. 为所有的适配器新增了一个`setSpaceMaxSize`函数，用于判断指定空间的最大容量，单位是字节，下面是一个示例。

```java
package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 将 1024 号空间的最大容量修改为 256MB
        adapter.setSpaceMaxSize("1024", 256 << 10 << 10);
        // 查看 1 号空间 和 1024 号空间的最大容量
        System.out.println(adapter.getSpaceMaxSize("1")); // 134217728
        System.out.println(adapter.getSpaceMaxSize("1024")); // 268435456
    }
}
```

6. 针对 HTTP 适配器的 `getSpaceMaxSize` 进行优化和重写，让其能够直接从远程 diskMirror 服务器获取数据，而不是从本地获取（需要远程的
   diskMirror 后端服务器的包 是在 2024年02月17日
   以及以后发布的！！！）。

```java
package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // TODO 设置 DiskMirror 远程 后端 服务器地址 这个是需要搭建的哦!!!
        // 具体的后端服务器搭建 可以阅读：https://github.com/BeardedManZhao/DiskMirrorBackEnd.git
        fsDefaultFS = "https://xxx/DiskMirrorBackEnd"
)
public final class MAIN {
    public static void main(String[] args) {
        // 获取到 远程 diskMirror 适配器
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        // 获取到 远程服务器版本
        System.out.println(adapter.version());
        // 查看 1 25 空间的 maxSize
        System.out.println(adapter.getSpaceMaxSize("1"));
        System.out.println(adapter.getSpaceMaxSize("25"));
    }
}
```

----

#### 2024-02-08 1.1.2 版本发布

1. 新增diskMirror 盘镜 后端服务器的适配器，通过该适配器您可以直接远程操作 diskMirror
   的后端服务，有关后端服务器版本的说明请查阅 [diskMirror 后端服务器版本](https://github.com/BeardedManZhao/DiskMirrorBackEnd.git)

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // TODO 设置 DiskMirror 远程 后端 服务器地址 这个是需要搭建的哦!!!
        // 具体的后端服务器搭建 可以阅读：https://github.com/BeardedManZhao/DiskMirrorBackEnd.git
        fsDefaultFS = "http://192.168.1.15:8778/DiskMirrorBackEnd"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 远程 diskMirror 适配器
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        // 获取到 远程服务器版本
        System.out.println(adapter.version());
        // 获取到用户空间 1 的文件系统结构
        show(adapter, 1);

        // 上传一个文件到 1 空间
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1);
        jsonObject.put("type", Type.Binary);
        jsonObject.put("secure.key", 9999);
        jsonObject.put("fileName", "test.jpg");
        final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\无标题.jpg");
        final JSONObject upload = adapter.upload(fileInputStream, jsonObject);
        System.out.println(upload);

        // 再次读取
        show(adapter, 1);
    }

    public static void show(Adapter adapter, int userId) throws IOException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("type", Type.Binary);
        // 您也可以设置密钥 用于让服务器确认你的身份
        jsonObject.put("secure.key", 9999);
        System.out.println(adapter.getUrls(jsonObject));
    }
}
```

下面就是运行结果

```
top.lingyuzhao.diskMirror.core.DiskMirrorHttpAdapter@2d7275fc:V1.1.2
 ↓↓↓ 
remote → ↘↘
             'WWWKXXXXNWWNk,     ,kkd7               KWWb,                     
             ;WWN3.....,lNWWk.                       KWWb,                     
             ;WWNl        XWWk.  :XXk,   oKNNWNKo    KWWb,   dXXO:             
             ;WWNl        3WWX7  7WWO7  0WWo:,:O0d,  KWWb, lNWKb:              
             ;WWNl        :WWNl  7WWO7  0WWO,.       KWWbbXWKb:.               
             ;WWNl        kWW03  7WWO7   lXWWWN0o.   KWWNWWW0;                 
             ;WWNl       lWWNo,  7WWO7     .,7dWWN;  KWWOolWWN7                
             'WWNo,..,'oXWWKo'   7WWO7 .lb:    XWNl. KWWb, .KWWk.              
             ;WWWWWWWWWNKOo:.    7WWO7  oNWX0KWWKb:  KWWb,   bWWX'             
              ,'''''''',,.        ,'',    ,;777;,.    '''.    .''',            
KWWNWK,        ,WWNWWd.   ;33:                                                 
KWWbWWO.       XWXkWWd.   ...    ...  .,,   ...  ,,.      .,,,,        ...  .,,
KWWodWWd      OWNlOWWd.  .WWN7   KWW3OWNWl.:WWOlNWNO:  3KWWXXNWWXo.   ,WWX3XWNK
KWWo.OWWo    oWWb;xWWd.  .WWXl   0WWXkl',, ;WWNKb:,,, XWWkl,..,oWWN'  ,WWNKd7,,
KWWo  XWN7  ;WWx3 dWWd.  .WWXl   0WWO3     ;WWWl,    bWW03      OWWk, ,WWWo'   
KWWo  ,NWK',NW0l  dWWd.  .WWXl   0WWd,     ;WWX3     kWWO:      dWMO: ,WWNl    
KWWo   ;WWkKWXl.  dWWd.  .WWXl   0WWd.     ;WWK7     7WWX7      XWWd; ,WWN3    
KWWo    lWWWNo,   dWWd.  .WWXl   0WWd.     ;WWK7      oWWX3,.,7XWWk3  ,WWN3    
kXXo     dXXd:    oXXb.  .KX0l   xXXb.     'KXO7       .o0XNNNXKkl'   .KXKl    
LocalFSAdapter:1.1.1
{"userId":1,"type":"Binary","secure.key":9999,"useSize":199408,"useAgreement":true,"maxSize":134217728,"res":"空间 [D:\\DiskMirror\\data\\1\\Binary] 不可读!!!"}
{"userId":1,"type":"Binary","secure.key":9999,"fileName":"test.jpg","useAgreement":true,"res":"ok!!!!","url":"https://192.168.1.15:8778/DiskMirrorBackEnd/data/1/Binary/test.jpg","useSize":398816,"maxSize":134217728}
{"userId":1,"type":"Binary","secure.key":9999,"useSize":398816,"useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"test.jpg","url":"https://192.168.1.15:8778/DiskMirrorBackEnd/data/1/Binary//test.jpg","lastModified":1707392017513,"size":199408,"type":"Binary","isDir":false}],"res":"ok!!!!"}

进程已结束,退出代码0
```

----

#### 2024-02-01 1.1.1 版本发布

1. 修复 HDFS 文件系统中删除函数返回结果中的 文件系统使用情况数值错误的问题。
2. 支持使用注解进行配置，且支持创建文件目录，下面是一个使用 注解配置获取适配器，并在 HDFS 文件系统中创建一个文件目录的需求
3. 针对路径生成逻辑进行优化，减少了不必要的计算

```java

package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

@DiskMirrorConfig(
        // TODO 设置 HDFS 协议前缀 在这里指向的是 HDFS 文件系统访问地址 是 hdfs 开头
        fsDefaultFS = "hdfs://192.168.0.141:8020",
        // TODO 设置 路径的协议前缀 在这里指向的是 HDFS 文件系统 盘镜目录 访问地址
        protocolPrefix = "http://192.168.0.141:9870/webhdfs/v1/DiskMirror",
        // TODO 设置请求参数 HDFS 要求需要设置 OP 参数（此参数只会在读取的时候生效）
        params = "{\"op\":\"OPEN\"}"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // TODO 装载到适配器 在这里使用HDFS文件系统
        final Adapter adapter = DiskMirror.HDFSAdapter.getAdapter(MAIN.class);
        System.out.println(DiskMirror.LocalFSAdapter.getVersion());
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileName", "MyLevel1/myDir");
        jsonObject.put("userId", 1024);
        jsonObject.put("type", Type.Binary);
        final JSONObject mkdirs = adapter.mkdirs(jsonObject);
        System.out.println(mkdirs);
    }
}
```

----

#### 2024-01-21 1.1.0 版本发布【稳定版本】

1. 修正依赖组件重复的问题，需要注意的是，您的在这里的 zhao-utils 依赖应在 1.0.20240121 及 以上版本!!! 新版本的工具类修正了一些
   bug
2. 针对 重命名失败 的错误信息进行详细的解答。
3. 提供了密钥的生成设置，设置密钥之后 将必须要使用密钥访问，这增加了安全性!

----

#### 2024-01-21 1.0.9 版本发布

1. 针对所有的操作，支持了文件夹，例如您可以获取到指定文件夹中的所有文件，下面是一个获取带有目录的空间 url 的json

```
   public static void main(String[] args) throws IOException {
   // 准备适配器对象
   final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(new Config());
   // 准备参数
   final JSONObject jsonObject = new JSONObject();
   // 设置文件所属空间id
   jsonObject.put("userId", 1024);
   // 设置文件类型 根据自己的文件类型选择不同的类型
   jsonObject.put("type", Type.Binary);
   System.out.println(adapter.getUrls(jsonObject));
   }
```

下面是打印出来的结果

```json
 {
  "userId": 1024,
  "type": "Binary",
  "useSize": 787141,
  "useAgreement": true,
  "maxSize": 134217728,
  "urls": [
    {
      "fileName": "fsdownload",
      "url": "http://localhost:8080/1024/Binary//fsdownload",
      "lastModified": 1705762229601,
      "size": 0,
      "type": "Binary",
      "isDir": true,
      "urls": [
        {
          "fileName": "myFile.png",
          "url": "http://localhost:8080/1024/Binary//fsdownload/myFile.png",
          "lastModified": 1705762229664,
          "size": 293172,
          "type": "Binary",
          "isDir": false
        }
      ]
    },
    {
      "fileName": "test.png",
      "url": "http://localhost:8080/1024/Binary//test.png",
      "lastModified": 1702903450767,
      "size": 493969,
      "type": "Binary",
      "isDir": false
    }
  ],
  "res": "ok!!!!"
}
```

2. 针对所有的删除操作 同样支持删除文件夹 值得一提的是 API 与旧版本使用方法一致

```
   public static void main(String[] args) throws IOException {
        // 准备适配器对象
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(new Config());
        // 准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 设置需要被删除的文件夹名字
        jsonObject.put("fileName", "fsdownload");
        // 删除这个文件夹
        final JSONObject result = adapter.remove(jsonObject);
        System.out.println(result);
   }
```

3. 针对所有的上传操作 也支持文件夹，API 使用方法与就版本一致

```
   public static void main(String[] args) throws IOException {
        // 准备适配器对象
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(new Config());
        // 准备文件数据流
        final InputStream inputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\图片1.png");
        // 准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 设置需要被上传的文件名字 如果我们希望上传到 fsdownload 目录中 可以像下面一样写
        jsonObject.put("fileName", "fsdownload/myFile.png");
        // 删除这个文件夹
        final JSONObject result = adapter.upload(inputStream, jsonObject);
        System.out.println(result);
        // 关闭数据流
        inputStream.close();
   }
```

----

#### 2024-01-04 1.0.8 版本发布

1. 针对所有的操作，返回的json中包含了 maxSize 参数，代表的就是当前操作的空间的最大容量

----

#### 2023-12-22 1.0.7 版本发布

1. 能够通过适配器对于文件系统中的文件进行重命名操作

----

#### 2023-12-19 1.0.6 版本发布

1. 针对所有的操作返回值都增加了实时文件空间占用字节数"useSize"的结果
2. 针对所有已存在的文件进行增删将会抛出错误，您可以不去进行文件是否存在的检测
3. 针对稳定性进行升级，修复了部分bug
4. 针对文件上传的接口增加了文件大小限制的配置项目，默认为 128Mb

### 可能出现的问题

#### 文件系统类问题

当我们使用 diskMirror 对接文件系统的时候，如果遇到了文件系统抛出的问题，则 diskMirror 会原样的抛给用户，且将堆栈包含在其中，diskMirror
不会对这类情况进行处理，因为这类情况与文件系统有关，diskMirror 不得修改任何有关文件系统的配置，这是为了安全考虑！

| 可能出现的情况 | 代表意义                                                                                     | diskMirror 是否干涉 |
|---------|------------------------------------------------------------------------------------------|-----------------|
| 权限问题    | 文件系统不允许我们使用 diskMirror 去操作，您只需要提升 diskMirror 的程序权限即可，确保diskMirror 的根目录可以被 diskMirror 读写！ | false           |
| 文件找不到   | 文件系统中没有找到您需要的文件，您可以检查一下文件是否存在，或者检查一下文件名是否正确！                                             | false           |

#### 依赖问题

在 diskMirror 中有诸多的组件，某些组件可能会需要使用到外部的依赖，在这里如果依赖导入的不全，会直接给出依赖的详细信息，有着完整的报错机制，您无需担心！

#### API 使用问题

API 的说明文档某些地方若描述不清晰，让您的使用难度增加，或者您有什么建议，您可以联系作者或者发布 issue，作者会尽快处理！

### 更多详细
----

- diskMirror starter SpringBoot：https://github.com/BeardedManZhao/diskMirror-spring-boot-starter.git
- diskMirror 后端服务器版本：https://github.com/BeardedManZhao/DiskMirrorBackEnd.git
- diskMirror Java API 版本：https://github.com/BeardedManZhao/DiskMirror.git
