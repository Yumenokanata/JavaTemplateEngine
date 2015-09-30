package indi.yume.daggergenerator;

import indi.yume.daggergenerator.generator.ClazzGenerator;
import indi.yume.daggergenerator.generator.Generator;
import indi.yume.daggergenerator.model.*;
import indi.yume.daggergenerator.util.TextUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yume on 15/9/27.
 */
public class ComponentGenerator implements Generator, Constants {
    public static final String POSTFIX = "Component";

    public static final String PRESENTER_VAR_NAME = "presenter";
    public String viewVarName = "Fragment";

    public static final String INJECT_PREFIX_NAME = "inject";

    private ClazzInfo presenter;
    private ClazzInfo viewClazz;
    private ClazzInfo module;

    private String prefix;

    private ClazzInfo extendsClazz;

    private AnnotationInfo component = new AnnotationInfo("dagger", "Component");

    private List<MethodInfo> methodList = new ArrayList<>();

    private ClazzInfo baseClazzInfo = new ClazzInfo();

    public ComponentGenerator(String packageName, String prefix, ClazzInfo presenter, ClazzInfo viewClazz, ClazzInfo module){
        this.prefix = prefix;
        this.presenter = presenter;
        this.viewClazz = viewClazz;
        this.module = module;

        baseClazzInfo.setType(Type.INTERFACE);
        baseClazzInfo.setPackageName(packageName);
        baseClazzInfo.setClazzName(prefix + POSTFIX);

        component.addParam("modules", module);
        baseClazzInfo.addAnnotation(component);
    }

    public void setViewVarName(String viewVarName) {
        this.viewVarName = viewVarName;
    }

    public void setExtendsClazz(ClazzInfo extendsClazz) {
        this.extendsClazz = extendsClazz;
    }

    public ClazzInfo getBaseClazzInfo() {
        return baseClazzInfo;
    }

    public void addMethod(MethodInfo methodInfo){
        methodList.add(methodInfo);
    }

    public void addAnnotation(AnnotationInfo annotation){
        baseClazzInfo.addAnnotation(annotation);
    }

    public void addDependenciesComponent(String key, ClazzInfo... value){
        component.addParam(key, value);
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

        MethodInfo injectFragment = new MethodInfo(voidClazz,
                INJECT_PREFIX_NAME + TextUtil.firstToUpper(viewVarName),
                new MethodInfo.ParamInfo[]{
                        new MethodInfo.ParamInfo(viewClazz, viewVarName.toLowerCase())
                });
        clazzGenerator.addMethod(injectFragment);

        MethodInfo injectPresenter = new MethodInfo(voidClazz,
                INJECT_PREFIX_NAME + PresenterGenerator.POSTFIX,
                new MethodInfo.ParamInfo[]{
                        new MethodInfo.ParamInfo(presenter, PRESENTER_VAR_NAME)
                });
        clazzGenerator.addMethod(injectPresenter);

        for(MethodInfo mi : methodList)
            clazzGenerator.addMethod(mi);

        return clazzGenerator;
    }
}
