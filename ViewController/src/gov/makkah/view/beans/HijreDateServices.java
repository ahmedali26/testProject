package gov.makkah.view.beans;

import javax.faces.event.ValueChangeEvent;

public class HijreDateServices {
    public HijreDateServices() {
    }

    public void deptCreationListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null) {
            System.out.println("valueChangeEvent " + valueChangeEvent.getNewValue());
        }
    }
}
