# CodeShell IntelliJ IDEA Extension

The `codeshell-intellij`project is an open-source plugin developed based on the [CodeShell LLM](https://github.com/WisdomShell/codeshell) that support various IDEs, including [IntelliJ IDEA、Pycharm、GoLang](https://www.jetbrains.com/zh-cn/products/). It serves as an intelligent coding assistant, offering support for various programming languages such as Python, Java, C/C++, JavaScript, Go, and more. This plugin provides features like code completion, code interpretation, code optimization, comment generation, and conversational Q&A to help developers enhance their coding efficiency in an intelligent manner.

## Requirements

- The [CodeShell](https://github.com/WisdomShell/llama_cpp_for_codeshell) service is running
- The IDE version requirement is between 2022.2 and 2023.2

##  Compile the Plugin

If you want to package from source code, please obtain the code first:

```bash
git clone https://github.com/WisdomShell/codeshell-intellij.git
```

- The project uses Gradle to manage dependencies. Click the "Refresh" button to automatically reload dependencies.
- To run the plugin locally, navigate to `Gradle` > `CodeShell` > `Task` > `intellij` > `runIde`.
- Right-click on `runIde` and choose to start in Debug mode.


![插件DEBUG截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/debug-plugin.png)

### Package the Plugin

- Generate a Local Plugin Installation Package: Navigate to `Gradle` > `CodeShell` > `Task` > `intellij` > `buildPlugin`.
- Once the packaging task is completed, the plugin installation package can be found in the `build/distributions` directory at the project's root.

### Install the Plugin

- Installation Process: Go to `Settings` > `Plugin` > `Install Plugin from Disk...`, and in the opened file selection window, choose the plugin installation package

![插件安装截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/install-plugin-disk.png)

##  Model Service

The [`llama_cpp_for_codeshell`](https://github.com/WisdomShell/llama_cpp_for_codeshell) project provides the 4-bit quantized model service of the [CodeShell](https://github.com/WisdomShell/codeshell) LLM, named `CodeShell_q4_0.gguf`. Here are the steps to deploy the model service:

### Get the Code

```bash
git clone https://github.com/WisdomShell/llama_cpp_for_codeshell.git
cd codeshell.cpp
```

### Load the model locally

After downloading the model from the [Hugging Face Hub](https://huggingface.co/WisdomShell/CodeShell-7B-Chat-int4/blob/main/codeshell-chat-q4_0.gguf) to your local machine, placing the model in the `llama_cpp_for_codeshell/models` folder path in the above code will allow you to load the model locally.

```bash
git clone https://huggingface.co/WisdomShell/CodeShell-7B-Chat-int4/blob/main/codeshell-chat-q4_0.gguf
```

### Deploy the Model

Use the `server` command in the `llama_cpp_for_codeshell` project to provide API services.

```bash
./server -m ./models/CodeShell_q4_0.gguf --host 127.0.0.1 --port 8080
```

The default deployment is on local port 8080, and it can be called through the POST method.

##  Configure the Plugin

- Set the address for the CodeShell service
- Configure whether to enable automatic code completion suggestions
- Specify the maximum number of tokens for code completion
- Specify the maximum number of tokens for Q&A

![插件配置截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_config.png)

## Features

### 1. Code Assistance

- Explain/Optimize/Cleanse a Code Segment
- Generate Comments/Unit Tests for Code
- Check Code for Performance/Security Issues

In the IDE sidebar, open the plugin's Q&A interface. Select a portion of code in the editor, right-click to access the CodeShell menu, and choose the corresponding function. The plugin will provide relevant responses in the Q&A interface.

Within the Q&A interface's code block, you can click the copy button to copy the code block or use the insert button to insert the code block's content at the editor's cursor location.

![代码辅助截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_inte.png)

### 2. Code Q&A

- Support for Multi-turn Conversations
- Edit Questions and Rephrase Inquiries
- Request Fresh Responses for Any Question
- Interrupt During the Answering Process

![智能问答截图](https://resource.zsmarter.cn/appdata/codeshell-intellij/screenshots/code_chat.png)

## License
Apache 2.0
