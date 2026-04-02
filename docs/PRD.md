# Tran Tools Android PRD

## 1. 文档信息

- 项目名称：Tran Tools Android
- 文档版本：v1.0
- 文档日期：2026-03-31
- 文档状态：可进入技术设计与开发排期
- 适用范围：Android 客户端，纯本地能力优先，不依赖自建服务端

## 2. 项目背景

当前项目是一个新的 Android Compose 工程，代码基线非常轻，仅包含基础主题和一个 `Hello Android` 页面，适合在此阶段先完成产品需求、架构设计和实施边界定义，再进入模块化开发。

基于 Stitch 设计稿，本产品不是单一翻译工具，而是一个围绕旅行场景的本地 AI 工具箱。核心价值在于：

1. 在弱网或无网场景下仍可提供基础可用能力。
2. 尽量将图片、语音、文本处理留在本地，降低隐私风险。
3. 通过翻译、相机识别、百科检索、地图推荐和问答整合成一个统一入口。

## 3. 产品定位

Tran Tools 是一款面向出境旅行、跨语种沟通和目的地信息获取的 Android 端侧 AI 助手。

产品定位如下：

- 主定位：本地优先的旅行翻译与知识助手
- 核心卖点：离线可用、隐私优先、旅行场景聚合
- 交互形态：底部 Tab 导航 + 首页聚合入口 + 各功能独立工作流

## 4. 目标用户

### 4.1 目标人群

- 出境旅游用户
- 海外短住、交换、留学用户
- 商务出行用户
- 对隐私敏感、不希望上传照片和语音到云端的用户

### 4.2 典型使用场景

- 在餐厅拍菜单并翻译
- 在街头识别路牌、告示和票据
- 与当地人进行短句语音交流
- 快速了解景点、文化、历史背景
- 在陌生城市查找附近值得去的地点
- 将翻译、百科和地图信息串起来进行多轮问答

## 5. 产品目标与非目标

### 5.1 产品目标

MVP 阶段要达成以下目标：

1. 用户可以在离线状态下完成文本翻译、拍照翻译和基础语音翻译。
2. 用户可以在本地知识包内完成百科检索和基础问答。
3. 用户可以在本地地图/POI 包内查看附近地点、筛选、收藏和详情。
4. 产品整体架构支持后续扩展多语言包、城市包和模型包。

### 5.2 非目标

以下内容不进入首期交付：

- 自建后端服务
- 实时联网搜索
- 实时在线地图评论与营业状态同步
- 跨设备账号同步
- iOS 版本
- 多人协作或社区内容发布

## 6. 产品原则

### 6.1 本地优先

- 所有核心能力优先在端侧完成。
- 联网仅用于首次下载模型、语言包、城市离线包，且必须可选。

### 6.2 功能明确，不做“万能 AI”

- 翻译使用专用翻译能力，不使用通用 LLM 硬翻。
- OCR、ASR、RAG、地图检索分别使用最合适的本地能力。
- 问答负责整合信息，不直接替代所有底层工具。

### 6.3 弱网可退化

- 每个核心功能都要有明确的未下载、初始化中、不可用、降级可用状态。

### 6.4 模块可替换

- AI 引擎与 UI 解耦，便于替换不同本地模型与推理框架。

## 7. 设计稿反推后的产品信息架构

根据 Stitch 设计稿，一级导航建议如下：

1. Home
2. Translate
3. Voice
4. Wiki
5. Gems

其中 `Photo Translate` 是 Translate 域下的重点子流程，也可以在 Home 中以快捷卡片暴露。

### 7.1 Home

首页提供以下入口：

- 全局提问入口
- 快捷卡片：Photo Translate、Voice Chat、Quick Text、AI Insights
- 最近使用上下文
- AI 提示与个性化推荐

### 7.2 Translate

翻译中心提供以下能力：

- 文本输入翻译
- 实时对话翻译
- 语言自动识别
- 源语言/目标语言切换
- 翻译历史记录
- 收藏常用表达

### 7.3 Photo Translate

拍照翻译提供以下能力：

- 实时相机取景
- 相册导入
- OCR 文本识别
- 翻译结果覆盖到图像区域
- 历史记录查看
- AI 增强按钮，例如解释菜品、提取重点

### 7.4 Wiki / Encyclopedia

百科模块提供以下能力：

- 搜索目的地知识
- 分类浏览：食物、历史、文化、建筑等
- 详情卡片
- 收藏
- 与问答联动

### 7.5 Gems / Nearby

地图模块提供以下能力：

- 当前定位
- 附近推荐地点展示
- 地图标记与列表联动
- 筛选条件
- 收藏
- 地点详情与 AI 小贴士

### 7.6 AI Q&A

问答模块提供以下能力：

- 多轮聊天
- 基于百科知识包和最近上下文进行回答
- 插入地图卡片、景点卡片、图片卡片
- 将翻译和知识结果串联起来

## 8. 功能范围

## 8.1 MVP 功能清单

### A. 首页

- 启动后展示欢迎区和统一搜索框
- 展示 4 个核心快捷卡片
- 展示最近历史入口
- 展示 AI 提示卡片

### B. 文本翻译

- 输入文本
- 自动检测源语言
- 指定目标语言
- 翻译结果展示
- 复制
- 收藏
- 历史记录
- 常用短语

### C. 语音翻译

- 点击开始录音
- 本地语音识别
- 自动翻译
- 文字对照显示
- 语音播放
- 会话历史

### D. 拍照翻译

- 相机实时预览
- 拍照识别
- 相册导入
- OCR 文本框识别
- 翻译覆盖层
- 翻译结果列表视图
- 历史记录

### E. 百科

- 搜索
- 分类浏览
- 知识详情
- 收藏
- 最近浏览

### F. AI 问答

- 多轮对话
- 上下文理解
- 从本地百科与地点数据中检索答案
- 展示引用卡片
- 推荐下一步问题

### G. 地图与附近推荐

- 获取定位
- 加载离线地图或本地地图底图
- 展示本地 POI 点位
- 列表与地图联动
- 按类型筛选
- 收藏

### H. 设置与资源管理

- 权限管理入口
- 订阅与权益中心
- 购买恢复与订阅管理入口
- 语言包管理
- 模型包管理
- 城市包管理
- 存储空间占用查看
- 清理缓存
- 隐私与离线模式开关
- 支持与反馈入口

## 8.2 P1 功能

- 文档翻译
- 对话模式自动轮流翻译
- POI 离线包按城市下载
- 多语言 TTS 语音包管理
- AI 卡片结果导出图片
- 用户自定义知识包导入

## 9. 关键用户流程

## 9.1 文本翻译流程

1. 用户进入 Translate 页面。
2. 选择或自动识别源语言。
3. 选择目标语言。
4. 输入文本。
5. 本地翻译引擎返回结果。
6. 用户可复制、收藏、加入历史。

## 9.2 拍照翻译流程

1. 用户进入 Photo Translate。
2. 授权相机或从相册导入。
3. 本地 OCR 识别文本框。
4. 对文本框逐段翻译。
5. UI 以原图叠层或列表方式显示结果。
6. 用户可保存、分享、收藏。

## 9.3 语音翻译流程

1. 用户点击录音按钮。
2. 本地 ASR 将语音转文字。
3. 文本进入翻译引擎。
4. 结果展示原文与译文。
5. 若目标语言支持 TTS，播报翻译结果。

## 9.4 百科问答流程

1. 用户在 Wiki 或 Q&A 输入问题。
2. 问题进入检索层。
3. 从本地知识索引中召回相关文档。
4. 将召回结果交给小模型做答案组织。
5. 展示答案、相关卡片和后续问题建议。

## 9.5 附近推荐流程

1. 用户授权定位。
2. App 读取本地地图包和 POI 数据。
3. 根据当前位置进行距离和类别排序。
4. 地图上显示点位，底部列表展示详情。
5. 用户可收藏、查看详情、跳转问答。

## 10. 非功能需求

### 10.1 离线能力

- 文本翻译、OCR、基础语音识别、百科检索需可在离线运行。
- 若某语言包或模型未下载，应提示并给出下载入口。

### 10.2 性能

- 首屏冷启动目标：3 秒内进入首页骨架屏
- 文本翻译响应目标：1 秒内返回短文本结果
- OCR 单张图首屏识别目标：2 到 4 秒
- RAG 问答首 Token 目标：2 到 5 秒，视设备性能分级

### 10.3 隐私

- 默认不上传用户图片、语音、文本到远端
- 历史数据本地存储
- 提供“一键清空历史”

### 10.4 稳定性

- 每个 AI 能力模块都要有超时、失败、重试和降级方案

## 11. 产品边界与约束

### 11.1 不使用自建服务端

产品不依赖业务后端。允许的联网行为仅包括：

- 下载模型
- 下载语言包
- 下载地图离线包
- 下载知识包

### 11.2 地图能力边界

由于没有服务端，MVP 中地图能力不承诺以下内容：

- 实时商户评论
- 实时营业状态
- 实时路线规划
- 实时交通信息

地图模块以“离线城市包 + 本地 POI 搜索排序”为主。

### 11.3 AI 回答边界

- 问答优先基于本地知识和本地 POI 数据
- 不承诺互联网最新信息
- 需要在结果中标注“来自本地知识包”或“来自地点离线包”

### 11.4 订阅边界（无自建服务端）

- 可接入 Google Play Billing 实现订阅，不依赖自建业务后端
- 首期权益判断基于本地缓存加 Play 返回结果，不做自建服务端二次校验
- 设置页需提供：开通订阅、恢复购买、管理订阅入口
- 离线场景下允许短时宽限展示已购状态，联网后需自动重新校验
- 高风险限制：不在首期引入自定义支付渠道和跨端权益同步

## 12. 技术架构设计

## 12.1 客户端架构风格

采用 `Jetpack Compose + MVVM + Repository + Offline-first` 架构。

### 选择原因

- Compose 适合快速实现设计稿中的卡片式、地图叠层式和聊天式界面
- MVVM 适合 UI 状态管理、权限状态管理和多引擎异步调用
- Repository 有利于统一抽象本地数据库、资源包、模型和系统能力
- Offline-first 符合本产品核心价值

## 12.2 架构分层

建议按以下层次拆分：

### 表现层

- Compose Screen
- Compose Components
- ViewModel
- UiState / UiEvent / UiAction

### 领域层

- UseCase
- 业务协调器
- 能力编排器，例如 `TranslateCoordinator`、`ChatCoordinator`

### 数据层

- Repository
- Local Data Source
- Resource Package Manager
- AI Engine Adapter

### 基础能力层

- OCR 引擎
- 翻译引擎
- ASR 引擎
- TTS 引擎
- 小模型推理引擎
- 地图与定位引擎

## 12.3 模块划分

建议按 Gradle module 演进，首期可以先在 `app` 内按包分层，第二阶段再拆 module。

推荐目标模块：

- `app`
- `core-ui`
- `core-model`
- `core-common`
- `core-data`
- `core-database`
- `core-ai`
- `core-camera`
- `core-location`
- `feature-home`
- `feature-translate`
- `feature-photo`
- `feature-voice`
- `feature-wiki`
- `feature-gems`
- `feature-chat`
- `feature-settings`

## 12.4 推荐包结构

```text
com.neil.trantools
├── app
│   ├── navigation
│   ├── di
│   └── MainActivity
├── core
│   ├── ui
│   ├── model
│   ├── data
│   ├── database
│   ├── ai
│   ├── camera
│   └── location
└── feature
    ├── home
    ├── translate
    ├── photo
    ├── voice
    ├── wiki
    ├── gems
    ├── chat
    └── settings
```

## 12.5 UI 架构约定

每个功能模块使用如下结构：

```text
feature/translate
├── TranslateRoute.kt
├── TranslateScreen.kt
├── TranslateViewModel.kt
├── TranslateContract.kt
├── component/
└── domain/
```

约定如下：

- `Route` 负责注入 ViewModel 和导航参数
- `Screen` 只负责渲染
- `ViewModel` 负责状态转换和异步调用
- `Contract` 定义状态、事件、动作

## 12.6 状态管理建议

采用单向数据流：

- `UiState`：页面稳定状态
- `UiAction`：用户输入动作
- `UiEvent`：一次性事件，例如 Toast、导航、权限请求

示例：

```kotlin
data class TranslateUiState(
    val sourceLang: String = "auto",
    val targetLang: String = "en",
    val inputText: String = "",
    val translatedText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
```

## 12.7 导航设计

使用 `Navigation Compose`。

主导航：

- `home`
- `translate`
- `voice`
- `wiki`
- `gems`
- `settings`

子页面：

- `photo_translate`
- `translation_history`
- `wiki_detail/{id}`
- `gem_detail/{id}`
- `chat`
- `resource_manager`

## 12.8 DI 方案

建议使用 `Hilt`。

原因：

- 与 Android 官方生态贴近
- 便于注入 Repository、UseCase、系统服务和 AI Engine

## 12.9 本地数据架构

### 数据存储

- `Room`：结构化业务数据
- `DataStore`：设置、用户偏好、最近语言对、是否完成引导
- 文件系统：模型包、知识包、地图包、OCR 中间文件

### 核心数据实体

- `TranslationHistoryEntity`
- `FavoritePhraseEntity`
- `PhotoTranslateHistoryEntity`
- `WikiArticleEntity`
- `WikiArticleFtsEntity`
- `PoiEntity`
- `PoiCategoryEntity`
- `ChatSessionEntity`
- `ChatMessageEntity`
- `ResourcePackageEntity`

### 建议表结构示例

`TranslationHistoryEntity`

- id
- sourceText
- translatedText
- sourceLang
- targetLang
- inputMode
- createdAt
- isFavorite

`PoiEntity`

- id
- cityCode
- name
- category
- lat
- lng
- description
- tags
- rating
- distanceMeters
- sourcePackageVersion

## 12.10 本地 AI 架构

### 核心原则

- 不让上层业务直接依赖具体模型实现
- 所有 AI 能力通过接口抽象

建议定义以下接口：

```kotlin
interface OcrEngine
interface TranslationEngine
interface SpeechToTextEngine
interface TextToSpeechEngine
interface RetrievalEngine
interface ChatEngine
interface PoiSearchEngine
```

### 能力编排关系

- Photo Translate = Camera + OCR + Translation
- Voice Translate = ASR + Translation + TTS
- AI Q&A = Retrieval + ChatEngine + Context Builder
- Gems = Location + PoiSearch + Ranking + Optional Chat Summary

## 12.11 本地能力选型建议

以下为推荐落地方案：

### OCR

- 首选：ML Kit Text Recognition v2
- 作用：菜单、路牌、票据、海报等识别

### 文本翻译

- 首选：端侧专用翻译能力
- 方案 A：ML Kit on-device translation
- 方案 B：自带离线翻译模型，通过统一接口封装

建议：

- MVP 可先使用成熟离线翻译能力快速落地
- 后续如需完全可控和包内预置，再替换为自管模型

### ASR

- 首选：系统端侧语音识别能力
- 兜底：本地轻量语音识别引擎

原因：

- Android 系统识别的设备兼容性不一致，必须有兜底方案

### TTS

- 使用 Android `TextToSpeech`

### 小模型问答

- 使用 1B 到 3B 量级的量化指令模型
- 通过本地推理框架运行

推荐推理方向：

- MediaPipe LLM Inference
- ONNX Runtime Mobile

### 地图

- 使用 MapLibre
- 配套离线地图瓦片包和 POI 数据包

## 12.12 RAG 架构建议

百科问答和附近推荐说明不应该直接靠大模型裸答，建议用轻量 RAG：

1. 知识包导入本地
2. 建立 SQLite FTS 索引
3. 根据问题检索 Top-K 文档片段
4. 组装 Prompt
5. 小模型生成答案
6. 前端展示引用来源

MVP 阶段不强制做向量数据库，优先使用：

- 标题匹配
- 关键词召回
- FTS 排序
- 少量规则重排

这样更容易落地，也更省资源。

## 12.13 权限设计

涉及权限：

- 相机
- 麦克风
- 定位
- 媒体读取
- 通知（资源下载完成提醒，可选）

要求：

- 权限请求必须延迟到功能触发时
- 拒绝后给出明确降级提示

## 13. 资源包设计

## 13.1 资源类型

- 语言包
- 翻译模型包
- ASR 模型包
- 小模型包
- 知识包
- 城市地图包
- POI 数据包

## 13.2 ResourcePackage 统一模型

统一管理以下字段：

- packageId
- packageType
- version
- sizeBytes
- languageOrCityCode
- installStatus
- localPath
- checksum
- updatedAt

## 13.3 安装状态

- `NotInstalled`
- `Downloading`
- `Installing`
- `Ready`
- `Failed`
- `UpdateAvailable`

## 14. 功能详细需求

## 14.1 首页

### 目标

降低用户决策成本，统一进入各能力。

### 功能点

- 统一搜索框
- 最近使用入口
- 快捷卡片
- 今日提示
- 最近城市信息

### 验收标准

- 用户进入首页后 1 次点击内可进入核心能力
- 最近一次翻译历史可在首页看到

## 14.2 文本翻译

### 功能点

- 源/目标语言切换
- 自动检测
- 文本输入
- 结果输出
- 一键交换语言
- 历史记录
- 收藏
- 朗读

### 异常态

- 语言包未安装
- 翻译引擎初始化失败
- 输入为空

### 验收标准

- 100 字以内文本翻译在目标设备上平均响应不超过 1 秒

## 14.3 语音翻译

### 功能点

- 录音开始/停止
- 实时识别中状态
- 识别文本回显
- 翻译结果展示
- TTS 播报
- 对话历史

### 异常态

- 麦克风权限缺失
- 本地 ASR 不支持当前语言
- 识别为空

## 14.4 拍照翻译

### 功能点

- CameraX 实时预览
- 拍照
- 相册导入
- OCR 文本框识别
- 文本框翻译叠层
- 列表模式查看
- 保存记录

### 异常态

- 相机权限缺失
- 图像过暗
- OCR 无结果
- 目标语言包未安装

### 验收标准

- 对常规菜单和路牌照片可以识别出主要文本区块

## 14.5 百科

### 功能点

- 搜索框
- 分类卡片
- 文章详情
- 收藏
- 最近浏览

### 验收标准

- 用户可在无网状态下打开知识详情页

## 14.6 AI 问答

### 功能点

- 聊天输入
- 上下文串联
- 本地知识引用
- 地图卡片嵌入
- 后续建议问题

### 约束

- 必须优先走本地检索
- 答案中要保留引用来源

## 14.7 地图与附近推荐

### 功能点

- 定位
- 地图查看
- POI 点位
- 列表卡片
- 筛选
- 收藏
- 地点 AI 小贴士

### 验收标准

- 用户在已下载城市包时可离线查看附近地点

## 14.8 设置与资源管理

### 功能点

- 订阅状态展示与权益说明
- 订阅开通入口
- 恢复购买入口
- 管理订阅入口
- 模型包下载和删除
- 语言包下载和删除
- 地图包下载和删除
- 知识包下载和删除
- 存储管理
- 隐私设置
- 行为偏好开关（离线模式、自动下载、OCR 质量）
- 视觉风格切换（Mint/Warm）
- 清空历史

### 订阅权益建议（Pro）

- 高质量 OCR 档位
- 大体量本地模型包下载权限
- 优先获得模型和知识包更新
- 高级离线包容量上限

## 15. 开发建议依赖

建议新增依赖方向如下：

- Navigation Compose
- Lifecycle ViewModel Compose
- Hilt
- Room
- DataStore
- WorkManager
- CameraX
- MapLibre
- ML Kit OCR
- 翻译引擎依赖
- 本地推理框架

## 16. 开发阶段规划

## 16.1 Phase 0：基建

- 建立 Navigation Compose
- 引入 Hilt
- 引入 Room 与 DataStore
- 建立设计系统与基础组件
- 建立模块包结构

交付结果：

- 从 `Hello Android` 升级为可导航的应用壳

## 16.2 Phase 1：翻译闭环

- 文本翻译
- 语音翻译
- 翻译历史
- 设置页初版
- 两套风格切换（Mint/Warm）
- 订阅中心 UI 与本地权益状态

交付结果：

- 完成最小可用翻译产品

## 16.3 Phase 2：拍照翻译

- CameraX
- OCR
- 叠层翻译
- 历史记录

交付结果：

- 菜单和路牌拍照翻译可用

## 16.4 Phase 3：百科

- 知识包导入
- 本地检索
- 分类浏览
- 详情页

交付结果：

- 完成本地百科闭环

## 16.5 Phase 4：AI Q&A

- RAG 编排
- 小模型接入
- 多轮聊天

交付结果：

- 问答可基于本地知识输出结构化结果

## 16.6 Phase 5：地图与附近推荐

- 离线地图
- 本地 POI 检索
- 收藏与筛选

交付结果：

- 完成旅行工具箱主闭环

## 17. 风险与应对

### 风险 1：设备性能差异大

应对：

- 引入能力探测
- 根据设备能力选择不同模型和不同功能级别

### 风险 2：本地小模型效果不稳定

应对：

- 缩小首期问答范围
- 使用 RAG 限制输出范围
- 对答案加入引用和免责声明

### 风险 3：地图与 POI 数据维护成本高

应对：

- 首期只做有限城市包
- 不承诺实时数据

### 风险 4：OCR 和 ASR 在多语言场景下准确率不足

应对：

- 首期优先支持有限语言对
- 提供手动编辑和二次确认入口

### 风险 5：无自建服务端下订阅校验被绕过

应对：

- 先接入 Play Billing 官方流程与恢复购买
- 本地权益状态加入到期时间与最近校验时间
- 每次联网启动时触发权益重新校验
- 高价值能力增加软降级和灰度开关

## 18. 验收标准

满足以下条件可认为 MVP 达标：

1. App 具备完整的首页和底部导航。
2. 文本翻译、语音翻译、拍照翻译可在主要测试机型运行。
3. 历史记录、收藏、设置页可用。
4. 百科搜索和详情页可离线打开。
5. AI Q&A 可基于本地知识输出答案和引用。
6. 地图页面可展示本地离线 POI。
7. 整体架构为 Compose + MVVM，并具备后续模块扩展空间。
8. 设置页支持 Mint/Warm 风格切换并在重启后保持。
9. 设置页包含订阅入口、恢复购买入口和权益展示。

## 19. 实施结论

该项目适合采用“先翻译，后知识，再问答，最后地图”的路线推进。原因如下：

- 你当前项目几乎没有历史包袱，适合从架构基线开始搭。
- 翻译和拍照翻译是最强刚需，也是最容易验证价值的能力。
- 百科与问答必须建立在本地知识包之上，否则纯本地方案无法成立。
- 地图与附近推荐的数据准备成本最高，应放在后续阶段。

最终建议的首版目标不是“万能旅行 AI”，而是：

一个可离线使用的旅行翻译 + 本地知识助手。

这会比一开始就做全量地图、全量问答、全量多语言更容易真正落地。
