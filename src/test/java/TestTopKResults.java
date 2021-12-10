//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import util.DataStructures.TopKResults;
//
//import org.junit.Assert.*;
//
//public class TestTopKResults {
//
//    public static int K = 10;
//    public static int[] sample1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
//
//    public TopKResults<Integer> topk;
//
//    public void populateTopK() {
//        for (int i = 0; i < sample1.length; i++) {
//            topk.add(sample1[i]);
//        }
//    }
//
//    @Before
//    public void setTopk() {
//        topk = new TopKResults<>(K);
//    }
//
//    @Test
//    public void testBasic() {
//        populateTopK();
//        Assert.assertTrue(topk.size() == K);
//    }
//
//    @Test
//    public void testStructure() {
//        populateTopK();
//        int i = sample1.length - K;
//        while (topk.hasNext()) {
//            Assert.assertTrue(topk.getMin() == sample1[i]);
//            i++;
//        }
//    }
//
//    @Test
//    public void testSize() {
//        populateTopK();
//        int size = 0;
//        System.out.println(topk.toString());
//        while (topk.hasNext()) {
//            System.out.println(topk.getMin());
//            size++;
//        }
//        Assert.assertTrue(size == K);
//    }
//}
