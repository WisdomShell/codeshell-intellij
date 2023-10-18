# CodeShell IntelliJ IDEA Extension

`codeshell-intellij`项目是基于[CodeShell大模型](https://github.com/WisdomShell/codeshell)开发的支持[IntelliJ IDEA、Pycharm、GoLang](https://www.jetbrains.com/zh-cn/products/)等多种IDE的智能编码助手插件，支持python、java、c++/c、javascript、go等多种编程语言，为开发者提供代码补全、代码解释、代码优化、注释生成、对话问答等功能，旨在通过智能化的方式帮助开发者提高编程效率。

## 环境要求

- [CodeShell 模型服务](https://github.com/WisdomShell/llama_cpp_for_codeshell)已启动
- IDE 版本要求在2022.2至2023.2之间

## 插件配置

- 设置CodeShell大模型服务地址
- 配置是否自动触发代码补全建议
- 配置自动触发代码补全建议的时间延迟
- 配置补全的最大tokens数量
- 配置问答的最大tokens数量

![插件配置截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_config.png)

## 功能特性

### 1. 代码建议

- 自动触发代码建议
- 热键触发代码建议

在编码过程中，当输入停止时，自动触发代码建议（可在插件配置中设置延迟时间1~3秒），或使用热键Alt+\\触发代码建议（Mac: option+\\）。

当插件给出代码建议时，建议内容以灰色显示在编辑器光标处，可按Tab键接受该建议，或继续输入即可忽略该建议。

![代码建议截图](https://resource.zsmarter.cn/appdata/codeshell-vscode/screenshots/docs_completion.png)

### 2. 代码辅助

- 对一段代码进行解释/优化/清理
- 为一段代码生成注释/单元测试
- 检查一段代码是否存在性能/安全性问题

在ide侧边栏中打开插件问答界面，在编辑器中选中一段代码，在鼠标右键CodeShell菜单中选择对应的功能项，插件将在问答界面中给出相应的答复。

在问答界面的代码块中，可以点击复制按钮复制该代码块，也可点击插入按钮将该代码块内容插入到编辑器光标处。

![代码辅助截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_inte.png)

### 3. 智能问答

- 支持多轮对话
- 支持会话历史
- 基于历史会话（做为上文）进行多轮对话
- 可编辑问题，重新提问
- 对任一问题，可重新获取回答
- 在回答过程中，可以打断

![智能问答截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_chat.jpg)

## 开源协议
Apache 2.0