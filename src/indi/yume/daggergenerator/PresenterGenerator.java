package indi.yume.daggergenerator;

import indi.yume.daggergenerator.generator.ClazzGenerator;
import indi.yume.daggergenerator.generator.Generator;
import indi.yume.daggergenerator.generator.NewLine;
import indi.yume.daggergenerator.model.*;

import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yume on 15/9/26.
 */
public class PresenterGenerator implements Generator {
    public static final String POSTFIX = "Presenter";
    private static final String LISTENER_POSTFIX = "Listener";
    private static final String LISTENER_VAR_NAME = "listener";
    private String prefix;

    private ClazzInfo extendsClazz;

    private ClazzInfo baseClazzInfo = new ClazzInfo();

    private ClazzInfo listenerClazz;

    public PresenterGenerator(String packageName, String prefix){
        this.prefix = prefix;

        String name = prefix + POSTFIX;
        baseClazzInfo.setType(Type.CLASS);
        baseClazzInfo.setPackageName(packageName);
        baseClazzInfo.setClazzName(name);

        listenerClazz = new InnerClazzInfo(Type.INTERFACE,
                baseClazzInfo.getPackageName() + "." + name,
                name + "." + prefix + LISTENER_POSTFIX);
        listenerClazz.getModifierInfo().setModifier(Modifier.STATIC);
    }

    public ClazzInfo getBaseClazzInfo() {
        return baseClazzInfo;
    }

    public ClazzInfo getListenerClazz() {
        return listenerClazz;
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

        ClazzInfo listenerClazzInner = new ClazzInfo(Type.INTERFACE, null, prefix + LISTENER_POSTFIX);
        listenerClazzInner.getModifierInfo().setModifier(Modifier.STATIC);
        clazzGenerator.addProperty(
                new PropertyInfo(listenerClazzInner, LISTENER_VAR_NAME));

        MethodInfo constructor = new MethodInfo(null,
                prefix + POSTFIX,
                new MethodInfo.ParamInfo[]{
                        new MethodInfo.ParamInfo(listenerClazzInner, LISTENER_VAR_NAME)
                });
        constructor.setMethodBodyGenerator(new MethodBodyGenerator() {
            @Override
            public StringBuilder generatorMethodBody(StringBuilder stringBuilder, NewLine newline) {
                stringBuilder.append(newline.getPrefix())
                        .append("this." + LISTENER_VAR_NAME + " = " + LISTENER_VAR_NAME + ";\n");
                return stringBuilder;
            }
        });
        clazzGenerator.addMethod(constructor);

        ClazzGenerator listenerInner = new ClazzGenerator(listenerClazzInner);
        clazzGenerator.addInnerClazz(listenerInner);

        return clazzGenerator;
    }
}
