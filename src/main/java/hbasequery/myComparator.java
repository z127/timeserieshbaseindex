package hbasequery;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.hadoop.hbase.exceptions.DeserializationException;
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.protobuf.generated.ComparatorProtos;
import org.apache.hadoop.hbase.util.Bytes;

public class myComparator  extends ByteArrayComparable{


        private String substr;

        /**
         * Constructor
         * @param substr the substring
         */
        public myComparator(String substr) {
            super(Bytes.toBytes(substr.toLowerCase()));
            this.substr = substr.toLowerCase();
        }

        @Override
        public byte[] getValue() {
            return Bytes.toBytes(substr);
        }

        @Override
        public int compareTo(byte[] value, int offset, int length) {
            return  1;

        }

        /**
         * @return The comparator serialized using pb
         */
        public byte [] toByteArray() {
            ComparatorProtos.SubstringComparator.Builder builder =
                    ComparatorProtos.SubstringComparator.newBuilder();
            builder.setSubstr(this.substr);
            return builder.build().toByteArray();
        }







}
