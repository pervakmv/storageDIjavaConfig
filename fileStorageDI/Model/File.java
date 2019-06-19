package ua.nikolay.fileStorageDI.Model;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Objects;
@JsonPropertyOrder(value= {
    "id",
    "name",
    "format",
    "size",
    "storage"
})
@Entity(name = "File")
@Table(name = "FILE_")
public class File {
    private long id;
    private String name;
    private String format;
    private long size;
    private Storage storage;




    public File() {
    }

    public File(long id, String name, String format, long size, Storage storage) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.size = size;
        this.storage = storage;
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

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "FORMAT")
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Column(name = "FILESIZE")
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    @SuppressWarnings("JpaAttributeTypeInspection")
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    //@JoinColumn(name = "STORAGE_ID",updatable = true, nullable = true)
   // @LazyToOne(value = LazyToOneOption.NO_PROXY)
    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        File file = (File) o;
//
//        return super.getId() == file.getId();
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hashCode(super.getId());
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        return id == file.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", size=" + size +
                ", storage=" + storage +
                '}';
    }
}
