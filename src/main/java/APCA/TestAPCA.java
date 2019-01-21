package APCA;

import java.util.ArrayList;

public class TestAPCA {
    public static void main(String[] args) {
        Apca apca = new Apca(5, false);
        double[] v = {1, 1, 4, 5, 1, 0, 1, 2, 1,5,6,7,8,9,10,5,6,8};

        NewMeanLastPair[] result = apca.transform(v);
        for(NewMeanLastPair l:result)
        {
            System.out.println(l.toString());
        }
        ApcaSegment firstSegment = apca.transformSegment(v);
        //compute mbr
        ArrayList<ArrayList<Double>> segmentMaxandMin=apca.ComputeMaxandMin(v,firstSegment);



    }
}
