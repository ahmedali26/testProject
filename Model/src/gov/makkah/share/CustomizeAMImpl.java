package gov.makkah.share;

import java.io.IOException;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import oracle.adf.share.ADFContext;

import oracle.jbo.JboException;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.SequenceImpl;
import oracle.jbo.server.ViewObjectImpl;

public class CustomizeAMImpl extends ApplicationModuleImpl {
    public CustomizeAMImpl() {
        super();
    }

    public Boolean authnticatUser(String _userName, String _password) {
        Boolean _result = false;

        if (!_userName.isEmpty() && !_password.isEmpty()) {
            String password = null;
            try {
                password = EncodeDecode(_password, 1);
            } catch (IOException e) {
                throw new JboException("Sory, When Encript password is error.");
            }
            RowIterator allUser = findViewObject("AllUsers").findByAltKey("ChekUserAltKey",
                              new Key(new Object[] { ((String) _userName).toUpperCase(), password, 1 }), 1, true);
            if (allUser.getRowCount() > 0) {
                ADFContext.getCurrent().getSessionScope().put("SUSERLOGIN", _userName.toUpperCase());
                ADFContext.getCurrent().getSessionScope().put("SUSERNAME", allUser.first().getAttribute("UserName"));
                ADFContext.getCurrent().getSessionScope().put("SUSERID", allUser.first().getAttribute("UserId"));
                ADFContext.getCurrent().getSessionScope().put("SMAINDEPTID", allUser.first().getAttribute("MainDeptid"));
                ADFContext.getCurrent().getSessionScope().put("SMAINDEPTNAME", allUser.first().getAttribute("MainDeptName"));
                ADFContext.getCurrent().getSessionScope().put("SSUBDEPTID", allUser.first().getAttribute("SubDeptid"));
                ADFContext.getCurrent().getSessionScope().put("SSUBDEPTNAME", allUser.first().getAttribute("SubDeptName"));
                ADFContext.getCurrent().getSessionScope().put("SJOBNAME", allUser.first().getAttribute("JobName"));
                ADFContext.getCurrent().getSessionScope().put("SLANG", allUser.first().getAttribute("DefualtLang"));
                ADFContext.getCurrent().getSessionScope().put("SLOGINDATE", new Date());
                ADFContext.getCurrent().getSessionScope().put("SMGRNAME", getLocationManagerName((Long) allUser.first().getAttribute("SubDeptid")));
                
                ViewObject voUApp = findViewObject("UsersApplicationsRVO");
                voUApp.executeQuery();
                ADFContext.getCurrent().getSessionScope().put("SAPPLICATIONID", (Long) voUApp.first().getAttribute("ApplicationId"));

                ViewObject voUAppRole = findViewObject("UsersAppsRolesRVO");
                voUAppRole.executeQuery();
                ADFContext.getCurrent().getSessionScope().put("SROLEID", (Long) voUAppRole.first().getAttribute("RoleId"));

                ViewObject voUAppRoleD = findViewObject("UserRolesDetailsRVO");
                voUAppRoleD.executeQuery();
                
                setUserAuditing();
                _result = true;

            }
        } else {
            ADFContext.getCurrent().getSessionScope().put("SUSERLOGIN", null);
            ADFContext.getCurrent().getSessionScope().put("SUSERNAME", null);
            ADFContext.getCurrent().getSessionScope().put("SUSERID", null);
            ADFContext.getCurrent().getSessionScope().put("SMAINDEPTID", null);
            ADFContext.getCurrent().getSessionScope().put("SMAINDEPTNAME", null);
            ADFContext.getCurrent().getSessionScope().put("SSUBDEPTID", null);
            ADFContext.getCurrent().getSessionScope().put("SSUBDEPTNAME", null);
            ADFContext.getCurrent().getSessionScope().put("JobName", null);
            ADFContext.getCurrent().getSessionScope().put("SLOGINDATE", null);
            ADFContext.getCurrent().getSessionScope().put("SLOGINHDATE", getHijriSysDate());
            throw new JboException("Sory, User Name or Password is Error.");
        }

        return _result;

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
    
    private Number getSequenceNextValue(String seqName) {
        Number seqVal = new Number(0);
        if (seqName != null && !seqName.equals("")) {
            SequenceImpl sequenceImpl = new SequenceImpl(seqName, getDBTransaction());
            seqVal = sequenceImpl.getSequenceNumber();
        }

        return seqVal;
    }
    
    private void setUserAuditing() {
        ViewObjectImpl ua = (ViewObjectImpl) findViewObject("UserLoginAuditingVO");
        Row logRow = ua.createRow();
        logRow.setAttribute("LoginId", getSequenceNextValue("LOGSEQ"));
        logRow.setAttribute("UserId", ADFContext.getCurrent()
                                                .getSessionScope()
                                                .get("SUSERID"));
        logRow.setAttribute("LoginDate", new Date());
        ua.insertRow(logRow);
        getDBTransaction().commit();

    }
    
    /*
     * this metion to insert record for login time page in database table for auditin
     */
    public void setUserLongPagesAutidign(Long pageId) {
           ViewObjectImpl ulp = (ViewObjectImpl) findViewObject("UserLoginPageAuditingVO");
           Row logRow = ulp.createRow();
          
           logRow.setAttribute("LoginId", (getSequenceNextValue("LOGIN_PAGE_SEQ")).intValue());
           logRow.setAttribute("UserId", (Long)ADFContext.getCurrent()
                                                   .getSessionScope()
                                                   .get("SUSERID"));
           logRow.setAttribute("SystemId",(Long) ADFContext.getCurrent()
                                                     .getSessionScope()
                                                     .get("SAPPLICATIONID"));
           logRow.setAttribute("PageId", pageId);
           logRow.setAttribute("LoginDate", new Date());
           ulp.insertRow(logRow);
           getDBTransaction().commit();
       }    
       
    
    public Map getUserRoles() {
        Map<Long,UserPriviliges> currentUserRole = new HashMap<Long, UserPriviliges>();
        ViewObjectImpl _role = (ViewObjectImpl) findViewObject("UserRolesDetailsRVO");
        _role.executeQuery();
                if (_role.getRowCount() > 0) {
                    Row _myRow;
                    while (_role.hasNext()) {
                        _myRow = _role.next();
                        currentUserRole.put((Long) _myRow.getAttribute("ActivityId"),
                                    new UserPriviliges(_myRow.getAttribute("ActivityName").toString(),
                                                      (Integer)_myRow.getAttribute("InsertFlg")==1,
                                                      (Integer)_myRow.getAttribute("UpdateFlg")==1,
                                                      (Integer)_myRow.getAttribute("DeleteFlg")==1,
                                                      (Integer)_myRow.getAttribute("QueryFlg")==1,
                                                      (Integer)_myRow.getAttribute("PrintFlg")==1));
    
                       /* System.out.println( _myRow.getAttribute("ActivityId"));
                        System.out.println( _myRow.getAttribute("ActivityName"));
                        System.out.println((Integer)_myRow.getAttribute("InsertFlg")==1);
                        System.out.println((Integer)_myRow.getAttribute("UpdateFlg")==1);
                        System.out.println((Integer)_myRow.getAttribute("DeleteFlg")==1);
                        System.out.println((Integer)_myRow.getAttribute("QueryFlg")==1);
                       System.out.println((Integer)_myRow.getAttribute("PrintFlg")==1);
    */

            
                    }
                } else {
                    //currentUserRole.clear();
                    throw new JboException("Sorry, dosen't Role!"); 
                }
       
       return currentUserRole; 
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
    
    public String getHijriSysDate() {
        String _hDate = null;
        CallableStatement statement = null;

        try {
            statement = getDBTransaction().createCallableStatement("BEGIN ? := GET_M_TO_H(?); END;", 0);
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.setDate(2, new java.sql.Date(new Date().getTime()));
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
    
   
    public String getLocationManagerName(Long deptid){
        String mgrName= null;
        CallableStatement statement = null;

        try {
            statement = getDBTransaction().createCallableStatement("BEGIN ? := GET_LOCATION_MANAGER_NAME(?); END;", 0);
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.setLong(2, deptid);
            statement.execute();
            mgrName = statement.getString(1);
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
        
        return mgrName; 
    }
    
}
