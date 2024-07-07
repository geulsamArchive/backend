package geulsam.archive.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteManager {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**업로드 된 객체를 참조할 수 있는 public url*/
    @Value("${s3.public.url}")
    private String publicUrl;

    public void deleteFile(UUID uuid, String type){
        try{
            String key = type + '-' + uuid.toString();
            amazonS3.deleteObject(bucketName, key);
        } catch (Exception e){
            throw new ArchiveException(ErrorCode.VALUE_ERROR, e.getMessage());
        }
    }
}
