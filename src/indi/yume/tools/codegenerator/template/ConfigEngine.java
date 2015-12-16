package indi.yume.tools.codegenerator.template;

import indi.yume.tools.codegenerator.model.AnnotationInfo;
import indi.yume.tools.codegenerator.model.ClazzInfo;
import indi.yume.tools.codegenerator.model.InnerClazzInfo;
import indi.yume.tools.codegenerator.model.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yume on 15/11/24.
 */
public class ConfigEngine {
    private Map<String, ClazzInfo> clazzInfoMap = new HashMap<>();
    private Map<String, AnnotationInfo> annoInfoMap = new HashMap<>();
    private Map<String, ClazzInfo> interfaceInfoMap = new HashMap<>();

    public ConfigEngine(File configFile, VarStringEngine varStringEngine) throws Exception {
        Document document = Jsoup.parse(new FileInputStream(configFile), "UTF-8", "", Parser.xmlParser());

        analysisClazz(document.getElementsByTag(ConfigItemsKey.CLASS_ITEM.KEY), varStringEngine);
        analysisInterface(document.getElementsByTag(ConfigItemsKey.INTERFACE_ITEM.KEY), varStringEngine);
        analysisAnno(document.getElementsByTag(ConfigItemsKey.ANNOTATION_ITEM.KEY), varStringEngine);
    }

    private void analysisClazz(Elements clazzEles, VarStringEngine varStringEngine) throws Exception {
        for(Element ele : clazzEles){
            if(!ele.hasAttr(ConfigItemsKey.CLASS_ITEM.NAME)
                    || !ele.hasAttr(ConfigItemsKey.CLASS_ITEM.PACKAGE_NAME)
                    || !ele.hasAttr(ConfigItemsKey.CLASS_ITEM.CLASS_NAME))
                throw new IOException("class must have packageName, name and clazzName: " + ele.toString());

            String packageName = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.PACKAGE_NAME));
            String clazzName = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.CLASS_NAME));
            String name = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.NAME));

            ClazzInfo clazzInfo;
            if(ele.hasAttr(ConfigItemsKey.CLASS_ITEM.IS_INNER)
                    && "true".equals(
                        varStringEngine.analysisString(
                                ele.attr(ConfigItemsKey.CLASS_ITEM.IS_INNER))))
                clazzInfo = new InnerClazzInfo(packageName, clazzName);
            else
                clazzInfo = new ClazzInfo(packageName, clazzName);

            String modifier = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.MODIFIER));
            clazzInfo.getModifierInfo().setModifier(ModifierUtil.analysisModifier(modifier));

            analysisGeneric(clazzInfo, ele, varStringEngine);

            clazzInfoMap.put(name, clazzInfo);
        }
    }

    private void analysisInterface(Elements clazzEles, VarStringEngine varStringEngine) throws Exception {
        for(Element ele : clazzEles){
            if(!ele.hasAttr(ConfigItemsKey.CLASS_ITEM.NAME)
                    || !ele.hasAttr(ConfigItemsKey.CLASS_ITEM.PACKAGE_NAME)
                    || !ele.hasAttr(ConfigItemsKey.CLASS_ITEM.CLASS_NAME))
                throw new IOException("class must have packageName, name and clazzName: " + ele.toString());

            String packageName = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.PACKAGE_NAME));
            String clazzName = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.CLASS_NAME));
            String name = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.NAME));

            ClazzInfo clazzInfo;
            if(ele.hasAttr(ConfigItemsKey.CLASS_ITEM.IS_INNER)
                    && "true".equals(
                    varStringEngine.analysisString(
                            ele.attr(ConfigItemsKey.CLASS_ITEM.IS_INNER))))
                clazzInfo = new InnerClazzInfo(Type.INTERFACE, packageName, clazzName);
            else
                clazzInfo = new ClazzInfo(Type.INTERFACE, packageName, clazzName);

            String modifier = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.MODIFIER));
            clazzInfo.getModifierInfo().setModifier(ModifierUtil.analysisModifier(modifier));

            analysisGeneric(clazzInfo, ele, varStringEngine);

            interfaceInfoMap.put(name, clazzInfo);
        }
    }

    private void analysisAnno(Elements clazzEles, VarStringEngine varStringEngine) throws Exception {
        for(Element ele : clazzEles){
            if(!ele.hasAttr(ConfigItemsKey.CLASS_ITEM.NAME)
                    || !ele.hasAttr(ConfigItemsKey.CLASS_ITEM.PACKAGE_NAME)
                    || !ele.hasAttr(ConfigItemsKey.CLASS_ITEM.CLASS_NAME))
                throw new IOException("class must have packageName, name and clazzName: " + ele.toString());

            String packageName = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.PACKAGE_NAME));
            String clazzName = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.CLASS_NAME));
            String name = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.NAME));

            AnnotationInfo clazzInfo = new AnnotationInfo(packageName, clazzName);

            String modifier = varStringEngine.analysisString(ele.attr(ConfigItemsKey.CLASS_ITEM.MODIFIER));
            clazzInfo.getModifierInfo().setModifier(ModifierUtil.analysisModifier(modifier));

            annoInfoMap.put(name, clazzInfo);
        }
    }

    private void analysisGeneric(ClazzInfo clazzInfo, Element oriEle, VarStringEngine varStringEngine) throws Exception {
        Elements genericList = oriEle.getElementsByTag(ConfigItemsKey.GENERIC_ITEM.KEY);
        for(Element genEle : genericList){
            if(genEle.hasAttr(ConfigItemsKey.GENERIC_ITEM.NAME))
                clazzInfo.addGenericClazz(
                        getClazzInfoInClazzInterface(
                                varStringEngine.analysisString(
                                        genEle.attr(ConfigItemsKey.GENERIC_ITEM.NAME))));

            for(Element item : genEle.getElementsByTag(ConfigItemsKey.GENERIC_ITEM.ITEM.KEY))
                clazzInfo.addGenericClazz(
                        getClazzInfoInClazzInterface(
                                varStringEngine.analysisString(
                                        item.attr(ConfigItemsKey.GENERIC_ITEM.ITEM.NAME))));
        }
    }

    public ClazzInfo getClazzInfoInClazzInterface(String varClazzName) throws Exception {
        if(clazzInfoMap.containsKey(varClazzName))
            return clazzInfoMap.get(varClazzName);
        if(interfaceInfoMap.containsKey(varClazzName))
            return interfaceInfoMap.get(varClazzName);
        throw new Exception("Class " + varClazzName + " not find.");
    }

    public ClazzInfo getClazzInfoInAllData(String varClazzName) throws Exception {
        if(clazzInfoMap.containsKey(varClazzName))
            return clazzInfoMap.get(varClazzName);
        if(interfaceInfoMap.containsKey(varClazzName))
            return interfaceInfoMap.get(varClazzName);
        if(annoInfoMap.containsKey(varClazzName))
            return annoInfoMap.get(varClazzName);
        throw new Exception("Class " + varClazzName + " not find.");
    }

    public ClazzInfo getClazzInfoInClazz(String varClazzName) throws Exception {
        if(clazzInfoMap.containsKey(varClazzName))
            return clazzInfoMap.get(varClazzName);
        throw new Exception("Class " + varClazzName + " not find.");
    }

    public ClazzInfo getClazzInfoInInterface(String varClazzName) throws Exception {
        if(interfaceInfoMap.containsKey(varClazzName))
            return interfaceInfoMap.get(varClazzName);
        throw new Exception("Interface " + varClazzName + " not find.");
    }

    public AnnotationInfo getAnnotationInfo(String varClazzName) throws Exception {
        if(annoInfoMap.containsKey(varClazzName))
            return annoInfoMap.get(varClazzName);
        throw new Exception("Annotation " + varClazzName + " not find.");
    }
}
