package com.javarush.task.task20.task2027;

import java.util.ArrayList;
import java.util.List;

/* 
Кроссворд
*/

public class Solution {
    public static void main(String[] args) {
        int[][] crossword = new int[][]{
                {'f', 'd', 'e', 'r', 'l', 'k'},
                {'u', 's', 'a', 'm', 'e', 'o'},
                {'l', 'n', 'g', 'r', 'o', 'v'},
                {'m', 'l', 'p', 'r', 'r', 'h'},
                {'p', 'o', 'e', 'e', 'j', 'j'}
        };

        List<Word> wordList = null;
        wordList = detectAllWords(crossword, "home", "same");
        System.out.println(wordList);
        /*
Ожидаемый результат
home - (5, 3) - (2, 0)
same - (1, 1) - (4, 1)
         */
    }

    public static List<Word> detectAllWords(int[][] crossword, String... words) {
        List<Word> wordList = new ArrayList<>();
        for (String word : words) {
            wordList.add(detectedWorld(crossword, word));
        }
        return wordList;
    }
    public static Word detectedWorld(int[][] crossword, String word) {
        String[] symbols = word.split("");
        //1. поиск первого символа
        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[i].length; j++) {
                int symbol = crossword[i][j];
                if (symbols[0].charAt(0) == symbol) {
                    int x1 = j;
                    int y1 = i;
                    //Начинаем искать слова
                    int[] route = new int[]{1, 0, -1};
                    Word fundedWord = null;
                    for (int k = 0; k < route.length; k++) {
                        for (int l = 0; l < route.length; l++) {
                            fundedWord = seekWorld(crossword, word, x1, y1, route[k], route[l]);
                            if (fundedWord != null) return fundedWord;
                        }
                    }
                }
            }
        }
        return null;
    }
    public static Word seekWorld(int[][] crossword, String word, int x1, int y1, int deltax, int deltay) {
        //1. Инициализация слова
        Word fundedWord = new Word(word);
        fundedWord.setStartPoint(x1, y1);
        //2.Поиск символов от отправной точки
        String[] symbols = word.split("");
        boolean wordFound = false;
        for (String symbol : symbols) {
            //Проверка не выхода за границу
            if (!(y1 >= 0 && crossword.length > y1)) return null;
            if (!(x1 >= 0 && crossword[y1].length > x1)) return null;

            if ((int) symbol.charAt(0) == crossword[y1][x1]) {
                x1 += deltax;
                y1 += deltay;
            } else return null;
        }
        fundedWord.setEndPoint(x1 - deltax, y1 -deltay);//костыль
        return fundedWord;
    }

    public static class Word {
        private String text;
        private int startX;
        private int startY;
        private int endX;
        private int endY;

        public Word(String text) {
            this.text = text;
        }

        public void setStartPoint(int i, int j) {
            startX = i;
            startY = j;
        }

        public void setEndPoint(int i, int j) {
            endX = i;
            endY = j;
        }

        @Override
        public String toString() {
            return String.format("%s - (%d, %d) - (%d, %d)", text, startX, startY, endX, endY);
        }
    }
}
