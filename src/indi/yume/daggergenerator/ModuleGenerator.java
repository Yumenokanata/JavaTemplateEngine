package indi.yume.daggergenerator;

import indi.yume.daggergenerator.generator.ClazzGenerator;
import indi.yume.daggergenerator.generator.Generator;
import indi.yume.daggergenerator.generator.NewLine;
import indi.yume.daggergenerator.model.*;

import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yume on 15/9/26.
 */
public class ModuleGenerator implements Generator {
    public static final String POSTFIX = "Module";

    public static final String PROVIDE = "provide";
    private static final String FRAGMENT_POSTFIX = "Fragment";
    public String viewVarName = "fragment";

    private ClazzInfo presenter;
    private ClazzInfo fragment;

    private String prefix;

    private ClazzInfo extendsClazz;

    private List<AnnotationInfo> presenterProvideAnnotation = new ArrayList<>();
    private List<MethodInfo> methodList = new ArrayList<>();

    private ClazzInfo baseClazzInfo = new ClazzInfo();

    public ModuleGenerator(String packageName, String prefix, ClazzInfo presenter, ClazzInfo fragment){
        this.prefix = prefix;
        this.presenter = presenter;
        this.fragment = fragment;

        baseClazzInfo.setType(Type.CLASS);
        baseClazzInfo.setPackageName(packageName);
        baseClazzInfo.setClazzName(prefix + POSTFIX);

        baseClazzInfo.addAnnotation(new AnnotationInfo("dagger", "Module"));


    }

    public void setViewVarName(String viewVarName) {
        this.viewVarName = viewVarName;
    }

    public ClazzInfo getBaseClazzInfo() {
        return baseClazzInfo;
    }

    public void setExtendsClazz(ClazzInfo extendsClazz) {
        this.extendsClazz = extendsClazz;
    }

    public void addMethod(MethodInfo methodInfo){
        methodList.add(methodInfo);
    }

    public void addPresenterProvideAnnotation(AnnotationInfo annotation){
        presenterProvideAnnotation.add(annotation);
    }

    @Override
    public ClazzGenerator generateClazzString() {
        ClazzGenerator clazzGenerator = new ClazzGenerator(baseClazzInfo);

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String note = "/**\n" +
                " * Created by DaggerGenerator on " + df.format(Calendar.getInstance().getTime()) + ".\n" +
                " */";
        clazzGenerator.setNote(note);

        clazzGenerator.setExtendsClazzInfo(extendsClazz);

        clazzGenerator.addProperty(
                new PropertyInfo(fragment, viewVarName.toLowerCase()));

        MethodInfo constructor = new MethodInfo(null,
                prefix + POSTFIX,
                new MethodInfo.ParamInfo[]{
                        new MethodInfo.ParamInfo(fragment, viewVarName.toLowerCase())
                });
        constructor.setMethodBodyGenerator(new MethodBodyGenerator() {
            @Override
            public StringBuilder generatorMethodBody(StringBuilder stringBuilder, NewLine newline) {
                stringBuilder.append(newline.getPrefix())
                        .append("this." + viewVarName.toLowerCase() + " = " + viewVarName.toLowerCase() + ";\n");
                return stringBuilder;
            }
        });
        clazzGenerator.addMethod(constructor);

        MethodInfo presenterProvide = new MethodInfo(presenter,
                PROVIDE + PresenterGenerator.POSTFIX,
                new MethodInfo.ParamInfo[]{});
        for(AnnotationInfo ci : presenterProvideAnnotation)
            presenterProvide.addAnnotation(ci);
        presenterProvide.addAnnotation(Constants.provides);
        presenterProvide.setMethodBodyGenerator(new MethodBodyGenerator() {
            @Override
            public StringBuilder generatorMethodBody(StringBuilder stringBuilder, NewLine newline) {
                stringBuilder.append(newline.getPrefix())
                        .append("return new " + presenter.toString() + "(" + viewVarName.toLowerCase() + ");\n");
                return stringBuilder;
            }
        });
        clazzGenerator.addMethod(presenterProvide);

        for(MethodInfo mi : methodList)
            clazzGenerator.addMethod(mi);

        return clazzGenerator;
    }
}
