package com.example.delivery.convertcom;

import java.util.ArrayList;

public class convertloc {
    public static String convertlocation(String locationname){
        switch (locationname){
            //cj대한통운
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

            case "옥천HUB" : locationname = "충청북도 옥천군 이원면 건진2길 65";
                break;

            case "광진B" : locationname = "경기도 구리시 벌말로 95";
                break;


                //우체국
            case "서울광진우체국" : locationname = "서울 광진구 강변역로 2";
                break;

            case "동서울물류센터" : locationname = "서울 광진구 자양로 76 동서울우편집중국청사";
                break;

            case "중부권광역우편물류센터" : locationname = "대전광역시 동구 안골로 11";
                break;

            case "고양덕양우체국" : locationname = "경기 고양시 덕양구 백양로 132";
                break;




        }
        return locationname;
    }
}
