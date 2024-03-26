package project.generate_receipt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
// import freemarker.template.TemplateException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.util.Iterator;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

public class GenerateReceipt {

    public static void generate(String templateName, String targetFileName, Map<String, Object> params) {
        
        Writer out = null;

        Path currentRelativePath = Paths.get("");
        String base_path = currentRelativePath.toAbsolutePath().toString();
        // System.out.println(base_path);

        String output_folder = "target\\output";        //通過匹配路徑格式拼接完整生成路徑
        String outFile = base_path + File.separator + output_folder + File.separator + targetFileName;
        System.out.println("Output file: " + outFile);
        
        try {

            File file = new File(outFile); 

            if (!file.exists()) {           // 生成空 HTML文件
                file.createNewFile();
            }
            else{
                file.delete();
            }

            // 初始化 freemarker 設定
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_26));
            cfg.setDefaultEncoding("UTF-8");
            
            String template_folder = base_path + File.separator + "src\\main\\resources\\templates";
            cfg.setDirectoryForTemplateLoading(new File(template_folder));   // 模板存放處

            // 根據模板名稱獲取模板
            Template template = cfg.getTemplate(templateName);
            
            // 設置輸出流
            out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

            // 模版數據插入參數，通過輸出流插入到HTML中
            template.process(params, out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done");
    }

    public static void json_test() {

        JSONParser parser = new JSONParser();

        Path currentRelativePath = Paths.get("");
        String base_path = currentRelativePath.toAbsolutePath().toString();
        String input_file = base_path + File.separator + "src\\main\\resources\\inputs\\input_test.json";

        try {     
            
            Object obj = parser.parse(new FileReader(input_file));
            System.out.println(obj);

            JSONArray jsonArray = new JSONArray();
            jsonArray = (JSONArray) obj;

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = new JSONObject();
            
            for (int i = 0 ; i < jsonArray.size() ; i++){
                
                jsonObject = (JSONObject) jsonArray.get(i);

                String name = (String) jsonObject.get("name");
                System.out.print(name + " ");

                String city = (String) jsonObject.get("city");
                System.out.print(city + " ");

                String job = (String) jsonObject.get("job");
                System.out.print(job + " ");

                // loop array
                JSONArray cars = (JSONArray) jsonObject.get("cars");

                Iterator<String> iterator = cars.iterator();
                while (iterator.hasNext()) {
                    System.out.print(iterator.next() + " ");
                }

                System.out.println();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    } 

    /**
     * 將JSONObject轉換成無序Map
     * @explain
     * @param jsonObject
     * @return HashMap 無序Map
     */
    public static Map<String, Object> toHashMap(JSONObject jsonObject) {

        // 用於儲存接收到的 key:value
        Map<String, Object> data = new HashMap<String, Object>();
        
        // 獲取json物件中的鍵
        @SuppressWarnings("unchecked")
        Set<String> keySet = jsonObject.keySet();
        String key = "";
        Object value = null;
        
        // 遍歷jsonObject資料，新增到 Map物件
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            key = iterator.next();
            value = jsonObject.get(key);
            data.put(key, value);
        }
        
        return data;
    }

    public static String readAll(BufferedReader reader) {

        StringBuffer buffer = new StringBuffer();
        
        while (true) {
            String line;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) {
                break;
            } else {
                buffer.append(line);
                buffer.append("\n");
            }
        }

        return buffer.toString();
    }

    public static void main(String[] args) throws Exception {

        // json_test();

        Map<String, Object> params = new HashMap<String, Object>();

        String file_subname = "11009 (before)";
        int from = -1;
        int to = 11009;
        int group_start = 1;
        int group_end = 6;
        // 跨年請注意

        JSONParser parser = new JSONParser();

        Path currentRelativePath = Paths.get("");
        String base_path = currentRelativePath.toAbsolutePath().toString();
        String input_file_path = base_path + File.separator + "src\\main\\resources\\inputs\\receipt_student.json";
        
        FileInputStream input_file = new FileInputStream(input_file_path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input_file, "UTF-8"));
        JSONObject students_information = (JSONObject) parser.parse(readAll(reader));
        // System.out.println(students_information);
        // System.out.println(students_information.size());
        
        List<Object> receipts = new ArrayList<Object>();

        for (int i = -1 ; i <= students_information.size() ; i++){

            if(group_start <= i && i <= group_end){
                
                String index = "";
                if( i < 0 ){
                    index =  "-" + Integer.toString( -1 * i );
                }
                else {
                    index = Integer.toString(i);
                }

                Map <String, Object> info = toHashMap( (JSONObject) students_information.get(index) );
                // System.out.println(info);
                // System.out.println(info.size());
            
                Map <String, Object> receipt_info = new HashMap<String, Object>();
                receipt_info.put("id", info.get("身分證字號").toString());
                receipt_info.put("address", info.get("地址").toString());
                receipt_info.put("job_name", info.get("職稱").toString());
                receipt_info.put("job_place", info.get("服務單位").toString());
                receipt_info.put("money_permonth", info.get("月薪").toString() + " 元 / 月");

                int per_money = Integer.parseInt( info.get("月薪").toString() );
                String job = info.get("職級").toString();
                // System.out.println(job);
                
                JSONObject info_date = (JSONObject) info.get("聘期");
                int date_start = Integer.parseInt( info_date.get("起").toString() );
                int date_end = Integer.parseInt( info_date.get("迄").toString() );

                if(to < date_start || date_end < from){
                    // not in date
                }
                else {
                    int t1 = 0;
                    int t2 = 0;
                    String detail = "";

                    if ( from < date_start )
                        t1 = date_start;
                    else
                        t1 = from;

                    if ( date_end < to )
                        t2 = date_end;
                    else
                        t2 = to;

                    if ( job.equals("兼任:大專生") )
                        detail = "大專生" + "助理";
                    else if ( job.equals("兼任:碩士生") )
                        detail = "碩士生" + "助理";
                    else if ( job.equals("兼任:博士生") )
                        detail = "博士生" + "助理";
                    // System.out.println(detail);

                    if (t1 == t2)
                        detail = detail + " " + t1;
                    else
                        detail = detail + " " + t1 + '-' + t2;
                    
                    int money = ( t2 - t1 + 1 ) * per_money;
                    String chi_money = convert_number(money);
                    
                    receipt_info.put("money", chi_money );
                    receipt_info.put("detail", detail);
                    
                    receipts.add(receipt_info);
                }
            }
        }

        params.put("receipts", receipts);
        
        GenerateReceipt.generate("Receipt_template.html", "receipt_" + file_subname + ".html", params);
    }

    // public void convert_id(String id){
    //     this.id_0 = Character.toString(id.charAt(0));
    //     this.id_1 = Character.toString(id.charAt(1));
    //     this.id_2 = Character.toString(id.charAt(2));
    //     this.id_3 = Character.toString(id.charAt(3));
    //     this.id_4 = Character.toString(id.charAt(4));
    //     this.id_5 = Character.toString(id.charAt(5));
    //     this.id_6 = Character.toString(id.charAt(6));
    //     this.id_7 = Character.toString(id.charAt(7));
    //     this.id_8 = Character.toString(id.charAt(8));
    //     this.id_9 = Character.toString(id.charAt(9));
    // }

    public static String convert_number(int num){

        String str = Integer.toString(num);
        String[] digitsChars = new String[] {"零","壹","貳","參","肆","伍","陸","柒","捌","玖"};
        String[] inCommaPoints = new String[] {"","拾","佰","仟"};
        String[] CommaPoints = new String[] {"","萬","億","兆"};
        
        ArrayList<String> targetNumberReverse = new ArrayList<String>();
        for(int i = str.length() - 1 ; i >= 0 ; i--){
            targetNumberReverse.add(Character.toString(str.charAt(i)));
        }
        
        String output = "";
        for(int i = 0 ; i < targetNumberReverse.size() ; i++){
            String tempOutput = "";
            int current_Digit = Integer.parseInt(targetNumberReverse.get(i));
            if(output.length() <= 0){
                tempOutput = (current_Digit == 0)? "" : digitsChars[current_Digit] + inCommaPoints[ i % 4 ];
            }
            else{
                if(current_Digit == 0) {
                    if (output.indexOf("零") == 0) {
                        tempOutput = "";
                    } else {
                        tempOutput = digitsChars[current_Digit];
                    }
                } else {
                    tempOutput =  digitsChars[current_Digit] + inCommaPoints[ i % 4 ];
                }
            }
            if(output.indexOf(CommaPoints[ Math.floorDiv( i , 4 ) ]) == -1) {
                tempOutput = tempOutput + CommaPoints[ Math.floorDiv( i , 4 ) ];
            }
            output = tempOutput + output;
        }
        
        return output;
    }
}