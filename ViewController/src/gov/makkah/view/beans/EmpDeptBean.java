package gov.makkah.view.beans;

import gov.makkah.view.util.ADFUtils;
import gov.makkah.view.util.JSFUtils;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.jbo.uicli.binding.JUCtrlListBinding;

import org.apache.myfaces.trinidad.event.ReturnEvent;

public class EmpDeptBean {


    private RichSelectOneChoice deptList;

    public EmpDeptBean() {
    }

    public void newDeptReturnListener(ReturnEvent returnEvent) {
        // Add event code here...
        Integer deptId = (Integer) JSFUtils.resolveExpression("#{pageFlowScope.returnDeptId}");
        System.out.println("The Department id is : " + deptId);
   
      /* 
        BindingContainer bc = ADFUtils.getBindingContainer();
        JUCtrlListBinding list = (JUCtrlListBinding) bc.get("DepartmentId");
        list.getListIterBinding().executeQuery();
       */
       
        ADFUtils.refershLov("DepartmentId"); 
        ADFUtils.setBoundAttributeValue("DepartmentId1", deptId);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.deptList);
    }


    public void setDeptList(RichSelectOneChoice deptList) {
        this.deptList = deptList;
    }

    public RichSelectOneChoice getDeptList() {
        return deptList;
    }
}
