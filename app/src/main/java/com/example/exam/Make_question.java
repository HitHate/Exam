package com.example.exam;

import java.security.SecureRandom;
import java.util.HashMap;

public class Make_question {
    final static int HIGH = 100;

    public HashMap<String, HashMap<String, String>> add(int number) {
        HashMap<String, HashMap<String, String>> question = new HashMap<>();
        SecureRandom secureRandom = new SecureRandom();
        HashMap<String, String> position = new HashMap<>();
        HashMap<String, String> total = new HashMap<>();
        for (int i = 1; i < number + 1; ) {
            int num1 = secureRandom.nextInt(HIGH + 1);
            int num2 = secureRandom.nextInt(HIGH + 1);
            int num3 = num1 + num2;
            String qu = num1 + "+" + num2 + "=";
            if (total.get(qu) != null) {
                continue;
            }
            total.put(qu, Integer.toString(num3));
            position.put(Integer.toString(i), qu);
            i++;
        }
        question.put("position", position);
        question.put("total", total);
        return question;
    }

    public HashMap<String, HashMap<String, String>> sub(int number) {
        HashMap<String, HashMap<String, String>> question = new HashMap<>();
        SecureRandom secureRandom = new SecureRandom();
        HashMap<String, String> position = new HashMap<>();
        HashMap<String, String> total = new HashMap<>();
        for (int i = 1; i < number + 1; ) {
            int num1 = secureRandom.nextInt(HIGH + 1);
            int num2 = secureRandom.nextInt(HIGH + 1);
            if (num1 < num2) {
                continue;
            }
            int num3 = num1 - num2;
            String qu = num1 + "-" + num2 + "=";
            if (total.get(qu) != null) {
                continue;
            }
            total.put(qu, Integer.toString(num3));
            position.put(Integer.toString(i), qu);
            i++;
        }
        question.put("position", position);
        question.put("total", total);
        return question;
    }

    public HashMap<String, HashMap<String, String>> mix(int number) {
        HashMap<String, HashMap<String, String>> question = new HashMap<>();
        SecureRandom secureRandom = new SecureRandom();
        HashMap<String, String> position = new HashMap<>();
        HashMap<String, String> total = new HashMap<>();
        for (int i = 1; i < number + 1; ) {
            int op = secureRandom.nextInt(2);
            if (op == 0) {
                int num1 = secureRandom.nextInt(HIGH + 1);
                int num2 = secureRandom.nextInt(HIGH + 1);
                if (num1 < num2) {
                    continue;
                }
                int num3 = num1 - num2;
                String qu = num1 + "-" + num2 + "=";
                if (total.get(qu) != null) {
                    continue;
                }
                total.put(qu, Integer.toString(num3));
                position.put(Integer.toString(i), qu);
                i++;
            }
            if (op == 1) {
                int num1 = secureRandom.nextInt(HIGH + 1);
                int num2 = secureRandom.nextInt(HIGH + 1);
                int num3 = num1 + num2;
                String qu = num1 + "+" + num2 + "=";
                if (total.get(qu) != null) {
                    continue;
                }
                total.put(qu, Integer.toString(num3));
                position.put(Integer.toString(i), qu);
                i++;
            }

        }
        question.put("position", position);
        question.put("total", total);
        return question;
    }
}
