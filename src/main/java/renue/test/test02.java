package renue.test;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("all")
public class test02 {

    // Указание номера колонки для поиска
    // Номер колонки для поиска не может быть меньше нуля или больше кол-ва колонок в строке
    private static int column_number = 2;

    public static void main(String[] args) throws IOException {

        // Проверка наличия номера колонки для индексации
        if (args.length > 0) {
            column_number = Integer.parseInt(args[0]);
            if (column_number < 1) {
                System.out.println(columnWarning());
                return;
            }
        }

        // Проверка количества колонок в файле по первой строке
        int total_columns = 0;
        try {
            CSVReader reader = new CSVReader(new FileReader("data.csv"), ',', '"', 0);
            String[] first_line;
            if ((first_line = reader.readNext()) != null) {
                total_columns = first_line.length;
            }
            if (total_columns < column_number) {
                System.out.println(columnWarning());
                return;
            }

            // Ввод строки для поиска
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите строку: ");
            String in = scanner.nextLine();
            scanner.close();

            Set<String[]> airports = new TreeSet<String[]>(new Comparator<String[]>() {
                public int compare(String[] airport1, String[] airport2) {
                    return airport1[column_number - 1].compareTo(airport2[column_number - 1]);
                }
            });

            // Отчёт начала поиска
            long start = System.currentTimeMillis();

            // Работа с первой строкой
            String[] nextLine = first_line;
            if (nextLine[column_number - 1].startsWith(in)) {
                airports.add(nextLine);
            }

            // Работа с оставшимися строками
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[column_number - 1].startsWith(in)) {
                    airports.add(nextLine);
                }
            }

            // Завершение поиска
            long finish = System.currentTimeMillis();
            long elapsed = finish - start;

            reader.close();

            // Вывод результата
            for (String[] s : airports) {
                String line = Arrays.toString(s);
                System.out.println(line.substring(1, line.length() - 1));
            }
            System.out.println("Количество совпадений: " + airports.size());
            System.out.println("Длительность поиска: " + elapsed + " мс.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл с данными 'data.csv' не найденн в каталоге с программой");
        }
    }

    private static String columnWarning() {
        return "Номер колонки для поиска меньшу нуля или больше кол-ва колонок в строке";
    }
}
