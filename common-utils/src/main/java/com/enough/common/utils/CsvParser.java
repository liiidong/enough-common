package com.enough.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.supercsv.cellprocessor.StrReplace;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @apiNote : Csv解析器
 * @author: lidong
 * @since : 2020/06/30
 */
public class CsvParser {

    public static final String CHARSET_NAME = "GB2312";
    private String[] nameMapping;
    private CellProcessor[] processors;
    private String charsetName;

    private CsvParser() {
    }

    public static CsvParser getInstance() {
        return new CsvParser();
    }

    public CsvParser nameMapping(String... nameMapping) {
        this.nameMapping = nameMapping;
        return this;
    }

    public CsvParser processors(CellProcessor... processors) {
        this.processors = processors;
        return this;
    }

    public CsvParser charsetName(String charsetName){
        if(StringUtils.isBlank(charsetName)){
            this.charsetName = CHARSET_NAME;
        }
        this.charsetName = charsetName;
        return this;
    }

    public List <List <String>> execute2List(URL url) throws Exception {
        return doExecute2List(new InputStreamReader(new BOMInputStream(url.openStream()), this.charsetName));
    }

    public List <List <String>> execute2List(String filePath) throws Exception {
        return doExecute2List(new InputStreamReader(new FileInputStream(new File(filePath)), this.charsetName));
    }

    /**
     * 返回 k-v（标题-单元格值集合）集合
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public List <Map <String, String>> execute2Map(String filePath) throws Exception {
        return doExecute2Map(new InputStreamReader(new FileInputStream(new File(filePath)), this.charsetName));
    }

    public List <Map <String, String>> execute2Map(URL url) throws Exception {
        return doExecute2Map(new InputStreamReader(new BOMInputStream(url.openStream()), this.charsetName));
    }

    public <T> List <T> execute2Bean(String filePath, Class <T> clazz) throws Exception {
        return doExecute2Bean(new InputStreamReader(new FileInputStream(new File(filePath)), this.charsetName), clazz);
    }

    public <T> List <T> execute2Bean(URL url, Class <T> clazz) throws Exception {
        return doExecute2Bean(new InputStreamReader(new BOMInputStream(url.openStream()), this.charsetName), clazz);
    }

    private List <Map <String, String>> doExecute2Map(final Reader reader) throws Exception {
        ICsvMapReader mapReader = new CsvMapReader(reader, CsvPreference.EXCEL_PREFERENCE);
        String[] headers = mapReader.getHeader(true);
        validNameMapping(headers);
        validCellProcessors(headers);
        List <Map <String, String>> list = Lists.newArrayList();
        Map <String, String> map;
        while ((map = mapReader.read(this.nameMapping)) != null) {
            list.add(map);
        }
        return list;
    }

    private <T> List <T> doExecute2Bean(final Reader reader, Class <T> clazz) throws Exception {
        ICsvBeanReader beanReader = new CsvBeanReader(reader, CsvPreference.EXCEL_PREFERENCE);
        String[] headers = beanReader.getHeader(true);
        validNameMapping(headers, clazz);
        validCellProcessors(headers);
        List <T> beans = Lists.newArrayList();
        T bean;

        while ((bean = beanReader.read(clazz, this.nameMapping, this.processors)) != null) {
            beans.add(bean);
        }
        return beans;
    }

    private List <List <String>> doExecute2List(final Reader reader) throws Exception {
        ICsvListReader listReader = new CsvListReader(reader, CsvPreference.EXCEL_PREFERENCE);
        String[] headers = listReader.getHeader(true);
        validNameMapping(headers);
        validCellProcessors(headers);
        List <List <String>> dataList = new ArrayList <>();
        List <String> values;
        while ((values = listReader.read()) != null) {
            dataList.add(values);
        }
        return dataList;
    }

    @Deprecated
    private String checkParseValue(String value) {
        if (StringUtils.equalsIgnoreCase(value.trim(), "#DIV/0!") || StringUtils.equalsIgnoreCase(value.trim(), "N/A")) {
            value = StringUtils.EMPTY;
        }
        return value;
    }

    private void validNameMapping(final String[] headers) {
        if (ArrayUtils.isEmpty(this.nameMapping)) {
            this.nameMapping = headers;
        } else {
            Validate.isTrue(nameMapping.length == headers.length,
                    "the nameMapping array and the csv_headers should be the same size (nameMapping length=%s csv_headers size = %s", nameMapping.length,
                    headers.length);
        }
    }

    private <T> void validNameMapping(final String[] headers, Class <T> clazz) {
        if (ArrayUtils.isEmpty(this.nameMapping)) {
            List <String> fieldNames = Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
            this.nameMapping = fieldNames.toArray(new String[fieldNames.size()]);
        } else {
            Validate.isTrue(this.nameMapping.length == headers.length,
                    "the nameMapping array and the csv_headers should be the same size (nameMapping length=%s csv_headers size = %s", nameMapping.length,
                    headers.length);
        }
    }

    private void validCellProcessors(final String[] headers) {
        if (ArrayUtils.isEmpty(this.processors)) {
            List <CellProcessor> processors = new ArrayList <>(headers.length);
            for (int i = 0; i < headers.length; i++) {
                //正则匹配公式计算出的单元格值因为分母不能为0导致的特殊字符
                processors.add(new StrReplace("(^\\s*#DIV/0!\\s*|^\\s*N/A\\s*)", StringUtils.EMPTY));
            }
            this.processors = processors.toArray(new CellProcessor[0]);
        } else {
            Validate.isTrue(processors.length == headers.length,
                    "the processors and the csv_headers should be the same size (processors length=%s csv_headers size = %s", processors.length,
                    headers.length);
        }
    }

    public String[] getHeaders(String filePath) throws IOException {
        InputStreamReader freader = new InputStreamReader(new FileInputStream(new File(filePath, CHARSET_NAME)));
        CsvMapReader reader = new CsvMapReader(freader, CsvPreference.EXCEL_PREFERENCE);
        //获取头部信息
        return reader.getHeader(true);
    }

    public String[] getHeaders(URL url) throws IOException {
        final Reader freader = new InputStreamReader(new BOMInputStream(url.openStream()), CHARSET_NAME);
        CsvMapReader reader = new CsvMapReader(freader, CsvPreference.EXCEL_PREFERENCE);
        //获取头部信息
        return reader.getHeader(true);
    }

    public boolean write(String savePath, String fileName, List <List <?>> colums, String... headers) throws Exception {
        File file = new File(savePath + File.separator + fileName + ".csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStreamWriter fwriter = new OutputStreamWriter(new FileOutputStream(file), CHARSET_NAME);
        CsvListWriter writer = new CsvListWriter(fwriter, CsvPreference.EXCEL_PREFERENCE);
        writer.writeHeader(headers);
        for (List <?> values : colums) {
            writer.write(values);
        }
        //记得刷新缓冲区，否则文件末尾数据会丢失
        writer.flush();
        return true;
    }

}
