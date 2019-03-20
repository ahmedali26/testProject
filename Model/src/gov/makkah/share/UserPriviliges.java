package gov.makkah.share;

public class UserPriviliges {
     
    private Long activityId;
    private String activityName;
    private Boolean insertFlg;
    private Boolean updateFlg;
    private Boolean deleteFlg;
    private Boolean queryFlg;
    private Boolean printFlg;
    
    
    public UserPriviliges() {
       super();
    }


    public UserPriviliges(String _activityName, Boolean _insertFlg, Boolean _updateFlg,
                         Boolean _deleteFlg, Boolean _queryFlg, Boolean _printFlg) {
       this.activityName = _activityName;
       this.insertFlg = _insertFlg;
       this.updateFlg = _updateFlg;
       this.deleteFlg = _deleteFlg;
       this.queryFlg = _queryFlg;
       this.printFlg = _printFlg;
    }

    public void setActivityId(Long _activityId) {
       this.activityId = _activityId;
    }

    public Long getActivityId() {
       return activityId;
    }

    public void setActivityName(String _activityName) {
       this.activityName = _activityName;
    }

    public String getActivityName() {
       return activityName;
    }

    public void setInsertFlg(Boolean _insertFlg) {
       this.insertFlg = _insertFlg;
    }

    public Boolean getInsertFlg() {
       return insertFlg;
    }

    public void setUpdateFlg(Boolean _updateFlg) {
       this.updateFlg = _updateFlg;
    }

    public Boolean getUpdateFlg() {
       return updateFlg;
    }

    public void setDeleteFlg(Boolean _deleteFlg) {
       this.deleteFlg = _deleteFlg;
    }

    public Boolean getDeleteFlg() {
       return deleteFlg;
    }

    public void setQueryFlg(Boolean _queryFlg) {
       this.queryFlg = _queryFlg;
    }

    public Boolean getQueryFlg() {
       return queryFlg;
    }

    public void setPrintFlg(Boolean _printFlg) {
       this.printFlg = _printFlg;
    }

    public Boolean getPrintFlg() {
       return printFlg;
    }
    }
