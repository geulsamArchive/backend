package geulsam.archive.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToUUIDConverter implements Converter<String, UUID> {
    @Override
    public UUID convert(String source) {
        if (source == null || source.isEmpty() || source.equals("null")) {
            return null;
        }
        return UUID.fromString(source);
    }
}