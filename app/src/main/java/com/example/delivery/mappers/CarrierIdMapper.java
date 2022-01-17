package com.example.delivery.mappers;

public class CarrierIdMapper {
    public static String mapBy(String companyName){
        switch (companyName){
            case "CJ대한통운" : companyName = "kr.cjlogistics";
            break;

            case "CU 편의점택배" : companyName = "kr.cupost";
                break;

            case "GS Postbox 택배" : companyName = "kr.cvsnet";
                break;

            case "우체국 택배" : companyName = "kr.epost";
                break;

            case "한진택배" : companyName = "kr.hanjin";
                break;

            case "합동택배" : companyName = "kr.hdexp";
                break;

            case "경동택배" : companyName = "kr.kdexp";
                break;

            case "건영택배" : companyName = "kr.kunyoung";
                break;

            case "로젠택배" : companyName = "kr.logen";
                break;

            case "롯데택배" : companyName = "kr.lotte";
                break;

            case "일양로지스" : companyName = "kr.ilyanglogis";
                break;

            case "한서호남택배" : companyName = "kr.honamlogis";
                break;

            case "홈픽" : companyName = "kr.homepick";
                break;

            case "한의사랑택배" : companyName = "kr.hanips";
                break;

            case "대신택배" : companyName = "kr.daesin";
                break;

            case "DHL" : companyName = "de.dhl";
                break;

            case "Sagawa" : companyName = "jp.sagawa";
                break;

            case "Kuroneko Yamato" : companyName = "jp.yamato";
                break;

            case "Japan Post" : companyName = "jp.yuubin";
                break;

            case "CWAY (Woori Express)" : companyName = "kr.cway";
                break;
        }
        return companyName;
    }
}
