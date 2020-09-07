import ar.edu.itba.sia.tp2.utils.Round;
import org.junit.Assert;

import java.util.List;

public class RoundTest {

    public double[] doubles = {1.123456,1.134578,1.234895};
    public double[] rounded1 = new double[4];
    public double[] rounded2= new double[4];;
    public double[] rounded3= new double[4];;
    public double[] rounded4= new double[4];;
    @org.junit.Before
    public void setUp() throws Exception {

       for(int i = 0; i < doubles.length; i++){
           rounded1[i] = Round.round(doubles[i],1);
           rounded2[i] = Round.round(doubles[i],2);
           rounded3[i] = Round.round(doubles[i],3);
           rounded4[i] = Round.round(doubles[i],4);
       }

    }

    @org.junit.Test
    public void test1() throws Exception {
        Assert.assertEquals(rounded1[0], rounded1[1], 0.1);
    }

    @org.junit.Test
    public void test2() throws Exception {
        for(int i = 0; i < doubles.length; i++){
            System.out.println(rounded1[i]);
            System.out.println(rounded2[i]);
            System.out.println(rounded3[i]);
            System.out.println(rounded4[i] );
        }
    }


}
