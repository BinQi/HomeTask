## Detekt 使用指南

Detekt是一个kotlin编写的静态代码检测工具，除了代码风格检测，还支持圈复杂度、潜在bug逻辑分析、性能检测等功能，具体可以详看GitHub上官网 detekt

### 基本概念

- baseline： 基线文件，基线是指你如果针对老代码不想要修改历史问题，可以把它放入基线文件中这样子就不会触发检查
- config： 规则配置文件，可以控制每条规则的开关，详细配置信息可参考官方文档（https://detekt.github.io/detekt/comments.html）

### 关键 Task

```
./gradlew detekt 执行代码检查，如果有问题会输出
./gradlew detektBaseline 重新生成 baseline 文件
```

### pre-commit hook

只要构建一次 app 模块就会自动复制 pre-commit hook 规则到 .git/hooks 下，之后每次 commit 代码都会执行 `./gradlew detekt` ，如果执行失败则提交失败，可以根据错误信息知道问题所在，

或通过手动执行 `./gradlew detekt`也可检查问题并修复

### 紧急使用

当遇到现下不能解决的 lint 问题时可使用下方几种方式之一暂时略过静态代码检查
- 使用 `Suppress` 注解

举例：
```
@Suppress("LargeClass") // or use complexity.LargeClass
object Constants {
    ...
}
```
具体可参考官方文档（https://detekt.github.io/detekt/suppressing-rules.html#formatting-rules-suppression）

- ./gradlew :{moduleName}:detektBaseline 
可重新生成这个 module 的 baseline 文件，baseline 中包含的问题会被忽略

- git commit --no-verify 
本次提交跳过 pre-commit hook 

### 参考链接

- Detekt 官方文档(https://detekt.github.io/detekt/index.html)
- 波动星球 Detekt 配置（https://git.woa.com/standards/kotlin/tree/master/detekt）
