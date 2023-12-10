package top.lingyuzhao.diskMirror.core;

public enum Type {


    TEXT {
        /**
         * @return 当前文件类别的中文名字
         */
        @Override
        public String toChName() {
            return "文本";
        }
    }, Binary {
        /**
         * @return 当前文件类别的中文名字
         */
        @Override
        public String toChName() {
            return "二进制数据";
        }
    };

    /**
     * @return 当前文件类别的中文名字
     */
    public abstract String toChName();

}
