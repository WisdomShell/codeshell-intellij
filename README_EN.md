# CodeShell IntelliJ IDEA Extension

The `codeshell-intellij`project is an open-source plugin developed based on the [CodeShell LLM](https://github.com/WisdomShell/codeshell) that support various IDEs, including [IntelliJ IDEA、Pycharm、GoLang](https://www.jetbrains.com/zh-cn/products/). It serves as an intelligent coding assistant, offering support for various programming languages such as Python, Java, C/C++, JavaScript, Go, and more. This plugin provides features like code completion, code interpretation, code optimization, comment generation, and conversational Q&A to help developers enhance their coding efficiency in an intelligent manner.

## Requirements

- The [CodeShell](https://github.com/WisdomShell/llama_cpp_for_codeshell) service is running
- The IDE version requirement is between 2022.2 and 2023.2.

##  Configuration

- Set the address for the CodeShell service.
- Configure whether to enable automatic code completion suggestions.
- Set the time delay for triggering automatic code completion suggestions.
- Specify the maximum number of tokens for code completion.
- Specify the maximum number of tokens for Q&A.

![插件配置截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_config.png)

## Features

### 1. Code Suggestions

- Automatic Code Suggestions
- Keyboard Shortcut for Code Suggestions

During the coding process, code suggestions are automatically triggered when there is a pause in typing (with a delay configurable in the plugin settings, ranging from 1 to 3 seconds). Alternatively, you can use the keyboard shortcut Alt+\\ (Mac: option+\\) to trigger code suggestions.

When the plugin provides code suggestions, they are displayed in gray at the editor's cursor location. You can press the Tab key to accept the suggestion or continue typing to ignore it.

![代码建议截图](https://resource.zsmarter.cn/appdata/codeshell-vscode/screenshots/docs_completion.png)

### 2. Code Assistance

- Explain/Optimize/Cleanse a Code Segment
- Generate Comments/Unit Tests for Code
- Check Code for Performance/Security Issues

In the ide sidebar, open the plugin's Q&A interface. Select a portion of code in the editor, right-click to access the CodeShell menu, and choose the corresponding function. The plugin will provide relevant responses in the Q&A interface.

Within the Q&A interface's code block, you can click the copy button to copy the code block or use the insert button to insert the code block's content at the editor's cursor location.

![代码辅助截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_inte.png)

### 3. Code Q&A

- Support for Multi-turn Conversations
- Maintain Conversation History
- Engage in Multi-turn Dialogues Based on Previous Conversations
- Edit Questions and Rephrase Inquiries
- Request Fresh Responses for Any Question
- Interrupt During the Answering Process

![智能问答截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_chat.jpg)

## License
Apache 2.0
