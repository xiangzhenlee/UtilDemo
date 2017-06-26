package com.yushan.utildemo;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by beiyong on 2017-6-8.
 */

public class PinYinUtil {
    /**
     * 获取汉字对应的拼音,由于内部实现是读取xml查找，所以有一定的效率问题
     * 该方法不应该被频繁调用
     * @param chinese:
     * @return :
     */
    public static String getPinYin(String chinese){
        if(TextUtils.isEmpty(chinese)) return null;

        // 拼音转化的格式化类
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//设置是大写字母
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//设置不要声调

        // 由于pinyin4j不能对词语进行转化，只能对单个汉字转化
        // 1.所以需要将chinese转成字符数组，逐一遍历获取
        StringBuilder sb = new StringBuilder();
        char[] charArray = chinese.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            //2.忽略空格，如果是空格则忽略不处理
            if(Character.isWhitespace(c))continue;

            // 3.判断是否是汉字，因为汉字是占2个字节，所以它在计算机中
            // 对应的数据肯定大于127,
            if(c > 127){
                // 如果大于127，就可能是汉字
                try {
                    // 由于多音字的存在，所以返回的是数组, 单：dan shan
                    String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if(pinyinArr!=null){
                        // 此处只能取第一个,因为由于客观原因，我们无能为力去判断真实应该读什么音节，只能取第1个
                        sb.append(pinyinArr[0]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else {
                //肯定不是汉字，一般是键盘上能直接输入的字符，那么则直接拼接
                sb.append(c);
            }
        }

        return sb.toString();

    }
}
