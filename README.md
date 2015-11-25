# JavaTemplateEngine
Simple Java code TemplateEngine, use xml.

一个简易(功能的意义上)的模板引擎，用于根据xml文件描述的类结构生成java代码文件。

1. 使用xml描述类结构，不同于置换型模板引擎，不需要声明导包只需要描述结构，引擎会管理自动导包。
2. 包含一个简易(功能意义上的)置换型模板引擎，可以对xml或者任意字符串进行变量置换，支持添加分隔符、首字母大小写、全大小写 语法。
3. 包含一个格式化引擎，通过语法分析进行自动代码格式化。
4. 通过配置文件定义待使用类。

## 使用方法

Demo：indi.yume.daggergenerator.example.Test11.java
```java
//声明一个字符串置换引擎   
VarStringEngine varStringEngine = new VarStringEngine();    
//绑定一个变量   
varStringEngine.binding("name", "A09_1_TestTest");    
//新建模板引擎，需要配置文件和一个字符串置换引擎，此时会根据这两个文件生成一个类的图，在之后渲染模板时进行类分析   
TemplateEngine templateEngine = new TemplateEngine(new File("config.xml"), varStringEngine);   
//设定模板文件，分析此文件并初始化类生成器   
templateEngine.setTemplateFile(new File("activity.xml"));   
//使用类生成器生成代码   
String content = templateEngine.render();  
```

## XML描述文件结构

### 配置文件

```xml
<!-- 基本属性（注解、类、接口都相同） -->
<!-- (必须)name 此引用类的引用名，在classVarName属性中调用 -->
<!-- (必须)packageName 所在包 -->
<!-- (必须)className 类名 -->
<!-- (可选)modifier 修饰符，如private、static等 -->
<!-- (可选)isInner 是否为内部类（导包时的处理方式不同） -->

<!-- config为根节点 -->
<config>
    <!-- annotation 声明一个注解引用  -->
    <annotation name="inject" packageName="javax.inject" className="Inject"/>
    <!-- class 声明一个类引用  -->
    <!-- 不需要导包时将packageName属性设置为空即可  -->
    <class name="string" packageName="" className="String"/>
    <!-- interface 声明一个接口引用  -->
    <interface isInner="true" name="inject" packageName="javax.inject" className="Inject"/>
    
    <!-- generic 声明一个此引用类的泛型（可用于接口和普通类）  -->
    <class name="map" packageName="" className="Map">
      <!-- 单个泛型 -->
      <generic classVarName="string">
      <!-- 多个泛型（两种方式可混合使用） -->
      <generic>
        <item classVarName="string">
        <item classVarName="string">
      </generic>
    </class>
</config>
```

### 类描述文件 

***classVarName 全为配置文件中 name属性的设置***

```xml
<!-- classMaker为根节点 -->
<!-- 需要设置此生成类的基本属性，如所在包、类名等，设置有两种方式：-->
<!-- 1、通过设定classVarName属性，调用配置文件中描述的类属性来设置此类的属性 -->
<!-- 2、通过type、packageName、className和modifier三个Attr属性设置 -->
<classMaker classVarName="fragment">

    <!-- extends 设置父类 -->
    <extends classVarName="baseFragment"/>
    <!-- implements 设置继承的接口，可通过Attr属性设置，也可使用<item>标签设置（两种可混用） -->
    <implements classVarName="presenterGetInterface">
      <item classVarName="presenterGetInterface"/>
    </implements>

    <!-- note 设置注释，会自动添加/** 和 */ 符号 -->
    <note>${note}</note>

    <!-- property 添加一个变量声明 -->
    <!-- valueName 变量名 -->
    <!-- classVarName 引用类 -->
    <!-- modifier（可选） 修饰符 -->
    <property valueName="presenter"
              classVarName="presenter"
              modifier="private">
        <!-- anno 声明此变量的注解，可对<property>、<method>、<classMaker>等标签中使用 -->
        <anno classVarName="inject"/>
    </property>

    <!-- method 添加一个方法 -->
    <!-- returnClassName 返回类型（不设置时则没有返回值，如构造函数） -->
    <!-- methodName 方法名 -->
    <!-- modifier（可选） 修饰符 -->
    <method returnClassName="view" methodName="onCreateView">
        <anno classVarName="override"/>

        <!-- methodParam 添加一个方法参数 -->
        <!-- classVarName 引用类 -->
        <!-- valueName 变量名 -->
        <methodParam classVarName="layoutInflater" valueName="inflater"/>
        <methodParam classVarName="viewGroup" valueName="container"/>
        <methodParam classVarName="bundle" valueName="savedInstanceState"/>

        <!-- include 额外导入的包 -->
        <include classVarName="butterknife"/>
        <include classVarName="R"/>
        
        <!-- body 方法体 -->
        <!-- 此标签内文本只会以手动\n符号计算换行符 -->
        <!-- 通过\n符号换行后会通过自动格式化引擎进行重新格式化 -->
        <body>
            View view = inflater.inflate(R.layout.${_-name}_fragment, container, false);\n
            ButterKnife.bind(this, view);\n
            \n
            return view;
        </body>
    </method>
</classMaker>
```

## 单独使用

字符串置换引擎、代码格式化引擎两个子模块也可单独使用

### 字符串置换引擎（VarStringEngine）

```java
//声明一个字符串置换引擎   
VarStringEngine varStringEngine = new VarStringEngine();    
//绑定一个变量   
varStringEngine.binding("name", "A09_1_TestTest");    
String oriString = "hello, ${_<name}";   
varStringEngine.binding("name", "TestTest");   
String string = varStringEngine.analysisString(oriString); // "hello, test_Test"   
```

#### 语法
  关键字：- + < >
  1. -   
  全部字母小写
  2. +   
  全部字母大写
  3. <   
  首字母小写
  4. \>   
  首字母大写
  
  其中以关键字为分割，前面如果有内容则以前面的内容作为分隔符对字符串进行分割   
  如"${&&&+name}"定义name="TestTest&&&Test"则输出："TEST&&&TEST&&&TEST"

### 代码格式化引擎（StringContentEngine）

通过一个堆栈分析字符串中的语法成分（主要是利用"{}"与"()"的层数与其他一些语法规则）来进行代码的格式化   
#### 用法
```java
StringContentEngine.generateString(String startString, String tabString, String content);
```
