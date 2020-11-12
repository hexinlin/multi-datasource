package com.xinlinh.multi.datasource.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: DyMultiDataSource
 * @Description: 动态多数据源
 * @Author:xinlinh
 * @Date: 2020/10/20 15:22
 * @Version: 1.0
 **/
public class DyMultiDataSource extends DyMultiAbstractRoutingDataSource {

    private final static Logger logger = LoggerFactory.getLogger(DyMultiDataSource.class);
    private static Properties configProps;
    private final static String configRegex = "(multi-datasource\\.)([0-9a-zA-Z-_]+\\.)([0-9a-zA-Z-_]+)";
    private static Pattern configPattern;

    @Override
    protected Object determineCurrentLookupKey() {
        return DyMultiDataSourceHolder.getThreadDataSourceKey();
    }

    public DyMultiDataSource() {
        init();
    }

    public DyMultiDataSource(boolean initByClass) {
        if(initByClass) {
            initDataSources();
        }else {
            init();
        }
    }

    protected void initDataSources() {
    }


    private void init() {
        configProps = getProperties();
        configPattern = Pattern.compile(configRegex);
        Map<String, DataSource> dataSourceMap = loadDataSources();
        String defaultKey = configProps.getProperty("multi-datasource.defaultDatasource");
        Optional.of(defaultKey);
        String[] defaultKeys = defaultKey.split(",");
        List<DataSource> defaultDataSourceList = new ArrayList<>();
        for (String key : defaultKeys) {
            defaultDataSourceList.add(Optional.of(dataSourceMap.get(key)).get());
        }
        setDataSourceMap(dataSourceMap);
        setDefaultDataSources(defaultDataSourceList);
        boolean lenientFallback = Boolean.parseBoolean(configProps.getProperty("multi-datasource.lenientFallback", "true"));
        setLenientFallback(lenientFallback);
    }

    private Map<String, DataSource> loadDataSources() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        String key = null;
        Map<String, String> value = null;
        DataSource ds = null;
        Map<String, Map<String, String>> datasourceConfigMap = loadDatasourceConfiguration();

        for (Map.Entry<String, Map<String, String>> datasourceConfigMapEntry : datasourceConfigMap.entrySet()) {
            key = datasourceConfigMapEntry.getKey();
            value = datasourceConfigMapEntry.getValue();
            dataSourceMap.put(key, buildDataSource(value));
        }

        return dataSourceMap;
    }

    private DataSource buildDataSource(Map<String, String> configMap) {
        String classType = Optional.of(configMap.get("type")).get();
        //String jdbcUrl = Optional.of(configMap.get("jdbcUrl")).get();
        //String username = Optional.of(configMap.get("username")).get();
        //String password = Optional.of(configMap.get("password")).get();
        configMap.remove("type");
        Class cls = null;
        DataSource dataSource = null;
        try {
            cls = Class.forName(classType);
            dataSource = (DataSource) cls.newInstance();
            bind(cls, dataSource, configMap);
        } catch (Exception e) {
            logger.error("Error when bulid datasource.", e);
            throw new RuntimeException("Error when bulid datasource.");
        }
        return dataSource;
    }

    private void bind(Class cls, DataSource dataSource, Map<String, String> configMap) {
        String propName = null;
        String propValue = null;
        PropertyDescriptor pd = null;
        Method method = null;
        try {
            for (Map.Entry<String, String> stringStringEntry : configMap.entrySet()) {
                propName = stringStringEntry.getKey();
                propValue = stringStringEntry.getValue();
                pd = new PropertyDescriptor(propName, cls);
                method = pd.getWriteMethod();
                setProperty(method, dataSource, propValue);
            }
        } catch (IntrospectionException e) {
            logger.error("Error when bind.", e);
            throw new RuntimeException("Error when bind.", e);
        }
    }

    private void setProperty(Method method, DataSource dataSource, String value) {
        String parameterTypeName = method.getParameterTypes()[0].getName();
        try {
            if (parameterTypeName.equals("java.lang.String")) {
                method.invoke(dataSource, value);
            } else {
                if (parameterTypeName.equals("java.lang.Integer") || parameterTypeName.equals("int")) {
                    method.invoke(dataSource, Integer.parseInt(value));
                } else if (parameterTypeName.equals("java.lang.Long") || parameterTypeName.equals("long")) {
                    method.invoke(dataSource, Long.parseLong(value));
                } else if (parameterTypeName.equals("java.lang.Short") || parameterTypeName.equals("short")) {
                    method.invoke(dataSource, Short.parseShort(value));
                } else if (parameterTypeName.equals("java.lang.Float") || parameterTypeName.equals("float")) {
                    method.invoke(dataSource, Float.parseFloat(value));
                } else if (parameterTypeName.equals("java.lang.Double") || parameterTypeName.equals("double")) {
                    method.invoke(dataSource, Double.parseDouble(value));
                } else {
                    logger.error("Error when setProperty,an unknown property type {}.", parameterTypeName);
                    throw new RuntimeException("Error when setProperty,an unknown property type.");
                }
            }
        } catch (Exception e) {
            logger.error("Error when setProperty.", e);
            throw new RuntimeException("Error when setProperty.", e);
        }
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.util.Map       <       java.lang.String       ,       java.lang.String>>
     * @Author xinlinh
     * @Description load datasource configuration,group by key
     * @Date 2020/10/26 11:09
     * @Param []
     **/
    private Map<String, Map<String, String>> loadDatasourceConfiguration() {
        String key = null;
        String value = null;
        Matcher matcher = null;
        Optional<Matcher> optional = null;
        Map<String, Map<String, String>> datasourceConfigMap = new HashMap<>();
        Map<String, String> dsConfigMap = null;
        String dsKey = null;
        String dsConfigKey = null;
        for (Map.Entry<Object, Object> objectObjectEntry : configProps.entrySet()) {
            key = (String) objectObjectEntry.getKey();
            value = (String) objectObjectEntry.getValue();
            optional = Optional.ofNullable(parseKey(key));
            if (optional.isPresent()) {
                matcher = optional.get();
                dsKey = matcher.group(2).substring(0, matcher.group(2).indexOf("."));
                dsConfigKey = matcher.group(3);
                dsConfigMap = datasourceConfigMap.get(dsKey);
                if (dsConfigMap == null) {
                    dsConfigMap = new HashMap<String, String>();
                    datasourceConfigMap.put(dsKey, dsConfigMap);
                }
                dsConfigMap.put(dsConfigKey, value);
            } else {
                continue;
            }
        }
        return datasourceConfigMap;
    }

    /**
     * @return java.util.regex.Matcher
     * @Author xinlinh
     * @Description parse key msg
     * @Date 2020/10/26 11:01
     * @Param [key]
     **/
    private Matcher parseKey(String key) {
        Matcher matcher = configPattern.matcher(key);
        if (matcher.find()) {
            return matcher;
        } else {
            return null;
        }
    }

    /**
     * @return java.util.Properties
     * @Author xinlinh
     * @Description load configuration
     * @Date 2020/10/26 10:27
     * @Param []
     **/
    private static Properties getProperties() {
        Properties props = new Properties();
        String propertyFile = "/multi-datasource.properties";
        if (logger.isDebugEnabled()) {
            logger.debug("Trying to use properties file {}.", propertyFile);
        }
        InputStream inputStream = DyMultiDataSource.class.getResourceAsStream(propertyFile);
        if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException e) {
                logger.error("Error when loading multi-datasource.properties from classpath.", e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Error when closing multi-datasource.properties file.", e);
                }
            }
        } else {
            logger.error("multi-datasource.properties file not found in classpath.");
        }
        return props;
    }
}
