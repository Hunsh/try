package com.thxpace.portal;

import com.thxpace.portal.model.Navigation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PortalApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void add() {
        Navigation navigation = new Navigation();
        navigation.setImgUrl("");
        navigation.setName("测试一级目录");
        navigation.setLevel(1);
        navigation.setTags(new String[]{"标签1","标签2","标签3"});

        mongoTemplate.insert(navigation);
        System.out.println("-------------success");
    }

    @Test
    public void find() throws Exception {
        List<Navigation> navigations= mongoTemplate.findAll(Navigation.class);
        navigations.forEach(navigation -> {
            log.info(navigation.toString());
        });
    }

    @Test
    public void find2(){
        Criteria criteria = Criteria.where("tags").elemMatch(Criteria.where("$eq").is("标签3"));
        List<Navigation> navigations = mongoTemplate.find(new Query(criteria), Navigation.class);
        System.out.println(navigations.size());

    }

    @Test
    public void delete(){
        Navigation navigation = new Navigation();
        mongoTemplate.remove(navigation);
    }

    @Test
    public void testInitialization() throws Exception{
        {
            XSSFWorkbook wb = null;
            File cfgFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/excelTemplate/200622a网站收集及分类梳理—zyh.xlsx");
            InputStream in = new FileInputStream(cfgFile);
            //读取excel模板
            wb = new XSSFWorkbook(in);


            XSSFSheet sheet = null;
            try{
                sheet = wb.getSheetAt(1);
                //先将获取的单元格设置为String类型，下面使用getStringCellValue获取单元格内容
                //如果不设置为String类型，如果单元格是数字，则报如下异常
                //java.lang.IllegalStateException: Cannot get a STRING value from a NUMERIC cell
                //cell列row行
                HashSet<String> level1Set = new HashSet<>();
                Iterator<Row> it = sheet.rowIterator();
                while(it.hasNext()){
                    XSSFRow row = (XSSFRow) it.next();

                    row.getCell(1).setCellType(CellType.STRING);
                    String level1 = row.getCell(1).getStringCellValue();

                    if(level1 .equalsIgnoreCase("一级标题") || StringUtils.isEmpty(level1)){
                        continue;
                    }
                    level1Set.add(level1);

                    Navigation navigation = new Navigation();
                    navigation.setLevel(1);
                    navigation.setName(level1);

                    mongoTemplate.insert(navigation);

                }

                HashSet<String> level2Set = new HashSet<>();
                Iterator<Row> level2Iterator = sheet.rowIterator();
                while(level2Iterator.hasNext()){
                    XSSFRow row = (XSSFRow) level2Iterator.next();

                    row.getCell(2).setCellType(CellType.STRING);
                    String level2 = row.getCell(2).getStringCellValue();

                    if(level2.equalsIgnoreCase("二级标题") || StringUtils.isEmpty(level2)){
                        continue;
                    }
                    level2Set.add(level2);

                    String[] args = level2.split(":");
                    String level1 = args[1];
                    level2 = args[0];


                    List<Navigation> navigations = mongoTemplate.find(new Query(Criteria.where("name").is(level1)), Navigation.class);
                    Navigation navigation = navigations.get(0);




                    Navigation navigationLevel2 = new Navigation();
                    navigationLevel2.setLevel(2);
                    navigationLevel2.setName(level2);
                    navigationLevel2.setParent(navigation.getObjectId());

                    mongoTemplate.insert(navigationLevel2);
                }


                sheet .getRow(2).getCell(2).setCellType(CellType.STRING);
                //读取单元格内容
                String cellValue = sheet.getRow(2).getCell(2).getStringCellValue();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}
