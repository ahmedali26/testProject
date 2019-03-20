package gov.makkah.share;

import java.io.IOException;

import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;

public interface ICustomize {
    public String EncodeDecode(String _string, int type) throws IOException;
    
    public Integer getSequenceValue(String SequenceName);
    
    public Date ConvertFromSQLDateToJboDate(java.sql.Date mydate);

    public Date ConvertFromUtilDateToJboDate(java.util.Date mydate);
    
    public java.sql.Date ConvertFromUtilDateToSqlDate(java.util.Date mydate);

    public java.sql.Date ConvertFromJboDateToSqlDate(Date mydate);

    public java.util.Date ConvertFromSQLDateToUtilDate(java.sql.Date mydate);

    public java.util.Date ConvertFromJboDateToUtilDate(Date mydate);

    public Number getSequenceNextValue(String seqName);

    public Integer getMaxValuePlusOneFromDB(String sql);
    
    public Long getCurrentUserId();
    
    }
