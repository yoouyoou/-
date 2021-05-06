package ddwu.mobile.finalproject.ma02_20180983;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class foodXmlParser {
    private XmlPullParser parser;

    private enum TagType{NONE, NAME, KCAL, CARBO, PROTEIN, FAT, SUGAR, NATRIUM, CHOLESTEROL, FATTYACID, TRANSFAT, MAKER, VOL};
    final static String TAG_ID = "FOOD_CD";
    final static String TAG_NAME = "DESC_KOR";
    final static String TAG_KCAL = "NUTR_CONT1";
    final static String TAG_CARBO = "NUTR_CONT2";
    final static String TAG_PROTEIN = "NUTR_CONT3";
    final static String TAG_FAT = "NUTR_CONT4";
    final static String TAG_SUGAR = "NUTR_CONT5";
    final static String TAG_NATRIUM = "NUTR_CONT6";
    final static String TAG_CHOLESTEROL = "NUTR_CONT7";
    final static String TAG_FATTYACID = "NUTR_CONT8";
    final static String TAG_TRANSFAT = "NUTR_CONT9";
    final static String TAG_MAKER = "MAKER_NAME";

    public foodXmlParser(){
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException e){
            e.printStackTrace();
        }
    }

    public ArrayList<food> parse(String xml){
        Log.d("current", "foodXmlParser내부 parse메소드 시작");
        ArrayList<food> foodList = new ArrayList<food>();
        food food = null;
        TagType tagType = TagType.NONE;

        try{
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){
                Log.d("current", "태그 루프문 내부");
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("row")){
                            Log.d("current", "row발견");
                            food = new food();
                        } else if(parser.getName().equals("NUTR_CONT1")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.KCAL;
                        } else if(parser.getName().equals("NUTR_CONT2")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.CARBO;
                        }else if(parser.getName().equals("NUTR_CONT3")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.PROTEIN;
                        }else if(parser.getName().equals("NUTR_CONT4")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.FAT;
                        }else if(parser.getName().equals("NUTR_CONT5")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.SUGAR;
                        }else if(parser.getName().equals("NUTR_CONT6")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.NATRIUM;
                        }else if(parser.getName().equals("NUTR_CONT7")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.CHOLESTEROL;
                        }else if(parser.getName().equals("NUTR_CONT8")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.FATTYACID;
                        }else if(parser.getName().equals("NUTR_CONT9")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.TRANSFAT;
                        }else if(parser.getName().equals("SERVING_SIZE")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.VOL;
                        } else if(parser.getName().equals("DESC_KOR")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.NAME;
                        }else if(parser.getName().equals("MAKER_NAME")){
                            if(!parser.isEmptyElementTag())
                                tagType = foodXmlParser.TagType.MAKER;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType){
                            case NAME:
                                food.setName(parser.getText());
                                break;
                            case VOL:
                                food.setVolume(Double.parseDouble(parser.getText()));
                                break;
                            case KCAL:
                                food.setKcal(Double.parseDouble(parser.getText()));
                                break;
                            case CARBO:
                                food.setCarbohydrate(Double.parseDouble(parser.getText()));
                                break;
                            case PROTEIN:
                                food.setProtein(Double.parseDouble(parser.getText()));
                                break;
                            case FAT:
                                food.setFat(Double.parseDouble(parser.getText()));
                                break;
                            case SUGAR:
                                food.setSugar(Double.parseDouble(parser.getText()));
                                break;
                            case NATRIUM:
                                food.setNatrium(Double.parseDouble(parser.getText()));
                                break;
                            case CHOLESTEROL:
                                food.setCholesterol(Double.parseDouble(parser.getText()));
                                break;
                            case FATTYACID:
                                food.setFattyAcid(Double.parseDouble(parser.getText()));
                                break;
                            case TRANSFAT:
                                food.setTransfat(Double.parseDouble(parser.getText()));
                                break;
                            case MAKER:
                                food.setMaker(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("row")){
                            Log.d("current", "row끝");
                            Log.d("check", food.getName()+","+food.getCarbohydrate()+", " + food.getCholesterol());
                            Log.d("current", "foodList 현재개수: " + foodList.size());
                            foodList.add(food);
                            food = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d("check", "파싱완료 foodList개수: " + foodList.size());
        return foodList;
        //return foodNameList;
    }

}
