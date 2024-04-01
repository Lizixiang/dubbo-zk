package com.dubbo.core.util;

import com.dubbo.core.permission.Permission;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class ExecutorPluginUtils {
    /**
     * 获取sql语句
     *
     * @param invocation
     * @return
     */
    public static String getSqlByInvocation(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        return boundSql.getSql();
    }

    /**
     * 包装sql后，重置到invocation中
     *
     * @param invocation
     * @param sql
     * @throws SQLException
     */
    public static void resetSql2Invocation(Invocation invocation, String sql) throws SQLException {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        MappedStatement newStatement = newMappedStatement(statement, new BoundSqlSqlSource(boundSql));
        MetaObject msObject = MetaObject.forObject(newStatement, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSql.sql", sql);
        args[0] = newStatement;
    }


    private static MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /**
     * 是否标记为区域字段
     *
     * @return
     */
    public static boolean isAreaTag(MappedStatement mappedStatement) throws ClassNotFoundException {
        String id = mappedStatement.getId();
        //获取类名
        String className = id.substring(0, id.lastIndexOf("."));
        Class clazz = Class.forName(className);
        //获取方法名
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        //这里是博主工作需求，防止pagehelper那里未生效
        if (methodName.contains("_COUNT")) {
            methodName = methodName.replace("_COUNT", "");
        }
        String m = methodName;
        Class<?> classType = Class.forName(id.substring(0, mappedStatement.getId().lastIndexOf(".")));

        //获取对应拦截方法名
        String mName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);
        //这里是博主工作需求，防止pagehelper那里未生效
        if (mName.contains("_COUNT")) {
            mName = mName.replace("_COUNT", "");
        }
        boolean ignore = false;
        //获取该类(接口)的所有方法,如果你查询的方法就写在该类，就不需要下面的if判断
        Method[] declaredMethods = classType.getDeclaredMethods();
        Method declaredMethod = Arrays.stream(declaredMethods).filter(it -> it.getName().equals(m)).findFirst().orElse(null);
        //该判断是拿到该接口的超类的方法，博主的查询方法就在超类里，因此需要利用下面代码来获取对应方法
        if (declaredMethod == null) {
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            declaredMethod = Arrays.stream(genericInterfaces).map(e ->
            {
                Method[] declaredMethods1 = ((Class) e).getDeclaredMethods();
                return Arrays.stream(declaredMethods1).filter(it -> it.getName().equals(m)).findFirst().orElse(null);

            }).filter(Objects::nonNull).findFirst().orElse(null);
        }
        if (declaredMethod != null) {
            //查询方法是否被permission标记注解
            ignore = declaredMethod.isAnnotationPresent(Permission.class);
        }
        return ignore;
    }


    /**
     * 是否标记为区域字段
     *
     * @return
     */
    public static boolean isAreaTagIngore(MappedStatement mappedStatement) throws ClassNotFoundException {
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        Class clazz = Class.forName(className);
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        Class<?> classType = Class.forName(id.substring(0, mappedStatement.getId().lastIndexOf(".")));
        //获取对应拦截方法名
        String mName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);
        boolean ignore = false;
        Method[] declaredMethods = classType.getDeclaredMethods();
        Method declaredMethod = Arrays.stream(declaredMethods).filter(it -> it.getName().equals(methodName)).findFirst().orElse(null);
        if (declaredMethod == null) {
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            declaredMethod = Arrays.stream(genericInterfaces).map(e ->
            {
                Method[] declaredMethods1 = ((Class) e).getDeclaredMethods();
                return Arrays.stream(declaredMethods1).filter(it -> it.getName().equals(methodName)).findFirst().orElse(null);

            }).filter(Objects::nonNull).findFirst().orElse(null);
        }
        ignore = declaredMethod.isAnnotationPresent(Permission.class);
        return ignore;
    }


    public static String getOperateType(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        SqlCommandType commondType = ms.getSqlCommandType();
        if (commondType.compareTo(SqlCommandType.SELECT) == 0) {
            return "select";
        }
        return null;
    }

    //    定义一个内部辅助类，作用是包装sq
    static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}

