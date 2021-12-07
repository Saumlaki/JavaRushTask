package com.javarush.task.task22.task2209;

import javax.sql.rowset.BaseRowSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/* 
Составить цепочку слов
*/

public class Solution {



    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String fineName = br.readLine();

        try (BufferedReader fBR = new BufferedReader(new FileReader(fineName))) {

            while (fBR.ready()) {
                StringBuilder result = getLine(fBR.readLine().split(" "));
                System.out.println(result.toString());
            }
        }
    }

    public static StringBuilder getLine(String... words) {

        List<WordsLine> wordsLineArray = new ArrayList<>();

        //Запускаем рекурсивный поиск
        for (int i = 0; i < words.length; i++) {
            wordsLineArray.add(new WordsLine(words[i]));
            getLineRec(words, wordsLineArray);
        }

        //Сортируем. Самые длинные на верху
        Collections.sort(wordsLineArray);

        //Возвращаем результат
        if (wordsLineArray.size() == 0)
            return new StringBuilder();
        else
            return wordsLineArray.get(0).getResult();
    }

    static public void getLineRec(String[] worlds,  List<WordsLine> wordsLineArray) {

        //Временная коллекция для обхода.
        List<WordsLine> worldsLinesTemp = new ArrayList<>(wordsLineArray);

        //Занова перебираем слова
        for (int i = 0; i < worlds.length; i++) {

            String world = worlds[i];

            //Перебираем уже имеющиеся коллекции и смотрим какая подходит нам
            for (WordsLine worldsLineTemp : worldsLinesTemp) {

                //Если это словов подходит для цепочки то....
                if (worldsLineTemp.itsGood(world)) {

                    //...создаем цепочку на основании той которую проверяем но уже с этим словом и...
                    WordsLine worldsLineNew = new WordsLine(worldsLineTemp, world);

                    //...если эта цепочка у нас еще не появлялась то добавляем ее в наш список...
                    //... и рекурсивно пытаемся подобрать слова под нее
                    if (!wordsLineArray.contains(worldsLineNew)) {
                        wordsLineArray.add(worldsLineNew);
                        getLineRec(worlds, wordsLineArray);
                    }
                }
            }
        }
    }

    /**Вспомогательный класс для поиска максимальной последовательности символов
     */
    static class WordsLine implements Comparable<WordsLine> {

        protected List<String> words; //Содержит найденную для данного класса последовательность слов
        protected String lastCharacter;//Последний символ в данной последовательнсти слов
        protected int count;//Количество слов в цепочке слов

        /**
         * Базовый конструктор для первой итерации поиска слов
         * */
        public WordsLine(String world) {
            this.words = new ArrayList<>();
            words.add(world);

            this.lastCharacter = world.substring(world.length() - 1);
            count = 1;
        }

        /**
         * Конструктор по созданию нового элемента класса на основании старого
         * */
        public WordsLine(WordsLine worldsLine, String world) {
            this.words = new ArrayList<>(worldsLine.words);
            words.add(world);

            this.lastCharacter = world.substring(world.length() - 1);
            this.count = worldsLine.count + 1;
        }

        /**Метод проверяет подходит ли нам новое слово
         *
         * */
        public boolean itsGood(String world) {

            //Это слово тут есть. Не обрабатываем его
            if (words.contains(world)) return false;

            //Проверяем соответствие последний - первый символ
            String firstCharacter = world.substring(0, 1);
            return lastCharacter.toLowerCase(Locale.ROOT).equals(firstCharacter.toLowerCase(Locale.ROOT));
        }

        /**
         * Метод возвращает результат в пиемлимом для валидатора формате*/
        public StringBuilder getResult() {

            StringJoiner sj= new StringJoiner(" ");
            words.stream().forEach(a->sj.add(a));
            return new StringBuilder(sj.toString());
        }
        @Override
        public int compareTo(WordsLine o) {
            return  o.count - this.count;
        }

        @Override
        public String toString() {
            return "[" + String.valueOf(count) + "]" + words.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WordsLine)) return false;
            WordsLine that = (WordsLine) o;
            return Objects.equals(words, that.words);
        }

        @Override
        public int hashCode() {
            return Objects.hash(words);
        }
    }
}
