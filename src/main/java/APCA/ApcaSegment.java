package APCA;

public class ApcaSegment {

        int start; //inclusive
        int end; //exclusive
        double mean;
        double error;
        double errorWithNext;
        ApcaSegment next;
        ApcaSegment prev;

    ApcaSegment(int start, int end, double mean, double error) {
            this.start = start;
            this.end = end;
            this.mean = mean;
            this.error = error;
            this.errorWithNext = Double.POSITIVE_INFINITY;
        }

}
