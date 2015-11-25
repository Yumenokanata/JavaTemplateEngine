package indi.yume.daggergenerator.template;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yume on 15/11/24.
 */
public class StringContentEngine {
    public static String generateString(String startString, String content) throws FormatterException {
        String[] contentList = content.split("\n");
        StringBuilder noEmptyString = new StringBuilder();
        for(String line : contentList)
            noEmptyString.append(line.trim());

        Formatter formatter = new Formatter();
        String formatString = formatter.formatSource(noEmptyString.toString());

        return startString + formatString.replace("\n", "\n" + startString);
    }

    public static String custom(String startString, String tabString, String content) throws Exception {
        if(content == null)
            return "";

        String[] contentList = content.split("\n");
        StringBuilder noEmptyString = new StringBuilder();
        for(String line : contentList)
            noEmptyString.append(line.trim()).append("\n");

        StringBuilder newContent = new StringBuilder();
        int index = 0;
        NewLineTab newLineTab = new NewLineTab(null, (char)0, "");
        String tempTab = "";
        HasInString hasInString = new HasInString();

        int lineIndex = 0;
        Map<Integer, String> tabMap = new HashMap<>();
        tabMap.put(0, "");
        while (index < noEmptyString.length()) {
            char nowChar = noEmptyString.charAt(index);
            if(nowChar == '\n') {
                lineIndex++;

                char backChar = getBackChar(index, noEmptyString);
                if(canNewLineChar(backChar))
                    tempTab += tabString;

                char nextChar = getNextChar(index, noEmptyString);
                if(!hasInString.inString && (nextChar == ')' || nextChar == '}')) {
                    if ((nextChar == ')' && newLineTab.getC() != '(')
                            || (nextChar == '}' && newLineTab.getC() != '{'))
                        throw new Exception("() or {} error");
                    tempTab = "";
                    tabMap.put(lineIndex, newLineTab.pop().getTabString() + tempTab);
                } else if(canNewLineChar(nextChar)){
                    if((nextChar != '+' && getNextChar(index + 1, noEmptyString) != '+')
                            && (nextChar != '-' && getNextChar(index + 1, noEmptyString) != '-')) {
                        tempTab += tabString;
                    }
                    tabMap.put(lineIndex, newLineTab.getTabString() + tempTab);
                } else if((backChar == ')' && isLetter(nextChar))) {
                    tempTab += tabString;
                    tabMap.put(lineIndex, newLineTab.getTabString() + tempTab);
                } else{
                    tabMap.put(lineIndex, newLineTab.getTabString() + tempTab);
                }

            } else if((nowChar == '\'' || nowChar == '"') && checkCharCanUse(index, noEmptyString)){
                hasInString.check(nowChar);
            } else if(!hasInString.inString && (nowChar == '(' || nowChar == '{')) {
                newLineTab = newLineTab.push(nowChar, newLineTab.getTabString() + tabString);
            } else if(!hasInString.inString && (nowChar == ')' || nowChar == '}')){
                if((nowChar == ')' && newLineTab.getC() != '(')
                        || (nowChar == '}' && newLineTab.getC() != '{'))
                    throw new Exception("() or {} error");
                newLineTab = newLineTab.pop();
                tempTab = "";
            } else if(nowChar == ';') {
                tempTab = "";
            }
            newContent.append(nowChar);
            index++;
        }


        StringBuilder answerString = new StringBuilder();
        String[] lineList = newContent.toString().split("\n");
        for(int i = 0; i < lineList.length; i++)
            answerString.append(startString)
                    .append(tabMap.get(i))
                    .append(lineList[i])
                    .append("\n");

        if(answerString.length() > 0 && answerString.charAt(answerString.length() - 1) == '\n')
            answerString.deleteCharAt(answerString.length() - 1);
        return answerString.toString();
    }

    public static boolean checkCharCanUse(int index, StringBuilder string){
        int num = 0;
        while(getBackChar(index--, string) == '\\')
            num++;
        return num % 2 == 0;
    }

    private static char getBackChar(int index, StringBuilder string){
        if(index <= 0)
            return 0;
        return string.charAt(index - 1);
    }

    private static char getNextChar(int index, StringBuilder string){
        if(index >= string.length() - 1)
            return 0;
        return string.charAt(index + 1);
    }

    private static boolean isLetter(char c){
        if(c >= 'A' && c <= 'Z')
            return true;
        else if(c >= 'a' && c <= 'z')
            return true;
        else if(c >= '0' && c <= '9')
            return true;
        return false;
    }

    private static boolean canNewLineChar(char c){
        return !isLetter(c) &&
                ((c >= '!' && c <= '\'') || (c >= '*' && c <= '/') || (c >= ':' && c <= '@' && c != ';') || (c >= '[' && c <= '`') || c == '|' || c == '~');
    }

    private static class HasInString{
        private char split;
        private boolean inString = false;

        public void check(char c){
            if(inString && c == split) {
                inString = false;
                return;
            }
            if(!inString && (c == '"' || c == '\'')){
                inString = true;
                split = c;
                return;
            }
        }

        public boolean isInString() {
            return inString;
        }
    }

    private static class NewLineTab{
        private NewLineTab back;
        private char c;
        private String tabString;

        public NewLineTab(NewLineTab back, char c, String tabString) {
            this.back = back;
            this.c = c;
            this.tabString = tabString;
        }

        public NewLineTab push(char c, String tabString){
            return new NewLineTab(this, c, tabString);
        }

        public NewLineTab pop(){
            return back;
        }

        public char getC() {
            return c;
        }

        public String getTabString() {
            return tabString;
        }
    }
}
