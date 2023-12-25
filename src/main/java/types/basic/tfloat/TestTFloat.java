package types.basic.tfloat;

import functions.functions;

import java.sql.SQLException;

public class TestTFloat {
    public static void main(String[] args) throws SQLException {
        functions.meos_initialize("UTC");
        TFloatInst stb = new TFloatInst("1,576297364@2019-09-01");
        TFloatSeq tseqr = new TFloatSeq("(1@2019-09-01, 0.5@2019-09-02]");
        TFloatSeq tseqq = new TFloatSeq("[1.5@2019-09-01, 0.5@2019-09-02]");
        TFloatSeqSet tseqset = new TFloatSeqSet("{[1.5@2019-09-01, 0.5@2019-09-02],[1.5@2019-09-03, 1.5@2019-09-05]}");
        //TFloatSeq po = new TFloatSeq("Interp=Step;[1@2019-09-01,2@2019-09-02]");
        System.out.println(stb.tostring(15));
    }
}
