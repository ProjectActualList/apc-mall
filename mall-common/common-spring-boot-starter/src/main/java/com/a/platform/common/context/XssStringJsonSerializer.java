package com.a.platform.common.context;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 防xss攻击的json转换器
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:37
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    /**
     * 覆写 serialize 方法，对string 值进行xss过滤
     * 使用的是Jsoup的clean方法
     *
     * @param value
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    @Override
    public void serialize(String value, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (value != null) {

            //此处定义的标签是Whitelist.basicWithImages()中可以放过的标签
            List<String> tags = Arrays.asList("a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", "p", "pre", "q", "small",
                    "span", "strike", "strong", "sub", "sup", "u", "ul", "table", "tbody", "tr", "td", "img");
            Whitelist whiteList = Whitelist.basicWithImages();

            for (String tag : tags) {
                whiteList.addAttributes(tag, "style", "class", "width", "valign", "src");
            }

            value = Jsoup.clean(value, whiteList);

            jsonGenerator.writeString(value);
        }
    }

}
