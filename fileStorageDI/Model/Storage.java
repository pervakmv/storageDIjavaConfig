package ua.nikolay.fileStorageDI.Model;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.springframework.beans.factory.annotation.Autowired;


import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@JsonPropertyOrder(value = {
        "id",
        "formatSupported",
        "storageCountry",
        "storageMaxSize"
})

@SuppressWarnings("JpaAttributeTypeInspection")
@Entity(name = "Storage")
@Table(name = "Storage")
public class Storage {
    private long id;
    private String formatsSupported;
    private String storageCountry;
    private long storageMaxSize;


    public Storage() {
    }

    public Storage(long id, String formatsSupported, String storageCountry, long storageMaxSize) {
        this.id = id;
        this.formatsSupported = formatsSupported;
        this.storageCountry = storageCountry;
        this.storageMaxSize = storageMaxSize;
    }

    @Id
    @SequenceGenerator(name = "PR_SEQ", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PR_SEQ")
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Column(name = "FORMATSUPPORTED", columnDefinition = "VARCHAR2(21)")
    public String getFormatsSupported() {

        //return formatsSupported.split(",");
        return formatsSupported;
    }

    public void setFormatsSupported(String formatsSupported) {
        //this.formatsSupported = formatSupportedToString(formatsSupported);
        this.formatsSupported = formatsSupported;
    }

    @Column(name = "STORAGECOUNTRY")
    public String getStorageCountry() {
        return storageCountry;
    }

    public void setStorageCountry(String storageCountry) {
        this.storageCountry = storageCountry;
    }

    @Column(name = "STORAGEMAXSIZE")
    public long getStorageMaxSize() {
        return storageMaxSize;
    }

    public void setStorageMaxSize(long storageMaxSize) {
        this.storageMaxSize = storageMaxSize;
    }


    public String formatSupportedToString(String[] strArray) {
        if (strArray == null)
            return null;
        String result = new String();
        for (int i = 0; i < strArray.length; i++) {
            if (i + 1 < strArray.length)
                result += (strArray[i] + ",");
            else
                result += strArray[i];
        }
        return result;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Storage storage = (Storage) o;

        return id == storage.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public String[] formatSupportedToStringArray(String inputStr) {
        if (inputStr == null)
            return null;
        return inputStr.split(",");
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", formatsSupported=" + formatsSupported +
                ", storageCountry='" + storageCountry + '\'' +
                ", storageMaxSize=" + storageMaxSize +
                '}';
    }

    public String[] formatAsArray() {
        return formatSupportedToStringArray(formatsSupported);
    }


}
