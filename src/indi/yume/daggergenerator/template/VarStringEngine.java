package indi.yume.daggergenerator.template;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yume on 15/11/23.
 */
public class VarStringEngine {
    public static final String VAR_KEY_WORD_START = "${";
    public static final String VAR_KEY_WORD_END = "}";

    CharChange.GetValueByKey getValueByKey = new CharChange.GetValueByKey() {
        @Override
        public String getValue(String key) throws Exception {
            return VarStringEngine.this.getValue(key);
        }
    };

    private Map<String, String> varMap = new HashMap<>();

    public void binding(String key, String value){
        varMap.put(key, value);
    }

    public void unbindingAllValue(){
        varMap.clear();
    }

    public String analysisString(String oriString) throws Exception {
        if(oriString == null)
            return null;

        StringBuilder answerStringBuilder = new StringBuilder();
        int index = 0;
        int length = oriString.length();
        VarMatcher varMatcher = new VarMatcher();
        while (index != -1 && index < length){
            if(oriString.charAt(index) == '$'){
                varMatcher.matcher(oriString, index);
                index = varMatcher.getOverIndex();

                if(varMatcher.isHasFind()){
                    answerStringBuilder.append(CharChange.analysisVarName(varMatcher.getVarName(), getValueByKey));
                } else{
                    answerStringBuilder.append(varMatcher.getVarName());
                }
            } else{
                answerStringBuilder.append(oriString.charAt(index));
                index++;
            }
        }

        return answerStringBuilder.toString();
    }

    private boolean isEmpty(String s){
        return s == null || "".equals(s);
    }

    public static class VarMatcher{
        private int overIndex = -1;
        private String varName;
        private String varNameFull;

        private boolean hasFind = false;

        public void reset(){
            overIndex = -1;
            varName = null;
            varNameFull = null;
            hasFind = false;
        }

        public void matcher(String oriString, int start) {
            reset();

            if(oriString.charAt(start) != '$' || oriString.length() - start < 3) {
                varName = varNameFull = String.valueOf(oriString.charAt(start));
                overIndex = ++start;
                return;
            }

            if(oriString.charAt(start) == '$' && oriString.charAt(start + 1) != '{'){
                if(oriString.charAt(start + 1) == '$')
                    varName = varNameFull = "$";
                else
                    varName = varNameFull = String.valueOf(oriString.charAt(start)) + oriString.charAt(start + 1);
                overIndex = start + 2;
                return;
            }

            overIndex = findVar(oriString, start);
        }

        private int findVar(String oriString, int start){
            int endIndex = oriString.indexOf('}', start);
            if(endIndex == -1) {
                varName = varNameFull = oriString.substring(start);
                return oriString.length();
            }

            hasFind = true;
            varName = oriString.substring(start + 2, endIndex);
            varNameFull = "${" + varName + "}";
            return endIndex + 1;
        }



        public int getOverIndex() {
            return overIndex;
        }

        public String getVarName() {
            return varName;
        }

        public String getVarNameFull() {
            return varNameFull;
        }

        public boolean isHasFind() {
            return hasFind;
        }
    }

    private String getValue(String key) throws Exception {
        if(varMap.keySet().contains(key))
            return varMap.get(key);
        else
            throw new Exception("Var " + key + " not existe");
    }

    private static class CharChange{
        public interface GetValueByKey{
            String getValue(String key) throws Exception;
        }

        public static String analysisVarName(String oriVarName, GetValueByKey getValueByKey) throws Exception {
            Pattern pattern = Pattern.compile("(.*)(\\+|-|<|>)(.*)");
            Matcher matcher = pattern.matcher(oriVarName);

            if(matcher.find()){
                String answerString = "";

                String s1 = matcher.group(1);
                String s2 = matcher.group(2);
                String s3 = getValueByKey.getValue(matcher.group(3));

                if(s1 != null && !"".equals(s1))
                    answerString = CharChange.separateCamelCase(s3, s1);
                else
                    answerString = s3;

                switch (s2){
                    case "+":
                        answerString = answerString.toUpperCase();
                        break;
                    case "-":
                        answerString = answerString.toLowerCase();
                        break;
                    case ">":
                        answerString = CharChange.upperCaseFirstLetter(answerString);
                        break;
                    case "<":
                        answerString = CharChange.lowerCaseFirstLetter(answerString);
                        break;
                    default:
                        throw new Error("Split only have + - and ^");
                }

                return answerString;
            } else{
                return getValueByKey.getValue(oriVarName);
            }
        }

        //以大写字母分割
//        private static String separateCamelCase(String name, String separator) {
//            StringBuilder translation = new StringBuilder();
//            for (int i = 0; i < name.length(); i++) {
//                char character = name.charAt(i);
//                if (Character.isUpperCase(character) && translation.length() != 0) {
//                    translation.append(separator);
//                }
//                translation.append(character);
//            }
//            return translation.toString();
//        }

        private static String separateCamelCase(String name, String separator) {
            StringBuilder translation = new StringBuilder();
            char oldChar = 0;
            for (int i = 0; i < name.length(); i++) {
                char character = name.charAt(i);
                String s = name.substring(0, i);
                if (!s.endsWith(separator) && Character.isUpperCase(character) && translation.length() != 0) {
                    translation.append(separator);
                }
                translation.append(character);
                oldChar = character;
            }
            return translation.toString();
        }

        //首字母大写
        private static String upperCaseFirstLetter(String name) {
            StringBuilder fieldNameBuilder = new StringBuilder();
            int index = 0;
            char firstCharacter = name.charAt(index);

            while (index < name.length() - 1) {
                if (Character.isLetter(firstCharacter)) {
                    break;
                }

                fieldNameBuilder.append(firstCharacter);
                firstCharacter = name.charAt(++index);
            }

            if (index == name.length()) {
                return fieldNameBuilder.toString();
            }

            if (!Character.isUpperCase(firstCharacter)) {
                String modifiedTarget = modifyString(Character.toUpperCase(firstCharacter), name, ++index);
                return fieldNameBuilder.append(modifiedTarget).toString();
            } else {
                return name;
            }
        }

        //首字母大写
        private static String lowerCaseFirstLetter(String name) {
            StringBuilder fieldNameBuilder = new StringBuilder();
            int index = 0;
            char firstCharacter = name.charAt(index);

            while (index < name.length() - 1) {
                if (Character.isLetter(firstCharacter)) {
                    break;
                }

                fieldNameBuilder.append(firstCharacter);
                firstCharacter = name.charAt(++index);
            }

            if (index == name.length()) {
                return fieldNameBuilder.toString();
            }

            if (!Character.isLowerCase(firstCharacter)) {
                String modifiedTarget = modifyString(Character.toLowerCase(firstCharacter), name, ++index);
                return fieldNameBuilder.append(modifiedTarget).toString();
            } else {
                return name;
            }
        }

        //合并字符串
        private static String modifyString(char firstCharacter, String srcString, int indexOfSubstring) {
            return (indexOfSubstring < srcString.length())
                    ? firstCharacter + srcString.substring(indexOfSubstring)
                    : String.valueOf(firstCharacter);
        }
    }
}
