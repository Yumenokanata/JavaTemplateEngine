package indi.yume.daggergenerator;

import indi.yume.daggergenerator.generator.ClazzGenerator;
import indi.yume.daggergenerator.generator.Generator;
import indi.yume.daggergenerator.generator.NewLine;
import indi.yume.daggergenerator.model.*;
import indi.yume.daggergenerator.util.TextUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yume on 2015/9/27.
 */
public class ActivityGenerator implements Generator, Constants {
    public static final String POSTFIX = "Activity";

    private static final String PRESENTER_VAR_NAME = "presenter";

    private String xmlName;

    private String prefix;

    private ClazzInfo module;
    private ClazzInfo presenter;
    private ClazzInfo appComponent;
    private ClazzInfo presenterListener;
    private ClazzInfo component;

    private ClazzInfo extendsClazz = activity;

    private ClazzInfo baseClazzInfo = new ClazzInfo();

    public ActivityGenerator(String packageName, String prefix, String xmlName,ClazzInfo module, ClazzInfo presenter, ClazzInfo presenterListener, ClazzInfo appComponent, ClazzInfo component){
        this.prefix = prefix;
        this.presenter = presenter;
        this.appComponent = appComponent;
        this.presenterListener = presenterListener;
        this.xmlName = xmlName;
        this.module = module;
        this.component = component;

        baseClazzInfo.setType(Type.CLASS);
        baseClazzInfo.setPackageName(packageName);
        baseClazzInfo.setClazzName(prefix + POSTFIX);
    }

    public ClazzInfo getBaseClazzInfo() {
        return baseClazzInfo;
    }

    public void setExtendsClazz(ClazzInfo extendsClazz) {
        this.extendsClazz = extendsClazz;
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
        clazzGenerator.addInterface(presenterListener);

        final PropertyInfo presenterVar = new PropertyInfo(presenter, PRESENTER_VAR_NAME);
        presenterVar.addAnnotation(inject);
        clazzGenerator.addProperty(presenterVar);

        MethodInfo onCreateView = new MethodInfo(view,
                "OnCreate",
                new MethodInfo.ParamInfo[]{
                        new MethodInfo.ParamInfo(bundle, "savedInstanceState")
                });
        onCreateView.addAnnotation(override);
        onCreateView.setMethodBodyGenerator(new MethodBodyGenerator() {
            @Override
            public StringBuilder generatorMethodBody(StringBuilder stringBuilder, NewLine newline) {
                if (xmlName == null) {
                    xmlName = prefix.replace(" ", "_");
                    xmlName = TextUtil.firstToLower(xmlName);
                    xmlName = xmlName + "_" + POSTFIX.toLowerCase();
                }
                stringBuilder.append(newline.getPrefix())
                        .append("setContentView(R.layout.")
                        .append(xmlName)
                        .append(");\n")
                        .append(newline.getPrefix())
                        .append("super.onCreate(savedInstanceState);\n\n");
                return stringBuilder;
            }
        });
        clazzGenerator.addMethod(onCreateView);

        // inject
        MethodInfo injectMethod = new MethodInfo(voidClazz,
                "inject",
                new MethodInfo.ParamInfo[]{
                        new MethodInfo.ParamInfo(appComponent, "appComponent")
                });
        injectMethod.addAnnotation(override);
        injectMethod.setMethodBodyGenerator(new MethodBodyGenerator() {
            @Override
            public StringBuilder generatorMethodBody(StringBuilder stringBuilder, NewLine newline) {
                String componentVar = "homeComponent";

                // A01_1_HomeComponent homeComponent = DaggerA01_1_HomeComponent.builder()
                stringBuilder.append(newline.getPrefix())
                        .append(prefix).append(ComponentGenerator.POSTFIX)
                        .append(" ")
                        .append(componentVar)
                        .append(" = Dagger")
                        .append(prefix).append(ComponentGenerator.POSTFIX)
                        .append(".builder()\n")

                                //.appComponent(appComponent)
                        .append(newline.getPrefix()).append(NewLine.EightSpace)
                        .append(".appComponent(appComponent)\n")

                                //.a01_1_HomeModule(new A01_1_HomeModule(this))
                        .append(newline.getPrefix()).append(NewLine.EightSpace)
                        .append(".")
                        .append(TextUtil.firstToLower(module.getClazzOriginName()))
                        .append("(new ")
                        .append(module.getClazzOriginName())
                        .append("(this))\n")

                                //.build();
                        .append(newline.getPrefix()).append(NewLine.EightSpace)
                        .append(".build();\n")

                                //homeComponent.injectFragment(this);
                        .append(newline.getPrefix())
                        .append(componentVar)
                        .append(".inject")
                        .append(POSTFIX)
                        .append("(this);\n")

                                //homeComponent.injectPresenter(presenter);
                        .append(newline.getPrefix())
                        .append(componentVar)
                        .append(".inject")
                        .append(PresenterGenerator.POSTFIX)
                        .append("(")
                        .append(PRESENTER_VAR_NAME)
                        .append(");\n");

                return stringBuilder;
            }
        });
        clazzGenerator.addMethod(injectMethod,
                component,
                module,
                R);

        return clazzGenerator;
    }
}
