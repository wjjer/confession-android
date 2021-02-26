

# confession-android

**应用的开源服务端请前往：[confession-server](https://github.com/wjjer/confession-server) 查看**



## 应用介绍

confession-android 项目中文名为 **告白吧**，是一款基于安卓开发的网页告白生成器，支持在模板商店中下载告白网页模板，下载后可以对模板进行个性化定制，编辑属于自己的个性化模板，可以根据已经配置好的模板生成唯美的告白页面，告白页面可以生成二维码进行分享

:rose: 支持在模板商店下载各式的告白网页模板

:rose: 支持配置网页表白模板，完全的个性化定制

:rose: 支持本地导入 zip 格式的告白页面模板，基于freemarker引擎生成页面

:rose: 支持一键 **二维码分享告白页面**





## 技术架构

:white_check_mark: 安卓端基于 [**Kotlin** ](https://www.kotlincn.net/)与 java 混合开发

:white_check_mark: 底层使用 [**FreeMarker Java Template Engine**](https://freemarker.apache.org/) 模板引擎渲染页面

:white_check_mark: 基于[ **Okhttp3**  ](https://square.github.io/okhttp/4.x/okhttp/okhttp3/)封装网络请求

:white_check_mark: 使用 [**BaseRecyclerViewAdapterHelper**](https://github.com/CymChad/BaseRecyclerViewAdapterHelper) 作为RecycleView适配器

:white_check_mark: 基于 [**yanzhenjie/AndPermission**](https://github.com/yanzhenjie/AndPermission) 处理应用层权限请求处理

:white_check_mark: 使用 [**ZXing**](https://github.com/zxing/zxing) 处理应用端二维码生成





## 应用展示



**模板数据配置页面**

<center class="half">
    <div style=" width: 40%;">
<img alt="模板数据配置页面" src="images/template.gif"/>
    </div>
</center>
<br/>

**模板数据配置页面**

 <center class="half">
  <div style=" width: 40%;">
    	<img alt="下载在线模板" src="images/template-down.gif" />
    </div>
</center>

<br/>

**生成告白**

<center>
    <div style=" width: 40%;">
        <img alt="生成告白" src="images/generate.gif"/>
    </div>
</center>

<br/>

**开始告白**

<center>
  <div style="width: 40%;">
    	<img alt="下载在线模板" src="images/share.gif" />
    </div>
</center>




## 部署应用

安卓端 `confession-android` 部署

1. 克隆项目到本地电脑硬盘

~~~shell
git clone https://github.com/wjjer/confession-android.git
~~~



2. 修改必要配置文件

修改 vip.ablog.confession.global.Constant 文件

~~~shell
# 改为你的confession-server地址
public static final String SERVER_URL = "";
# 改为你的web页面服务器地址
public static final String SERVER_HOST = "";
~~~



3. 服务端部署 confession-server 部署

服务端部署参见服务端应用 [**confession-server部署教程**](https://github.com/wjjer/confession-server)







