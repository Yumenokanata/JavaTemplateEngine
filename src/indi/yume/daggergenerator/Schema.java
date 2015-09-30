package indi.yume.daggergenerator;

import indi.yume.daggergenerator.generator.ClazzGenerator;
import indi.yume.daggergenerator.generator.NewLine;
import indi.yume.daggergenerator.model.ClazzInfo;
import indi.yume.daggergenerator.model.MethodBodyGenerator;
import indi.yume.daggergenerator.model.MethodInfo;
import indi.yume.daggergenerator.model.Type;
import indi.yume.daggergenerator.util.FileUtil;
import indi.yume.daggergenerator.util.TextUtil;

import java.io.File;

/**
 * Created by yume on 15/9/26.
 */
public class Schema implements Constants {
    private String packageName = "";
    private String viewPackage = "";
    private String presenterPackage = "";
    private String modulePackage = "";
    private String componentPackage = "";

    private String xmlName;

    private String prefix;
    private String viewName;

    private String baseMainPath;
    private String layoutPath;

    private ClazzGenerator view;
    private ClazzGenerator presenter;
    private ClazzGenerator component;
    private ClazzGenerator module;

    private static final String xmlFileContent = "<FrameLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\" android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\">\n" +
            "\n" +
            "    <!-- TODO: Update blank fragment layout -->\n" +
            "    <TextView android:layout_width=\"match_parent\" android:layout_height=\"match_parent\"\n" +
            "        android:text=\"@string/hello_blank_fragment\" />\n" +
            "\n" +
            "</FrameLayout>\n";

    private Schema(String baseMainPath, String packageName, String prefix){
        this.baseMainPath = baseMainPath;
        this.packageName = packageName;
        this.prefix = prefix;
    }

    public void generatorFiles(){
        File viewFile = FileUtil.newFile(baseMainPath,
                "java",
                viewPackage.replace(".", File.separator),
                prefix + viewName + ".java");
        File presenterFile = FileUtil.newFile(baseMainPath,
                "java",
                presenterPackage.replace(".", File.separator),
                prefix + PresenterGenerator.POSTFIX + ".java");
        File componentFile = FileUtil.newFile(baseMainPath,
                "java",
                componentPackage.replace(".", File.separator),
                prefix + ComponentGenerator.POSTFIX + ".java");
        File moduleFile = FileUtil.newFile(baseMainPath,
                "java",
                modulePackage.replace(".", File.separator),
                prefix + ModuleGenerator.POSTFIX + ".java");
        File xmlFile = FileUtil.newFile(layoutPath,
                xmlName + ".xml");

        if(viewFile.exists())
            throw new Error("File " + viewFile.getAbsolutePath() + " is exists");
        if(presenterFile.exists())
            throw new Error("File " + presenterFile.getAbsolutePath() + " is exists");
        if(componentFile.exists())
            throw new Error("File " + componentFile.getAbsolutePath() + " is exists");
        if(moduleFile.exists())
            throw new Error("File " + moduleFile.getAbsolutePath() + " is exists");
        if(xmlFile.exists())
            throw new Error("File " + xmlFile.getAbsolutePath() + " is exists");

        FileUtil.writeToFile(view.generate(), viewFile);
        System.out.println("Generate file: " + viewFile.getAbsolutePath());
        FileUtil.writeToFile(presenter.generate(), presenterFile);
        System.out.println("Generate file: " + presenterFile.getAbsolutePath());
        FileUtil.writeToFile(component.generate(), componentFile);
        System.out.println("Generate file: " + componentFile.getAbsolutePath());
        FileUtil.writeToFile(module.generate(), moduleFile);
        System.out.println("Generate file: " + moduleFile.getAbsolutePath());
        FileUtil.writeToFile(xmlFileContent, xmlFile);
        System.out.println("Generate file: " + xmlFile.getAbsolutePath());
    }

    public static Builder createActivity(String baseMainPath, String packageName, String prefix){
        return new Builder(baseMainPath, packageName, prefix, Builder.TYPE_ACTIVITY);
    }

    public static Builder createFragment(String baseMainPath, String packageName, String prefix){
        return new Builder(baseMainPath, packageName, prefix, Builder.TYPE_FRAGMENT);
    }

    public static class Builder{
        private static final int TYPE_ACTIVITY = 0;
        private static final int TYPE_FRAGMENT = 1;

        private Schema schema;
        private int type;
        private ClazzInfo dependencies;
        private ClazzInfo presenterExtend = new ClazzInfo(Type.CLASS, null, "BasePresenter");
        private ClazzInfo viewExtend;
        private ClazzInfo componentExtend;
        private ClazzInfo moduleExtend = new ClazzInfo(Type.CLASS, null, "BaseModule");

        private Builder(String baseMainPath, String packageName, String prefix, int type){
            prefix = TextUtil.firstToUpper(prefix);
            schema = new Schema(baseMainPath, packageName, prefix);
            this.type = type;
            if(type == TYPE_FRAGMENT) {
                schema.viewName = "Fragment";
            } else if(type == TYPE_ACTIVITY){
                schema.viewName = "Activity";
            } else {
                throw new Error("Type is wrong: " + type);
            }

            setComponentPackage(null);
            setViewPackage(null);
            setPresenterPackage(null);
            setModulePackage(null);
        }

        public Builder setViewPackage(String packageName){
            if(packageName != null)
                schema.viewPackage = schema.packageName + "." + packageName;
            else
                schema.viewPackage = schema.packageName;
            return this;
        }

        public Builder setPresenterPackage(String packageName){
            if(packageName != null)
                schema.presenterPackage = schema.packageName + "." + packageName;
            else
                schema.presenterPackage = schema.packageName;
            return this;
        }

        public Builder setModulePackage(String packageName){
            if(packageName != null)
                schema.modulePackage = schema.packageName + "." + packageName;
            else
                schema.modulePackage = schema.packageName;
            return this;
        }

        public Builder setComponentPackage(String packageName){
            if(packageName != null)
                schema.componentPackage = schema.packageName + "." + packageName;
            else
                schema.componentPackage = schema.packageName;
            return this;
        }

        public Builder setXmlName(String xmlName){
            schema.xmlName = xmlName;
            return this;
        }

        public Builder addComponentDependencies(ClazzInfo dependencies){
            this.dependencies = dependencies;
            return this;
        }

        public Builder setPresenterExtend(ClazzInfo presenterExtend) {
            this.presenterExtend = presenterExtend;
            return this;
        }

        public Builder setViewExtend(ClazzInfo viewExtend) {
            this.viewExtend = viewExtend;
            return this;
        }

        public Builder setComponentExtend(ClazzInfo componentExtend) {
            this.componentExtend = componentExtend;
            return this;
        }

        public Builder setModuleExtend(ClazzInfo moduleExtend) {
            this.moduleExtend = moduleExtend;
            return this;
        }

        private String getXmlName(String viewName){
            if(schema.xmlName == null)
                return separateCamelCase(schema.prefix, '_').toLowerCase() + "_" + viewName.toLowerCase();
            return schema.xmlName.toLowerCase();
        }

        private static String separateCamelCase(String name, char separator) {
            StringBuilder translation = new StringBuilder();
            char oldChar = 0;
            for (int i = 0; i < name.length(); i++) {
                char character = name.charAt(i);
                if (oldChar != separator && Character.isUpperCase(character) && translation.length() != 0) {
                    translation.append(separator);
                }
                translation.append(character);
                oldChar = character;
            }
            return translation.toString();
        }

        public Schema create(){
            ClazzInfo view;
            if(type == TYPE_FRAGMENT)
                view = new ClazzInfo(Type.CLASS, schema.viewPackage, schema.prefix + FragmentGenerator.POSTFIX);
            else
                view = new ClazzInfo(Type.CLASS, schema.viewPackage, schema.prefix + ActivityGenerator.POSTFIX);

            schema.xmlName = getXmlName(schema.viewName);

            ClazzInfo presenter = new ClazzInfo(Type.CLASS,
                    schema.presenterPackage,
                    schema.prefix + PresenterGenerator.POSTFIX);
            ClazzInfo module = new ClazzInfo(Type.CLASS,
                    schema.modulePackage,
                    schema.prefix + ModuleGenerator.POSTFIX);
            ClazzInfo component = new ClazzInfo(Type.CLASS,
                    schema.componentPackage,
                    schema.prefix + ComponentGenerator.POSTFIX);

            PresenterGenerator presenterGenerator = new PresenterGenerator(presenter.getPackageName(), schema.prefix);
            presenterGenerator.setExtendsClazz(presenterExtend);
            schema.presenter = presenterGenerator.generateClazzString();

            final ModuleGenerator moduleGenerator = new ModuleGenerator(schema.modulePackage, schema.prefix, presenter, view);
            moduleGenerator.setExtendsClazz(moduleExtend);
            moduleGenerator.setViewVarName(TextUtil.firstToLower(schema.viewName));
            moduleGenerator.addPresenterProvideAnnotation(activityScope);
            MethodInfo presenterProvide = new MethodInfo(context,
                    ModuleGenerator.PROVIDE + "Activity",
                    new MethodInfo.ParamInfo[]{});
            presenterProvide.addAnnotation(Constants.override);
            final String getContext;
            if(type == TYPE_FRAGMENT)
                getContext = moduleGenerator.viewVarName + ".getContext()";
            else
                getContext = moduleGenerator.viewVarName;
            presenterProvide.setMethodBodyGenerator(new MethodBodyGenerator() {
                @Override
                public StringBuilder generatorMethodBody(StringBuilder stringBuilder, NewLine newline) {
                    stringBuilder.append(newline.getPrefix())
                            .append("return " + getContext + ";\n");
                    return stringBuilder;
                }
            });
            moduleGenerator.addMethod(presenterProvide);
            schema.module = moduleGenerator.generateClazzString();

            ComponentGenerator componentGenerator = new ComponentGenerator(schema.componentPackage,
                    schema.prefix,
                    presenter,
                    view,
                    module);
            componentGenerator.setViewVarName(schema.viewName);
            componentGenerator.addAnnotation(Constants.activityScope);
            if(dependencies != null)
                componentGenerator.addDependenciesComponent("dependencies", dependencies);
            schema.component = componentGenerator.generateClazzString();

            if(type == TYPE_FRAGMENT) {
                FragmentGenerator fragmentGenerator = new FragmentGenerator(schema.viewPackage,
                        schema.prefix,
                        schema.xmlName,
                        module,
                        presenter,
                        presenterGenerator.getListenerClazz(),
                        Constants.appComponent,
                        component);
                if(viewExtend != null)
                    fragmentGenerator.setExtendsClazz(viewExtend);
                schema.view = fragmentGenerator.generateClazzString();
            } else if(type == TYPE_ACTIVITY){
                ActivityGenerator activityGenerator = new ActivityGenerator(schema.viewPackage,
                        schema.prefix,
                        schema.xmlName,
                        module,
                        presenter,
                        presenterGenerator.getListenerClazz(),
                        Constants.appComponent,
                        component);
                if(viewExtend != null)
                    activityGenerator.setExtendsClazz(viewExtend);
                schema.view = activityGenerator.generateClazzString();
            } else {
                throw new Error("Type is wrong: " + type);
            }

            schema.layoutPath = schema.baseMainPath + File.separator + "res" + File.separator + "layout";
            return schema;
        }
    }
}
