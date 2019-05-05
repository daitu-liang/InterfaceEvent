package com.example.library;


/**
 * @Query("id") String id,
 * 保持参数的注解值  参数值  用于拼接请求
 */
abstract class ParameterHandler {
    abstract void apply(RequestBuilder builder, String value) ;

    public static class Query extends ParameterHandler {
        private final String name;
       // 传过去的参数是注解的值，并非参数值
        public Query(String name) {
            if(name.isEmpty()){
                throw  new IllegalArgumentException("name==null");
            }
            this.name=name;
    }

        @Override
        void apply(RequestBuilder builder, String value)  {
            //拼接Query参数，此处的name为注解的值，value为参数值
            builder.addQueryParam(name, value);
        }
    }

    public static class Field extends ParameterHandler {
        private final String name;

        public Field(String name) {
            if(name.isEmpty()){
                throw  new IllegalArgumentException("name==null");
            }
            this.name=name;
        }

        @Override
        void apply(RequestBuilder builder, String value)  {
            //拼接Query参数，此处的name为注解的值，value为参数值
            builder.addFormField(name, value);
        }
    }
}
