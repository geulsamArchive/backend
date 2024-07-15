package geulsam.archive.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * S3에 MultipartFile 객체를 업로드하기 위한 클래스
 */
@Component
@RequiredArgsConstructor
public class UploadManager {

    private final AmazonS3 amazonS3;
    private final PreSignedUrlManager preSignedUrlManager;

    /**bucket name*/
    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**업로드 된 객체를 참조할 수 있는 public url*/
    @Value("${s3.public.url}")
    private String publicUrl;

    /**
     *
     * @param multipartFile 업로드 할 파일
     * @param uuid 해당 파일이 속한 엔티티의 uuid ex)Book.id
     * @param type 해당 파일이 속한 속성 ex)book, bookCover, poster, contentHTML...
     * @return 파일을 업로드 한 뒤 제공하는 String ex)https://pub-dsaf/a.dfa
     */
    public String uploadFile(MultipartFile multipartFile, UUID uuid, String type){
        File file = null;
        try {
            file = convertMultipartFileToFile(multipartFile);
            // 저장할 파일명 생성
            String uploadName = type + '-' + uuid.toString();
            // 파일 저장
            amazonS3.putObject(new PutObjectRequest(bucketName, uploadName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            // 삭제
            boolean delete = file.delete();
            return publicUrl + uploadName;
        } catch (IOException | SdkClientException e){
            throw new ArchiveException(ErrorCode.VALUE_ERROR, e.getMessage());
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    /**
     * MultipartFile 객체를 임시로 저장하여 File 객체로 만드는 메서드
     * @param multipartFile 저장이 필요한 MultipartFile
     * @return 서버에 임시로 저장된 File 객체
     * @throws IOException
     */
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException{
        String currentDir = System.getProperty("user.dir");
        File convertFile = new File(multipartFile.getOriginalFilename());


        if(convertFile.createNewFile()) {
            try(FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return convertFile;
        }
        return convertFile;
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // No extension found
        }
        return fileName.substring(lastDotIndex + 1);
    }
}
