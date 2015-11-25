package indi.yume.daggergenerator.template;

import indi.yume.daggergenerator.model.ClazzInfo;

import java.lang.reflect.Modifier;

/**
 * Created by yume on 15/11/24.
 */
public class ModifierUtil {
    public static int analysisModifier(String modifierList){
        int modSum = 0;
        if(modifierList != null && !"".equals(modifierList)){
            String[] mods = modifierList.split("|");
            for(String mod : mods)
                modSum = modSum | addModifier(mod);
        }
        return modSum;
    }

    private static int addModifier(String modifier){
        switch (modifier){
            case "private":
                return Modifier.PRIVATE;
            case "public":
                return Modifier.PUBLIC;
            case "protected":
                return Modifier.PROTECTED;
            case "abstract":
                return Modifier.ABSTRACT;
            case "static":
                return Modifier.STATIC;
            case "final":
                return Modifier.FINAL;
            case "transient":
                return Modifier.TRANSIENT;
            case "volatile":
                return Modifier.VOLATILE;
            case "synchronized":
                return Modifier.SYNCHRONIZED;
            case "native":
                return Modifier.NATIVE;
            case "strictfp":
                return Modifier.STRICT;
            default:
                return 0;
        }
    }
}
