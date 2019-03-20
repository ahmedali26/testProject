package gov.makkah.view.beans;

public class SendWhatsAppBean {
    
    private String mobileNo;
    private String message;
    
    
    public SendWhatsAppBean() {
        super();
    }


    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
