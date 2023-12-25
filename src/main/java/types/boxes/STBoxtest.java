package types.boxes;

import functions.functions;

import java.sql.SQLException;

public class STBoxtest {
    public static void main(String[] args) throws SQLException {
        functions.meos_initialize("UTC");
        STBox stb = new STBox("STBox XT(((1, 1),(2, 2)),[2019-09-01,2019-09-02])");
        //System.out.println(stb.toString(15));
    }
}
