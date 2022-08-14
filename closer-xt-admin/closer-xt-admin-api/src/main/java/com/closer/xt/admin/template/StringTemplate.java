package com.closer.xt.admin.template;

import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.stereotype.Component;

@Component
public class StringTemplate extends TemplateDirective{
    @Override
    public String getName() {
        return "strstr";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        // 获取参数
        String username = handler.getString("username");
        String city = handler.getString("city");
        // 处理参数
        String template = "{}来自{}.";
        String format = StrUtil.format(template, username, city);
        // 传回去
        handler.put(RESULT,format).render();
    }
}
