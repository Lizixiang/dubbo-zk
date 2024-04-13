package com.dubbo.user.interceptor;

import com.dubbo.core.permission.Permission;
import com.dubbo.user.util.ReflectionUtils;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


@Component
@Intercepts(
        {
//                @Signature(type = StatementHandler.class,
//                        method = "query",
//                        args = {java.sql.Statement.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
//                @Signature(type = ParameterHandler.class,
//                        method = "setParameters",
//                        args = PreparedStatement.class)
        }
)
public class MybatisPlusInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(MybatisPlusInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        // 获取原始sql
        BoundSql boundSql = ((MappedStatement) args[0]).getBoundSql(args[1]);
        String originSql = boundSql.getSql();
//        logger.info("originSql:{}", originSql);
        // 通过StatemenHandler间接获取MappedStatement
        MappedStatement mappedStatement = (MappedStatement) args[0];
        // 获取请求类和请求方法
        String className = mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf("."));
        String methodName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);
        Class<?> clazz = Class.forName(className);
        // 判断是否有@Permission注解,是否需要过滤权限
        Annotation permission = ReflectionUtils.getAnnotation(clazz, methodName, Permission.class);
        if (permission != null) {
            // 获取statement
            Statement statement = CCJSqlParserUtil.parse(originSql);
            if (statement instanceof Select) {
                // 获取sql中的所有表
                Select select = (Select) statement;
                PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                // 查询条件
                Expression where = plainSelect.getWhere();
                // TODO: 2024/4/1 支持from后面跟多表查询
                Table from = (Table) plainSelect.getFromItem();
                String tableName = from.getName();
                Alias alias = from.getAlias();
                StringBuilder sql = new StringBuilder();
                if ("customer".equals(tableName)) {
                    // TODO: 2024/4/1 支持查询部门下的所有组员id
                    List<Long> userIds = new ArrayList<>();
                    userIds.add(1L);
                    if (!CollectionUtils.isEmpty(userIds)) {
                        sql.append((alias != null ? (alias.getName() + ".") : "") + "user_id in (" +
                                userIds.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")");
                    }
                }
                if (sql.length() > 0) {
                    if (where != null) {
                        sql.append(" and ( " + where.toString() + " ) ");
                    }
                    Expression whereExpression = CCJSqlParserUtil.parseCondExpression(sql.toString());
                    plainSelect.setWhere(whereExpression);
                }
                resetSql2Invocation(invocation, plainSelect.toString());
//                logger.info("sql:{}", plainSelect.toString());
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 将MapperMethod.ParamMap中的参数类型全部取出
     *
     * @param paramMap
     * @return
     */
    private Class<?>[] getParameterTypes(MapperMethod.ParamMap paramMap) {
        long paramCount = paramMap.keySet().stream().filter(e -> e.toString().contains("param")).count();
        Class<?>[] paramTypes = new Class[(int) paramCount];
        for (int i = 1, j = 0; i <= paramCount; i++, j++) {
            paramTypes[j] = paramMap.get("param" + i).getClass();
        }
        return paramTypes;
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

    //    定义一个内部辅助类，作用是包装sq`
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
