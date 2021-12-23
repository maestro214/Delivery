package com.example.delivery.convertcom;

import android.util.Log;

public class convertcom {
    public String convertcompany(String com){
        switch (com){
            case "CJ대한통운" : com = "kr.cjlogistics";
            break;

            case "CU 편의점택배" : com = "kr.cupost";
                break;

            case "GS Postbox 택배" : com = "kr.cvsnet";
                break;

            case "우체국 택배" : com = "kr.epost";
                break;

            case "한진택배" : com = "kr.hanjin";
                break;

            case "합동택배" : com = "kr.hdexp";
                break;

            case "경동택배" : com = "kr.kdexp";
                break;

            case "건영택배" : com = "kr.kunyoung";
                break;

            case "로젠택배" : com = "kr.logen";
                break;

            case "롯데택배" : com = "kr.lotte";
                break;

            case "일양로지스" : com = "kr.ilyanglogis";
                break;

            case "한서호남택배" : com = "kr.honamlogis";
                break;

            case "홈픽" : com = "kr.homepick";
                break;

            case "한의사랑택배" : com = "kr.hanips";
                break;

            case "대신택배" : com = "kr.daesin";
                break;

            case "DHL" : com = "de.dhl";
                break;

            case "Sagawa" : com = "jp.sagawa";
                break;

            case "Kuroneko Yamato" : com = "jp.yamato";
                break;

            case "Japan Post" : com = "jp.yuubin";
                break;

            case "CWAY (Woori Express)" : com = "kr.cway";
                break;
        }
        return com;
    }
}
