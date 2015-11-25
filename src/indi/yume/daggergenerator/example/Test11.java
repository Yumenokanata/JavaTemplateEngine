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
