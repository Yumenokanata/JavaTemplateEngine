# JavaTemplateEngine
Simple Java code TemplateEngine, use xml.

一个简易(功能的意义上)的模板引擎，用于根据xml文件描述的类结构生成java代码文件。

1. 使用xml描述类结构，不同于置换型模板引擎，不需要声明导包只需要描述结构，引擎会管理自动导包。
2. 包含一个简易(功能意义上的)置换型模板引擎，可以对xml或者任意字符串进行变量置换，支持添加分隔符、首字母大小写、全大小写 语法。
3. 包含一个格式化引擎，通过语法分析进行自动代码格式化。
4. 通过配置文件定义待使用类。

## 使用方法

Demo：indi.yume.daggergenerator.example.Test11.java

//声明一个字符串置换引擎   
VarStringEngine varStringEngine = new VarStringEngine();    
//绑定一个变量   
varStringEngine.binding("name", "A09_1_TestTest");    
//新建模板引擎，需要配置文件和一个字符串置换引擎，此时会根据这两个文件生成一个类的图，在之后渲染模板时进行类分析   
TemplateEngine templateEngine = new TemplateEngine(new File(baseFile.getAbsoluteFile()+"/src/indi/yume/daggergenerator/example/config.xml"), varStringEngine);   
//设定模板文件，分析此文件并初始化类生成器   
templateEngine.setTemplateFile(new File(baseFile.getAbsoluteFile()+"/src/indi/yume/daggergenerator/example/activity.xml"));   
//使用类生成器生成代码   
String content = templateEngine.render();   

## 单独使用

字符串置换引擎、代码格式化引擎两个子模块也可单独使用

### 字符串置换引擎（VarStringEngine）

//声明一个字符串置换引擎   
VarStringEngine varStringEngine = new VarStringEngine();    
//绑定一个变量   
varStringEngine.binding("name", "A09_1_TestTest");    
String oriString = "hello, ${_<name}";   
varStringEngine.binding("name", "TestTest");   
String string = varStringEngine.analysisString(oriString); // "hello, test_Test"   

### 代码格式化引擎（StringContentEngine）

StringContentEngine.generateString(String startString, String tabString, String content);
