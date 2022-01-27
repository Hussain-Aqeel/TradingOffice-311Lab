import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The TradingOffice class should implement all the functionalities in
 * the requirements such as calculating the average dividends
 *
 * @author  Hussain Aljassim, Ahmed Baabdullah, Omar Mashhrawi
 * @version 1.0
 */
public class TradingOffice {

    /**
     * This method aims to calculate the average of dividends
     * during a specified period of time
     * @param arrayList This is the first parameter, an array contains the data
     * @param from  This is the second parameter, the first year
     * @param to  This is the second parameter, the second year
     * @return BigDecimal The method will return the average value, it will return zero
     * if one of the years does not exist in the file
     * @see BigDecimal
     * @see ArrayList
     * @see RoundingMode
     */
     BigDecimal averageDividends(ArrayList<String[]> arrayList, int from, int to){
        BigDecimal count = BigDecimal.ZERO;
        BigDecimal dividends = BigDecimal.ZERO;

        if (!yearsExist(arrayList, from, to)) return BigDecimal.ZERO;

        for (String[] row: arrayList) {
            int year = LocalDate.parse(row[0]).getYear();

            if((year >= from) && (year <= to)){
                dividends = dividends.add(new BigDecimal(row[1]));
                count = count.add(BigDecimal.ONE);
            }
        }
        // safeguard to avoid dividing by zero exception
        return count != BigDecimal.ZERO ?
                dividends.divide(count, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }


    /**
     * This method aims to calculate the frequency of dividends distribution
     * @param dividends This is the only parameter, an array of the dividends info
     * @return int The method will return the floored value of the average period
     * @see ArrayList
     * @see LocalDate
     */
    int frequencyOfDividendsDistribution(ArrayList<String[]> dividends) {
        int yearOfFirstMonth = 0;
        int months = 0;
        int counter = 0;

        for (int i = 0; i < dividends.size(); i++) {

            yearOfFirstMonth = LocalDate.parse(dividends.get(i)[0]).getYear();
            int firstMonth = LocalDate.parse(dividends.get(i)[0]).getMonthValue();
            int secondMonth;    // this value will be determined if the consecutive months have the same year

            for (int j = i+ 1; j < dividends.size(); j++) {
                int yearOfSecondMonth = LocalDate.parse(dividends.get(j)[0]).getYear();

                if ((yearOfFirstMonth == yearOfSecondMonth)
                        && (i + 1 != dividends.size())) {
                    secondMonth = LocalDate.parse(dividends.get(i + 1)[0]).getMonthValue();

                    months += Math.abs(secondMonth - firstMonth);   // adding the difference between them
                    counter++;
                }
            }
        }

        // safeguard to avoid dividing by zero exception
        return counter != 0 ? months / counter : 0;
    }


    /**
     * This method aims to calculate the simple moving average of the whole file,
     * without a specified period of time
     * @param data This is the only parameter, an array of the stock info, including
     * the close price which we need to calculate the SMA
     * @return BigDecimal The method will return the value of
     * the simple moving average of the whole file
     * @see ArrayList
     * @see BigDecimal
     * @see RoundingMode
     */
    BigDecimal SMA(ArrayList<String[]> data) {
        BigDecimal sumClose = BigDecimal.ZERO; // close price

        for (int i = 0; i < data.size(); i++) {
            sumClose = sumClose.add(new BigDecimal(data.get(i)[4]));
        }

        return sumClose.divide(new BigDecimal(data.size()), RoundingMode.HALF_UP);
    }


    /**
     * This method aims to calculate the simple moving average of
     * a specified period of time
     * @param data This is the first parameter, an array of the stock info, including
     * the close price which we need to calculate the SMA
     * @param from This is the second parameter, the first year in the period
     * @param to This is the third parameter, the second year in the period
     * @return BigDecimal The method will return the value of
     * the simple moving average of the specified period. it will return zero
     * if one of the years does not exist in the file
     * @see BigDecimal
     * @see ArrayList
     * @see RoundingMode
     * @see LocalDate
     */
    BigDecimal SMAOfSpecificPeriod(ArrayList<String[]> data, int from, int to) {
        if (!yearsExist(data, from, to)) return BigDecimal.ZERO;

        BigDecimal sumClose = BigDecimal.ZERO; // close
        BigDecimal days = BigDecimal.ZERO;

        for (int i = 0; i < data.size(); i++) {
            int yearOfRow = LocalDate.parse(data.get(i)[0]).getYear();
            if (yearOfRow >= from || yearOfRow <= to){
                sumClose = sumClose.add(new BigDecimal(data.get(i)[4]));
                days = days.add(BigDecimal.ONE);
            }
        }

        return sumClose.divide(days, RoundingMode.HALF_UP);
    }


    /**
     * This method aims to calculate the simple moving average of
     * a specified period of time
     * @param data This is the first parameter, an array of the stock info, including
     * the close price which we need to calculate the SMA
     * @param year This is the second parameter, the target year we want to
     * calculate the SMA in
     * @return BigDecimal The method will return the value of
     * the simple moving average of the choosing year
     * @see BigDecimal
     * @see ArrayList
     * @see RoundingMode
     */
    BigDecimal SMAYear(ArrayList<String[]> data, String year) {
        int intValueOfYear = Integer.parseInt(year);
        if (!yearsExist(data, intValueOfYear, intValueOfYear)) return BigDecimal.ZERO;

        BigDecimal sumClose = BigDecimal.ZERO; // close
        BigDecimal days = BigDecimal.ZERO;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].contains(year)) {
                sumClose = sumClose.add(new BigDecimal(data.get(i)[4]));
                days = days.add(BigDecimal.ONE);
            }
        }

        return sumClose.divide(days, RoundingMode.HALF_UP);
    }


    /**
     * This method will calculate the Exponential Moving Average using the
     * following formula: EMA (Last time period) = Value(Now) x Smoothing Factor +
     * (1 – Smoothing factor) x EMA (Previous period)
     * @param data This is the first parameter, an array of the stock info
     * @param startingYear This is the second parameter, The starting year
     * @param month This is the third parameter, the month of the year chosen
     * @param dayOfMonth This is the forth parameter, the day of month
     * @param days This is the fifth parameter, the number of days to calculate EMA
     * @return BigDecimal The method will return the EMA
     * @see BigDecimal
     * @see ArrayList
     */
    BigDecimal ExponentialMovingAverage(ArrayList<String[]> data,
                                        int startingYear,
                                        int month,
                                        int dayOfMonth,
                                        int days){

        if (!yearsExist(data, startingYear, startingYear)) return BigDecimal.ZERO;

        BigDecimal smoothingFactor = new BigDecimal("2.00")
                .divide(BigDecimal.valueOf(days + 1), RoundingMode.HALF_UP);

        int index = getIndex(data, startingYear, month, dayOfMonth);
        BigDecimal EMA = new BigDecimal(data.get(index)[4]);

        for (int i = index; i < index + days; i++) {
            EMA = (EMA.multiply(BigDecimal.ONE.subtract(smoothingFactor)))
                    .add(smoothingFactor.multiply(new BigDecimal(data.get(i)[4])));
        }

        return EMA.setScale(6, RoundingMode.HALF_UP);
    }


    //------------------------------Helping Methods---------------------------

    /**
     * This method aims to read data from a specified csv file
     * @param file This is the only parameter, the csv file to extract data from
     * @exception IOException On input error
     * @return ArrayList<String[]> The method will return the data in an arraylist
     * of type String[]
     * @see ArrayList
     */
    ArrayList<String[]> getDataFromCSV(File file) throws IOException {
        String line = "";
        ArrayList<String[]> data = new ArrayList();

        BufferedReader br = new BufferedReader(new FileReader(file.getName()));
        br.readLine();  // To skip the first row (columns names)

        while ((line = br.readLine()) != null) {
            String[] row = line.split(",");
            data.add(row);
        }

        return data;
    }


    /**
     * This method is a safeguard in case a year does not exist
     * @param arrayList This is the first parameter, an array of the stock info
     * @param from This is the second parameter, the first year in the period
     * @param to This is the third parameter, the second year in the period
     * @return boolean The method will return false if one of the years does not exist
     * @see ArrayList
     * @see Arrays
     */
    boolean yearsExist(ArrayList<String[]> arrayList, int from, int to) {
        for (String[] row: arrayList){
            if (Arrays.toString(row).contains(String.valueOf(from)) ||
                    Arrays.toString(row).contains(String.valueOf(to))) {
                return true;
            }
        }

        return false;
    }


    /**
     * This method will get the index of the starting day to calculate the EMA, assuming that
     * the date exists in the file
     * @param arrayList This is the first parameter, an array of the stock info
     * @param startingYear This is the second parameter, The starting year
     * @param month This is the third parameter, the month of the year chosen
     * @param dayOfMonth This is the forth parameter, the day of month
     * @return int The method will return the index of the specified data
     * @see ArrayList
     * @see LocalDate
     */
    int getIndex(ArrayList<String[]> arrayList, int startingYear, int month, int dayOfMonth){
        int index = 0;

        for (int i = 0; i < arrayList.size(); i++) {
            if (LocalDate.parse(arrayList.get(i)[0]).getYear() == startingYear &&
                    LocalDate.parse(arrayList.get(i)[0]).getMonthValue() == month &&
                    LocalDate.parse(arrayList.get(i)[0]).getDayOfMonth() == dayOfMonth) {
                index = i;
                break;
            }
        }

        return index;
    }

}
