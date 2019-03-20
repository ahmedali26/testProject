package gov.makkah.share;

import java.io.IOException;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Types;

import oracle.adf.share.ADFContext;

import oracle.jbo.JboException;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.SequenceImpl;

public class CustomizeEntityImpl extends EntityImpl implements ICustomize {

    public String getHijriShortSysDate(java.util.Date date) {
        String _hDate = null;
        CallableStatement statement = null;

        try {
            statement = getDBTransaction().createCallableStatement("BEGIN ? := GET_M_TO_H_SHORT(?); END;", 0);
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.setDate(2, new java.sql.Date(date.getTime()));
            statement.execute();
            _hDate = statement.getString(1);
        } catch (SQLException sqlerr) {
            throw new JboException(sqlerr);
        } finally {

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException closeerr) {
                throw new JboException(closeerr);
            }
        }

        return _hDate;
    }

    @Override
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

    @Override
    public Integer getSequenceValue(String SequenceName) {
        SequenceImpl MySeq = new SequenceImpl(SequenceName, getDBTransaction());
        return (MySeq.getSequenceNumber()).intValue();
    }

    @Override
    public Date ConvertFromSQLDateToJboDate(java.sql.Date mydate) {
        return new Date(mydate);
    }

    public java.sql.Date ConvertFromUtilDateToSqlDate(java.util.Date mydate) {
        java.sql.Date mysqldate = new java.sql.Date(mydate.getTime());
        return mysqldate;
    }

    @Override
    public Date ConvertFromUtilDateToJboDate(java.util.Date mydate) {
        java.sql.Date mysqldate = this.ConvertFromUtilDateToSqlDate(mydate);
        Date jboDate = this.ConvertFromSQLDateToJboDate(mysqldate);
        return jboDate;
    }

    @Override
    public java.sql.Date ConvertFromJboDateToSqlDate(Date mydate) {
        return mydate.dateValue();
    }

    @Override
    public java.util.Date ConvertFromSQLDateToUtilDate(java.sql.Date mydate) {
        java.util.Date myutildate = new java.util.Date(mydate.getTime());
        return myutildate;
    }

    @Override
    public java.util.Date ConvertFromJboDateToUtilDate(Date mydate) {
        java.sql.Date mysqldate = this.ConvertFromJboDateToSqlDate(mydate);
        java.util.Date myutildate = this.ConvertFromSQLDateToUtilDate(mysqldate);
        return myutildate;
    }

    @Override
    public Long getCurrentUserId() {
        Long userId = null;
        Object _result = ADFContext.getCurrent()
                                   .getSessionScope()
                                   .get("SUSERID");
        if (_result != null) {
            userId = Long.parseLong(_result.toString());
        } else {
            userId = new Long(0);
        }
        return userId;
    }

    public static String RPad(String str, Integer length, char car) {
        return str + String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car));
    }

    public static String LPad(String str, Integer length, char car) {
        return String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car)) + str;
    }

    @Override
    public Number getSequenceNextValue(String seqName) {
        Number seqVal = new Number(0);
        if (seqName != null && !seqName.equals("")) {
            SequenceImpl sequenceImpl = new SequenceImpl(seqName, getDBTransaction());
            seqVal = sequenceImpl.getSequenceNumber();
        }

        return seqVal;
    }

    @Override
    public Integer getMaxValuePlusOneFromDB(String sql) {
        Integer maxValue = 0;
        if (sql != null) {
            try {

                PreparedStatement stat = getDBTransaction().createPreparedStatement(sql, 1);
                ResultSet sr = stat.executeQuery();

                if (sr.next()) {
                    maxValue = sr.getInt(1) + 1;

                } else {
                    maxValue = 1;
                }
            } catch (SQLException sqle) {
                // TODO: Add catch code
                sqle.printStackTrace();
            }
        }
        return maxValue;
    }


}
