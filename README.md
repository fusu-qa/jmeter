
新增功能:
HTTP代理服务器：一次录制中，根据method、url、parameter去重


以下操作均在Macos操作系统
改动：
NewDriver.java
使这个条件不生效，OS_NAME_LC.startsWith("mac os x")

ProxyControl.java
//存放一次录制中所有请求的基本信息
private List<SimpleHttpRequest> simpleHttpRequests = new ArrayList<>();
//一次录制中，根据method、url、parameter去重
try {
SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest(sampler.getMethod(), sampler.getUrl().toString(), sampler.getArguments().toString());
if (!simpleHttpRequests.contains(simpleHttpRequest)) {
simpleHttpRequests.add(simpleHttpRequest);
sampleQueue.add(new SamplerInfo(sampler, testElements, myTarget, getPrefixHTTPSampleName(), groupingMode));
}
} catch (MalformedURLException e) {
e.printStackTrace();
}
//清空集合
simpleHttpRequests.clear();

安装chrome浏览器插件：SwitchyOmega
指定录制域名使用代理：localhost:8888

IDEA启动：
--双击createDist
![img.png](img.png)

--配置-Djmeter.home为项目根目录
![img_1.png](img_1.png)

--右键启动
NewDriver.main()

--开始录制
![img_2.png](img_2.png)
