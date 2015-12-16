package indi.yume.tools.codegenerator.model;

/**
 * Created by yume on 15/9/26.
 */
public enum Type {
    CLASS {
        @Override
        public String get() {
            return "class";
        }
    },
    INTERFACE {
        @Override
        public String get() {
            return "interface";
        }
    },
    ANNOTATION {
        @Override
        public String get() {
            return "@interface";
        }
    };

    public abstract String get();
}
