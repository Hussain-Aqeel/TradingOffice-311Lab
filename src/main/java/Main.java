import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        TradingOffice tradingOffice = new TradingOffice();

        try {
            ArrayList<String[]> APPLDividendsData =
                    tradingOffice.getDataFromCSV(new File("AAPL-Dividends.csv"));
            ArrayList<String[]> APPLData =
                    tradingOffice.getDataFromCSV(new File("AAPL.csv"));

            Scanner userInput = new Scanner(System.in);
            int choice = 0;

            while(choice != 5) {
                System.out.println(
                        "Choose one of the following options \n" +
                                "1 - Simple Moving Average: \n" +
                                "2 - Exponential Moving Average: \n" +
                                "3 - Average Dividends: \n" +
                                "4 - Frequency Of Dividends Distribution: \n" +
                                "5- exit");
                choice = userInput.nextInt();
                if (choice == 1) {
                    System.out.println(
                            "Choose one of the following options: \n" +
                                    "1 - SMA for the whole file: \n" +
                                    "2 - SMA for a Specific period : \n" +
                                    "3 - SMA for a Specific year: \n" +
                                    "4 - exit: ");
                    int SMAChoice = userInput.nextInt();
                    if (SMAChoice == 1) {
                        System.out.println(
                                "SMA of the closing price for the entire period = " +
                                        tradingOffice.SMA(APPLData) + '\n');

                    } else if (SMAChoice == 2) {
                        System.out.println("Enter the first year: ");
                        int firstYear = userInput.nextInt();
                        System.out.println("Enter the second year: ");
                        int secondYear = userInput.nextInt();
                        System.out.println("SMA for the specified period = " +
                                tradingOffice.SMAOfSpecificPeriod(APPLData, firstYear, secondYear)
                                + '\n');

                    } else if (SMAChoice == 3) {
                        System.out.println("Enter the year: ");
                        String year = userInput.next();
                        System.out.println("SMA for the year " + year + " = " +
                                tradingOffice.SMAYear(APPLData, year) + '\n');

                    } else if (SMAChoice == 4) {
                        System.exit(0);
                    }

                } else if (choice == 2) {
                    System.out.println("Enter the year: ");
                    int year = userInput.nextInt();
                    System.out.println("Enter the month: ");
                    int month = userInput.nextInt();
                    System.out.println("Enter the day: ");
                    int day = userInput.nextInt();
                    System.out.println("Enter the number of days to calculate the EMA: ");
                    int numOfDays = userInput.nextInt();

                    System.out.println("EMA from " + LocalDate.of(year, month, day) + " for " +
                            numOfDays + " days = " +
                            tradingOffice.ExponentialMovingAverage(APPLData, year, month, day, numOfDays));

                } else if (choice == 3) {
                    System.out.println("Enter the first year: ");
                    int firstYear = userInput.nextInt();
                    System.out.println("Enter the second year: ");
                    int secondYear = userInput.nextInt();
                    System.out.println("The average of dividends during that period = " +
                            tradingOffice.averageDividends(APPLDividendsData, firstYear, secondYear)
                            + '\n');

                } else if (choice == 4) {
                    System.out.println("The Frequency of dividends distribution is every " +
                            tradingOffice.frequencyOfDividendsDistribution(APPLDividendsData) +
                            " months \n");

                } else if (choice == 5) {
                    System.out.println("Thank you for using the app :)");
                    System.exit(0);

                } else if (choice < 1 || choice > 5) {
                    System.out.println("Please enter a number from the list.\n");
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n");
            System.out.println(
                    "You can't get any data, it might be due to one of these problems:\n" +
                            "1. The file does not exist\n" +
                            "2. The file is located in a different directory. " +
                            "(it must be in the swe311-first-lab) directory\n" +
                            "3. The spelling of the file name is wrong");
            System.exit(0);
        }

    }
}