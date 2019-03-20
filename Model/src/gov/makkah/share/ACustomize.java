package gov.makkah.share;

import java.io.IOException;

public abstract class ACustomize {
    public ACustomize() {
        super();
    }
    public String EncodeDecode(String _string, int type) throws IOException {

        String _result = null;
        if (type == 1) {
            _result = new sun.misc.BASE64Encoder().encode(_string.getBytes());
        } else {
            byte[] decode = new sun.misc.BASE64Decoder().decodeBuffer(_string);
            _result = new String(decode);
        }
        return _result;
    }
    
    
    public static String RPad(String str, Integer length, char car) {
        return str + String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car));
    }

    public static String LPad(String str, Integer length, char car) {
        return String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car)) + str;
    }


    }
