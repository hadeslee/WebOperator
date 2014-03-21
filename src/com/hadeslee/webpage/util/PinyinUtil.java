/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

/**
 *
 * @author Hadeslee
 */
public class PinyinUtil {

    //��ĸZʹ����������ǩ�������У�����ֵ
    //i, u, v��������ĸ, ����ǰ�����ĸ
    private static char[] chartable = {
        '��', '��', '��', '��', '��', '��', '��', '��', '��',
        '��', '��', '��', '��', '��', 'Ŷ', 'ž', '��', 'Ȼ',
        '��', '��', '��', '��', '��', '��', 'ѹ', '��', '��'
    };
    private static char[] alphatable = {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };
    private static int[] table = new int[27];

    //��ʼ��
    static {
        for (int i = 0; i < 27; ++i) {
            table[i] = gbValue(chartable[i]);
        }
    }

    //������,�����ַ�,�õ�������ĸ,
    //Ӣ����ĸ���ض�Ӧ�Ĵ�д��ĸ
    //�����Ǽ��庺�ַ��� '0'
    public static char Char2Alpha(char ch) {

        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 'a' + 'A');
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        int gb = gbValue(ch);
        if (gb < table[0]) {
            return '0';
        }
        int i;
        for (i = 0; i < 26; ++i) {
            if (match(i, gb)) {
                break;
            }
        }
        if (i >= 26) {
            return '0';
        } else {
            return alphatable[i];
        }
    }

    //����һ���������ֵ��ַ�������һ������ƴ������ĸ���ַ���
    public static String String2Alpha(String SourceStr) {
        String Result = "";
        int StrLength = SourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                Result += Char2Alpha(SourceStr.charAt(i));
            }
        } catch (Exception e) {
            Result = "";
        }
        return Result;
    }

    private static boolean match(int i, int gb) {
        if (gb < table[i]) {
            return false;
        }

        int j = i + 1;

        //��ĸZʹ����������ǩ
        while (j < 26 && (table[j] == table[i])) {
            ++j;
        }

        if (j == 26) {
            return gb <= table[j];
        } else {
            return gb < table[j];
        }

    }

    //ȡ�����ֵı���
    private static int gbValue(char ch) {
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] &
                    0xff);
        } catch (Exception e) {
            return 0;
        }

    }

    public static void main(String[] args) {
        System.out.println(PinyinUtil.String2Alpha("���ԣ��л����񹲺͹���"));
        System.out.println(PinyinUtil.String2Alpha("����"));
        return;
    }
}
