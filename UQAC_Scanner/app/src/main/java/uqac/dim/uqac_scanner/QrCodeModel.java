package uqac.dim.uqac_scanner;

import java.util.Date;

public class QrCodeModel {
    private Integer ID;
    private String name;
    private String url;
    private String description;
    private  byte[] codeQR;
    private Date dateCreation;
    private Date dateEdit;
    private boolean isScanned;
    public QrCodeModel(){}
    public QrCodeModel(String name, String url, String description, byte[] codeQR, Date dateCreation, Date dateEdit, boolean isScanned){
        this.name = name;
        this.url = url;
        this.description = description;
        this.codeQR = codeQR;
        this.dateCreation = dateCreation;
        this.dateEdit = dateEdit;
        this.isScanned = isScanned;
    }
    public QrCodeModel(Integer id, String name, String url, String description, byte[] codeQR, Date dateCreation, Date dateEdit, boolean isScanned) {
        this.ID = id;
        this.name = name;
        this.url = url;
        this.description = description;
        this.codeQR = codeQR;
        this.dateCreation = dateCreation;
        this.dateEdit = dateEdit;
        this.isScanned = isScanned;
    }
    //getter and setter
    public String getName() {
        return name;
    }
    public void setName(String value) {
        name = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    public boolean isScanned() {
        return isScanned;
    }

    public void setScanned(boolean scanned) {
        isScanned = scanned;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public byte[] getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(byte[] codeQR) {
        this.codeQR = codeQR;
    }
}
