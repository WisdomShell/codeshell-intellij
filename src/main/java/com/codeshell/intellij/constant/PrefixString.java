package com.codeshell.intellij.constant;

public interface PrefixString {

    String EXPLAIN_CODE = "请解释以下%s代码: %s";

    String OPTIMIZE_CODE = "请优化以下%s代码: %s";

    String CLEAN_CODE = "请清理以下%s代码: %s";

    String COMMENT_CODE = "请为以下%s代码的每一行生成注释: %s";

    String UNIT_TEST_CODE = "请为以下%s代码生成单元测试: %s";

    String PERFORMANCE_CODE = "检查以下%s代码，是否存在性能问题，请给出优化建议: %s";

    String STYLE_CODE = "检查以下%s代码的风格样式，请给出优化建议: %s";

    String SECURITY_CODE = "检查以下%s代码，是否存在安全性问题，请给出优化建议: %s";

    String MARKDOWN_CODE_FIX = "```";

    String REQUST_END_TAG = "|<end>|";

    String RESPONSE_END_TAG = "<|endoftext|>";
}
