package gov.makkah.view.beans;

import gov.makkah.view.util.ADFUtils;

public class DeptBean {
    public DeptBean() {
    }

    public String saveAndReturnButn() {
      // Integer deptId = (Integer) ADFUtils.getBoundAttributeValue("DeptartmentId");
       
        return "save";
    }
}
