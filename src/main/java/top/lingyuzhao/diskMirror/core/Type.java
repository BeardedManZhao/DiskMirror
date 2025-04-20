package top.lingyuzhao.diskMirror.core;

/**
 * 盘镜空间类型 （这个类型并没有什么强制的限定要求，您可以只使用 Binary）
 */
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
