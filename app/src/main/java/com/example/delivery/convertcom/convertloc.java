package com.example.delivery.convertcom;

import java.util.ArrayList;

public class convertloc {
    public static String convertlocation(String locationname){
        switch (locationname){
            case "인천남수" : locationname = "인천 미추홀구 경원대로 857";
                break;

            case "인천남MP" : locationname = "인천광역시 미추홀구 학익동 587-190";
                break;

            case "이천MPHub" : locationname = "경기도 이천시 모가면 두미리 9";
                break;

            case "마포1" : locationname = "서울 마포구 월드컵로42길 12";
                break;

            case "서울마포숭문" : locationname = "서울 마포구 구룡길 36";
                break;

            case "군포HUB" : locationname = "경기 군포시 번영로 82";
                break;

            case "기흥A" : locationname = "경기 용인시 기흥구 용구대로2325번길 14";
                break;

            case "중동집배점" : locationname = "인천 계양구 서운산업로 16-4";
                break;
        }
        return locationname;
    }
}
