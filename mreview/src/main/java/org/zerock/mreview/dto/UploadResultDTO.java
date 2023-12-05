package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {

    private String fileName;
    private String uuid;

    private String folderPath;

    // 실제 파일과 관련된 모든 정보를 가짐
    // 나중에 전체 경로가 필요한 경우를 대비하여 getImageURL()을 제공함
    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    // JSON으로 전달되는 UploadResultDTO에는 getImageURL처럼
    // 섬네일의 링크를 처리하기 위한 메서드 추가
    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(folderPath+"/s_" + uuid + "_" + fileName, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
