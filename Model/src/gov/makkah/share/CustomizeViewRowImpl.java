package gov.makkah.share;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jbo.JboException;
import oracle.jbo.domain.Date;
import oracle.jbo.server.ViewRowImpl;

public class CustomizeViewRowImpl extends ViewRowImpl {
    public CustomizeViewRowImpl() {
        super();
    }
    
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


    public Date ConvertFromSQLDateToJboDate(java.sql.Date mydate) {
        return new Date(mydate);
    }

    public java.sql.Date ConvertFromUtilDateToSqlDate(java.util.Date mydate) {
        java.sql.Date mysqldate = new java.sql.Date(mydate.getTime());
        return mysqldate;
    }


    public Date ConvertFromUtilDateToJboDate(java.util.Date mydate) {
        java.sql.Date mysqldate = this.ConvertFromUtilDateToSqlDate(mydate);
        Date jboDate = this.ConvertFromSQLDateToJboDate(mysqldate);
        return jboDate;
    }

  
    public java.sql.Date ConvertFromJboDateToSqlDate(Date mydate) {
        return mydate.dateValue();
    }


    public java.util.Date ConvertFromSQLDateToUtilDate(java.sql.Date mydate) {
        java.util.Date myutildate = new java.util.Date(mydate.getTime());
        return myutildate;
    }

   
    public java.util.Date ConvertFromJboDateToUtilDate(Date mydate) {
        java.sql.Date mysqldate = this.ConvertFromJboDateToSqlDate(mydate);
        java.util.Date myutildate = this.ConvertFromSQLDateToUtilDate(mysqldate);
        return myutildate;
    }

}
