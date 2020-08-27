package parser.lianjia;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kp on 2020/8/9.
 */
public class CSVGenerator {

    List<Object> data = new ArrayList<Object>();
    Class clz;
    String str = ",";

    public void add(Object o) {
        if (clz == null) {
            clz = o.getClass();
        }
        this.data.add(o);
    }

    public void generateCSV(String path) {
        List<Field> fields = getAllFields(this.clz);
        List<Method> methods = generateAllGetMethods(fields, this.clz);
        List<String> list = Arrays.asList(HouseInfo.attrs);
        List<String> list1 = new ArrayList<String>(list);
        String fieldStr = StringUtils.join(list1, ",");
        File file = new File(path);
        OutputStreamWriter ops;
        BufferedWriter writer = null;
        try {
            ops = new OutputStreamWriter(new FileOutputStream(file));
            writer = new BufferedWriter(ops);
            writer.append("地址," +fieldStr + "\n");
            for (Object o : data) {
                StringBuilder stringBuilder = new StringBuilder();
                generateObjectToStr(o, methods, stringBuilder);
                String str = stringBuilder.toString();
                writer.append(str.substring(0, str.length()) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void generateSpringString(Object o, StringBuilder stringBuilder) {
        List<Field> fields = getAllFields(o.getClass());
        Class springClz = o.getClass();
        List<Method> springMethods = generateAllGetMethods(fields, springClz);
        generateObjectToStr(o, springMethods, stringBuilder);
    }

    public void generateObjectToStr(Object o, List<Method> methods, StringBuilder stringBuilder) {
        try {
            for (Method method : methods) {
                Object str = method.invoke(o);

                if (str.getClass() == String.class) {
                    stringBuilder.append(str);
                    stringBuilder.append(",");
                } else if (str.getClass() == ArrayList.class) {
                    List list = (List)str;
                    stringBuilder.append(StringUtils.join(list, "$"));
                } else {
                   generateSpringString(str, stringBuilder);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Method> generateAllGetMethods(List<Field> fields, Class clz) {
        List<Method> methods = new ArrayList<Method>();
        for (Field field : fields) {
            String name = field.getName();
            try {
                if ("attrs".equals(name)) {
                    continue;
                }
                Method method = clz.getDeclaredMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                methods.add(method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return methods;
    }

    private String generateFieldsToStr(List<Field> fields) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            String name = field.getName();
            stringBuilder.append(name);
            stringBuilder.append(str);
        }
        String fieldStrTemp = stringBuilder.toString();
        return fieldStrTemp.substring(0, fieldStrTemp.length() - 1);
    }

    private List<Field> getAllFields(Class clz) {
        List<Field> fieldList = new ArrayList<Field>();
        Class c = clz;
        fieldList.addAll(new ArrayList<Field>(Arrays.asList(c.getDeclaredFields())));
        return fieldList;
    }

    public static void main(String[] args) {
        CSVGenerator csvGenerator = new CSVGenerator();
        Stu stu = new Stu();
        stu.setId(1);
        stu.setName("kp");
        csvGenerator.add(stu);
        csvGenerator.generateCSV("/Users/kp/Desktop/kp.txt");
    }

    @Data
    public static class Stu {
        private int id;
        private String name;
    }
}
