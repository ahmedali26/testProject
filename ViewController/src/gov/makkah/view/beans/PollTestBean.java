package gov.makkah.view.beans;

import gov.makkah.view.util.ADFUtils;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.myfaces.trinidad.event.PollEvent;

public class PollTestBean {
    private RichTable employeeTable;

    public PollTestBean() {
    }

    public void setEmployeeTable(RichTable employeeTable) {
        this.employeeTable = employeeTable;
    }

    public RichTable getEmployeeTable() {
        return employeeTable;
    }

    public void pollListenerAction(PollEvent pollEvent) {
        ADFUtils.findOperation("Execute").execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(getEmployeeTable());
    }
}
