
## 新增功能:
<li>HTTP代理服务器：一次录制过程中，根据method、url、parameter去重
<li>json断言，大于某个数字，支持整数和小数点(小数点位数最长支持10位)

![img_9.png](img_9.png)

![img_8.png](img_8.png)
<br />

以下操作均在Macos操作系统
<br />
## HTTP代理服务器去重逻辑，代码改动：
NewDriver.java
<br />
### 使这个条件不生效，OS_NAME_LC.startsWith("mac os x")

ProxyControl.java
<br />
### 存放一次录制中所有请求的基本信息
<br />
private List<SimpleHttpRequest> simpleHttpRequests = new ArrayList<>();
<br />
### 一次录制中，根据method、url、parameter去重
<br />
try {
SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest(sampler.getMethod(), sampler.getUrl().toString(), sampler.getArguments().toString());
if (!simpleHttpRequests.contains(simpleHttpRequest)) {
simpleHttpRequests.add(simpleHttpRequest);
sampleQueue.add(new SamplerInfo(sampler, testElements, myTarget, getPrefixHTTPSampleName(), groupingMode));
}
} catch (MalformedURLException e) {
e.printStackTrace();
}
<br />
### 清空集合
simpleHttpRequests.clear();
<br />
### 安装chrome浏览器插件：SwitchyOmega
<br />
### 指定录制域名使用代理：localhost:8888

![img_5.png](img_5.png)


![img_7.png](img_7.png)

##【1】IDEA启动：
--双击createDist
![img.png](img.png)
<br />
--配置-Djmeter.home为项目根目录
![img_1.png](img_1.png)

--右键启动
NewDriver.main()
<br />

##【2】二进制启动
sh bin/jmeter.sh

##【3】开始录制
![img_2.png](img_2.png)

### 同一个接口请求多次
1、改造后的jmeter只录制一份
![img_3.png](img_3.png)
<br />
2、原生jmeter录制多份
<br />
![img_4.png](img_4.png)

## HTTP代理服务器调用栈：
![img_6.png](img_6.png)
