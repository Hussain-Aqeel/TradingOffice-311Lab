import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIOException;

class TradingOfficeTest {

    TradingOffice underTest = new TradingOffice();

    @Test
    void itShouldCalculateTheAverageDividends() {
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"2020-02-07", "0.192500"});
                add(new String[]{"2020-05-08", "0.205000"});
                add(new String[]{"2020-08-07", "0.205000"});
                add(new String[]{"2020-11-06", "0.205000"});
                add(new String[]{"2021-02-05", "0.205000"});
                add(new String[]{"2021-05-07", "0.220000"});
                add(new String[]{"2021-08-06", "0.220000"});
                add(new String[]{"2021-11-05", "0.220000"});
            }
        };

        // when
        BigDecimal result = underTest.averageDividends(data, 2020, 2021);

        // then
        BigDecimal expected = BigDecimal.valueOf(0.209063);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldCalculateTheFrequencyOfDividendsDistribution() {
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"2020-02-07", "0.192500"});
                add(new String[]{"2020-05-08", "0.205000"});
                add(new String[]{"2020-08-07", "0.205000"});
                add(new String[]{"2020-11-06", "0.205000"});
                add(new String[]{"2021-02-05", "0.205000"});
                add(new String[]{"2021-05-07", "0.220000"});
                add(new String[]{"2021-08-06", "0.220000"});
                add(new String[]{"2021-11-05", "0.220000"});
            }
        };

        // when
        int result = underTest.frequencyOfDividendsDistribution(data);

        // then
        int expected = 3;
        assertThat(result).isEqualTo(expected);
    }


    @Test
    void itShouldGetDataFromCSVIfTheFileExist() throws IOException {
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"1980-12-12","0.128348","0.128906","0.128348","0.128348","0.100453","469033600"});
                add(new String[]{"1980-12-15","0.122210","0.122210","0.121652","0.121652","0.095213","175884800"});
            }
        };

        // when
        ArrayList<String[]> result = underTest.getDataFromCSV(new File("testingData.csv"));

        // then
        boolean comparison = false;
        for (int i = 0; i < data.size(); i++) {
            comparison = Arrays.equals(data.get(i), result.get(i));
        }

        boolean expected = true;
        assertThat(comparison).isEqualTo(expected);
    }

    @Test
    void itShouldThrowExceptionWhenFileDoesNotExist() {
        assertThatIOException().isThrownBy(() -> {
            underTest.getDataFromCSV(new File("tesstingData.csv"));
        });
    }

    @Test
    void itShouldReturnFalseIfYearDoesNotExist() {
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"1980-12-12","0.128348","0.128906","0.128348","0.128348","0.100453","469033600"});
                add(new String[]{"1980-12-15","0.122210","0.122210","0.121652","0.121652","0.095213","175884800"});
            }
        };

        // when
        boolean result = underTest.yearsExist(data, 2019, 2020);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void itShouldReturnTrueIfYearExists() {
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"1980-12-12","0.128348","0.128906","0.128348","0.128348","0.100453","469033600"});
                add(new String[]{"1984-12-15","0.122210","0.122210","0.121652","0.121652","0.095213","175884800"});
            }
        };

        // when
        boolean result = underTest.yearsExist(data, 1980, 1984);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void itShouldCalculateTheSimpleMovingAverage(){
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"2020-07-22","96.692497","97.974998","96.602501","97.272499","96.336311","89001600"});
                add(new String[]{"2020-07-23","96.997498","97.077499","92.010002","92.845001","91.951431","197004400"});
                add(new String[]{"2020-07-24","90.987503","92.970001","89.144997","92.614998","91.723640","185438800"});
                add(new String[]{"2021-07-27","93.709999","94.904999","93.480003","94.809998","93.897514","121214000"});
            }
        };

        // when
        BigDecimal result = underTest.SMA(data);

        // then
        assertThat(result).isEqualTo(new BigDecimal("94.385624"));
    }

    @Test
    void itShouldCalculateTheSimpleMovingAverageOfSpecifiedPeriod(){
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"2020-07-22","96.692497","97.974998","96.602501","97.272499","96.336311","89001600"});
                add(new String[]{"2020-07-23","96.997498","97.077499","92.010002","92.845001","91.951431","197004400"});
                add(new String[]{"2020-07-24","90.987503","92.970001","89.144997","92.614998","91.723640","185438800"});
                add(new String[]{"2021-07-27","93.709999","94.904999","93.480003","94.809998","93.897514","121214000"});
            }
        };

        // when
        BigDecimal result = underTest.SMAOfSpecificPeriod(data, 2020, 2021);

        // then
        assertThat(result).isEqualTo(new BigDecimal("94.385624"));
    }

    @Test
    void itShouldCalculateTheSimpleMovingAverageOfSpecificYear(){
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"2020-07-22","96.692497","97.974998","96.602501","97.272499","96.336311","89001600"});
                add(new String[]{"2020-07-23","96.997498","97.077499","92.010002","92.845001","91.951431","197004400"});
                add(new String[]{"2020-07-24","90.987503","92.970001","89.144997","92.614998","91.723640","185438800"});
                add(new String[]{"2021-07-27","93.709999","94.904999","93.480003","94.809998","93.897514","121214000"});
            }
        };

        // when
        BigDecimal result = underTest.SMAYear(data, "2020");

        // then
        assertThat(result).isEqualTo(new BigDecimal("94.244166"));
    }

    @Test
    void itShouldCalculateTheExponentialMovingAverage(){
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"2020-07-22","96.692497","97.974998","96.602501","10","96.336311","89001600"});
                add(new String[]{"2020-07-23","96.997498","97.077499","92.010002","11","91.951431","197004400"});
                add(new String[]{"2020-07-24","90.987503","92.970001","89.144997","12","91.723640","185438800"});
                add(new String[]{"2021-07-27","93.709999","94.904999","93.480003","13","93.897514","121214000"});
            }
        };

        // when
        BigDecimal result = underTest.ExponentialMovingAverage(
                data,
                2020,
                7,
                22,
                3
        );

        // then
        assertThat(result).isEqualTo(new BigDecimal("11.250000"));
    }

    @Test
    void itShouldGetTheIndex(){
        // given
        ArrayList<String[]> data = new ArrayList<>(){
            {
                add(new String[]{"2020-07-22","96.692497","97.974998","96.602501","10","96.336311","89001600"});
                add(new String[]{"2020-07-23","96.997498","97.077499","92.010002","11","91.951431","197004400"});
                add(new String[]{"2020-07-24","90.987503","92.970001","89.144997","12","91.723640","185438800"});
                add(new String[]{"2021-07-27","93.709999","94.904999","93.480003","13","93.897514","121214000"});
            }
        };

        // when
        int result = underTest.getIndex(
                data,
                2020,
                7,
                24
        );

        // then
        assertThat(result).isEqualTo(2);
    }

}