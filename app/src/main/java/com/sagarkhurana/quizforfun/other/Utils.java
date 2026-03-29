package com.sagarkhurana.quizforfun.other;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.sagarkhurana.quizforfun.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Utils {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String formatDate(long time){
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    public static Map<String,String> getMathQuestions(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("1+1","2");
        questions.put("2+2","4");
        questions.put("3+3","6");
        questions.put("4+4","8");
        questions.put("5+5","10");
        questions.put("6+6","12");
        questions.put("7+7","14");
        questions.put("8+8","16");
        questions.put("9+9","18");
        questions.put("10+10","20");
        questions.put("11+11","22");
        questions.put("12+12","24");
        questions.put("13+13","26");
        questions.put("14+14","28");
        questions.put("15+15","30");

        return questions;
    }

    public static Map<String,String> getRandomMathQuestions(int SIZE){
        HashMap<String,String> questionsMap = new HashMap<>();
        Map<String,String> originalQuestion = getMathQuestions();
        int originalSize =  originalQuestion.size();
        ArrayList<String> keyList = new ArrayList<String>(originalQuestion.keySet());

        while (questionsMap.size()<SIZE){
            Random random = new Random();
            int randomNumber = random.nextInt(originalSize);
            String question = keyList.get(randomNumber);
            if (!questionsMap.containsKey(question)){
                questionsMap.put(question,originalQuestion.get(question));
            }
        }
        return questionsMap;
    }



    public static Map<String,Map<String,Boolean>> getLiteratureQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("Maya Angelou",true);
        answer1.put("Robert Hass",false);
        answer1.put("Jessica Hagdorn",false);
        answer1.put("Micheal Palmer",false);
        questions.put("Nhà văn Mỹ nào đã xuất bản cuốn 'A brave and startling truth' vào năm 1996?",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("Thơ chữ đầu (Acrostic)",true);
        answer2.put("Haiku",false);
        answer2.put("Sử thi",false);
        answer2.put("Thơ điệp âm",false);
        questions.put("Tên gọi của loại thơ mà các chữ cái đầu tiên của mỗi dòng tạo thành một từ là gì?",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("Limerick",true);
        answer3.put("Quartet",false);
        answer3.put("Sextet",false);
        answer3.put("Palindrome",false);
        questions.put("Một bài thơ hài hước gồm năm dòng được gọi là gì?",answer3);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Hamlet",true);
        answer5.put("Cymbeline",false);
        answer5.put("Titus Andronicus",false);
        answer5.put("Pericles",false);
        questions.put("Vở kịch nổi tiếng nào của Shakespeare có câu trích dẫn: 'Đừng là người vay cũng đừng là người cho vay'?",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("Thế kỷ 16",true);
        answer6.put("Thế kỷ 17",false);
        answer6.put("Thế kỷ 14",false);
        answer6.put("Thế kỷ 15",false);
        questions.put("Shakespeare sinh ra vào thế kỷ nào?",answer6);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getGeographyQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("TP.HCM",false);
        answer1.put("Đà Nẵng",false);
        answer1.put("Hà Nội",true);
        answer1.put("Huế",false);
        questions.put("Thủ đô của Việt Nam là gì?",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("Châu Âu",false);
        answer2.put("Châu Á",true);
        answer2.put("Châu Phi",false);
        answer2.put("Châu Mỹ",false);
        questions.put("Châu lục nào lớn nhất thế giới?",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("Sông Nile",true);
        answer3.put("Sông Amazon",false);
        answer3.put("Sông Mekong",false);
        answer3.put("Sông Mississippi",false);
        questions.put("Sông dài nhất thế giới là sông nào?",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("Mỹ",false);
        answer4.put("Trung Quốc",false);
        answer4.put("Nga",true);
        answer4.put("Canada",false);
        questions.put("Quốc gia nào có diện tích lớn nhất thế giới?",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Andes",false);
        answer5.put("Alps",false);
        answer5.put("Himalaya",true);
        answer5.put("Rocky",false);
        questions.put("Dãy núi cao nhất thế giới là gì?",answer5);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getHistoryQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("1914",false);
        answer1.put("1939",true);
        answer1.put("1941",false);
        answer1.put("1945",false);
        questions.put("Chiến tranh Thế giới thứ hai bắt đầu vào năm nào?",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("Đức tấn công Liên Xô",false);
        answer2.put("Nhật tấn công Trân Châu Cảng",false);
        answer2.put("Đức tấn công Ba Lan",true);
        answer2.put("Ý tấn công Ethiopia",false);
        questions.put("Sự kiện nào được xem là nguyên nhân trực tiếp bùng nổ Chiến tranh Thế giới thứ hai?",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("Anh, Mỹ, Liên Xô",false);
        answer3.put("Đức, Ý, Nhật Bản",true);
        answer3.put("Pháp, Anh, Mỹ",false);
        answer3.put("Trung Quốc, Mỹ, Liên Xô",false);
        questions.put("Phe Trục trong Chiến tranh Thế giới thứ hai gồm những quốc gia chủ yếu nào?",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("Đức đầu hàng vô điều kiện",true);
        answer4.put("Nhật đầu hàng",false);
        answer4.put("Mỹ ném bom nguyên tử Hiroshima",false);
        answer4.put("Liên Xô tấn công Berlin",false);
        questions.put("Sự kiện nào đánh dấu sự kết thúc của Chiến tranh Thế giới thứ hai ở châu Âu?",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Tokyo và Osaka",false);
        answer5.put("Hiroshima và Nagasaki",true);
        answer5.put("Kyoto và Kobe",false);
        answer5.put("Nagoya và Yokohama",false);
        questions.put("Hai thành phố nào của Nhật Bản bị Mỹ ném bom nguyên tử năm 1945?",answer5);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomLiteratureAndGeographyQuestions(Context context, String subject, int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;
        if (subject.equals(context.getString(R.string.geography))){
            originalQuestion = getGeographyQuestions();
        }else if (subject.equals(context.getString(R.string.history_subject))){
            originalQuestion = getHistoryQuestions();
        }else{
            originalQuestion = getLiteratureQuestions();
        }

        int originalSize =  originalQuestion.size();
        ArrayList<String> keyList = new ArrayList<String>(originalQuestion.keySet());

        while (questionsMap.size()<SIZE && questionsMap.size() < originalSize){
            Random random = new Random();
            int randomNumber = random.nextInt(originalSize);
            String question = keyList.get(randomNumber);
            if (!questionsMap.containsKey(question)){
                questionsMap.put(question,originalQuestion.get(question));
            }
        }
        return questionsMap;
    }

}
