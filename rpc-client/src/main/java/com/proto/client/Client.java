package com.proto.client;

import com.proto.provider.AddressBookProtos;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Park on 15. 1. 13..
 */
public class Client {

    /**
     * Synchronized Mutex for Initialization
     */
    private final static Object mutex = new Object();

    /**
     * Is initialized?
     */
    private static boolean isInitialized;

    /**
     * Spring Framework Application Context
     */
    private static AbstractApplicationContext context;
    public static final String UNKNOWN = "Unknown";

    public static void main(String args[]) {
        synchronized (mutex) {
            if (!isInitialized) {
                isInitialized = true;

                Thread t = new Thread(new ThreadGroup("Flamingo"), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            startup();
                            context = new ClassPathXmlApplicationContext("applicationContext.xml");
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException("Flamingo :: Cannot initialized.", e);
                        }
                    }
                }, "Resource Manager Agent");
                t.start();
            }
        }
    }

    private static void startup() throws Exception {
        System.setProperty("PID", getPid());

        StringBuilder builder = new StringBuilder();

        builder.append("_________ .__  .__               __   \n" +
                "\\_   ___ \\|  | |__| ____   _____/  |_ \n" +
                "/    \\  \\/|  | |  |/ __ \\ /    \\   __\\\n" +
                "\\     \\___|  |_|  \\  ___/|   |  \\  |  \n" +
                " \\______  /____/__|\\___  >___|  /__|  \n" +
                "        \\/             \\/     \\/      ");

        SortedProperties properties = new SortedProperties();
        InputStream is = ResourceUtils.getResource("classpath:/version.properties").getInputStream();
        properties.load(is);
        is.close();

        printHeader(builder, "Application Information");
        SortedProperties appProps = new SortedProperties();
        appProps.put("Application", "Flamingo Hadoop Resource Manager Monitoring Agent");
        appProps.put("Version", properties.get("version"));
        appProps.put("Build Date", properties.get("build.timestamp"));
        appProps.put("Build Number", properties.get("build.number"));
        appProps.put("Revision Number", properties.get("revision.number"));
        appProps.put("Build Key", properties.get("build.key"));

        SortedProperties systemProperties = new SortedProperties();
        systemProperties.putAll(System.getProperties());
        appProps.put("Java Version", systemProperties.getProperty("java.version", UNKNOWN) + " - " + systemProperties.getProperty("java.vendor", UNKNOWN));
        appProps.put("Current Working Directory", systemProperties.getProperty("user.dir", UNKNOWN));

        print(builder, appProps);

        printHeader(builder, "JVM Heap Information");

        Properties memPros = new Properties();

        print(builder, memPros);

        printHeader(builder, "Java System Properties");
        SortedProperties sysProps = new SortedProperties();
        for (final Map.Entry<Object, Object> entry : systemProperties.entrySet()) {
            sysProps.put(entry.getKey(), entry.getValue());
        }

        print(builder, sysProps);

        printHeader(builder, "System Environments");
        Map<String, String> getenv = System.getenv();
        SortedProperties envProps = new SortedProperties();
        Set<String> strings = getenv.keySet();
        for (String key : strings) {
            String message = getenv.get(key);
            envProps.put(key, message);
        }

        print(builder, envProps);

        System.out.println(builder.toString());
    }

    /**
     * 헤더값을 출력한다.
     *
     * @param builder {@link StringBuilder}
     * @param message 출력할 메시지
     */
    private static void printHeader(StringBuilder builder, String message) {
        builder.append("\n== " + message + " =====================\n").append("\n");
    }

    /**
     * Key Value 속성을 출력한다.
     *
     * @param builder {@link StringBuilder}
     * @param props   출력할 Key Value 속성
     */
    private static void print(StringBuilder builder, Properties props) {
        int maxLength = getMaxLength(props);
        Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = props.getProperty(key);
            builder.append("  ").append(key).append(getCharacter(maxLength - key.getBytes().length, " ")).append(" : ").append(value).append("\n");
        }
    }

    /**
     * 콘솔에 출력할 Key Value 중에서 가장 긴 Key 문자열의 길이를 반환한다.
     *
     * @param props {@link java.util.Properties}
     * @return Key 문자열 중에서 가장 긴 문자열의 길이
     */
    private static int getMaxLength(Properties props) {
        Enumeration<Object> keys = props.keys();
        int maxLength = -1;
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (maxLength < 0) {
                maxLength = key.getBytes().length;
            } else if (maxLength < key.getBytes().length) {
                maxLength = key.getBytes().length;
            }
        }
        return maxLength;
    }

    /**
     * 지정한 크기 만큼 문자열을 구성한다.
     *
     * @param size      문자열을 구성할 반복수
     * @param character 문자열을 구성하기 위한 단위 문자열. 반복수만큼 생성된다.
     * @return 문자열
     */
    private static String getCharacter(int size, String character) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(character);
        }
        return builder.toString();
    }

    public static String getPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            if (name != null) {
                return name.split("@")[0];
            }
        } catch (Throwable ex) {
            // Ignore
        }
        return "????";
    }

}
