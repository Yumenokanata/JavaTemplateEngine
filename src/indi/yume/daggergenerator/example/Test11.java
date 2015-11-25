package indi.yume.daggergenerator.example;

import com.google.googlejavaformat.java.FormatterException;
import indi.yume.daggergenerator.template.StringContentEngine;
import indi.yume.daggergenerator.template.TemplateEngine;
import indi.yume.daggergenerator.template.VarStringEngine;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yume on 15/11/24.
 */
public class Test11 {
    public static void main(String[] args){
//        VarStringEngine varStringEngine = new VarStringEngine();
//
//        varStringEngine.binding("name", "Bush");
//        varStringEngine.binding("name1", "_-bushTang");
//        varStringEngine.binding("name2", "bushTang");
//
//        String oriString = "My name is ${name} not is $$${-name2}, but $ not in ${ ^name1}";
//
//        String targetString = null;
//        try {
//            targetString = varStringEngine.analysisString(oriString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(oriString);
//        System.out.println(targetString);

//        System.out.println(Jsoup.parse("<b><b/></b><b/>").getElementsByTag("c").first());
//
//        String ori = "while (index < noEmptyString.length()) {\n" +
//                "            char nowChar = noEmptyString.charAt(index);\n" +
//                "            if(nowChar == '\\n'){\n" +
//                "                newContent.append('\\n')\n" +
//                "                        .append(newLineTab.getTabString())\n" +
//                "                        .append(newLineTab.getTabString());\n" +
//                "            } else if(nowChar == '(' || nowChar == '{') {\n" +
//                "                newLineTab = newLineTab.push(nowChar, newLineTab.getTabString() + tabString);\n" +
//                "                tempTab = \"\";\n" +
//                "                newContent.append(nowChar);\n" +
//                "            } else if(nowChar == ')' || nowChar == '}'){\n" +
//                "                if((nowChar == ')' && newLineTab.getC() != '(')\n" +
//                "                        || (nowChar == '}' && newLineTab.getC() != '{'))\n" +
//                "                    throw new Exception(\"() or {} error\");\n" +
//                "                newLineTab = newLineTab.pop();\n" +
//                "\n" +
//                "                newContent.append(nowChar);\n" +
//                "            } else {\n" +
//                "                newContent.append(nowChar);\n" +
//                "            }\n" +
//                "            ++index;\n" +
//                "        }";
//        try {
//            System.out.println(StringContentEngine.custom("", "    ", ori));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
//        try {
//            Configuration cfg = Configuration.defaultConfiguration();
//            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
//            Template t = gt.getTemplate("hello,${name}");
//            t.binding("name", "Bush");
//            String str = t.render();
//            System.out.println(str);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        VarStringEngine varStringEngine = new VarStringEngine();
        varStringEngine.binding("basePackage", "com.happy_bears.mybears.android");
        varStringEngine.binding("diPackage", ".di");
        varStringEngine.binding("uiPackage", ".ui");
        varStringEngine.binding("fragmentPackage", ".ui.fragment");
        varStringEngine.binding("activityPackage", ".ui.activity");
        varStringEngine.binding("presenterPackage", ".ui.presenter");
        varStringEngine.binding("componentPackage", ".ui.component");
        varStringEngine.binding("modulePackage", ".ui.module");

        varStringEngine.binding("name", "A09_1_TestTest");

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        varStringEngine.binding("note", "Created by DaggerGenerator on " + df.format(Calendar.getInstance().getTime()) + ".");

        varStringEngine.binding("type", "Activity");
        try {
            File baseFile = new File("");
            TemplateEngine templateEngine = new TemplateEngine(new File(baseFile.getAbsoluteFile() + "/src/indi/yume/daggergenerator/example/config.xml"), varStringEngine);
            templateEngine.setTemplateFile(new File(baseFile.getAbsoluteFile() + "/src/indi/yume/daggergenerator/example/activity.xml"));
            String content = templateEngine.render();
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
