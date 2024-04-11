import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger niceWord3lettersCounter = new AtomicInteger(0);
    public static AtomicInteger niceWord4lettersCounter = new AtomicInteger(0);
    public static AtomicInteger niceWord5lettersCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        Runnable logic1 = () -> {
            for (String text : texts) {
                //Подробнее о isPalindrome в самом методе.
                if (text.length() == 3 && !isPalindrome(text, 3)) {
                    if (text.charAt(0) == text.charAt(2)) {
                        niceWord3lettersCounter.getAndIncrement();
                    }
                } else if (text.length() == 4 && !isPalindrome(text, 4)) {
                    if ((text.charAt(0) == text.charAt(3)) && (text.charAt(1) == text.charAt(2))) {
                        niceWord4lettersCounter.getAndIncrement();
                    }
                } else if (text.length() == 5 && !isPalindrome(text, 5)) {
                    if ((text.charAt(0) == text.charAt(4)) && (text.charAt(1) == text.charAt(3))) {
                        niceWord5lettersCounter.getAndIncrement();
                    }
                }
            }
        };

        Thread thread1 = new Thread(logic1);
        thread1.start();
        threads.add(thread1);

        Runnable logic2 = () -> {
            for (String text : texts) {
                theSameLetters(text, 3);
                theSameLetters(text, 4);
                theSameLetters(text, 5);
            }
        };

        Thread thread2 = new Thread(logic2);
        thread2.start();
        threads.add(thread2);

        Runnable logic3 = () -> {
            for (String text : texts) {
                int counter = 0;
                for (int i = 0; i < text.length() - 1; i++) {
                    if (text.charAt(0) < text.charAt(text.length() - 1)) {
                        if (text.charAt(i) <= text.charAt(i + 1)) {
                            counter++;
                        }
                        if (text.length() == 3 && counter == (text.length() - 1)) {
                            niceWord3lettersCounter.getAndIncrement();
                        } else if (text.length() == 4 && counter == (text.length() - 1)) {
                            niceWord4lettersCounter.getAndIncrement();
                        } else if (text.length() == 5 && counter == (text.length() - 1)) {
                            niceWord5lettersCounter.getAndIncrement();
                        }
                    }
                }
            }
        };

        Thread thread3 = new Thread(logic3);
        thread3.start();
        threads.add(thread3);

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.printf("Красивых слов с длиной 3: %d шт\n", niceWord3lettersCounter.get());
        System.out.printf("Красивых слов с длиной 4: %d шт\n", niceWord4lettersCounter.get());
        System.out.printf("Красивых слов с длиной 5: %d шт", niceWord5lettersCounter.get());
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void theSameLetters(String text, int letterQuantity) {
        if (text.length() == letterQuantity) {
            int counter = 0;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(0) == text.charAt(i)) {
                    counter++;
                }
                if (counter == letterQuantity) {
                    if (letterQuantity == 3) {
                        niceWord3lettersCounter.getAndIncrement();
                    } else if (letterQuantity == 4) {
                        niceWord4lettersCounter.getAndIncrement();
                    } else {
                        niceWord5lettersCounter.getAndIncrement();
                    }
                }
            }
        }
    }

    /*
    Данная проверка необходима для того, чтобы не наврать о точном количестве "красивых" слов
    в массиве, поскольку одно и тоже генерируемое слово может относиться как к палиндрому, так
    и к слову, состоящему из одних и тех же букв.
        Пример:
    String[] texts = {"aaa", "aaaa", "aaaaa"};
    Количество слов из 3 букв - 1, 4 б - 1, 5 б - 1;
    ааа имеет все одинаковые буквы + задом-наперёд читается также => к AtomicInteger +2;
    Без данной проверки у нас будет засчитано красивых слов с длиной 3, 4 и 5 как 2, хотя по факту
    их по 1 шт., что выходит из логики.
     */
    public static boolean isPalindrome(String text, int letterQuantity) {
        if (text.length() == letterQuantity) {
            int counter = 0;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(0) == text.charAt(i)) {
                    counter++;
                }
                if (counter == letterQuantity) {
                    if (letterQuantity == 3) {
                        return true;
                    } else if (letterQuantity == 4) {
                        return true;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}