![logo](https://github.com/user-attachments/assets/0b05e34b-96e3-44ec-8a69-bb9b82059a07)

[<img src="https://api.gitsponsors.com/api/badge/img?id=729754597" height="20">](https://api.gitsponsors.com/api/badge/link?p=Kb4oezU+2A832W0+VM9ivjADnHekHcp3XbfbuoHa8RTBVo9kXlcWASHs6MuoXAtTvfnZdJUUW2DpGzmFGSp7FEEe2SAe2Z9lgGgucBdQvlU3gWvKqUeif4zMz6138lzPZsctY5fRwr0KhdEcvynrUA==)

# DiskMirror

盘镜（DiskMirror）是一款开源文件系统管理框架，旨在通过提供一系列适配器来简化不同存储系统间的文件操作。无论是本地文件系统、Hadoop分布式文件系统（HDFS），还是自定义的远程存储解决方案，盘镜都能帮助开发者轻松地进行统一管理和操作。通过减少重复编码工作量，提高开发效率，盘镜为现代应用提供了灵活且高效的文件存储解决方案。

## 社区 qq 群

> 大家可以直接从这里进入来咨询作者和交流哦！！

![无标题](https://github.com/user-attachments/assets/0d33aa4c-099e-4ac1-9f0a-0ec48199da15)

## 核心功能

* **统一接口**：盘镜定义了一套标准API，无论底层使用哪种存储技术，应用程序都可以一致地访问这些服务。

* **多种适配器支持**：包括本地文件系统适配器、HDFS适配器、HTTP适配器等，以及用于集群环境的DM_DfsAdapter和基于TCP协议的适配器。

* **数据安全与加密**：允许设置密钥保护文件系统的操作，增强安全性。

* **用户空间管理**：支持按用户ID和文件类型管理存储空间，便于资源分配与监控。

* **集群支持**：通过集群适配器实现跨多个节点的数据管理和故障转移，提高系统的可靠性和可扩展性。

## 什么是适配器

适配器在这里是用于进行文件传输的桥梁，能够让您将自己的数据源（例如您的后端服务器）与指定的数据终端（例如您的各类文件系统）进行连接，将数据提供给数据终端，减少了您手动开发IO代码的时间。

例如您要开发一个涉及到数据上传或保存的网站，您的网站后端通常需要涉及到与文件系统之间的交互，而您可以使用盘镜完成您的大部分需求，例如上传之后返回
url 等操作，这会大大减少您开发IO代码的时间。

在未来，我们将会提供更多的适配器选项，让适配器的数据终端具有更多的支持。

## 为什么要选择 diskMirror

* **简化IO操作**：降低了文件上传、下载及管理代码的复杂度。
* **灵活部署**：支持Maven依赖、HTTP服务器部署等多种方式，适应不同的开发环境。
* **易于集成**：提供清晰的文档和示例代码，帮助开发者快速上手。
* **在线服务**：提供团队的专属服务，您可前往 https://www.lingyuzhao.top/LS-WebBackEnd/Article/6401810956434550 查询！

## 使用场景

* **Web应用**：简化网站后台文件管理逻辑，如图片、视频等多媒体内容的上传与分发。
* **大数据处理**：在Hadoop生态中，作为文件管理系统的一部分，提高数据处理效率。
* **云存储服务**：连接不同的云存储提供商，提供统一的访问接口。
* **企业级应用**：适用于需要对大量文件进行集中管理的企业内部系统。

## 关于 diskMirror 的使用！

### 支持哪些适配器

所有支持的适配器都会在 `top.lingyuzhao.diskMirror.core.DiskMirror` 类中找到，您可以查阅类文件，也可以在这里查看已支持的适配器的表格。

| 适配器                              | 自从何时支持 | 需要引入的外部依赖（fastjson2 和 zhao-utils 除外） | 功能介绍                                                                                                                                                                                          |
|----------------------------------|--------|--------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| DiskMirror.LocalFSAdapter        | v1.0   | 无外部依赖                                | 能够将适配器运行所在的主机的磁盘做为 diskMirror 管理，一般来说是作用于本机的本地文件系统磁盘管理。                                                                                                                                       |
| DiskMirror.HDFSAdapter           | v1.0   | hadoop-client                        | 将 HDFS 文件系统提供给 diskMirror 管理，能够通过 diskMirror 操作 HDFS                                                                                                                                          |
| DiskMirror.DiskMirrorHttpAdapter | v1.1.2 | httpclient, httpmime                 | 将 [后端版本的 diskMirror](https://github.com/BeardedManZhao/DiskMirrorBackEnd.git) 接入到 diskMirror 管理，能够通过 diskMirror 操作 [后端 diskMirror]((https://github.com/BeardedManZhao/DiskMirrorBackEnd.git)) |
| DiskMirror.DM_DfsAdapter         | v1.1.8 | DiskMirror                           | 集中管理多个 适配器 的集群适配器，能够实现将多个适配器合并管理，实现集群的效果                                                                                                                                                      |
| DiskMirror.TCP_Adapter           | v1.2.1 | 与子适配器一致                              | 使用TCP协议监听两个端口分别接收指令以及文件数据                                                                                                                                                                     |
| DiskMirror.TCP_CLIENT_Adapter    | v1.2.1 | 无外部依赖                                | 向指定的TCP适配器的两端口分别发送指令以及文件数据实现远程控制                                                                                                                                                              |

### 我如何获取 盘镜

您可以通过配置 maven 依赖的方式实现库的获取，下面就演示了如何获取到 盘镜。

> 库的 maven 版本在一般情况下是与 diskMirror 内部的版本号一致的，您可以在 [更新列表](#-更多详细) 中查询到支持 maven
> 下载的版本，若不存在于列表中的版本，则会自动尝试使用相近的 SNAPSHOT 版本，若您需要使用 SNAPSHOT 版本。
> SNAPSHOT 版本是一些已发布版本的中间过渡版本，他们最显著的特点就是框架内部的版本号与maven版本号不一致！
> 因此，若您没有特殊的需求，请在[更新列表](#-更多详细) 中选择您需要的版本哦！！！

```xml

<dependencies>
    <dependency>
        <groupId>io.github.BeardedManZhao</groupId>
        <artifactId>diskMirror</artifactId>
        <version>1.5.1</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba.fastjson2</groupId>
        <artifactId>fastjson2</artifactId>
        <version>2.0.25</version>
    </dependency>
    <!-- 从 disk Mirror 1.4.3 版本开始，此依赖为必选依赖，它的作用是为 diskMirror 对于一些空间的存储使用 redis 能够避免 HashMapper 存储配置数据冲突的情况 -->
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>4.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-pool2</artifactId>
        <version>2.11.1</version> <!-- 使用最新稳定版本 -->
    </dependency>
    <!-- 从 disk Mirror 1.1.0 版本开始 请确保 zhao-utils 的版本 >= 1.0.20240121 -->
    <!-- 从 disk Mirror 1.3.0 版本开始 请确保 zhao-utils 的版本 >= 1.0.20241026 -->
    <!-- 从 disk Mirror 1.5.1 版本开始 请确保 zhao-utils 的版本 >= 1.2.20250430 -->
    <dependency>
        <groupId>io.github.BeardedManZhao</groupId>
        <artifactId>zhao-utils</artifactId>
        <version>1.2.20250430</version>
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

## 基本使用示例 与 API 说明

在开始之前，我们需要了解下这个库的字段，它的函数接收的大部分是一个 json 对象 其具有几个字段，下面就是有关字段的解释。

> 在下面的表展示的就是各种常见的返回结果中的字段，diskMirror 中所有的返回值中，如果存在某个字段，则字段一定遵循下面的规则！
> 当然，您设置的也需要遵循下面的规则！
> 值得一提的是，不论是 [DiskMirror后端服务器](https://github.com/BeardedManZhao/diskmirror-backEnd-spring-boot.git)
> 还是 [DiskMirrorTCP服务器](http://github.com/BeardedManZhao/diskMirror-backEnd-tcp-Java.git)，都遵循下面的规则，您可以直接使用
> API 与其对接~

| 参数名称         | 参数类型    | 参数解释                                                              | 参数类型  |
|--------------|---------|-------------------------------------------------------------------|:------|
| secure.key   | int     | diskMirror 允许您对于文件系统的操作设置密钥，如果您在启动的配置中设置了密钥，则您需要在该字段写入密钥          | 输入    |
| fileName     | String  | 被落盘的文件名称 或者 您要操作的文件的路径（绝对路径）                                      | 输入&输出 |
| useSize      | long    | 当前用户空间的某个类型的文件总共使用量，按照字节为单位                                       | 输出    |
| userId       | int     | 落盘文件所在的空间id                                                       | 输入    |
| type         | String  | 落盘文件所在的空间的类型                                                      | 输入    |
| res          | String  | 落盘结果正常/错误信息                                                       | 输出    |
| url          | String  | 落盘文件的读取 url 这个url 会根据协议前缀拼接字符串                                    | 输出    |
| urls         | array   | 一般来说，这个代表的是一个目录中包含的所有子文件/目录对象的列表，常常会在 [`getUrls`](#从适配器中读取数据) 中见到 | 输出    |
| maxSize      | long    | 当前用户空间中某个类型的文件最大使用量，按照字节为单位                                       | 输出    |
| useAgreement | boolean | 是否使用了协议前缀 如过此值不为 undefined/null 且 为 true 则代表使用了协议前缀 否则代表没有使用协议前缀  | 输出    |
| suffix       | String  | 如果请求被 `HandleModule.ImageCompressModule` 处理过，则可能会包含此字段，字段值为文件的后缀名 | 输出    |

接下来我们将演示如何使用 盘镜 进行一些基本的 CRUD 操作，能够实现在文件系统中的文件管理操作。

### 实例化适配器

适配器就是在 盘镜 中进行数据的传输的通道，而如果想要使用 盘镜，就需要实例化适配器，下面我们将演示如何实例化本地文件系统的适配器。

> 本文章中有 JSONObject 和 DiskMirrorRequest 两种请求构造方法，DiskMirrorRequest是一种语法糖，相较于构建 json
> 要简单和简洁，之所以要在这里展示。
> json 的请求也并不是完全无用，如果您需要在没有被我们封装过的环境中对接 diskMirror 的服务器，您可以根据这里的 json
> 请求来构造请求！

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

#### 文件数据流写入方式

您可以直接将你的数据流提供给盘镜，然后盘镜会自动的将数据落盘，并返回一个 url 和文件的信息给您。

```java

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // 配置根目录 也是能够被盘镜 管理的目录，所有的管理操作只会在这个目录中生效，默认是/DiskMirror!
        // rootDir = "/DiskMirror",
        // 配置所有的 url 中的协议前缀，这会影响 getUrls 的结果， 如果您只是在本地文件系统中获取这些数据 就是文件系统的协议前缀，也就是什么都不加
        // 如果您要在 hdfs 文件系统中获取这些数据 这就是 hdfs 的协议前缀
        // 如果您要在 web JS 或者通过 url 中获取这些数据 这就是 web 的 http 协议前缀
        // 在这里我们给定空字符串就是代表使用本地文件系统
        protocolPrefix = ""
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 开始构建盘镜 由于我们在这里使用的是本地文件系统 所以我们使用 DiskMirror.LocalFSAdapter.getAdapter(config) 来实例化 本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
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

#### 将链接中的数据写入

这是一种针对文件上传操作的二开组件，其可以实现将一个 url 解析并写入盘镜的操作，它相对于文件流写入的方式具有一些标记的操作。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

@DiskMirrorConfig(
        rootDir = "/DiskMirror/"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        System.out.println(DiskMirror.VERSION);
        // 准备参数 把 url 放到参数中
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "https://github.com/BeardedManZhao/DiskMirror/assets/113756063/b8a15b22-5ca0-4552-aab2-7131c63dc727");
        jsonObject.put("fileName", "test.txt");
        jsonObject.put("userId", 1);
        jsonObject.put("type", Type.Binary);

        // 准备适配器对象
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 开始进行转存
        final JSONObject jsonObject1 = adapter.transferDeposit(jsonObject);
        // 打印转存好的文件的信息
        System.out.println(jsonObject1);
    }
}
```

接下来我们将演示有关适配器中转存数据的一些标记操作，在转存状态中的文件，是可以直接获取到对应文件的状态 json
对象的，接下来就是一个示例，我们使用多线程的方式将被转存的文件放到另一个线程中进行转存，然后我们调用了 `transferDepositStstus`
函数
获取到了当前正在转存的文件。

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

@DiskMirrorConfig(
        rootDir = "/DiskMirror/"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 准备参数 把 url 放到参数中
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "https://s01.oss.sonatype.org/content/repositories/releases/io/github/BeardedManZhao/diskMirror/1.1.9/diskMirror-1.1.9.jar");
        jsonObject.put("fileName", "diskMirror-1.1.9.jar");
        jsonObject.put("userId", 1);
        jsonObject.put("type", Type.Binary);
        // 准备适配器对象
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);

        // 使用一个线程进行转存记录的查看 因为我们要测试查看线程转存状态的小玩意，因此在这里就需要保持转存的同时调用 transferDepositStatus
        new Thread(() -> {
            try {
                // 使用这个 确保转存操作已开始
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            show(adapter, jsonObject);
        }).start();

        // 开始进行转存
        System.out.println("开始转存！");
        final JSONObject jsonObject1 = adapter.transferDeposit(jsonObject);
        // 打印转存好的文件的信息
        System.out.println("转存完毕！" + jsonObject1);
        // 转存完毕再打印一下看看
        show(adapter, jsonObject);
    }

    public static void show(Adapter adapter, JSONObject jsonObject) {
        adapter.transferDepositStatus(jsonObject).forEach((k, v) -> System.out.println("正在保存的文件：k" + "\t文件对应的链接：" + v));
    }
}
```

下面就是程序运行之后的结果

```
开始转存！
正在保存的文件：k	文件对应的链接：https://s01.oss.sonatype.org/content/repositories/releases/io/github/BeardedManZhao/diskMirror/1.1.9/diskMirror-1.1.9.jar
转存完毕！{"fileName":"diskMirror-1.1.9.jar","userId":1,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"http://localhost:8080/1/Binary/diskMirror-1.1.9.jar","useSize":8617,"maxSize":134217728}

```

### 从适配器中读取数据

在这里我们将演示如何从适配器中读取数据，并将读取的数据的 url
返回给您，当您从适配器中读取数据的时候，盘镜中的适配器将自动的根据您设置的各种参数将文件系统结构返回给您!

#### 读取完整的文件系统目录

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

@DiskMirrorConfig(protocolPrefix = "")
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 使用适配器获取到文件系统结构
        final JSONObject read = read(adapter, 1024);
        System.out.println(read);
    }

    private static JSONObject read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        // 设置文件所属空间id 以及 设置文件类型 根据自己的文件类型选择不同的类型
        DiskMirrorRequest urls = DiskMirrorRequest.getUrls(userId, Type.Binary);
        return adapter.getUrls(urls);
    }
}
```

在下面就是读取出来的 json 以及所有参数的解释

```json
{
  "userId": 1024,
  "type": "Binary",
  "useSize": 13374,
  "useAgreement": false,
  "maxSize": 134217728,
  "urls": [
    {
      "fileName": "default_notpublic_img2.png",
      "url": "/1024/Binary//default_notpublic_img2.png",
      "lastModified": 1738592233961,
      "size": 6687,
      "type": "Binary",
      "isDir": false
    },
    {
      "fileName": "test",
      "url": "/1024/Binary//test",
      "lastModified": 1744447392575,
      "size": 0,
      "type": "Binary",
      "isDir": true,
      "urls": [
        {
          "fileName": "default_notpublic_img2.png",
          "url": "/1024/Binary//test/default_notpublic_img2.png",
          "lastModified": 1738592233961,
          "size": 6687,
          "type": "Binary",
          "isDir": false
        }
      ]
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

#### 读取指定目录下的文件

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

@DiskMirrorConfig(protocolPrefix = "")
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 使用适配器获取到文件url
        final JSONObject read = read(adapter, 1024);
        System.out.println(read);
    }

    private static JSONObject read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        // 设置文件所属空间id 以及 设置文件类型 根据自己的文件类型选择不同的类型 以及目录 如果是空字符串就代表是 根目录
        DiskMirrorRequest urlsNoRecursion = DiskMirrorRequest.getUrlsNoRecursion(userId, Type.Binary, "");
        return adapter.getUrlsNoRecursion(urlsNoRecursion);
    }
}
```

下面是返回结果

```json
{
  "userId": 1024,
  "type": "Binary",
  "fileName": "",
  "useSize": 13374,
  "useAgreement": false,
  "maxSize": 134217728,
  "urls": [
    {
      "fileName": "default_notpublic_img2.png",
      "url": "/1024/Binary//default_notpublic_img2.png",
      "lastModified": 1738592233961,
      "size": 6687,
      "type": "Binary",
      "isDir": false
    },
    {
      "fileName": "test",
      "url": "/1024/Binary//test",
      "lastModified": 1744447392575,
      "size": 0,
      "type": "Binary",
      "isDir": true
    }
  ],
  "res": "ok!!!!"
}
```

### 从适配器中获取数据的数据流对象 下载文件

在 1.1.4 版本之后，diskMirror 的文件存储可以获取到数据流对象，而不是只能使用 url 下载文件，这有助于您在使用 diskMirror
的时候，可以使用更灵活的下载方式，也可以避免一些配置！

在下面就是一个使用的示例！

```java
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 diskMirror 适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 准备我们需要的文件的描述信息
        DiskMirrorRequest download = DiskMirrorRequest.download(
                // 分别是 用户id，文件类型，文件名
                1024, Type.Binary, "default_notpublic_img2.png"
        );
        // 获取到数据流对象
        try (
                final InputStream inputStream = adapter.downLoad(download);
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

新版本建议使用下面的操作，他们更加简单，提示更丰富！性能与之前无异。

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.diskMirror.core.filter.FileMatchManager;

import java.io.IOException;

/**
 * @author zhao
 */
// 这个注解就可以代替 Config 但 如果坚持使用 Config 则可以忽略此注解
@DiskMirrorConfig
public class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到Adapter
        Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 开始执行删除任务
        JSONObject jsonObject = adapter.remove(
                // 构建删除请求对象 这里我们的删除操作是要作用在 1024 空间 Binary类型下的 arc.png 文件
                DiskMirrorRequest.uploadRemove(1024, Type.Binary, "arc.png")
                        // 如果 arc.png 是文件 则 filter 不生效 而是直接删除
                        // 在这里使用的是允许所有，要求删除所有文件
                        .setFilter(FileMatchManager.ALLOW_ALL, null)
        );
        System.out.println(jsonObject);
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

更建议使用新版本的方法，更加简洁

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

@DiskMirrorConfig(
        // 配置根目录 也是能够被盘镜 管理的目录，所有的管理操作只会在这个目录中生效，默认是/DiskMirror!
        // rootDir = "/DiskMirror" 默认的不需要设置
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 开始查询 1024 空间目录
        System.out.println(read(adapter, 1024));
        // 对目录中的文件 《defimage2.svg.png》 重命名为 test.png
        System.out.println(reName(adapter, 1024, "defimage2.svg.png", "test.png"));
        // 再一次查询 1024 空间目录
        System.out.println(read(adapter, 1024));
    }

    private static JSONObject read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        DiskMirrorRequest urls = DiskMirrorRequest.getUrls(userId, Type.Binary);
        return adapter.getUrls(urls);
    }

    private static JSONObject reName(Adapter adapter, int userId, String oldFileName, String newFileName) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.reName(userId, Type.Binary, oldFileName, newFileName);
        return adapter.reName(diskMirrorRequest);
    }
}
```

### 通过适配器创建一个文件目录

从 diskMirror 1.1.1 版本开始 针对文件目录的创建功能开始被支持，1.0.0 版本之后，diskMirror 能够实现路径的功能，提高了灵活性，而在最新的
1.1.1 版本中，可以显式的创建一个文件目录。

```java
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 准备参数
        DiskMirrorRequest myDir = DiskMirrorRequest.mkdirs(1024, Type.Binary, "MyDir");
        // 直接创建
        // 结果 {"userId":1024,"type":"Binary","dirName":"MyDir","fileName":"MyDir","useSize":20061,"res":"ok!!!!"}
        System.out.println(adapter.mkdirs(myDir));
        // 查看文件系统结构 这里由于只需要 userId type 而恰巧在上面我们设置好了 所以直接将上面的json 输入
        // 是的哦 可以复用请求！！！只要必要的参数存在就可以 如果您不确定请求是否可用 也可以使用 DiskMirrorRequest.getUrls(1024, Type.Binary)
        // 结果 {"userId":1024,"type":"Binary","dirName":"MyDir","fileName":"MyDir","useSize":20061,"res":"ok!!!!","useAgreement":true,"maxSize":134217728,"urls":[]}
        System.out.println(adapter.getUrlsNoRecursion(myDir));
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

### 模块管理器

在 1.3.3 版本中，我们对模块管理器进行了重大改进，使其更加灵活和高效。具体改进如下：
模块分离：模块管理器现在独立出来，用户可以直接对其进行操作。
读写校验模块：模块管理器中存储了读写操作的校验模块。每次请求到达时，系统会根据请求类型选择相应的读或写校验模块，并依次进行校验，直至所有校验通过。
增强灵活性：与之前的版本相比，新的模块管理器不再局限于单一的 sk 校验模块，默认追加的校验模块也支持读写分流管控，大大提升了系统的灵活性和可配置性。
接下来，我们将详细介绍如何使用请求校验模块。

#### 将一个校验模块加入到校验模块管理器中

```java
import top.lingyuzhao.diskMirror.core.module.ModuleManager;
import top.lingyuzhao.diskMirror.core.module.SkCheckModule;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        // 实例化 sk 校验模块
        final SkCheckModule skCheckModule = new SkCheckModule(
                // 分别是模块名，以及提示信息
                "skCheck", "用于校验请求中的 sk 信息"
        );

        // 将 skCheckModule 注册到 读操作校验管理模块
        ModuleManager.registerModuleRead(skCheckModule);
        // 将 skCheckModule 注册到 写操作校验管理模块
        ModuleManager.registerModuleWriter(skCheckModule);
    }
}
```

#### 将一个模块从校验器中取出

```java
import top.lingyuzhao.diskMirror.core.module.ModuleManager;
import top.lingyuzhao.diskMirror.core.module.VerificationModule;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        // 尝试从模块管理器移除名为 skCheck 的模块！
        final VerificationModule skCheck0 = ModuleManager.removeVerificationModuleRead("skCheck");
        final VerificationModule skCheck1 = ModuleManager.removeVerificationModuleWriter("skCheck");
        // 如果成功移除的话 这里会打印出数据 如果没有成功移除 这里是 null
        System.out.println(skCheck0);
        System.out.println(skCheck1);
    }
}
```

### 校验模块

此模块是来处理请求的，其可以实现请求中的一些校验，例如校验请求中的参数。

您可以根据自己的需求实现一个自己需要的校验模块，实现操作只需要继承 `Verification` 类即可！

#### 查看校验模块是否支持读或写操作

```java
import top.lingyuzhao.diskMirror.core.module.ModuleManager;
import top.lingyuzhao.diskMirror.core.module.SkCheckModule;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        final SkCheckModule skCheckModule = new SkCheckModule("test", "一个用来测试的模块");
        // 这个时候都是 false 因为其没有被装载到模块管理器中
        System.out.println(skCheckModule.isRead());
        System.out.println(skCheckModule.isWriter());

        // 将模块注册到读模块管理器中
        ModuleManager.registerModuleRead(skCheckModule);

        // 这个时候Read是 true 因为其被装载到读操作模块管理器中
        System.out.println(skCheckModule.isRead());
        System.out.println(skCheckModule.isWriter());
    }
}
```

#### 综合示例

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.diskMirror.core.module.ModuleManager;
import top.lingyuzhao.diskMirror.core.module.SkCheckModule;
import top.lingyuzhao.diskMirror.core.module.VerificationModule;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        secureKey = 123456
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 准备一个适配器 这个时候虽然设置了密钥，但是由于密钥校验模块没被加载，因此密钥将会被忽略
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 尝试进行 url 获取 TODO 这个时候请求中是没有密钥的 但可以成功获取到数据
        final JSONObject urls = adapter.getUrls(DiskMirrorRequest.getUrls(1, Type.Binary));
        System.out.println(urls);

        // 实例化 sk 校验模块
        final SkCheckModule skCheckModule = new SkCheckModule(
                // 分别是模块名，以及提示信息
                "skCheck", "用于校验请求中的 sk 信息"
        );
        // 装载验证模块
        ModuleManager.registerModule(skCheckModule);

        // 尝试进行 url 获取 TODO 现在这个时候是有密钥校验的，因此这个时候会出现错误
        final JSONObject urls1 = adapter.getUrls(DiskMirrorRequest.getUrls(1, Type.Binary));
        System.out.println(urls1);

        // 移除模块 此操作会将注册的对象返回出来
        final VerificationModule skCheck = ModuleManager.removeVerificationModuleRead("skCheck");
        final VerificationModule skCheck1 = ModuleManager.removeVerificationModuleWriter("skCheck");
        System.out.println("被取出的读操作模块：" + skCheck);
        System.out.println("被取出的写操作模块：" + skCheck1);

        // 尝试进行 url 获取 TODO 这个时候校验就消失了
        final JSONObject urls2 = adapter.getUrls(DiskMirrorRequest.getUrls(1, Type.Binary));
        System.out.println(urls2);
    }
}
```

### 空间设置映射器

在上面描述的一些对于某个空间单独的设置操作，其是存储在 `HashMapper` 中的，事实上，您还可以指定一个空间设置映射器，其是存储在其它平台，在这里我们会演示！

> 您如果不设置，则会使用默认的 HashMapper 进行存储

#### 使用 redis 进行空间配置信息的存储

```java
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // 设置默认的空间大小，这个空间设置会作用在所有的空间中
        userDiskMirrorSpaceQuota = 1024,
        // 设置存储空间配置使用redis存储（如 空间的大小，空间的sk）
        useSpaceConfigMode = "JedisMapper",
        // 设置redis的配置
        redisHostPortDB = "redis.lingyuzhao.top:46379:0",
        redisPassword = "38243824"
)
public final class MAIN {
    public static void main(String[] args) {
        try (final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class)) {
            // 直接获取就是默认的配置
            long spaceMaxSize = adapter.getSpaceMaxSize("1");
            System.out.println(spaceMaxSize);

            // 我们可以为某个空间单独设置这个最大使用量
            adapter.setSpaceMaxSize("1", 1024 * 1024 * 1024);
            // 再获取就是1024 * 1024 * 1024
            spaceMaxSize = adapter.getSpaceMaxSize("1");
            System.out.println(spaceMaxSize);

            System.out.println(adapter.getSpaceMaxSize("2"));
        }
    }
}
```

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

### diskMirror http 适配器

diskMirror 提供了后端服务器的版本，因为实际开发任务中我们经常需要使用 http 服务器来进行存储工作，因此 diskMirror
后端服务器由此出现，且提供了一个 JS 文件用来操作后端服务器，实际上您还可以通过 Java API 操作 diskMirror 服务器，这也就是
http 适配器的工作，下面是一个使用示例！

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
@DiskMirrorConfig(
        rootDir = "/diskMirror",
        // TODO 设置我们要使用 http 协议访问的后端服务器的地址（如果您使用的是 DM_DfsAdapter 适配器，可以在这里指定多个，这样会被当作一整个来进行处理！）
        //  后端服务器是已经启动了的哦！
        fsDefaultFS = "http://localhost:8080"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 http 适配器
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        // 打印出远程服务器的版本
        System.out.println(adapter.version());
        // 直接在这里打印出远程服务器的文件系统目录
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1);
        jsonObject.put("type", Type.Binary);
        jsonObject.put("secure.key", 2139494269);
        System.out.println(adapter.getUrls(jsonObject));
    }
}
```

接下来展示的就是计算结果

```
E:\RunTime\jdk8\jdk-8u351\bin\java.exe "-javaagent:D:\Liming\MyApplication\IntelliJ_IDEA\IntelliJ IDEA 2021.3.2\lib\idea_rt.jar=51839:D:\Liming\MyApplication\IntelliJ_IDEA\IntelliJ IDEA 2021.3.2\bin" -Dfile.encoding=UTF-8 -classpath E:\RunTime\jdk8\jdk-8u351\jre\lib\charsets.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\deploy.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\access-bridge-64.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\cldrdata.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\dnsns.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\jaccess.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\jfxrt.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\localedata.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\nashorn.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\sunec.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\sunjce_provider.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\sunmscapi.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\sunpkcs11.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\ext\zipfs.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\javaws.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\jce.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\jfr.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\jfxswt.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\jsse.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\management-agent.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\plugin.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\resources.jar;E:\RunTime\jdk8\jdk-8u351\jre\lib\rt.jar;G:\MyGithub\DiskMirror\target\classes;G:\RunTime\MAVEN\MAVEN_BASE\com\alibaba\fastjson2\fastjson2\2.0.25\fastjson2-2.0.25.jar;G:\RunTime\MAVEN\MAVEN_BASE\io\github\BeardedManZhao\zhao-utils\1.0.20240121\zhao-utils-1.0.20240121.jar;G:\RunTime\MAVEN\MAVEN_BASE\org\apache\httpcomponents\httpclient\4.5.14\httpclient-4.5.14.jar;G:\RunTime\MAVEN\MAVEN_BASE\org\apache\httpcomponents\httpcore\4.4.16\httpcore-4.4.16.jar;G:\RunTime\MAVEN\MAVEN_BASE\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;G:\RunTime\MAVEN\MAVEN_BASE\commons-codec\commons-codec\1.11\commons-codec-1.11.jar;G:\RunTime\MAVEN\MAVEN_BASE\org\apache\httpcomponents\httpmime\4.5.14\httpmime-4.5.14.jar top.lingyuzhao.diskMirror.test.MAIN
top.lingyuzhao.diskMirror.core.DiskMirrorHttpAdapter@2d7275fc:V1.1.8
 ↓↓↓ 
remote → ↘↘
             'WWWKXXXXNWWNk,     ,kkd7               KWWb,                     
             ;WWN3.....,lNWWk.                       KWWb,                     
             ;WWNl        XWWk.  :XXk,   oKNNWNKo    KWWb,   dXXO:             
             ;WWNl  ^  ^  3WWX7  7WWO7  0WWo:,:O0d,  KWWb, lNWKb:              
             ;WWNl  -__-  :WWNl  7WWO7  0WWO,.       KWWbbXWKb:.               
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
LocalFSAdapter:1.1.7
{"userId":1,"type":"Binary","secure.key":2139494269,"useSize":43172,"useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"test","url":"http://localhost:8080//1/Binary//test","lastModified":1711613815127,"size":0,"type":"Binary","isDir":true,"urls":[{"fileName":"application.yaml","url":"http://localhost:8080//1/Binary//test/application.yaml","lastModified":1711613815070,"size":1437,"type":"Binary","isDir":false},{"fileName":"diskMirror.js","url":"http://localhost:8080//1/Binary//test/diskMirror.js","lastModified":1711613815127,"size":20149,"type":"Binary","isDir":false}]}],"res":"ok!!!!"}

进程已结束，退出代码为 0
```

### DM_DfsAdapter 分布式集群 使用示例！

在 `DiskMirror` 框架中，我们提供了分布式存储适配器，您可以使用此适配器将多个 diskMirror 进行链接，成为一个大集群！通常情况下，它会将多个
diskMirror 的适配器进行统一管理，您可以手动追加适配器的数量 也可以指定 fs-default-fs 配置项来指定要链接的几个后端
diskMirror 下面是一个示例！

下面的代码中，我们通过注解指定了一些 diskMirror 的后端服务器做为集群的子节点！并上传了三个文件！

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.DM_DfsAdapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.StorageStrategy;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        rootDir = "/diskMirror",
        // TODO 设置 DM-DFS 中所有的子节点的 http 服务器访问地址
        // 这样会自动的准备 两个 http 服务器 对应的适配器对象，需要注意的是，您需要在启动时，启动两个 diskMirror 的 http 服务器
        fsDefaultFS = "http://192.168.0.104:8080/,http://localhost:8080"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 分布式 适配器
        final DM_DfsAdapter adapter = (DM_DfsAdapter) DiskMirror.DM_DfsAdapter.getAdapter(MAIN.class);
        // 设置存储策略 这里的存储策略 默认是轮询 在这里我们修改为以文件名字取 hash 值
        adapter.setStorageStrategy(StorageStrategy.fileNameHash);
        // 在这里我们打开三个数据流
        try (
                final FileInputStream fileInputStream1 = new FileInputStream("C:\\Users\\zhao\\Downloads\\application.yaml");
                final FileInputStream fileInputStream2 = new FileInputStream("C:\\Users\\zhao\\Downloads\\工程进度计划.txt");
                final FileInputStream fileInputStream3 = new FileInputStream("C:\\Users\\zhao\\Downloads\\diskMirror.js")
        ) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", 1);
            jsonObject.put("type", Type.Binary);
            // 设置集群内所有子节点的密钥 要和子节点的 diskMirror 配置一样！
            jsonObject.put("secure.key", 2139494269);

            // 上传三个文件 在这里的三个文件会按照存储策略被分布到集群中不同的节点上
            jsonObject.put("fileName", "test/application.yaml");
            adapter.remove(jsonObject);
            adapter.upload(fileInputStream1, jsonObject.clone());

            jsonObject.put("fileName", "test/工程进度计划.txt");
            adapter.remove(jsonObject);
            adapter.upload(fileInputStream2, jsonObject.clone());

            jsonObject.put("fileName", "test/diskMirror.js");
            adapter.remove(jsonObject);
            adapter.upload(fileInputStream3, jsonObject.clone());

            // 移除掉文件名字，这是因为调用 getUrls 操作不需要文件名字
            jsonObject.remove("fileName");

            // 查看文件系统的结构
            System.out.println(adapter.getUrls(jsonObject));
        }

    }
}
```

下面就是计算结果

需要注意的是，在这里的集群中，有两个节点，所以上传的文件会分别被存储到两个节点上！而集群所在主机必须要启动 [diskMirrorBackEnd](https://github.com/BeardedManZhao/DiskMirrorBackEnd.git)
或者 [diskMirror-springBoot](https://github.com/BeardedManZhao/diskMirror-backEnd-spring-boot.git)。

> 结果中之所以包含 `"fileName": "/test/diskMirror.js"` 这是因为内部为了节约资源考虑，没有进行 json对象的 创建，直接使用的传递进去的
> json，您不需要使用 `getUrls` 返回的json 中的第一层对象中的 `fileName`！

```json
{
  "userId": 1,
  "type": "Binary",
  "secure.key": 2139494269,
  "fileName": "/test/diskMirror.js",
  "useSize": 43172,
  "useAgreement": true,
  "maxSize": 134217728,
  "urls": [
    {
      "fileName": "test",
      "url": "http://192.168.0.105:8080//1/Binary//test",
      "lastModified": 1711613815001,
      "size": 0,
      "type": "Binary",
      "isDir": true,
      "urls": [
        {
          "fileName": "工程进度计划.txt",
          "url": "http://192.168.0.105:8080//1/Binary//test/工程进度计划.txt",
          "lastModified": 1711613815001,
          "size": 907,
          "type": "Binary",
          "isDir": false
        },
        {
          "fileName": "application.yaml",
          "url": "http://localhost:8080//1/Binary//test/application.yaml",
          "lastModified": 1711613815070,
          "size": 1437,
          "type": "Binary",
          "isDir": false
        },
        {
          "fileName": "diskMirror.js",
          "url": "http://localhost:8080//1/Binary//test/diskMirror.js",
          "lastModified": 1711613815127,
          "size": 20149,
          "type": "Binary",
          "isDir": false
        }
      ]
    }
  ],
  "res": "ok!!!!"
}
```

### TCP_Adapter & TCP_CLIENT_Adapter TCP 交互使用示例

在 1.2.1 版本中，这两个适配器可以直接使用 TCP 协议来进行文件和数据的管理操作，此适配器还是雏形，会逐渐进行完善，接下来我们将展示一个示例。

#### TCP_Adapter 服务端

在 服务端适配器中，我们准备了一个 `run` 函数，可以直接进行监听任务，值得注意的是，`run` 函数只会监听一次！需要您手动管理它的循环监听操作，下面是一个示例。

```java
package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.TcpAdapter;

import java.io.IOException;

public final class MAIN {
    public static void main(String[] args) throws IOException {
        System.out.println(DiskMirror.VERSION);
        // 使用适配器对象 用来接收远程数据并进行处理
        final TcpAdapter adapterPacking0 = (TcpAdapter) DiskMirror.TCP_Adapter.getAdapterPacking(
                // 指定此适配器内部要使用的适配器的类型
                DiskMirror.LocalFSAdapter,
                // 指定此适配器的配置
                ConfigTCPAdapter1.class,
                // 指定此适配器的子适配的配置
                ConfigAdapter.class
        );

        // 开始监听！ 这里代表的是监听三次 实际的项目中 您也可以使用循环逻辑实现监听
        for (int i = 0; i < 3; i++) {
            // run 函数会阻塞线程进行监听！
            adapterPacking0.run();
        }
    }

    // TCP 适配器1的配置
    @DiskMirrorConfig(
            // 设置 TCP 适配器服务打开的端口 分别是 元数据端口 文件传输端口
            fsDefaultFS = "10001,10002"
    )
    public static final class ConfigTCPAdapter1 {
    }

    // TCP 适配器中所包含的子适配器的配置，TCP适配器实现了包装器 因此可以将任意一种适配器接入到TCP的数据传输中
    // 这里就是子适配器的配置
    @DiskMirrorConfig()
    public static final class ConfigAdapter {
    }
}
```

#### TCP_CLIENT_Adapter 客户端

在 1.2.1 客户端中，您可以向之前的所有适配器调用一样，直接调用这些函数操作，实现有效的文件操作！

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

// TCP 客户端适配器配置 在这里指定的就是 TCP 适配器所在的主机 和 元数据端口 文件端口
@DiskMirrorConfig(
        fsDefaultFS = "127.0.0.1:10001,10002"
)
public final class MAIN2 {
    public static void main(String[] args) throws IOException {
        System.out.println("开始发送数据！");
        // 实例化出 Tcp 客户端适配器
        final Adapter adapter = DiskMirror.TCP_CLIENT_Adapter.getAdapter(MAIN2.class);
        // 直接将 TCP 客户端适配器中的 upload 方法进行调用
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1);
        jsonObject.put("type", Type.Binary);
        jsonObject.put("secure.key", 0);
        jsonObject.put("fileName", "test1.jpg");

        // 删除名为 test1.jpg 的文件
        final JSONObject remove = adapter.remove(jsonObject);
        System.out.println(remove);

        // 再将 test1.jpg 上传
        final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\arc.png");
        final JSONObject upload = adapter.upload(fileInputStream, jsonObject);
        System.out.println(upload);

        // 下载文件
        try (
                final InputStream inputStream = adapter.downLoad(jsonObject);
                final FileOutputStream outputStream = new FileOutputStream("./out.jpg")
        ) {
            IOUtils.copy(inputStream, outputStream, true);
        }

        // 把 test1.jpg 重命名为 test2.jpg
        jsonObject.put("newName", "test2.jpg");
        final JSONObject jsonObject1 = adapter.reName(jsonObject);
        System.out.println(jsonObject1);
        jsonObject.remove("newName");

        // 查看文件结构
        jsonObject.remove("fileName");
        final JSONObject urls = adapter.getUrls(jsonObject);
        System.out.println(urls);

        // 查看版本
        System.out.println(adapter.version());
    }
}
```

#### 客户端执行结果

```
{"userId":1,"type":"Binary","secure.key":0,"fileName":"test1.jpg","maxSize":134217728,"res":"删除失败!!!文件不存在!"}
{"userId":1,"type":"Binary","secure.key":0,"fileName":"test1.jpg","useAgreement":true,"streamSize":4237376,"res":"ok!!!!","url":"http://localhost:8080/1/Binary/test1.jpg","useSize":12712128,"maxSize":134217728}
{"userId":1,"type":"Binary","secure.key":0,"fileName":"test1.jpg","newName":"test2.jpg","useSize":12712128,"maxSize":134217728,"res":"ok!!!!"}
{"userId":1,"type":"Binary","secure.key":0,"useSize":12712128,"useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"test0.jpg","url":"http://localhost:8080/1/Binary//test0.jpg","lastModified":1713851382691,"size":4237376,"type":"Binary","isDir":false},{"fileName":"test1 - 副本.jpg","url":"http://localhost:8080/1/Binary//test1 - 副本.jpg","lastModified":1713851382691,"size":4237376,"type":"Binary","isDir":false},{"fileName":"test2.jpg","url":"http://localhost:8080/1/Binary//test2.jpg","lastModified":1713858641609,"size":4237376,"type":"Binary","isDir":false}],"res":"ok!!!!"}
top.lingyuzhao.diskMirror.core.TcpClientAdapter@5b275dab:V1.2.1

进程已结束，退出代码为 0
```

## 更新记录

### 2025-05-22 1.5.3 版本发布

- 变更版本号为 1.5.3
- 优化 jedis 管理配置的时候，使用连接池！

### 2025-05-19 1.5.2 版本发布

- 变更版本号为 1.5.2
- 优化 `getUrlsNoRecursion` 的返回结果，让其不仅仅可以返回子文件结构，还可以返回被操作的文件/目录其本身的元数据！
- 优化 url 拼接方法，修复了 `getUrlsNoRecursion` 的 url 拼接错误，修复了双斜杠问题！

### 2025-04-30 1.5.1 版本发布

- 变更版本号为 1.5.1
- 优化上传操作中对于字节数值的计算规则，增强性能！
- 新增 ddos 防御器
```java
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.diskMirror.core.module.DDosCheckModule;
import top.lingyuzhao.diskMirror.core.module.ModuleManager;

import java.io.IOException;

@DiskMirrorConfig
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 注册一个 ddos 验证器 防御写操作的ddos 这里设置的是 1000 毫秒内不能超过5次请求
        // 由于注册到了读操作 因此是 1000 毫秒内不能超过5次读取请求
        ModuleManager.registerModuleRead(new DDosCheckModule("ddos", "一个用于防ddos的模块", 1000, 5));
        Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        System.out.println(adapter.version());
        for (int i = 0; i < 20; i++) {
            System.out.println(adapter.getUrls(DiskMirrorRequest.getUrls(47, Type.Binary)));
        }
    }
}
```

### 2025-04-30 1.5.0 版本发布【稳定版本】

- 变更版本号号为 1.5.0
- 对于允许覆写情况下的空间使用量计算错误已修复！

### 2025-04-15 1.4.9 版本发布

- 变更版本号为 1.4.9
- 对于 转存表 以及 空间使用量 使用了 `ConcurrentHashMap` 来保证线程安全！
- TCP 以及 http 适配器的关闭操作进行了优化
- TCP 适配器的版本号功能被支持！

### 2025-04-12 1.4.8 版本发布

- 修复 `DiskMirrorRequest.mkdir` 操作中，对目录的设置不生效的问题！
- 新增 `getUrlsNoRecursion` 可以避免递归获取文件结构，而是直接获取到一层（已测试过 `LocalFSAdapter` 适配器，可用，其它适配器还未测试）！

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        try (Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class)) {
            JSONObject urlsNoRecursion = adapter.getUrlsNoRecursion(DiskMirrorRequest.getUrlsNoRecursion(1, Type.Binary, "/test"));
            System.out.println(urlsNoRecursion);
        }
    }
}
```

### 2025-02-05 1.4.7 版本发布

- 对 http 适配器新增了一个 `downLoadUrl` 方法，用于获取到指定文件的下载地址！

### 2025-02-05 1.4.6 版本发布

- 优化了删除文件时 使用文件的匹配的逻辑！同时修复了过滤器删除本地适配器文件的时候发生的错误！
- 修复 `config` 调用 `loadSpaceConfig()` 之后，旧的SpaceConfig 数据会丢失的问题！

### 2025-02-04 1.4.4 版本发布

- 支持使用 redis 存储空间单独的配置信息，这种配置信息会单独存储在 redis，默认是基于 hashMapper

### 2025-01-05 1.4.0 稳定版本发布

- 经过长时间的迭代和使用，此版本的目前修正了所有的 bug，并且进行了一些优化。
- 为 http 适配器中的 download 操作不支持中文的问题。下面是一个示例！

```java

@DiskMirrorConfig(
        fsDefaultFS = "https://xxx.com",
        // 设置下载时 要使用的字符集 用于进行 url 编码等操作！
        charSet = "UTF-8"
)
public final class MAIN {
    public static void main(String[] args) {
        Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        try (
                final InputStream inputStream = adapter.downLoad(
                        // 这里是一个带有中文的路径
                        DiskMirrorRequest.uploadRemove(-999999999, Type.Binary, "/111.31.150.102/share/2025_01_05/12月7日.mp4")
                );
                final FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\zhao\\Desktop\\fsdownload\\e.mp4")
        ) {
            IOUtils.copy(inputStream, fileOutputStream, true);
        }
    }
}
```

### 2024-12-29 1.3.9 版本发布

- 新增 `remove` 操作 ` filter ` 请求参数，其可以实现删除指定的文件，下面是参数列表。

| 过滤器名称           | 使用语法示例                          | 解释                                        |
|-----------------|---------------------------------|-------------------------------------------|
| ALLOW_ALL       | `ALLOW_ALL`                     | 本次操作的文件是所有文件                              |
| FILE_NAME_MATCH | `FILE_NAME_MATCH:zhao.*`        | 只有匹配 `zhao.*` 的文件才会被操作                    |
| FILE_TIME_MATCH | `FILE_TIME_MATCH:1720598323221` | 只有最后一次修改时间是在 `1720598323221`ms 之前的文件才会被操作 |

- 优化本地文件适配器中删除不存在文件会报错的情况，本次改为，若删除不存在的文件，则不会有信息，而是直接视作删除成功。

### 2024-12-26 1.3.8 版本发布

- 新增配置项目 `IS_NOT_OVER_WRITE` 以及 `@DiskMirrorConfig(isNotOverWrite = false)`, 可以通过设置此项目来配置
  是否允许覆盖同名文件！
- 将一些频繁从 config 获取的数据，改用 final 类全局变量存储，提高性能。

### 2024-12-06 1.3.6 版本发布

- 修复 TCP 客户端适配器中的 `downLoad` 方法，返回空数据流的问题。
- 性能优化 以及 注释优化

### 2024-11-29 1.3.5 版本发布

- 为 https 适配器优化了 `setSpaceSk` 方法的返回值，便于排错。
- 优化了 `DiskMirror` 的 `getVersion` 方法，使其更小且更清晰。

### 2024-11-27 1.3.4 版本发布

- 为 `setSpaceSk` 方法进行了启用，我们将开始允许用户自定义加密密钥，这能够实现动态的密钥设置，数据将会更安全。

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(fsDefaultFS = "https://xxxxxx")
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到链接服务器的客户端 http 适配器
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        // 3457834578 是服务器的配置文件中的密钥 这个密钥不可外露 只有这个密钥可以修改任何用户空间的 sk
        final int i = adapter.setSpaceSk("1", 3457834578);
        // 这里的 i 就是更新之后的密钥了
        System.out.println(i);
        // 可以使用新密钥操作 TODO 假设服务器设置了 "SkCheckModule$read"
        final JSONObject urls = adapter.getUrls(
                DiskMirrorRequest.getUrls(1, Type.Binary).setSk(i)
        );
        System.out.println(urls);
    }
}
```

### 2024-11-27 1.3.3 版本发布

- 优化了验证模块的管理逻辑，移除了 `SkCheckModule`
  的默认添加，您可以手动使用 `ModuleManager.registerModule(new SkCheckModule())` 添加，且可以实现您自己的校验模块！

- 优化了适配器模块的加载逻辑，使其能够更灵活的进行配置，并且能够更灵活的进行模块的加载！

### 2024-11-04 1.3.2 版本发布

- 修复 `TcpClientAdapter` 适配器中，调用 upload 操作会空指针 以及 只能上传 64KB数据的情况~

### 2024-10-26 1.3.0 版本发布

- 新增了处理模块，所有被装载到适配器的模块，都会在接收到文件数据流的时候被尝试调用（在模块可以处理的前提下，若模块无法处理，则不会被调用）
- 新增了 `HandleModule.ImageCompressModule` 模块，用于对图片进行压缩，压缩模式默认为 `PalettePng.RGB_8`

```java
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.diskMirror.core.module.HandleModule;
import top.lingyuzhao.utils.PalettePng;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 修改压缩模式，这里我们使用的是 png RGB 8位 压缩 如果期望使用其他压缩模式可以自行修改
        HandleModule.ImageCompressModule.setImageCompress(PalettePng.RGB_8);
        // 将压缩处理器装到适配器中
        adapter.addHandleModule(HandleModule.ImageCompressModule);
        // 上传一个 png 图片~
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\图片2.png")) {
            final DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.uploadRemove(
                    // 在这里的 fileName 如果是以 png 等格式结尾的话，则会经由 ImageCompressModule 进行压缩处理
                    1, Type.Binary, "图片21.png"
            );
            System.out.println(
                    adapter.upload(fileInputStream, diskMirrorRequest)
            );
        }

        // 移除模块之后 将不会再压缩
        adapter.deleteHandleModule(HandleModule.ImageCompressModule);
        // 上传一个 png 图片~
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\图片2.png")) {
            final DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.uploadRemove(
                    // 这里的 fileName 即使是 png 也不会被压缩 因为压缩模块被移除了
                    1, Type.Binary, "图片211.png"
            );
            System.out.println(
                    adapter.upload(fileInputStream, diskMirrorRequest)
            );
        }
    }
}
```

### 2024-09-20 1.2.9 版本发布

- 为 `DiskMirror.DiskMirrorHttpAdapter` 适配器增加了转存支持，目录创建支持。
- 为 `DiskMirror.DiskMirrorHttpAdapter` 适配器的错误信息进行了优化，错误信息更详细。
- 为 `DiskMirrorRequest` 请求类增加了转存支持，目录创建支持。
- 为 `DiskMirror.DiskMirrorHttpAdapter` 适配器的返回结果新增了 `statusLine` 字段。

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(fsDefaultFS = "http://localhost:8080")
public final class MAIN {
    public static void main(String[] args) {
        // 获取到 Http 适配器对象
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);

        new Thread(() -> {
            // 转存一个文件 首先构建请求对象 TODO 此操作是同步的，因此我们需要将其放到线程中，实现异步调用 transferDepositStatus （如果不需要 transferDepositStatus 可以不使用线程）
            final DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.transferDeposit(
                    1, Type.Binary, "algorithmStar-1.42-javadoc.jar",
                    // 下面是被转存的文件在互联网中的 http 地址
                    "https://s01.oss.sonatype.org/content/repositories/releases/io/github/BeardedManZhao/algorithmStar/1.42/algorithmStar-1.42-javadoc.jar"
            );
            // 然后使用请求对象开始进行转存操作
            final JSONObject jsonObject;
            try {
                System.out.println("转存开始");
                jsonObject = adapter.transferDeposit(diskMirrorRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(jsonObject);
        }).start();

        new Thread(() -> {
            // TODO 由于转存文件的操作是阻塞的，因此我们如果期望获取到正在转存的数据结果 这里就需要使用一个线程来获取结果 在这里启动一个线程来获取转存信息
            // 为了确保缓存开始了，我们等待1000ms 这个时间根据您自己的情况来调整
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 获取到转存的所有文件列表 首先构建请求对象
            final DiskMirrorRequest transferDepositStatus = DiskMirrorRequest.transferDepositStatus(1, Type.Binary);
            // 获取到所有的转存文件信息
            final JSONObject mkdirs1 = adapter.transferDepositStatus(transferDepositStatus);
            System.out.println(mkdirs1);
        }).start();
    }
}
```

### 2024-09-14 1.2.8 版本发布

> 修复更新：https://github.com/BeardedManZhao/DiskMirror/issues/15

- 将 `DiskMirror.DiskMirrorHttpAdapter` 中的 `upload` 功能修复！

### 2024-09-09 1.2.7 版本发布

- 将 `SkCheckModule` 模块注册！融合到服务中。

### 2024-09-01 1.2.5 版本发布

- 为  `DiskMirror.DiskMirrorHttpAdapter` 适配器增加了对于新版本服务器的支持！

### 2024-07-07 1.2.4 版本发布

> 请注意 在 1.2.3 1.2.4 中所添加的 `DiskMirror.DiskMirrorHttpAdapter` 适配器中的 `setSpaceSk` 目前并不安全，因此这只是一个试验功能！
> 因为服务器中的密钥设置服务对外暴露并不安全，因此需要一种有效的方案，后期会将此方案启用，并打开此服务！
> 其它适配器中的 `setSpaceSk` 操作安全可用！

- 修复在 1.2.3 版本中对于 `DiskMirror.DiskMirrorHttpAdapter` 适配器中的 `setSpaceSk`
  操作的已知问题（服务器返回的数据无法被客户端正常解析，但客户端的请求可以成功被服务器使用）进行了修复！

### 2024-06-17 1.2.3 版本发布

- 将安全密钥模块与diskMirror分离，后期会融合起来 在 1.2.6 版本中进行了融合！
- 新增 `top.lingyuzhao.diskMirror.core.DiskMirrorRequest`
  类，通过此类操作适配器将可以更简单且简洁的代码，例如下面的代码，效果和 [文件数据流写入方式](#文件数据流写入方式)
  中完全一致，但是 `top.lingyuzhao.diskMirror.core.DiskMirrorRequest` 可以减少代码量。

```java
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        // 我们发现创建请求的时候 有时候会很繁琐，因此我们提供了快捷的创建请求的方法 就是下面这种，它的效果等同于手动操作 FastJson2
        final DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.reName(1, Type.TEXT, "test.txt", "test2.txt");
        System.out.println(diskMirrorRequest);
    }
}
```

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // 设置不在 url 中使用协议 为了和上面的对比的例子保持一致
        protocolPrefix = ""
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 开始构建盘镜 由于我们在这里使用的是本地文件系统 所以我们使用 DiskMirror.LocalFSAdapter.getAdapter(config) 来实例化 本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 准备需要被操作的文件
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\arc.png")) {
            // 输出数据
            final JSONObject save = adapter.upload(
                    fileInputStream,
                    // 在这里我们进行了简化，不需要手动创建JSON对象的键值对，只需要使用这一行即可！
                    DiskMirrorRequest.uploadRemove(1, Type.Binary, "arc.png")
            );
            // 打印结果
            System.out.println(save);
        }
    }
}

```

- 新增空间密钥的自定义操作

```java
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        secureKey = 123456
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 diskMirror 对象 注意这里使用的是注解中的密钥 123456
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 为 1号 用户空间生成安全密钥 这里返回的是 1号 空间专用的密钥！ 假设是 1779931471 (可能会有变化)
        final int i = adapter.setSpaceSk("1");

        // 在下面继续从 adapter 获取到 1 号空间的文件列表 TODO 这里会出现问题 因为 1 号空间对应的密钥被修改为了 i 的值
        final DiskMirrorRequest urls = DiskMirrorRequest.getUrls(1, Type.Binary);
        urls.setSk(123456);
        final JSONObject urls1 = adapter.getUrls(urls);
        System.out.println(urls1);

        // 所以我们需要使用 i 做为sk
        urls.setSk(i);
        final JSONObject urls2 = adapter.getUrls(urls);
        System.out.println(urls2);

        // 然而 如果是 其它空间 则是继续使用 123456 做为密钥
        // 简单来说就是 通过 adapter.setSpaceSk 方法设置过密钥，就需要使用新密钥，反之则是使用 Config.secureKey 做为密钥
        urls.setUserId(2);
        urls.setSk(123456);
        final JSONObject urls3 = adapter.getUrls(urls);
        System.out.println(urls3);
    }
}
```

### 2024-04-24 1.2.2 版本发布

- 新增 `getAllProgressBar` 函数，能够实时的获取到当前文件上传进度！

```java
package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

public final class MAIN2 {
    public static void main(String[] args) {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN2.class);
        // 获取所有 1 号空间的文件 进度条对象
        final JSONObject allProgressBar = adapter.getAllProgressBar("1");
        System.out.println(allProgressBar);
    }
}
```

### 2024-04-24 1.2.1 版本发布

- 新增适配器包装类，它可以将任意的适配器对象进行包装，并将方法自动实现为被包装的适配器对象，这样的适配器构造和拓展更加灵活！
- 修复当程序启动之后，最先调用的是 `remove` 方法时，会导致 `useSize` 不能够正确统计的情况。
- 新增 `TCP_Adapter` 和 `TCP_CLIENT_Adapter` 适配器，它们可以互相配合，实现通过 TCP 协议来进行文件数据的传输等效果！

### 2024-04-12 1.2.0 版本发布【稳定版本】

- 对于所有的适配器，提供了 `transferDeposit` 函数，用来将一个 url 中的文件数据转存！

### 2024-04-06 1.1.9 版本发布

- 优化 `LocalFSAdapter` 适配器的`long rDelete(String path) throws IOException` 函数，让它的性能更好，且具有更精确的文件容量计算。

### 2024-03-28 1.1.8 版本发布

- 将适配器的 `long rDelete(String path) throws IOException` 函数变更为 `protected`
  权限，因为它的操作能够删除文件目录但是不释放可用空间标记，这导致一些存储空间泄漏的情况发生!!!
- 为 `DiskMirrorHttpAdapter` 添加编码集支持，防止乱码，且为此适配器新增异常信息的自动判断逻辑！
- 新增 `DM_DfsAdapter` 分布式集群存储适配器，其可以将多个 diskMirror 进行链接，成为一个大集群！（还在不断优化中）

### 2024-03-26 1.1.7 版本发布

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

### 2024-03-24 1.1.4 版本发布

> PS:此版本的更新 HTTP 适配器的 `download` 函数有些问题，在新版中已成功解决！

1. 针对每个适配器对象，新增 `download` 函数，能将一个文件以数据流的方式获取到，这能够最大程度上的提升文件获取的灵活性，避免了被
   url 协议所限制的问题！
2. 为了避免 sk 泄漏，在异常发生时 如果涉及到 sk 的输出，则会自动被进行掩码掩盖！
3. 日志 logo 小更新！

### 2024-02-17 1.1.3 版本发布

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

### 2024-02-08 1.1.2 版本发布

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

### 2024-02-01 1.1.1 版本发布

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

### 2024-01-21 1.1.0 版本发布【稳定版本】

1. 修正依赖组件重复的问题，需要注意的是，您的在这里的 zhao-utils 依赖应在 1.0.20240121 及 以上版本!!! 新版本的工具类修正了一些
   bug
2. 针对 重命名失败 的错误信息进行详细的解答。
3. 提供了密钥的生成设置，设置密钥之后 将必须要使用密钥访问，这增加了安全性!

----

### 2024-01-21 1.0.9 版本发布

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

### 2024-01-04 1.0.8 版本发布

1. 针对所有的操作，返回的json中包含了 maxSize 参数，代表的就是当前操作的空间的最大容量

----

### 2023-12-22 1.0.7 版本发布

1. 能够通过适配器对于文件系统中的文件进行重命名操作

----

### 2023-12-19 1.0.6 版本发布

1. 针对所有的操作返回值都增加了实时文件空间占用字节数"useSize"的结果
2. 针对所有已存在的文件进行增删将会抛出错误，您可以不去进行文件是否存在的检测
3. 针对稳定性进行升级，修复了部分bug
4. 针对文件上传的接口增加了文件大小限制的配置项目，默认为 128Mb

## 可能出现的问题

### 各类功能性错误

在我们的 [issue](https://github.com/BeardedManZhao/DiskMirror/issues) 界面中，您可以查看所有已经存在的问题，以及如何解决它。

### 文件系统类问题

当我们使用 diskMirror 对接文件系统的时候，如果遇到了文件系统抛出的问题，则 diskMirror 会原样的抛给用户，且将堆栈包含在其中，diskMirror
不会对这类情况进行处理，因为这类情况与文件系统有关，diskMirror 不得修改任何有关文件系统的配置，这是为了安全考虑！

| 可能出现的情况 | 代表意义                                                                                     | diskMirror 是否干涉 |
|---------|------------------------------------------------------------------------------------------|-----------------|
| 权限问题    | 文件系统不允许我们使用 diskMirror 去操作，您只需要提升 diskMirror 的程序权限即可，确保diskMirror 的根目录可以被 diskMirror 读写！ | false           |
| 文件找不到   | 文件系统中没有找到您需要的文件，您可以检查一下文件是否存在，或者检查一下文件名是否正确！                                             | false           |

### 依赖问题

在 diskMirror 中有诸多的组件，某些组件可能会需要使用到外部的依赖，在这里如果依赖导入的不全，会直接给出依赖的详细信息，有着完整的报错机制，您无需担心！

### API 使用问题

API 的说明文档某些地方若描述不清晰，让您的使用难度增加，或者您有什么建议，您可以联系作者或者发布 issue，作者会尽快处理！

### 更多详细
----

- diskMirror starter SpringBoot：https://github.com/BeardedManZhao/diskMirror-spring-boot-starter.git
- diskMirror 后端服务器版本：https://github.com/BeardedManZhao/DiskMirrorBackEnd.git
- diskMirror Java API 版本：https://github.com/BeardedManZhao/DiskMirror.git
