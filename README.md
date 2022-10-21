## 背景
- 通过Jmeter录制前端接口时会出现很多重复接口，需要人工删除，维护成本较高
- 原生Jmeter JSON断言支持等于、正则表达式、判空、反向断言功能，不支持其他关系运算符(大于、大于等于、小于、小于等于、包含)

## 开源地址
https://gitee.com/fusu-qa/jmeter
<br/>
https://github.com/fusu-qa/jmeter

## 新增功能:
- HTTP录制支持去重：根据method、url、parameter去重
- json断言支持大于、大于等于、小于、小于等于，仅支持整数，小数
- json断言支持包含，仅支持字符串
- json断言支持降噪处理，通过配置jsonpath，过滤掉干扰字段比如时间搓、随机数等，提高断言成功率
![img_10.png](img_10.png)

https://github.com/json-path/JsonPath
![img_9.png](img_9.png)
## 去重逻辑代码改动
NewDriver.java
### 使这个条件不生效，OS_NAME_LC.startsWith("mac os x")

ProxyControl.java

### 存放一次录制中所有请求的基本信息
private List<SimpleHttpRequest> simpleHttpRequests = new ArrayList<>();

### 一次录制中，根据method、url、parameter去重
try {
SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest(sampler.getMethod(), sampler.getUrl().toString(), sampler.getArguments().toString());
if (!simpleHttpRequests.contains(simpleHttpRequest)) {
simpleHttpRequests.add(simpleHttpRequest);
sampleQueue.add(new SamplerInfo(sampler, testElements, myTarget, getPrefixHTTPSampleName(), groupingMode));
}
} catch (MalformedURLException e) {
e.printStackTrace();
}

### 清空集合
simpleHttpRequests.clear();

## 安装chrome浏览器插件：SwitchyOmega

## 指定录制域名使用代理：localhost:8888

![img_5.png](img_5.png)


![img_7.png](img_7.png)

## IDEA启动
### 双击createDist
![img.png](img.png)

### 配置-Djmeter.home为项目根目录
![img_1.png](img_1.png)

### 右键启动
NewDriver.main()

## 二进制启动
sh bin/jmeter.sh

## 打包
tar -czf jmeter-bin.tar.gz bin lib

## 开始录制
![img_2.png](img_2.png)

### 同一个接口请求多次
1、改造后的jmeter只录制一份
![img_3.png](img_3.png)

2、原生jmeter录制多份

![img_4.png](img_4.png)


## http录制样本
jmeter/blob/master/HTTP代理服务器.jmx

![img_12.png](img_12.png)

## HTTP代理服务器调用栈：
![img_6.png](img_6.png)

如何觉得对自己工作有所帮助，帮忙点个Star，多谢！！！
