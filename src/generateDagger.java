import indi.yume.daggergenerator.*;
import indi.yume.daggergenerator.generator.NewLine;
import indi.yume.daggergenerator.model.*;
import indi.yume.daggergenerator.util.FileUtil;

import java.io.File;
import java.util.Scanner;

/**
 * Created by yume on 15/9/26.
 */
public class generateDagger {
    public static void main(String[] args){
        String mainPath = "";
        String prefix = "";

        Scanner sc = new Scanner(System.in);

        if(mainPath == null || "".equals(mainPath)) {
            System.out.println("请输入工程的 app->src->main文件夹所在的路径：");
            mainPath = sc.nextLine();
        }
        if(prefix == null || "".equals(prefix)) {
            System.out.println("请输入前缀：");
            prefix = sc.nextLine();
        }

        ClazzInfo dependencies = new ClazzInfo(Type.CLASS, "com.happy_bears.mybears.ui", "AppComponent");

        Schema.Builder builder = Schema.createFragment(mainPath,
                "com.happy_bears.mybears.ui",
                prefix);
        builder.setModulePackage("module");
        builder.setComponentPackage("component");
        builder.setViewPackage("fragment");
        builder.setPresenterPackage("presenter");
        builder.addComponentDependencies(dependencies);
        builder.setViewExtend(new ClazzInfo(Type.CLASS, "com.happy_bears.mybears.ui.fragment", "BaseFragment"));
        Schema schema = builder.create();
        schema.generatorFiles();
    }
}
