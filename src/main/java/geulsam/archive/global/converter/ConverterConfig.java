package geulsam.archive.global.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {

    private final StringToUUIDConverter stringToUUIDConverter;

    public ConverterConfig(StringToUUIDConverter stringToUUIDConverter) {
        this.stringToUUIDConverter = stringToUUIDConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToUUIDConverter);
    }
}