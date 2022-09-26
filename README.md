
新增功能:
<br />
HTTP代理服务器：一次录制中，根据method、url、parameter去重
<br />

以下操作均在Macos操作系统
<br />
改动：
<br />
NewDriver.java
<br />
使这个条件不生效，OS_NAME_LC.startsWith("mac os x")

ProxyControl.java
<br />
//存放一次录制中所有请求的基本信息
<br />
private List<SimpleHttpRequest> simpleHttpRequests = new ArrayList<>();
<br />
//一次录制中，根据method、url、parameter去重
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
//清空集合
simpleHttpRequests.clear();
<br />
安装chrome浏览器插件：SwitchyOmega
<br />
指定录制域名使用代理：localhost:8888

IDEA启动：
<br />
--双击createDist
![img.png](img.png)
<br />
--配置-Djmeter.home为项目根目录
![img_1.png](img_1.png)

--右键启动
NewDriver.main()
<br />
--开始录制
![img_2.png](img_2.png)
